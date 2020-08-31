package com.ruigu.rbox.workflow.config;

import com.ruigu.rbox.workflow.manager.NotifyConfirmCallback;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liqingtian
 * @date 2019/09/08 23:17
 */
@Slf4j
@Configuration
public class RabbitMqConfig {
    @Autowired
    private NotifyConfirmCallback notifyConfirmCallback;

    /**
     * 推推棒 - 微信 - 消息队列
     */
    public static final String WEIXIN_QUEUE_NAME = "rbox_weixin_ttb_msg_queue";

    /**
     * 推推棒流程事件交换器
     */
    public static final String WORKFLOW_EVENT_TOPIC_EXCHANGE = "rbox.workflow.event.exchange";

    /**
     * 推推棒 - 微信 - 交换器
     */
    @Value("${rbox.mq.msg.weixin.exchang}")
    private String weixinMsgExchange;

    /**
     * 推推棒 - 微信 - routingKey
     */
    @Value("${rbox.msg.weixin.agentId}")
    private String agentId;
    /**
     * 推推棒 - 微信 - 消息队列（成员变更）
     */
    @Value("${rbox.mq.msg.weixin.queue}")
    private String weixinLightningQueue;

    /**
     * 推推棒 - 微信 - routingKey（成员变更）
     */
    @Value("${rbox.mq.msg.weixin.routing}")
    private String weixinLightningRouting;

    @Bean("rboxGuaranteeSuccessAmqpAdmin")
    public AmqpAdmin amqpAdmin(@Autowired @Qualifier("rboxGuaranteeSuccessConnectionFactory") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue lightningQueue() {
        return new Queue(weixinLightningQueue);
    }

    @Bean
    public Binding lightningBinding() {
        return BindingBuilder.bind(lightningQueue()).to(weixinMsgExchange()).with(weixinLightningRouting);
    }

    @Bean
    public Queue rboxWorkflowMsgQueue() {
        return new Queue(WEIXIN_QUEUE_NAME);
    }

    @Bean
    public TopicExchange weixinMsgExchange() {
        return new TopicExchange(weixinMsgExchange);
    }

    @Bean
    public Binding rboxBindingWeixinExchange() {
        return BindingBuilder.bind(rboxWorkflowMsgQueue()).to(weixinMsgExchange()).with(agentId + ":event");
    }

    /**
     * 推推棒 - 聊天室 - 消息队列
     */
    public static final String CHAT_QUEUE_NAME = "rbox.chat.ttb.msg.queue";

    /**
     * 推推棒 - 聊天室 - 交换器
     */
    @Value("${rbox.mq.msg.chat.exchang}")
    private String ttbChatMsgExchange;

    /**
     * 推推棒 - 聊天室 - 消息队列 -  routing - key
     */
    @Value("${robx.mq.msg.chat.routing.key}")
    private String chatRoutingKey;

    @Bean
    public Queue rboxWorkflowChatMsgQueue() {
        return new Queue(CHAT_QUEUE_NAME);
    }

    @Bean
    public TopicExchange chatMsgExchange() {
        return new TopicExchange(ttbChatMsgExchange);
    }

    @Bean
    public Binding rboxBindingChatExchange() {
        return BindingBuilder.bind(rboxWorkflowChatMsgQueue()).to(chatMsgExchange()).with(chatRoutingKey);
    }

    @ConfigurationProperties(prefix = "rbox.mq.msg")
    @Configuration
    @Data
    public static class SendProperties {
        private String exchange;
        private String routing;
    }

    @Bean("rboxConnectionFactory")
    public ConnectionFactory connectionFactory(@Value("${spring.rabbitmq.rbox.host}") String host,
                                               @Value("${spring.rabbitmq.rbox.port}") int port,
                                               @Value("${spring.rabbitmq.rbox.virtual-host}") String virtualHost,
                                               @Value("${spring.rabbitmq.rbox.username}") String username,
                                               @Value("${spring.rabbitmq.rbox.password}") String password) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean("rabbitTemplate")
    public RabbitTemplate rabbitmqTemplate(
            @Autowired @Qualifier("rboxConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConfirmCallback(notifyConfirmCallback);
        return rabbitTemplate;
    }

    @Bean("rboxGuaranteeSuccessConnectionFactory")
    public ConnectionFactory guaranteeSuccessConnectionFactory(@Value("${spring.rabbitmq.rbox.host}") String host,
                                                               @Value("${spring.rabbitmq.rbox.port}") int port,
                                                               @Value("${spring.rabbitmq.rbox.virtual-host}") String virtualHost,
                                                               @Value("${spring.rabbitmq.rbox.username}") String username,
                                                               @Value("${spring.rabbitmq.rbox.password}") String password,
                                                               @Value("${spring.rabbitmq.rbox.requested-heartbeat}") Integer requestedHeartbeat
    ) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setRequestedHeartBeat(requestedHeartbeat);
        return connectionFactory;
    }

    @Bean("guaranteeSuccessRabbitTemplate")
    public RabbitTemplate guaranteeSuccessRabbitTemplate(
            @Autowired @Qualifier("rboxGuaranteeSuccessConnectionFactory") ConnectionFactory connectionFactory
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.containerAckMode(AcknowledgeMode.MANUAL);
        return rabbitTemplate;
    }

    @Bean("rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory consumerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer
            , @Qualifier("rboxConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
