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

        if (lamp == null) return Pollution.UNKNOWN;

        switch (lamp){
            case VSAP:
                return Pollution.LOW;
            case VMCC:
                return Pollution.HIGH;
            case FCBC:
                return Pollution.MEDIUM;
            case HM:
                return Pollution.HIGH;
            case MC:
                return Pollution.MEDIUM;
            case VSBP:
                return Pollution.LOW;
            case F:
                return Pollution.HIGH;
            case H:
                return Pollution.HIGH;
            case I:
                return Pollution.MEDIUM;
            case LED:
                return Pollution.HIGH;
            case PAR:
                return Pollution.HIGH;
            case VMAP:
                return Pollution.HIGH;
        }



//        if (covered == null || wattage == null || height == null ) return pollution;
//        if (!covered || wattage.equals(Wattage.HIGH)) pollution = Pollution.HIGH;
//        else if (!wattage.equals(Wattage.HIGH) && height.equals(Height.HIGH)) pollution = Pollution.MEDIUM;
//        else pollution = Pollution.LOW;

        return Pollution.UNKNOWN;


    }

}
