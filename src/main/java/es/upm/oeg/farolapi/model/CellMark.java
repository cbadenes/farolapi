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
public class CellMark {

    Double latitude;

    Double longitude;

    Integer lampposts;

    public CellMark(Double latitude, Double longitude, Integer lampposts){
        this.latitude = latitude;
        this.longitude = longitude;
        this.lampposts = lampposts;
    }

}
