package es.upm.oeg.farolapi.controller;

import es.upm.oeg.farolapi.model.Lamppost;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
@RestController
public class LamppostController {


    @RequestMapping(value = "/lampposts", method = GET)
    public List<Lamppost> list(@RequestParam(value="name", defaultValue="World") String name) {
        return Arrays.asList(new Lamppost[]{new Lamppost()});
    }


    @RequestMapping(value = "/lampposts/{id}", method = GET)
    public Lamppost get(@PathVariable("id") String id) {
        Lamppost lamppost = new Lamppost();
        lamppost.setId(id);
        return lamppost;
    }

}
