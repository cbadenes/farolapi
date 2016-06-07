package es.upm.oeg.farolapi.utils;

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

    public static Integer uncategorize(String category){
        switch(category.toLowerCase()){
            case "low": return 99;
            case "medium": return 125;
            case "high": return 151;
            default: return 0;
        }
    }

}
