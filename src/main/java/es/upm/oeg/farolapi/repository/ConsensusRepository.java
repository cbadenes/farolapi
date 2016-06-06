package es.upm.oeg.farolapi.repository;

import com.google.common.base.Strings;
import es.upm.oeg.farolapi.model.*;
import es.upm.oeg.farolapi.service.AttributeUtils;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 06/06/16:
 *
 * @author cbadenes
 */
@Component
public class ConsensusRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ConsensusRepository.class);

    @Value("${farolapp.consensus.endpoint}")
    @Setter
    private String endpoint;


    public List<LamppostMark> findMarks(Point bottomLeft, Point topRight){
        RestTemplate restTemplate = new RestTemplate();
        LOG.info("requesting not validated lamppost from: " + endpoint);

        //TODO
        String parameters = new StringBuilder()
                .append("lat1=").append(bottomLeft.getLatitude()).append("&")
                .append("long1=").append(bottomLeft.getLongitude()).append("&")
                .append("lat2=").append(topRight.getLatitude()).append("&")
                .append("long2=").append(topRight.getLongitude())
                .toString();
        LamppostNewList list = restTemplate.getForObject(endpoint+"?"+parameters, LamppostNewList.class);


        List<LamppostMark> marks = new ArrayList<>();

        if (list!=null && list.getLampposts() != null && !list.getLampposts().isEmpty()){
            for(LamppostInfo lamppost : list.getLampposts()){
                LamppostMark mark = new LamppostMark();
                mark.setId(lamppost.getId());
                mark.setLatitude(lamppost.getLatitude());
                mark.setLongitude(lamppost.getLongitude());
                mark.setColor(lamppost.getColor());
                mark.setPollution(lamppost.getPollution());

                if (!Strings.isNullOrEmpty(lamppost.getWattage())){
                    Integer value = Integer.valueOf(lamppost.getWattage());
                    mark.setRadius(AttributeUtils.categorize(value));
                }
                marks.add(mark);
            }
        }

        return marks;

    }

}
