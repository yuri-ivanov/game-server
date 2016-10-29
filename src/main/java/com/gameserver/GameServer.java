package com.gameserver;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;
import javax.sql.DataSource;

@SpringBootApplication
public class GameServer extends SpringBootServletInitializer implements CommandLineRunner{
    @Value("${connection.jndiName}")
    String connectionJndiName;
    @Value("${connection.driver-class-name}")
    String connectionDriverClassName;
    @Value("${connection.url}")
    String connectionURL;
    @Value("${connection.username}")
    String connectionUserName;
    @Value("${connection.password}")
    String connectionPassword;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(GameServer.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public void run(String... strings) throws Exception {
        //during startup
    }


    @Lazy
    @Bean(destroyMethod="")
    public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("java:comp/env/"+connectionJndiName);
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatFactory() {
        return new TomcatEmbeddedServletContainerFactory() {

            @Override
            protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
                    Tomcat tomcat) {
                tomcat.enableNaming();
                return super.getTomcatEmbeddedServletContainer(tomcat);
            }

            @Override
            protected void postProcessContext(Context context) {
                ContextResource resource = new ContextResource();
                resource.setName(connectionJndiName);
                resource.setType(DataSource.class.getName());
                resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
                resource.setProperty("driverClassName", connectionDriverClassName);
                resource.setProperty("url", connectionURL);
                resource.setProperty("password", connectionPassword);
                resource.setProperty("username", connectionUserName);

                context.getNamingResources().addResource(resource);
            }
        };
    }

}
