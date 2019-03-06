package com.blog.config;

import com.blog.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJms
@PropertySource({
        "classpath:queue.properties",
        "classpath:activeMQ.properties"
})
public class MessagingConfiguration {

    @Value("${jms.service.address}")
    private String BROKER_URL;

    @Value("${jms.service.username}")
    private String BROKER_USERNAME;

    @Value("${jms.service.password}")
    private String BROKER_PASSWORD;

    @Value("${queue.tag}")
    private String TAG_QUEUE;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
        connectionFactory.setPassword(BROKER_USERNAME);
        connectionFactory.setUserName(BROKER_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setPubSubDomain(true);
        return template;
    }

    @Bean("tag")
    public Queue queue() {
        return new ActiveMQQueue(TAG_QUEUE);
    }

    @Bean(name = "jmsTopicTemplate")
    public JmsTemplate createJmsTopicTemplate() {
        JmsTemplate template = new JmsTemplate(cachingConnectionFactory());
        template.setConnectionFactory(connectionFactory());
        template.setPubSubDomain(true);
        template.setDefaultDestination(queue());
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
        connectionFactory.setSessionCacheSize(10);
        return connectionFactory;
    }

    @Bean
    @Autowired
    public MappingJackson2MessageConverter getMappingJackson2MessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        Map<String, Class<?>> typeIdMappings = getClassSerializationId();
        messageConverter.setTypeIdMappings(typeIdMappings);
        messageConverter.setTargetType(MessageType.TEXT);
        messageConverter.setTypeIdPropertyName("_type");
        return messageConverter;
    }

    private Map<String, Class<?>> getClassSerializationId() {
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put(Tag.class.getName(), Tag.class);
        return typeIdMappings;
    }

}

