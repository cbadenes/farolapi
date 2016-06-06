package es.upm.oeg.farolapi.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created on 23/05/16:
 *
 * @author cbadenes
 */
public class TimeUtils {

    public static Long beforeTo(String time){
        String unit = time.substring(time.length() - 1).toLowerCase();
        String value = StringUtils.substringBeforeLast(time,unit);
        Long shift = 0l;
        switch (unit){
            case "s":
                shift = Long.valueOf(value);
                break;
            case "m":
                shift = Long.valueOf(value) * 60;
                break;
            case "h":
                shift = Long.valueOf(value) * 3600;
                break;
        }
        return System.currentTimeMillis()-(shift*1000);
    }

}
