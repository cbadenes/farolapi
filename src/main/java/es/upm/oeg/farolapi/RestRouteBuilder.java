package es.upm.oeg.farolapi;

import es.upm.oeg.farolapi.views.RestResource;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
@Component
public class RestRouteBuilder extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RestRouteBuilder.class);

    @Autowired
    List<RestResource> resources;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .scheme("http")
                .bindingMode(RestBindingMode.json_xml)
                .skipBindingOnErrorCode(true)
                .jsonDataFormat("json-jackson")
                .xmlDataFormat("jaxb")
                .enableCORS(false)
                .componentProperty("minThreads", "1")
                .componentProperty("maxThreads", "10")
                .componentProperty("maxConnectionsPerHost", "-1")
                .componentProperty("maxTotalConnections", "-1")
                .dataFormatProperty("include", "NON_NULL")
                .dataFormatProperty("prettyPrint", "true")
                .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES,ADJUST_DATES_TO_CONTEXT_TIME_ZONE")
//                .dataFormatProperty("json.out.disableFeatures", "WRITE_NULL_MAP_VALUES")
                .dataFormatProperty("xml.out.mustBeJAXBElement", "false")
                .contextPath("api/0.2")
                .port(Application.port);


        // Configure REST routes
        this.getRestCollection().setCamelContext(this.getContext());
        resources.forEach(resource -> configureRest(resource.initialize(getRestCollection())));

    }

}
