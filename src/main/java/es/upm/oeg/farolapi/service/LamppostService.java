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
import org.apache.jena.ext.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    ConsensusRepository consensusRepository;

    @Autowired
    @Setter
    BusManager busManager;

    @Value("${farolapp.achannel.key}")
    @Setter
    String keybinding;

    @Value("${farolapp.prefix}")
    @Setter
    String prefix;

    private ObjectMapper jsonMapper;

    private static final String RESOURCE_URI = "http://farolas.linkeddata.es/resource/";

    @PostConstruct
    public void setup(){
        this.jsonMapper = new ObjectMapper();
    }

    public LamppostId create(Point point){
        String id           = prefix + UUID.randomUUID().toString();
        LamppostId lamppostId   = new LamppostId();
        lamppostId.setId(id);
        LOG.info("Created lamppost: " + lamppostId + " from point: " + point);
        return lamppostId;
    }

    public void annotate(String lamppostId, LamppostAnnotation annotation) throws JsonProcessingException {

        LOG.info("Annotate lamppost: " + lamppostId + " with: " + annotation);

        AnnotationMessage message = new AnnotationMessage();
        message.setId(lamppostId);

        String uri  = RESOURCE_URI + lamppostId;
        Double latitude = annotation.getLatitude();
        Double longitude = annotation.getLongitude();

        if (!Strings.isNullOrEmpty(annotation.getColor())){
            annotate(lamppostId,uri, latitude, longitude, "color",annotation.getColor());
        }

        if (!Strings.isNullOrEmpty(annotation.getCovered())){
            annotate(lamppostId,uri, latitude, longitude,"covered",annotation.getCovered());
        }

        if (!Strings.isNullOrEmpty(annotation.getHeight())){
            annotate(lamppostId,uri, latitude, longitude,"height",annotation.getHeight());
        }

        if (!Strings.isNullOrEmpty(annotation.getLamp())){
            annotate(lamppostId,uri, latitude, longitude,"lamp",annotation.getLamp());
        }

        if (!Strings.isNullOrEmpty(annotation.getLight())){
            annotate(lamppostId,uri, latitude, longitude,"light",annotation.getLight());
        }

        if (!Strings.isNullOrEmpty(annotation.getStatus())){
            annotate(lamppostId,uri, latitude, longitude,"status",annotation.getStatus());
        }

        if (!Strings.isNullOrEmpty(annotation.getWattage())){
            Integer value = AttributeUtils.uncategorize(annotation.getWattage());
            annotate(lamppostId,uri, latitude, longitude,"wattage",String.valueOf(value));
        }

        if (annotation.getStreetViewPov() != null){
            if (!Strings.isNullOrEmpty(annotation.getStreetViewPov().getHeading())){
                annotate(lamppostId,uri, latitude, longitude,"heading",annotation.getStreetViewPov().getHeading());
            }
            if (!Strings.isNullOrEmpty(annotation.getStreetViewPov().getPitch())){
                annotate(lamppostId,uri, latitude, longitude,"pitch",annotation.getStreetViewPov().getPitch());
            }
        }
    }

    private void annotate(String id, String uri, Double latitude, Double longitude, String key, String value) throws
            JsonProcessingException {
        AnnotationMessage msg = new AnnotationMessage();
        msg.setId(id);
        msg.setUri(uri);
        msg.setLatitude(latitude);
        msg.setLongitude(longitude);
        msg.setAttribute(key);
        msg.setValue(value);
        String json = jsonMapper.writeValueAsString(msg);
        LOG.info("Annotating attribute: '" + key + "' of ["+id+"] with value: " + value );
        busManager.post(json,keybinding);
    }

    public List<LamppostMark> findMarksBy(Point bottomLeft, Point topRight, Optional<Long> time, Boolean verified)
            throws IOException {

        if (verified){
            return repository.findByLatLong(bottomLeft,topRight,time);
        }else{
            LOG.info("Requesting not verified lamppost to consensus engine: " + bottomLeft + " - " + topRight);
            return consensusRepository.findMarks(bottomLeft,topRight);
        }
    }

    public LamppostDetail readBy(String id) throws IOException, LamppostNotFoundException {
        LOG.info("Searching lamppost by id: " + id);
        return repository.findById(id);
    }

}
