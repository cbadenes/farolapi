package es.upm.oeg.farolapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LamppostAnnotation {

    String wattage;

    String lamp;

    String height;

    String light;

    String color;

    String covered;

    String status;
}
