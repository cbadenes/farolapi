package es.upm.oeg.farolapi.model;

import lombok.Getter;

/**
 * Created on 07/06/16:
 *
 * @author cbadenes
 */
public enum Height {

    LOW("low"), MEDIUM("medium"), HIGH("high");

    @Getter
    private final String value;

    Height(String value){
        this.value = value;
    }


    public static Height from(String value){
        if (value.equalsIgnoreCase("baja")) return Height.LOW;
        if (value.equalsIgnoreCase("media")) return Height.MEDIUM;
        if (value.equalsIgnoreCase("alta")) return Height.HIGH;
        throw new RuntimeException("Unknown height value: " + value);
    }

}
