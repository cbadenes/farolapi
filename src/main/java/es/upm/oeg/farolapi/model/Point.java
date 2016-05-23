package es.upm.oeg.farolapi.model;

import lombok.Data;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Data
public class Point {

    Double latitude;

    Double longitude;

    public Point(Double latitude, Double longitude){
        this.latitude   = latitude;
        this.longitude  = longitude;
    }

    public Point(){};
}
