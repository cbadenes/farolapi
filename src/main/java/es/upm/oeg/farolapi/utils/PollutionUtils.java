package es.upm.oeg.farolapi.utils;

import es.upm.oeg.farolapi.model.Height;
import es.upm.oeg.farolapi.model.Lamp;
import es.upm.oeg.farolapi.model.Pollution;
import es.upm.oeg.farolapi.model.Wattage;
import org.apache.tomcat.jni.Poll;

/**
 * Created on 07/06/16:
 *
 * @author cbadenes
 */
public class PollutionUtils {


    public static Pollution calculate(Boolean covered, String color, Wattage wattage, Height height, Lamp lamp){

        Pollution pollution = Pollution.UNKNOWN;



        switch (lamp){
            case VSAP:
                pollution = Pollution.LOW;
                break;
            case VMCC:
                pollution = Pollution.HIGH;
                break;
            case FCBC:
                pollution = Pollution.MEDIUM;
                break;
            case HM:
                pollution = Pollution.HIGH;
                break;
            case MC:
                pollution = Pollution.MEDIUM;
                break;
            case VSBP:
                pollution = Pollution.LOW;
                break;
            case F:
                pollution = Pollution.HIGH;
                break;
            case H:
                pollution = Pollution.HIGH;
                break;
            case I:
                pollution = Pollution.MEDIUM;
                break;
            case LED:
                pollution = Pollution.HIGH;
                break;
            case PAR:
                pollution = Pollution.HIGH;
                break;
            case VMAP:
                pollution = Pollution.HIGH;
                break;
        }



//        if (covered == null || wattage == null || height == null ) return pollution;
//        if (!covered || wattage.equals(Wattage.HIGH)) pollution = Pollution.HIGH;
//        else if (!wattage.equals(Wattage.HIGH) && height.equals(Height.HIGH)) pollution = Pollution.MEDIUM;
//        else pollution = Pollution.LOW;

        return pollution;


    }

}
