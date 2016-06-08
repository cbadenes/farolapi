package es.upm.oeg.farolapi.repository;

import com.google.common.base.Strings;
import es.upm.oeg.farolapi.exception.LamppostNotFoundException;
import es.upm.oeg.farolapi.model.*;
import es.upm.oeg.farolapi.utils.AttributeUtils;
import es.upm.oeg.farolapi.utils.PollutionUtils;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Component
public class LamppostRepository {

    private static final Logger LOG = LoggerFactory.getLogger(LamppostRepository.class);

    @Value("${farolapp.virtuoso.endpoint}")
    @Setter
    private String endpoint;

    public LamppostDetail findById(String id) throws IOException, LamppostNotFoundException {

        String queryTxt = "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "prefix ap: <http://vocab.linkeddata" +
                ".es/datosabiertos/def/urbanismo-infraestructuras/alumbrado-publico#>\n" +
                "prefix skoslampara: <http://vocab.linkeddata" +
                ".es/datosabiertos/kos/urbanismo-infraestructuras/alumbrado-publico/tipo-lampara/>\n" +
                "\n" +
                "\n" +
                "select ?farola (str(?long) as ?longStr) (str(?lat) as ?latStr) (str(?pot) as ?potStr) ?altura " +
                "?lampara (str(?prot) as ?protStr) (str(?estado) as ?estadoStr) (str(?color) as ?colorStr) (str(?luz)" +
                " as ?luzStr) (str(?cont) as ?contStr) (str(?heading) as ?headingStr) (str(?pitch) as ?pitchStr)\n" +
                "from <http://farolas.linkeddata.es/resource>\n" +
                "where {\n" +
                "?farola a ap:PuntoDeAlumbrado;\n" +
                "  ap:id ?fId;\n" +
                "  geo:long ?long;\n" +
                "  geo:lat ?lat .\n" +
                "  OPTIONAL {\n" +
                "   ?farola ap:potencia ?pot\n" +
                "  }\n" +
                "  OPTIONAL {\n" +
                "   ?farola ap:altura ?altura\n" +
                "  }\n" +
                "  OPTIONAL {\n" +
                "   ?farola ap:tieneTipoDeLampara ?lampara\n" +
                "  }\n" +
                "  OPTIONAL {\n" +
                "     ?farola ap:proteccion ?prot\n" +
                "  }\n" +
                "  OPTIONAL {\n" +
                "     ?farola ap:color ?color\n" +
                "  }\n" +
                "  OPTIONAL {\n" +
                "     ?farola ap:luz ?luz\n" +
                "  }\n" +
                "  OPTIONAL {\n" +
                "       ?farola ap:estado ?estado\n" +
                "    }\n" +
                "  OPTIONAL {\n" +
                "     ?farola ap:contaminacion ?cont\n" +
                "  }\n" +
                "  OPTIONAL {\n" +
                "     ?farola ap:heading ?heading\n" +
                "  }\n" +
                "  OPTIONAL {\n" +
                "     ?farola ap:pitch ?pitch\n" +
                "  }\n" +
                "}";


        ParameterizedSparqlString pss = new ParameterizedSparqlString();
        pss.setCommandText(queryTxt);
        pss.setLiteral("?fId", id);
        String sparqlQuery = pss.toString();

        LOG.info("Sparql Endpoint: " + endpoint + " ,  Query: " + sparqlQuery);


        List<LamppostDetail> details = new ArrayList<>();
        Query query = QueryFactory.create(sparqlQuery);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query)) {
            {


                ResultSet results = qexec.execSelect();

                // collect all the values
                results.forEachRemaining(soln ->
                        {
                            LamppostDetail detail = new LamppostDetail();

                            // ID
                            String farolaURI    = soln.get("farola").asResource().getURI();
                            String fId           = StringUtils.substringAfterLast(farolaURI,"/");
                            detail.setId(fId);

                            // Latitude
                            if (soln.contains("latStr")){
                                double latitude     = soln.get("latStr").asLiteral().getDouble();
                                detail.setLatitude(latitude);
                            }

                            // Longitude
                            if (soln.contains("longStr")) {
                                double longitude = soln.get("longStr").asLiteral().getDouble();
                                detail.setLongitude(longitude);
                            }

                            // Wattage
                            WattageAttribute wat = new WattageAttribute();
                            if (soln.contains("potStr")){
                                String wattage      = soln.get("potStr").asLiteral().getString();
                                Integer value       = Integer.valueOf(wattage);
                                String wattageLevel = AttributeUtils.categorize(value);
                                wat.setValue(wattageLevel);
                            }
                            detail.setWattage(wat);

                            // Height
                            HeightAttribute hat = new HeightAttribute();
                            if (soln.contains("altura")){
                                String heightURI    = soln.get("altura").asResource().getURI();
                                String altura       = StringUtils.substringAfterLast(heightURI,"/").toLowerCase();
                                String height;
                                switch(altura){
                                    case "alta":
                                        height = "high";
                                        break;
                                    case "media":
                                        height = "medium";
                                        break;
                                    case "baja":
                                        height = "low";
                                        break;
                                    default:height="";
                                }
                                hat.setValue(height);
                            }
                            detail.setHeight(hat);

                            // Lamp
                            LampAttribute lat = new LampAttribute();
                            if (soln.contains("lampara")){
                                String lampURI    = soln.get("lampara").asResource().getURI();
                                String lamp       = StringUtils.substringAfterLast(lampURI,"/").toLowerCase();
                                lat.setValue(lamp.toUpperCase());
                            }
                            detail.setLamp(lat);

                            // Covered
                            CoveredAttribute cat = new CoveredAttribute();
                            if (soln.contains("protStr")){
                                String covered    = soln.get("protStr").asLiteral().getString();
                                cat.setValue(covered);
                            }
                            detail.setCovered(cat);


                            // Color
                            ColorAttribute colorAttribute = new ColorAttribute();
                            if (soln.contains("colorStr")){
                                String color    = soln.get("colorStr").asLiteral().getString();
                                colorAttribute.setValue(color);
                            }
                            detail.setColor(colorAttribute);

                            // Light
                            LightAttribute lightAttribute = new LightAttribute();
                            if (soln.contains("luzStr")){
                                String light    = soln.get("luzStr").asLiteral().getString();
                                lightAttribute.setValue(light.toUpperCase());
                            }
                            detail.setLight(lightAttribute);

                            // Status
                            StatusAttribute statusAttribute = new StatusAttribute();
                            if (soln.contains("estadoStr")){
                                String status    = soln.get("estadoStr").asLiteral().getString();
                                statusAttribute.setValue(status);
                            }
                            detail.setStatus(statusAttribute);

                            // Pollution
                            PollutionAttribute pollutionAttribute = new PollutionAttribute();
                            if (soln.contains("contStr")){
                                String pollution    = soln.get("contStr").asLiteral().getString();
                                pollutionAttribute.setValue(pollution);
                            }
                            detail.setPollution(pollutionAttribute);

                            // StreetViewPov
                            StreetViewPov streetViewPov = new StreetViewPov();

                            if (soln.contains("headingStr")){
                                String heading    = soln.get("headingStr").asLiteral().getString();
                                streetViewPov.setHeading(heading);
                            }

                            if (soln.contains("pitchStr")){
                                String pitch    = soln.get("pitchStr").asLiteral().getString();
                                streetViewPov.setPitch(pitch);
                            }

                            detail.setStreetViewPov(streetViewPov);


                            LOG.info("Lamppost: " + detail);

                            details.add(detail);
                        }
                );
            }
        }
        if (details.isEmpty()){
            LOG.info("No lamppost found!");
            throw new LamppostNotFoundException();
        }
        return details.get(0);
    }

    public List<LamppostMark> findByLatLong(Point bottomLeft, Point topRight, Optional<Long> time) throws IOException {


        StringBuilder queryBuilder = new StringBuilder();

        // Prefix
        queryBuilder.append("prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n");
        queryBuilder.append("prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n");
        queryBuilder.append("prefix ap: <http://vocab.linkeddata" +
                ".es/datosabiertos/def/urbanismo-infraestructuras/alumbrado-publico#>\n");
        queryBuilder.append("prefix skoslampara: <http://vocab.linkeddata" +
                ".es/datosabiertos/kos/urbanismo-infraestructuras/alumbrado-publico/tipo-lampara/>\n");

        // Select
        queryBuilder.append("select ?farola (str(?long) as ?longStr) (str(?lat) as ?latStr) (str(?pot) as ?potStr) (str(?color) " +
                "as ?colorStr) (str(?cont) as ?contStr) ?altura ?lampara (str(?prot) as ?protStr)  \n");

        // From
        queryBuilder.append("from <http://farolas.linkeddata.es/resource>\n");

        // Where
        queryBuilder.append("where {\n");
        queryBuilder.append("?farola a ap:PuntoDeAlumbrado;\n");
        if (time.isPresent()){
            queryBuilder.append("ap:time ?time;\n");
        }
        queryBuilder.append("geo:long ?long;\n");
        queryBuilder.append("geo:lat ?lat .\n");
        queryBuilder.append("OPTIONAL { ?farola ap:potencia ?pot }\n");
        queryBuilder.append("OPTIONAL { ?farola ap:altura ?altura }\n");
        queryBuilder.append("OPTIONAL { ?farola ap:tieneTipoDeLampara ?lampara }\n");
        queryBuilder.append("OPTIONAL { ?farola ap:proteccion ?prot }\n");
        queryBuilder.append("OPTIONAL { ?farola ap:color ?color }\n");
        queryBuilder.append("OPTIONAL {?farola ap:contaminacion ?cont}\n");

        if (time.isPresent()){
            queryBuilder.append("FILTER (xsd:double(?long) >= ?long1 && xsd:double(?long) <= ?long2 && xsd:double(?lat) > ?lat1 && " +
                    "xsd:double(?lat) <= ?lat2  && ?time >= ?time1)\n");
        }else{
            queryBuilder.append("FILTER (xsd:double(?long) >= ?long1 && xsd:double(?long) <= ?long2 && xsd:double(?lat) > ?lat1 && " +
                    "xsd:double(?lat) <= ?lat2)\n");
        }
        queryBuilder.append("}");


        String queryTxt =queryBuilder.toString();
        ParameterizedSparqlString pss = new ParameterizedSparqlString();
        pss.setCommandText(queryTxt);
        pss.setLiteral("?lat1", bottomLeft.getLatitude());
        pss.setLiteral("?long1", bottomLeft.getLongitude());
        pss.setLiteral("?lat2", topRight.getLatitude());
        pss.setLiteral("?long2", topRight.getLongitude());
        if (time.isPresent())
            pss.setLiteral("?time1",time.get());
        String sparqlQuery = pss.toString();

        LOG.info("Sparql Endpoint: " + endpoint);
        LOG.debug("Query: " + sparqlQuery);
        Query query = QueryFactory.create(sparqlQuery);
        return findMarks(query);
    }


    private List<LamppostMark> findMarks(Query query){
        List<LamppostMark> marks = new ArrayList<>();
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query)) {
            {

                ResultSet results = qexec.execSelect();

                // collect all the values
                results.forEachRemaining(soln ->
                        {
                            LamppostMark mark = new LamppostMark();

                            // ID
                            String farolaURI    = soln.get("farola").asResource().getURI();
                            String fId           = StringUtils.substringAfterLast(farolaURI,"/");
                            mark.setId(fId);

                            // Latitude
                            if (soln.contains("latStr")){
                                double latitude     = soln.get("latStr").asLiteral().getDouble();
                                mark.setLatitude(latitude);
                            }

                            // Longitude
                            if (soln.contains("longStr")) {
                                double longitude = soln.get("longStr").asLiteral().getDouble();
                                mark.setLongitude(longitude);
                            }

                            // Radius
                            String wattage = null;
                            if (soln.contains("potStr")){
                                wattage      = soln.get("potStr").asLiteral().getString();
                                Integer value = Integer.valueOf(wattage);
                                String radius = AttributeUtils.categorize(value);
                                mark.setRadius(radius);
                            }

                            // Height
                            String height = null;
                            if (soln.contains("altura")){
                                String altURI      = soln.get("altura").asResource().getURI();
                                height             = StringUtils.substringAfterLast(altURI,"/").toUpperCase();
                            }

                            // Lamp
                            Lamp lamp = null;
                            if (soln.contains("lampara")){
                                String lampURI      = soln.get("lampara").asResource().getURI();
                                String lampString = StringUtils.substringAfterLast(lampURI, "/").toUpperCase();
                                lamp = Lamp.valueOf(lampString);
                                mark.setLamp(lampString);
                            }

                            // Color
                            String color = null;
                            if (soln.contains("colorStr")){
                                color    = soln.get("colorStr").asLiteral().getString();
                                mark.setColor(color);
                            }else if (lamp != null){
                                switch(lamp){
                                    case VSAP: mark.setColor(Color.YELLOW.getValue());
                                        break;
                                    case VMCC: mark.setColor(Color.BLUE.getValue());
                                        break;
                                    case FCBC: mark.setColor(Color.ORANGE.getValue());
                                        break;
                                    case HM: mark.setColor(Color.WHITE.getValue());
                                        break;
                                    case MC: mark.setColor(Color.GREEN.getValue());//change this color
                                        break;
                                    case VSBP: mark.setColor(Color.RED.getValue()); //change this color
                                        break;
                                    case F: mark.setColor(Color.BLUE.getValue());
                                        break;
                                    case H: mark.setColor(Color.YELLOW.getValue());
                                        break;
                                    case I: mark.setColor(Color.WHITE.getValue());
                                        break;
                                    case LED: mark.setColor(Color.ORANGE.getValue());
                                        break;
                                    case PAR: mark.setColor(Color.RED.getValue());
                                        break;
                                    case VMAP: mark.setColor(Color.YELLOW.getValue());
                                        break;
                                }

                            }


                            // Covered
                            String covered = null;
                            if (soln.contains("protStr")){
                                covered    = soln.get("protStr").asLiteral().getString();
                            }


                            // Pollution
                            if (soln.contains("contStr")){
                                String pollution    = soln.get("contStr").asLiteral().getString();
                                mark.setPollution(pollution);
                            }else{
                                // Calculated Pollution
                                Wattage wattageValue = null;
                                if (!Strings.isNullOrEmpty(wattage)) wattageValue = Wattage.from(Integer.valueOf(wattage));

                                Boolean coveredValue = true;
                                if (!Strings.isNullOrEmpty(covered)) coveredValue = Boolean.valueOf(covered);

                                Height heightValue = null;
                                if (!Strings.isNullOrEmpty(height)) heightValue = Height.from(height);

                                Pollution pollution = PollutionUtils.calculate(coveredValue, color, wattageValue,
                                        heightValue, lamp);

                                if (!pollution.equals(Pollution.UNKNOWN)) mark.setPollution(pollution.getValue());
                            }

                            marks.add(mark);
                        }
                );
            }
        }
        LOG.info(marks.size() + "  marks read.");
        if (!marks.isEmpty()){
            LOG.info("One of them is: " + marks.get(0));
        }
        return marks;
    }


    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
