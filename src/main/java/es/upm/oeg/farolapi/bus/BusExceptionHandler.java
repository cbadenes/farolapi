package es.upm.oeg.farolapi.bus;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
public class BusExceptionHandler implements ExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BusExceptionHandler.class);

    @Override
    public void handleUnexpectedConnectionDriverException(Connection connection, Throwable throwable) {
        LOG.error("Unexpected Connection Driver Error on RabbitMQ: " + connection, throwable);
    }

    @Override
    public void handleReturnListenerException(Channel channel, Throwable throwable) {
        LOG.error("Return Listener Error on RabbitMQ: " + channel, throwable);
    }

    @Override
    public void handleFlowListenerException(Channel channel, Throwable throwable) {
        LOG.error("Flow Listener Error on RabbitMQ: " + channel, throwable);
    }

    @Override
    public void handleConfirmListenerException(Channel channel, Throwable throwable) {
        LOG.error("Confirm Listener Error on RabbitMQ: " + channel, throwable);
    }

    @Override
    public void handleBlockedListenerException(Connection connection, Throwable throwable) {
        LOG.error("Blocked Listener Error on RabbitMQ: " + connection, throwable);
    }

    @Override
    public void handleConsumerException(Channel channel, Throwable throwable, Consumer consumer, String s, String s1) {
        LOG.error("Consumer Error on RabbitMQ: " + channel, throwable);
    }

    @Override
    public void handleConnectionRecoveryException(Connection connection, Throwable throwable) {
        LOG.error("Connection Recovery Error on RabbitMQ: " + connection, throwable);
    }

    @Override
    public void handleChannelRecoveryException(Channel channel, Throwable throwable) {
        LOG.error("Channel Recovery Error on RabbitMQ: " + channel, throwable);
    }

    @Override
    public void handleTopologyRecoveryException(Connection connection, Channel channel, TopologyRecoveryException e) {
        LOG.error("Topology Recovery Error on RabbitMQ: " + connection, e);
    }
}
