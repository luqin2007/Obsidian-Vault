package com.example.mybank;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.xbean.XBeanBrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;

import java.net.URI;
import java.util.List;

@Configuration
@EnableJms
public class MyBankConfig {

    /**
     * 内嵌模式
     */
    @Bean
    public XBeanBrokerService activeMQBroker() throws Exception {
        XBeanBrokerService broker = new XBeanBrokerService();
        TransportConnector connector = new TransportConnector();
        connector.setUri(URI.create("tcp://localhost:61616"));
        broker.setTransportConnectors(List.of(connector));
        return broker;
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL("tcp://localhost:61616");
        factory.setTrustedPackages(List.of("com.example.mybank", "java.util"));
        return factory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        return new CachingConnectionFactory(connectionFactory);
    }
}
