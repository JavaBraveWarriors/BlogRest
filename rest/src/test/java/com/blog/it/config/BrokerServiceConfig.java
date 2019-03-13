package com.blog.it.config;

import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrokerServiceConfig {

    @Value("${activeMqUrl}")
    private String BROKER_URL;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public BrokerService brokerService() throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.setPersistent(false);
        brokerService.setUseJmx(false);
        brokerService.addConnector(BROKER_URL);
        brokerService.setBrokerName("broker");
        brokerService.setUseShutdownHook(false);
        return brokerService;
    }
}
