package es.upm.oeg.farolapi.views;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
public interface RestResource {

    RestDefinition initialize(RestsDefinition restsDefinition);

}
