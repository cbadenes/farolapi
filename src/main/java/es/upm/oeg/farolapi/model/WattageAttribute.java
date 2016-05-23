package es.upm.oeg.farolapi.model;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Data
public class WattageAttribute extends Attribute {

    @Override
    public List<String> getRange() {
        return Arrays.asList(new String[]{"low","medium","high"});
    }
}
