package es.upm.oeg.farolapi.repository;

import com.google.common.base.Strings;
import es.upm.oeg.farolapi.model.*;
import es.upm.oeg.farolapi.utils.AttributeUtils;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 06/06/16:
 *
 * @author cbadenes
 */
@Component
public class ConsensusRepository implements ResponseErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ConsensusRepository.class);

    @Value("${farolapp.consensus.endpoint}")
    @Setter
    private String endpoint;

    private RestTemplate restTemplate;

    @PostConstruct
    public void setup(){
        this.restTemplate = new RestTemplate();
//        this.restTemplate.setErrorHandler(this);
    }

    public List<LamppostMark> findMarks(Point bottomLeft, Point topRight){

        LOG.info("requesting not validated lamppost from: " + endpoint);

        //TODO
        String parameters = new StringBuilder()
                .append("lat1=").append(bottomLeft.getLatitude()).append("&")
                .append("long1=").append(bottomLeft.getLongitude()).append("&")
                .append("lat2=").append(topRight.getLatitude()).append("&")
                .append("long2=").append(topRight.getLongitude())
                .toString();

        List<LamppostMark> marks = new ArrayList<>();
        try{
            LamppostNewList list = restTemplate.getForObject(endpoint+"?"+parameters, LamppostNewList.class);


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

        } catch (Exception e){
            LOG.warn("Error connecting to consensus engine: " + endpoint + "=>" + e.getMessage());
        }
        return marks;

    }

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        LOG.warn("Error connecting to: " + endpoint + "=>" + clientHttpResponse.getStatusText());
    }
}
