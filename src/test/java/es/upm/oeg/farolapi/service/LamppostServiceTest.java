package es.upm.oeg.farolapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upm.oeg.farolapi.bus.BusManager;
import es.upm.oeg.farolapi.model.AnnotationMessage;
import es.upm.oeg.farolapi.model.LamppostAnnotation;
import es.upm.oeg.farolapi.repository.LamppostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@RunWith(MockitoJUnitRunner.class)
public class LamppostServiceTest {

    @Mock
    private LamppostRepository repository;

    @Mock
    private BusManager busManager;

    String keybinding="achannel.key";

    private LamppostService service;


    @Before
    public void setup(){
        service = new LamppostService();
        service.setRepository(repository);
        service.setBusManager(busManager);
        service.setKeybinding("achannel.key");
        service.setup();
    }



    @Test
    public void annotate() throws JsonProcessingException {
        final String id = "12121";
        LamppostAnnotation annotation = new LamppostAnnotation();
        annotation.setColor("green");

        AnnotationMessage msg = new AnnotationMessage();
        msg.setId(id);
        msg.setAttribute("color");
        msg.setValue("green");

        service.annotate(id,annotation);
        // verify repository was called with user
        ObjectMapper jsonMapper = new ObjectMapper();
        String json = jsonMapper.writeValueAsString(msg);
        verify(busManager, times(1)).post(json,keybinding);
    }


    private LamppostAnnotation stubRepositoryToReturnAnnotationOnSave() {
        LamppostAnnotation lamppostAnnotation = new LamppostAnnotation();
        when(busManager.post(any(String.class),any(String.class))).thenReturn(true);
        return lamppostAnnotation;
    }

}
