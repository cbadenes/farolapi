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
public class LamppostDetail {

    String id;

    Double latitude;

    Double longitude;

    WattageAttribute wattage;

    LampAttribute lamp;

    HightAttribute height;

    LightAttribute light;

    ColorAttribute color;

    CoveredAttribute covered;

    StatusAttribute status;

    PollutionAttribute pollution;

    Double radius;

    StreetViewPov streetViewPov;

}
