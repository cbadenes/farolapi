package es.upm.oeg.farolapi.model;

import lombok.Getter;

/**
 * Created on 07/06/16:
 *
 * @author cbadenes
 */
public enum Wattage {

    LOW("low"), MEDIUM("medium"), HIGH("high");

    @Getter
    private final String value;

    Wattage(String value){
        this.value = value;
    }


    public static Wattage from(Integer value){
        if (value < 100) return Wattage.LOW;
        if (value > 150) return Wattage.HIGH;
        return Wattage.MEDIUM;
    }

}
