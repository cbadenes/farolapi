package es.upm.oeg.farolapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LamppostDetail {

    String id;

    Double latitude;

    Double longitude;

    WattageAttribute wattage;

    Attribute lamp;

    Attribute height; //low-medium-high

    Attribute light;

    Attribute color;

    Attribute covered;

    Attribute status;

    Attribute pollution; //low-medium-high

    Double radius;

    StreetViewPov streetViewPov;

}
