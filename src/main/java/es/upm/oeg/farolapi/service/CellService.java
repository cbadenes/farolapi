package es.upm.oeg.farolapi.service;

import es.upm.oeg.farolapi.model.CellMark;
import es.upm.oeg.farolapi.model.GeoPoint;
import es.upm.oeg.farolapi.model.LamppostMark;
import lombok.Setter;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.evaluation.ClusterEvaluator;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.clustering.Clusterable;
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
public class CellService {

    private static final Logger LOG = LoggerFactory.getLogger(CellService.class);


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
            List<CentroidCluster<GeoPoint>> clusterResults = clusterer.cluster(points);
            cells = clusterResults.parallelStream().map(centroid -> new CellMark(centroid.getCenter()
                    .getPoint()[0], centroid
                    .getCenter().getPoint()[1], centroid.getPoints().size())).collect(Collectors.toList());
        }





        LOG.info("Cells: " + cells);

        return cells;
    }

}
