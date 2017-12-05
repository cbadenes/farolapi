package es.upm.oeg.farolapi.bus;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
@Component
public class BusManager {

    private static final Logger LOG = LoggerFactory.getLogger(BusManager.class);

    @Value("${farolapp.achannel.exchange}")
    private String exchange;

    @Value("#{environment['FAROLAPI_BUS']?:'${farolapp.achannel.host}'}")
    private String host;

    @Value("${farolapp.achannel.port}")
    private String port;

    @Value("${farolapp.achannel.user}")
    private String user;

    @Value("${farolapp.achannel.password}")
    private String pwd;

    @Value("${farolapp.achannel.keyspace}")
    private String keyspace;

    private Channel channel;

    private BusClient client;

    @PostConstruct
    public void init() {
        try {
            String uri = new StringBuilder().
                    append("amqp://").append(user).append(":").append(pwd).
                    append("@").append(host).append(":").append(port).
                    append("/").append(keyspace).toString();
            LOG.info("Initializing RabbitMQ Event-Bus in: " + uri);
            this.client = new BusClient();
//            this.client.connect(uri);
            this.client.connect(user,pwd, host, Integer.valueOf(port), keyspace);
            this.channel = this.client.newChannel(exchange);
            LOG.info("RabbitMQ Event-Bus initialized successfully");
        } catch (IOException | TimeoutException | NoSuchAlgorithmException | KeyManagementException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            this.client.disconnect();
        } catch (TimeoutException e) {
            LOG.warn("Timeout trying to disconnect from MessageBroker");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(BusSubscriber subscriber, String queue, String key) {
        try {
            LOG.debug("subscribing: " + subscriber + " to: " + queue + "/" + key);
            this.client.consume(exchange, queue, key, subscriber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void unsubscribe(BusSubscriber subscriber) {
        try {
            LOG.debug("unsubscribing: " + subscriber);
            this.client.clean(subscriber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            LOG.warn("Timeout trying to close subscriber: " + subscriber);
        }
    }

    public boolean post(String msg, String key) {
        try {
            LOG.debug("post event: " + msg + " to: " + key);
            this.client.publish(channel, exchange, key, msg.getBytes());
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
