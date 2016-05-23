package es.upm.oeg.farolapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.upm.oeg.farolapi.model.LamppostAnnotation;
import es.upm.oeg.farolapi.service.LamppostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
@RunWith(MockitoJUnitRunner.class)
public class LamppostControllerTest {

    @Mock
    private LamppostService service;

    private LamppostController controller;

    @Before
    public void setup(){
        controller = new LamppostController();
        controller.setService(service);
    }

    @Test
    public void annotate() throws JsonProcessingException {
        final LamppostAnnotation annotation = new LamppostAnnotation();
        final String lamppostId = "sample";
        controller.annotate(lamppostId, annotation);

        verify(service,times(1)).annotate(lamppostId,annotation);
        assertTrue("Annotation completed", true);
    }
}