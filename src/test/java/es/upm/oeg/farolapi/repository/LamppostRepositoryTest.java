package es.upm.oeg.farolapi.repository;

import es.cbadenes.lab.test.IntegrationTest;
import es.upm.oeg.farolapi.Application;
import es.upm.oeg.farolapi.exception.LamppostNotFoundException;
import es.upm.oeg.farolapi.model.LamppostDetail;
import es.upm.oeg.farolapi.model.LamppostMark;
import es.upm.oeg.farolapi.model.Point;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created on 24/05/16:
 *
 * @author cbadenes
 */
@Category(IntegrationTest.class)
public class LamppostRepositoryTest {

    Logger LOG = LoggerFactory.getLogger(LamppostRepositoryTest.class);

    @Test
    public void readDetail() throws IOException, LamppostNotFoundException {

        ApplicationContext ctx = SpringApplication.run(Application.class, new String[]{});

        LamppostRepository repo = ctx.getBean(LamppostRepository.class);

        LamppostDetail lamppost = repo.findById("b0");

        System.out.println(lamppost);

    }

    @Test
    public void readMarks() throws IOException {

        ApplicationContext ctx = SpringApplication.run(Application.class, new String[]{});

        LamppostRepository repo = ctx.getBean(LamppostRepository.class);

        Point p1 = new Point();
        p1.setLatitude(43.0);
        p1.setLongitude(-116.0);

        Point p2 = new Point();
        p2.setLatitude(44.0);
        p2.setLongitude(-117.0);

        List<LamppostMark> marks = repo.findByLatLong(p1, p2, Optional.empty());

        System.out.println(marks);

    }


    @Test
    public void readMarksInTime() throws IOException {

        ApplicationContext ctx = SpringApplication.run(Application.class, new String[]{});

        LamppostRepository repo = ctx.getBean(LamppostRepository.class);

        Point p1 = new Point();
        p1.setLatitude(43.0);
        p1.setLongitude(-116.0);

        Point p2 = new Point();
        p2.setLatitude(44.0);
        p2.setLongitude(-117.0);

        List<LamppostMark> marks = repo.findByLatLong(p1, p2, Optional.of(System.currentTimeMillis()-30000));

        System.out.println(marks);

    }


    @Test
    public void sparqlQuery(){

        String endpoint = "http://138.4.249.224/virtuoso/sparql";

        ParameterizedSparqlString pss = new ParameterizedSparqlString();
        pss.setCommandText("prefix ap: <http://vocab.linkeddata" +
                ".es/datosabiertos/def/urbanismo-infraestructuras/alumbrado-publico#>\n " +
                "select ?farola where {?farola a ap:PuntoDeAlumbrado}");
        //pss.setLiteral("?fId", id);
        String sparqlQuery = pss.toString();

        LOG.info("Sparql Endpoint: " + endpoint + " ,  Query: " + sparqlQuery);


        List<String> farolas = new ArrayList<>();
        Query query = QueryFactory.create(sparqlQuery);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query)) {


            LOG.info("Executing sparql...");
            ResultSet results = qexec.execSelect();
            LOG.info("Reading result...");

            // collect all the values
            results.forEachRemaining(soln ->
                {
                    LamppostDetail detail = new LamppostDetail();

                    // ID
                    String farolaURI = soln.get("farola").asResource().getURI();
                    String fId = StringUtils.substringAfterLast(farolaURI, "/");
                    farolas.add(fId);
                }
            );
        }

        LOG.info("Size: " + farolas.size());

    }

}
