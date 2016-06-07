package es.upm.oeg.farolapi.service;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.EuclideanDoublePoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created on 07/06/16:
 *
 * @author cbadenes
 */
@RunWith(MockitoJUnitRunner.class)
public class ClusterServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterServiceTest.class);


    @Test
    public void kmeans() throws Exception {

        Integer k   = 3;

        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer(k);


        List<GeoPoint> points = new ArrayList<>();

        Random random = new Random();

        for (int i=0;i<100;i++){
            GeoPoint point = new GeoPoint(random.nextDouble(), random.nextDouble());
            LOG.info("Point: " + point.getPoint());
            points.add(point);
        }

        List<CentroidCluster<GeoPoint>> clusterResults = clusterer.cluster(points);

        // output the clusters
        for (int i=0; i<clusterResults.size(); i++) {
            LOG.info("Cluster " + i +"["+ clusterResults.get(i).getPoints().size() + "] ->" + clusterResults.get(i)
                    .getCenter());
            for (GeoPoint locationWrapper : clusterResults.get(i).getPoints())
                LOG.info(""+locationWrapper.getPoint());
        }

    }


    private class GeoPoint implements Clusterable{

        double[] point;

        public GeoPoint(double latitude, double longitude){
            point = new double[]{latitude, longitude};
        }

        @Override
        public double[] getPoint() {
            return point;
        }
    }


}
