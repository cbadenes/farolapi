package es.upm.oeg.farolapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Data
public class LamppostAnnotation {

    Double latitude;

    Double longitude;

    String wattage;

    String lamp;

    String height;

    String light;

    String color;

    String covered;

    String status;

    StreetViewPov streetViewPov;
}
