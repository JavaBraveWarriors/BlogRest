package com.blog.it.config;

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

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

@Configuration
@EnableJms
@PropertySource({
        "classpath:jmsConsumer.properties",
        "classpath:activeMQ-test.properties"
})
public class JmsTestConfiguration {
    @Value("${activeMqUrl}")
    private String BROKER_URL;

    @Value("${queue.tag}")
    private String TAG_QUEUE;

    @Value("${queue.comment}")
    private String COMMENT_QUEUE;

    @Value("${queue.default}")
    private String DEFAULT_QUEUE;

    @Value("${queue.responseComment}")
    private String COMMENT_RESPONSE_QUEUE;

    @Value("${jms.service.username}")
    private String BROKER_USERNAME;

    @Value("${jms.service.password}")
    private String BROKER_PASSWORD;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
        connectionFactory.setUserName(BROKER_USERNAME);
        connectionFactory.setPassword(BROKER_PASSWORD);
        return connectionFactory;
    }

    @Bean("responseComment")
    public Queue responseComment() {
        return new ActiveMQQueue(COMMENT_RESPONSE_QUEUE);
    }

    @Bean("tag")
    public Queue tagQueue() {
        return new ActiveMQQueue(TAG_QUEUE);
    }

    @Bean("comment")
    public Queue commentQueue() {
        return new ActiveMQQueue(COMMENT_QUEUE);
    }

    @Bean
    @Autowired
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
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
}
