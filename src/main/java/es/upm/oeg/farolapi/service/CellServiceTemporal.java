package es.upm.oeg.farolapi.service;

import es.upm.oeg.farolapi.model.CellMark;
import es.upm.oeg.farolapi.model.GeoPoint;
import es.upm.oeg.farolapi.model.LamppostMark;
import lombok.Setter;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.EuclideanDoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Component
public class CellServiceTemporal {

    private static final Logger LOG = LoggerFactory.getLogger(CellServiceTemporal.class);


    @Value("${farolapp.cells}")
    @Setter
    Integer cellNumbers;


    public List<CellMark> clusterize(List<LamppostMark> marks){
        if (marks == null || marks.isEmpty()) return Collections.emptyList();

        Integer k   = cellNumbers;

        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer(k);
        
        List<GeoPoint> points = marks.parallelStream().map( lmark -> new GeoPoint(lmark.getLatitude(),lmark.getLongitude())).collect
                (Collectors.toList());

        List<CellMark> cells = Collections.emptyList();
        if (points.size()<=cellNumbers){
            cells = points.stream().map(point -> new CellMark(point.getPoint()[0], point.getPoint()[1],1)).collect
                    (Collectors.toList());
        }else{
            // K-Means Cluster
            List<CentroidCluster<GeoPoint>> clusterResults = clusterer.cluster(points);


            List<CentroidMark> cMarks = clusterResults.stream().map(c -> new CentroidMark(c.getCenter
                    ().getPoint()[0], c.getCenter().getPoint()[1], c.getPoints().size())).collect(Collectors.toList());

            double maxDistance = 0;
            for(int i=0; i < cMarks.size(); i++){
                maxDistance = maxDistance(cMarks.get(i),cMarks,maxDistance);
            }


            List<CentroidMark> centroids = merge(cMarks, maxDistance);


            cells = centroids.parallelStream().map(centroid -> new CellMark(centroid.latitude, centroid.longitude,
                    centroid.size)).collect(Collectors.toList());
        }

        LOG.info("Cells: " + cells);

        return cells;
    }


    private double maxDistance (CentroidMark point, List<CentroidMark> points, double distance){
        if (points.size() == 1) return Math.max(point.toEuclidean().distanceFrom(points.get(0).toEuclidean()),distance);
        double distanceAux = point.toEuclidean().distanceFrom(points.get(0).toEuclidean());
        return maxDistance(point, points.subList(1,points.size()-1), Math.max(distanceAux,distance));
    }

    private List<CentroidMark> merge(List<CentroidMark> points, double maxDistance){

        List<CentroidMark> list = new ArrayList<>();

        for (int i=0; i< points.size(); i++){
            if (points.size() > i+1){
                double distance = points.get(i).toEuclidean().distanceFrom(points.get(i + 1).toEuclidean());
                if (distance > (maxDistance/10)) {

                    EuclideanDoublePoint ePoint = points.get(i).toEuclidean().centroidOf(Arrays.asList(new
                            EuclideanDoublePoint[]{points.get(i).toEuclidean(), points.get(i + 1).toEuclidean()}));

                    list.add(new CentroidMark(ePoint.getPoint()[0],ePoint.getPoint()[1],points.get(i).size+points.get
                            (i+1).size));
                }
                else list.add(points.get(i));
            }else list.add(points.get(i));
        }

        if (list.size() != points.size()) return merge(list,maxDistance);

        return list;
    }



    public class CentroidMark{
        public double latitude;
        public double longitude;
        public int size;

        public CentroidMark(double latitude, double longitude, int size){
            this.latitude = latitude;
            this.longitude = longitude;
            this.size = size;
        }

        public EuclideanDoublePoint toEuclidean(){
            return new EuclideanDoublePoint(new double[]{latitude,longitude});
        }

    }

}
