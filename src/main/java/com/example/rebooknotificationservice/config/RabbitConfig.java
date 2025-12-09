package com.example.rebooknotificationservice.config;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // 1. 큐 이름
    private static final String BOOK_NOTIFICATION_QUEUE = "book.notification.queue";
    private static final String TRADE_NOTIFICATION_QUEUE = "trade.notification.queue";
    private static final String CHAT_NOTIFICATION_QUEUE = "chat.notification.queue";
    private static final String DLQ_QUEUE = "dlq.notification.queue";

    // 2. 익스체인지 이름 (각각 분리)
    private static final String BOOK_NOTIFICATION_EXCHANGE = "book.notification.exchange";
    private static final String TRADE_NOTIFICATION_EXCHANGE = "trade.notification.exchange";
    private static final String CHAT_NOTIFICATION_EXCHANGE = "chat.notification.exchange";
    private static final String DLQ_EXCHANGE = "dlq.notification.exchange";

    // 3. 라우팅키 (각각 분리)
    private static final String BOOK_ROUTING_KEY = "book";
    private static final String TRADE_ROUTING_KEY = "trade";
    private static final String CHAT_ROUTING_KEY = "chat";
    private static final String DLQ_ROUTING_KEY = "dlq";

    // --- BOOK ---
    @Bean
    public Queue bookNotificationQueue() {
        return QueueBuilder.durable(BOOK_NOTIFICATION_QUEUE)
            .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
            .build();
    }

    @Bean
    public TopicExchange bookNotificationExchange() {
        return new TopicExchange(BOOK_NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding bookNotificationBinding() {
        return BindingBuilder.bind(bookNotificationQueue())
            .to(bookNotificationExchange())
            .with(BOOK_ROUTING_KEY);
    }

    // --- TRADE ---
    @Bean
    public Queue tradeNotificationQueue() {
        return QueueBuilder.durable(TRADE_NOTIFICATION_QUEUE)
            .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
            .build();
    }

    @Bean
    public TopicExchange tradeNotificationExchange() {
        return new TopicExchange(TRADE_NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding tradeNotificationBinding() {
        return BindingBuilder.bind(tradeNotificationQueue())
            .to(tradeNotificationExchange())
            .with(TRADE_ROUTING_KEY);
    }

    // --- CHAT ---
    @Bean
    public Queue chatNotificationQueue() {
        return QueueBuilder.durable(CHAT_NOTIFICATION_QUEUE)
            .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
            .build();
    }

    @Bean
    public TopicExchange chatNotificationExchange() {
        return new TopicExchange(CHAT_NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding chatNotificationBinding() {
        return BindingBuilder.bind(chatNotificationQueue())
            .to(chatNotificationExchange())
            .with(CHAT_ROUTING_KEY);
    }


    // DLQ
    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLQ_EXCHANGE);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
            .to(deadLetterExchange())
            .with(DLQ_ROUTING_KEY);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
        ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setDefaultRequeueRejected(false); // 예외 발생 시 재큐잉 금지 → DLQ로 이동
        return factory;
    }
}
