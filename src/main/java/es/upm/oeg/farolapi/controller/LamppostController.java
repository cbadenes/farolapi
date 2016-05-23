package es.upm.oeg.farolapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import es.upm.oeg.farolapi.bus.BusManager;
import es.upm.oeg.farolapi.model.*;
import es.upm.oeg.farolapi.service.LamppostService;
import es.upm.oeg.farolapi.utils.TimeUtils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
@RestController
public class LamppostController {

    @Autowired
    @Setter
    LamppostService service;

    @RequestMapping(value = "/lampposts", method = GET)
    public LamppostList list(@RequestParam(value="lat1") Double lat1,
                             @RequestParam(value="long1") Double long1,
                             @RequestParam(value="lat2") Double lat2,
                             @RequestParam(value="long2") Double long2,
                             @RequestParam(value="time",required = false) String time
    ) {
        Point lowerLeft     = new Point(lat1,long1);
        Point upperRight    = new Point(lat2,long2);
        List<LamppostMark> marks;

        if (Strings.isNullOrEmpty(time)){
            marks       = service.findMarksBy(lowerLeft, upperRight);
        }else{
            Long from   = TimeUtils.beforeTo(time);
            marks       = service.findMarksBy(lowerLeft, upperRight, from);
        }

        LamppostList response = new LamppostList();
        response.setLampposts(marks);
        return response;
    }


    @RequestMapping(value = "/lampposts/{id}", method = GET)
    public LamppostDetail read(@PathVariable("id") String id) {
        return service.readBy(id);
    }

    @RequestMapping(value = "/lampposts/{id}/annotations", method = POST)
    public void annotate(@PathVariable("id") String id,@RequestBody @Valid final LamppostAnnotation
            annotation) throws JsonProcessingException {
            service.annotate(id,annotation);

    }

}
