package es.upm.oeg.farolapi.model;

import lombok.Data;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Data
@ToString (callSuper = true)
public class PollutionAttribute extends Attribute {

    @Override
    public List<String> getRange() {
        return Arrays.asList(new String[]{"low", "medium", "high"});
    }
}
