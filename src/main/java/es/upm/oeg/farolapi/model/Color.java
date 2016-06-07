package es.upm.oeg.farolapi.model;

import lombok.Getter;

/**
 * Created on 07/06/16:
 *
 * @author cbadenes
 */
public enum Color {

    RED("red"), ORANGE("orange"), YELLOW("yellow"), WHITE("white"), GREEN("green"), BLUE("blue");

    @Getter
    private final String value;

    Color(String value){
        this.value = value;
    }

}
