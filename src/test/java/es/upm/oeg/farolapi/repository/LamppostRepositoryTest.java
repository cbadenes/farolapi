package es.upm.oeg.farolapi.repository;

import es.cbadenes.lab.test.IntegrationTest;
import es.upm.oeg.farolapi.Application;
import es.upm.oeg.farolapi.model.LamppostDetail;
import es.upm.oeg.farolapi.model.LamppostMark;
import es.upm.oeg.farolapi.model.Point;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created on 24/05/16:
 *
 * @author cbadenes
 */
@Category(IntegrationTest.class)
public class LamppostRepositoryTest {


    @Test
    public void readDetail() throws IOException {

        ApplicationContext ctx = SpringApplication.run(Application.class, new String[]{});

        LamppostRepository repo = ctx.getBean(LamppostRepository.class);

        LamppostDetail lamppost = repo.findById("b0");

        System.out.println(lamppost);

    }

    @Test
    public void readMarks() throws IOException {

        ApplicationContext ctx = SpringApplication.run(Application.class, new String[]{});

        LamppostRepository repo = ctx.getBean(LamppostRepository.class);

        Point p1 = new Point();
        p1.setLatitude(43.0);
        p1.setLongitude(-116.0);

        Point p2 = new Point();
        p2.setLatitude(44.0);
        p2.setLongitude(-117.0);

        List<LamppostMark> marks = repo.findByLatLong(p1, p2, Optional.empty());

        System.out.println(marks);

    }


    @Test
    public void readMarksInTime() throws IOException {

        ApplicationContext ctx = SpringApplication.run(Application.class, new String[]{});

        LamppostRepository repo = ctx.getBean(LamppostRepository.class);

        Point p1 = new Point();
        p1.setLatitude(43.0);
        p1.setLongitude(-116.0);

        Point p2 = new Point();
        p2.setLatitude(44.0);
        p2.setLongitude(-117.0);

        List<LamppostMark> marks = repo.findByLatLong(p1, p2, Optional.of(System.currentTimeMillis()-30000));

        System.out.println(marks);

    }

}
