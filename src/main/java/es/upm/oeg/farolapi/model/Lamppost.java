package es.upm.oeg.farolapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Lamppost {

    String id;

    Double latitude;

    Double longitude;

    Double wattage;

    String lamp;

    String height; //low-medium-high

    String light;

    String color;

    Boolean covered;

    String status;

    String pollution; //low-medium-high

}
