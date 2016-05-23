package es.upm.oeg.farolapi.repository;

import es.upm.oeg.farolapi.model.LamppostDetail;
import es.upm.oeg.farolapi.model.LamppostMark;
import es.upm.oeg.farolapi.model.Point;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Component
public class LamppostRepository {

    public LamppostDetail findById(String id){
        //TODO
        String ontology_service = "http://ambit.uni-plovdiv.bg:8080/ontology";
        String endpoint = "otee:Endpoints";
        String endpointsSparql =
                "PREFIX ot:<http://www.opentox.org/api/1.1#>\n"+
                        "	PREFIX ota:<http://www.opentox.org/algorithms.owl#>\n"+
                        "	PREFIX owl:<http://www.w3.org/2002/07/owl#>\n"+
                        "	PREFIX dc:<http://purl.org/dc/elements/1.1/>\n"+
                        "	PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
                        "	PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
                        "	PREFIX otee:<http://www.opentox.org/echaEndpoints.owl#>\n"+
                        "		select ?url ?title\n"+
                        "		where {\n"+
                        "		?url rdfs:subClassOf %s.\n"+
                        "		?url dc:title ?title.\n"+
                        "		}\n";
        QueryExecution x = QueryExecutionFactory.sparqlService(ontology_service, String.format(endpointsSparql,endpoint));
        ResultSet results = x.execSelect();
        ResultSetFormatter.out(System.out, results);
        return null;
    }

    public List<LamppostMark> findByLatLong(Point lowerLeft, Point upperRight){
        //TODO
        return null;
    }

    public List<LamppostMark> findByLatLongInTime(Point lowerLeft, Point upperRight, Long time){
        //TODO
        return null;
    }

}
