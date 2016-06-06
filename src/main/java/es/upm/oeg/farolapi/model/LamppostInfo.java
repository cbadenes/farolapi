package es.upm.oeg.farolapi.model;

import lombok.Data;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Data
public class LamppostInfo {

    String id;

    Double latitude;

    Double longitude;

    String wattage;

    String color;

    String pollution;
}
