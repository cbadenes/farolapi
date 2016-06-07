package es.upm.oeg.farolapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import es.upm.oeg.farolapi.exception.LamppostNotFoundException;
import es.upm.oeg.farolapi.model.*;
import es.upm.oeg.farolapi.service.CellService;
import es.upm.oeg.farolapi.service.LamppostService;
import es.upm.oeg.farolapi.utils.TimeUtils;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
@RestController
public class CellController {

    private static final Logger LOG = LoggerFactory.getLogger(CellController.class);

    @Autowired
    @Setter
    LamppostService lamppostService;

    @Autowired
    @Setter
    CellService cellService;

    @PostConstruct
    public void setup(){
        LOG.info("Service: /cells available");
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/cells", method = GET)
    public CellList list(@RequestParam(value="lat1") Double lat1,
                             @RequestParam(value="long1") Double long1,
                             @RequestParam(value="lat2") Double lat2,
                             @RequestParam(value="long2") Double long2,
                             @RequestParam(value="time",required = false) String time,
                             @RequestParam(value="verified",required = false, defaultValue = "true") Boolean verified
    ) throws IOException {
        LOG.info(StringUtils.repeat("#",50));
        LOG.info("> Cells inside of:");
        LOG.info(StringUtils.repeat("-",10));
        LOG.info("Lat1: " + lat1);
        LOG.info("Long1: " + long1);
        LOG.info("Lat2: " + lat2);
        LOG.info("Long2: " + long2);
        LOG.info("Time: " + time);
        LOG.info("Verified: " + verified);
        LOG.info(StringUtils.repeat("#",50));

        Point bottomLeft    = new Point(lat1,long1);
        Point topRight      = new Point(lat2,long2);

        Optional<Long> from = Optional.empty();
        if (!Strings.isNullOrEmpty(time)){
            from = Optional.of(TimeUtils.beforeTo(time));
        }

        List<LamppostMark> marks = lamppostService.findMarksBy(bottomLeft, topRight, from, verified);

        List<CellMark> cells = cellService.clusterize(marks);

        // Composing response
        CellList response = new CellList();
        response.setCells(cells);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleTodoNotFound(LamppostNotFoundException ex) {
    }

}
