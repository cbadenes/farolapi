package es.upm.oeg.farolapi.model;

import lombok.Data;

import java.util.List;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Data
public class Attribute {

    String value;

    List<String> range;
}
