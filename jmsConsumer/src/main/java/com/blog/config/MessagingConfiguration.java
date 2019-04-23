package com.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 * The Messaging configuration.
 */
@Configuration
@EnableJms
@PropertySource({
        "classpath:jmsConsumer.properties",
})
public class MessagingConfiguration {

    private static final int SESSION_CACHE_SIZE = 10;

    @Profile("dev")
    @Configuration
    @PropertySource("classpath:dev/activeMQ.properties")
    static class Dev {
    }

    @Profile("prod")
    @Configuration
    @PropertySource("classpath:prod/activeMQ.properties")
    static class Prod {
    }

    @Value("${jms.service.address}")
    private String brokerUrl;

    @Value("${jms.service.username}")
    private String brokerUsername;

    @Value("${jms.service.password}")
    private String brokerPassword;

    @Value("${queue.tag}")
    private String tagQueue;

    @Value("${queue.comment}")
    private String commentQueue;

    @Value("${queue.default}")
    private String defaultQueue;

    @Value("${queue.responseComment}")
    private String commentResponseQueue;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setPassword(brokerUsername);
        connectionFactory.setUserName(brokerPassword);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setPubSubDomain(true);
        return template;
    }

    @Bean("default")
    public Queue defaultQueue() {
        return new ActiveMQQueue(defaultQueue);
    }

    @Bean("responseComment")
    @Lazy
    public Queue responseComment() {
        return new ActiveMQQueue(commentResponseQueue);
    }

    @Bean("tag")
    public Queue tagQueue() {
        return new ActiveMQQueue(tagQueue);
    }

    @Bean("comment")
    public Queue commentQueue() {
        return new ActiveMQQueue(commentQueue);
    }

    @Bean(name = "jmsTopicTemplate")
    public JmsTemplate createJmsTopicTemplate() {
        JmsTemplate template = new JmsTemplate(cachingConnectionFactory());
        template.setConnectionFactory(connectionFactory());
        template.setPubSubDomain(true);
        template.setDefaultDestination(defaultQueue());
        return template;
    }

    @Bean
    public DestinationResolver destinationResolver() {
        return new DynamicDestinationResolver();
    }

    @Bean
    @Autowired
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("1-1");
        return factory;
    }

    @Bean
    public ConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setTargetConnectionFactory(connectionFactory());
        connectionFactory.setSessionCacheSize(SESSION_CACHE_SIZE);
        return connectionFactory;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}