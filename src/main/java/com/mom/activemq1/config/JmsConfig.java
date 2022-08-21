package com.mom.activemq1.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsConfig {

    @Value( "${activemq.url}" )
    private String brokerUrl;
    @Value( "${activemq.user}" )
    private String user;
    @Value( "${activemq.password}" )
    private String password;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        if ("".equals(user)) {
            return new ActiveMQConnectionFactory(brokerUrl);
        }
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, brokerUrl);
//        connectionFactory.setRedeliveryPolicy(redeliveryPolicy());//todo 1
        return connectionFactory;
    }

    @Bean(name="topicListenerFactory")
    public JmsListenerContainerFactory jmsFactoryTopic(ConnectionFactory connectionFactory,
                                                       DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(true);
        factory.setClientId("topic-order-producer");
        factory.setSubscriptionDurable(true);//durable subscription
        return factory;
    }

    @Bean
    @Qualifier("topicJmsTemplate")
    public JmsTemplate jmsTemplateTopic() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }

    @Bean(name="queueListenerFactory")
    public JmsListenerContainerFactory jmsFactoryQueue(ConnectionFactory connectionFactory,
                                                       DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setClientId("queue-order-producer");
        return factory;
    }

    @Bean
    @Qualifier("queueJmsTemplate")
    public JmsTemplate jmsTemplateQueue() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        return jmsTemplate;
    }

    //@Bean//todo 1
    //    public RedeliveryPolicy redeliveryPolicy() {
    //        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
    //        redeliveryPolicy.setRedeliveryDelay(600000L); //keep trying every 10 minutes
    //        redeliveryPolicy.setMaximumRedeliveries(-1); //Keep trying till its successfully inserted
    //        return redeliveryPolicy;
    //    }
}