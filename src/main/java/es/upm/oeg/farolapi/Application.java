package es.upm.oeg.farolapi;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.List;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
//@SpringBootApplication
@Configuration
@EnableAutoConfiguration(exclude = {
        JndiConnectionFactoryAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class
})
@ComponentScan({"es.upm.oeg.farolapi"})
@PropertySource({"classpath:application.properties"})
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    static int port = 8080;

    @Autowired
    List<RouteBuilder> builders;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static EmbeddedServletContainerFactory getTomcatEmbeddedFactory(){
        TomcatEmbeddedServletContainerFactory servlet = new TomcatEmbeddedServletContainerFactory();
        servlet.setPort(port);
        return servlet;
    }

    @Bean
    public SpringCamelContext camelContext(ApplicationContext applicationContext) throws Exception {
        SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
        for (RouteBuilder builder : builders) {
            camelContext.addRoutes(builder);
        }
        return camelContext;
    }

    public static void main(String[] args){
        try {

            if (args != null || args.length > 0){
                port = Integer.valueOf(args[0]);
            }

            ApplicationContext ctx = SpringApplication.run(Application.class, args);

            LOG.info("Listening on port: " + port);

        } catch (Exception e) {
            LOG.error("Error executing test",e);
            System.exit(-1);
        }

    }
}