package es.upm.oeg.farolapi.views;

import es.upm.oeg.farolapi.models.Lamppost;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.springframework.stereotype.Component;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
@Component
public class LampResource implements RestResource{

    @Override
    public RestDefinition initialize(RestsDefinition definitions) {
        return definitions.rest("/lampposts").description("rest service for streetlights management")

                .post().description("Add a new lamppost").type(Lamppost.class).outType(Lamppost.class)
                .produces("application/json").to("bean:lamppostController?method=create")

                .get("/")
                .description("List all existing lampposts").outTypeList(String.class)
                .produces("application/json").to("bean:lamppostController?method=list")

                .get("/{id}")
                .description("More details about  a lamppost by id").outType(Lamppost.class)
//                .param().name("verbose").type(RestParamType.query).defaultValue("false").description("Verbose order details").endParam()
                .produces("application/json").to("bean:lamppostController?method=get(${header.id})")

                .delete("/").description("Remove all existing lampposts")
                .produces("application/json").to("bean:lamppostController?method=removeAll()")

                .delete("/{id}").description("Remove an existing lamppost")
                .produces("application/json").to("bean:lamppostController?method=remove(${header.id})")

                .put("/{id}").description("Update an existing lamppost").type(Lamppost.class).outType(Lamppost.class)
                .produces("application/json").to("bean:lamppostController?method=update");

    }
}
