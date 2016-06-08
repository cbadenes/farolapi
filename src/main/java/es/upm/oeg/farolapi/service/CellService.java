package es.upm.oeg.farolapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upm.oeg.farolapi.bus.BusManager;
import es.upm.oeg.farolapi.exception.LamppostNotFoundException;
import es.upm.oeg.farolapi.model.*;
import es.upm.oeg.farolapi.repository.ConsensusRepository;
import es.upm.oeg.farolapi.repository.LamppostRepository;
import es.upm.oeg.farolapi.utils.AttributeUtils;
import lombok.Setter;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Component
public class CellService {

    private static final Logger LOG = LoggerFactory.getLogger(CellService.class);


    @Value("${farolapp.cells}")
    @Setter
    Integer cellNumbers;


    public List<CellMark> clusterize(List<LamppostMark> marks){
        if (marks == null || marks.isEmpty()) return Collections.emptyList();

        Integer k   = cellNumbers;

        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer(k);


        List<GeoPoint> points = marks.parallelStream().map( lmark -> new GeoPoint(lmark.getLatitude(),lmark.getLongitude())).collect
                (Collectors.toList());

        List<CentroidCluster<GeoPoint>> clusterResults = clusterer.cluster(points);


        List<CellMark> cells = clusterResults.parallelStream().map(centroid -> new CellMark(centroid.getCenter()
                .getPoint()[0], centroid
                .getCenter().getPoint()[1], centroid.getPoints().size())).collect(Collectors.toList());

        LOG.info("Cells: " + cells);

        return cells;
    }

}
