package es.upm.oeg.farolapi.service;

import es.cbadenes.lab.test.IntegrationTest;
import es.upm.oeg.farolapi.Application;
import es.upm.oeg.farolapi.model.LamppostAnnotation;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * Created on 24/05/16:
 *
 * @author cbadenes
 */
@Category(IntegrationTest.class)
public class AnnotateTest {


    @Test
    public void annotate() throws IOException {

        ApplicationContext ctx = SpringApplication.run(Application.class, new String[]{});

        LamppostService service = ctx.getBean(LamppostService.class);

        LamppostAnnotation annotation = new LamppostAnnotation();
        annotation.setLatitude(43.581003827358217);
        annotation.setLongitude(-116.173377862671714);
        annotation.setLamp("LED");
        String lamppostId = "b6";
        service.annotate(lamppostId,annotation);

    }

}
