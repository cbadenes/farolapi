package es.upm.oeg.farolapi.service;

/**
 * Created on 06/06/16:
 *
 * @author cbadenes
 */
public class AttributeUtils {

    public static String categorize(Integer value){
        if (value < 100){
            return "low";
        }else if (value > 150 ){
            return "high";
        } else {
            return "medium";
        }
    }

}
