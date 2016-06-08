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
public class LamppostMark {

    String id;

    Double latitude;

    Double longitude;

    String radius;

    String color;

    String lamp;

    String pollution;

}
