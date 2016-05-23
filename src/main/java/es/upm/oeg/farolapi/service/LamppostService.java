package es.upm.oeg.farolapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upm.oeg.farolapi.bus.BusManager;
import es.upm.oeg.farolapi.model.*;
import es.upm.oeg.farolapi.repository.LamppostRepository;
import lombok.Setter;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@Component
public class LamppostService {

    private static final Logger LOG = LoggerFactory.getLogger(LamppostService.class);

    @Autowired
    @Setter
    LamppostRepository repository;

    @Autowired
    @Setter
    BusManager busManager;

    @Value("${farolapp.achannel.key}")
    @Setter
    String keybinding;

    private ObjectMapper jsonMapper;

    @PostConstruct
    public void setup(){
        this.jsonMapper = new ObjectMapper();
    }

    public void annotate(String lamppostId, LamppostAnnotation annotation) throws JsonProcessingException {

        AnnotationMessage message = new AnnotationMessage();
        message.setId(lamppostId);

        //TODO iterate for attributes
        if (!Strings.isNullOrEmpty(annotation.getColor())){
            annotate(lamppostId,"color",annotation.getColor());
        }

        if (!Strings.isNullOrEmpty(annotation.getCovered())){
            annotate(lamppostId,"covered",annotation.getCovered());
        }

        if (!Strings.isNullOrEmpty(annotation.getHeight())){
            annotate(lamppostId,"height",annotation.getHeight());
        }

        if (!Strings.isNullOrEmpty(annotation.getLamp())){
            annotate(lamppostId,"lamp",annotation.getLamp());
        }

        if (!Strings.isNullOrEmpty(annotation.getLight())){
            annotate(lamppostId,"light",annotation.getLight());
        }

        if (!Strings.isNullOrEmpty(annotation.getStatus())){
            annotate(lamppostId,"status",annotation.getStatus());
        }

        if (!Strings.isNullOrEmpty(annotation.getWattage())){
            annotate(lamppostId,"wattage",annotation.getWattage());
        }
    }

    private void annotate(String id, String key, String value) throws JsonProcessingException {
        AnnotationMessage msg = new AnnotationMessage();
        msg.setId(id);
        msg.setAttribute(key);
        msg.setValue(value);
        String json = jsonMapper.writeValueAsString(msg);
        LOG.info("Annotating attribute: '" + key + "' of ["+id+"] with value: " + value );
        busManager.post(json,keybinding);
    }

    public List<LamppostMark> findMarksBy(Point lowerLeft, Point upperRight){
        //TODO
        return null;
    }


    public List<LamppostMark> findMarksBy(Point lowerLeft, Point upperRight, Long from){
        //TODO
        return null;
    }


    public LamppostDetail readBy(String id){
        //TODO
        return null;
    }

}
