package es.upm.oeg.farolapi.bus;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
public interface BusSubscriber {

    boolean handle(String message);
}
