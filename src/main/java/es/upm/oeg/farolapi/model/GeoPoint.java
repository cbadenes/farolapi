package es.upm.oeg.farolapi.model;

import lombok.Data;
import org.apache.commons.math3.ml.clustering.Clusterable;

/**
 * Created on 07/06/16:
 *
 * @author cbadenes
 */
@Data
public class GeoPoint implements Clusterable{

    double[] point;

    public GeoPoint(double latitude, double longitude){
        point = new double[]{latitude, longitude};
    }
}
