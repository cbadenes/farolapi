package es.upm.oeg.farolapi;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
@Category(IntegrationTest.class)
public class ApplicationTest {

    @Test
    public void launch(){
        Application.main(new String[]{"5555"});
    }
}
