package es.upm.oeg.farolapi.model;

import lombok.Getter;

/**
 * Created on 07/06/16:
 *
 * @author cbadenes
 */
public enum Pollution {

    LOW("low"), MEDIUM("medium"), HIGH("medium"), UNKNOWN("unknown");

    @Getter
    private final String value;

    Pollution(String value){
        this.value = value;
    }
}
