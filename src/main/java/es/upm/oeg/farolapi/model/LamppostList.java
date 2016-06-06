package es.upm.oeg.farolapi.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Data
@ToString
public class LamppostList {

    List<LamppostMark> lampposts;
}
