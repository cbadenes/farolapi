package es.upm.oeg.farolapi.utils;

import es.upm.oeg.farolapi.model.Height;
import es.upm.oeg.farolapi.model.Lamp;
import es.upm.oeg.farolapi.model.Pollution;
import es.upm.oeg.farolapi.model.Wattage;

/**
 * Created on 07/06/16:
 *
 * @author cbadenes
 */
public class PollutionUtils {


    public static Pollution calculate(Boolean covered, String color, Wattage wattage, Height height, Lamp lamp){

        Pollution pollution = Pollution.UNKNOWN;


        if (covered != null && !covered) pollution = Pollution.HIGH;
        else{

            // not covered
                if (wattage != null && wattage.equals(Wattage.HIGH)) pollution = Pollution.HIGH;
                else{
                    // wattage is MEDIUM or LOW
                    if (height != null && !height.equals(Height.LOW)) pollution = Pollution.MEDIUM;
                    else pollution = Pollution.LOW;
                }
        }

        return pollution;


    }

}
