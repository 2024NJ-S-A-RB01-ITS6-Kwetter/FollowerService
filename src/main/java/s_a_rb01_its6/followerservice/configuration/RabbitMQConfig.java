package s_a_rb01_its6.followerservice.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    // follow Request Configuration
    public static final String FOLLOW_REQUEST_EXCHANGE = "followRequestExchange";
    public static final String FOLLOW_REQUEST_QUEUE = "followRequestQueue";
    public static final String FOLLOW_REQUEST_ROUTING_KEY = "followRequestKey";

    @Bean
    public DirectExchange FollowRequestExchange() {
        return new DirectExchange(FOLLOW_REQUEST_EXCHANGE);
    }

    @Bean
    public Queue followRequestQueue() {
        return new Queue(FOLLOW_REQUEST_QUEUE);
    }

    @Bean
    public Binding bindingRequest(Queue followRequestQueue, DirectExchange FollowRequestExchange) {
        return BindingBuilder.bind(followRequestQueue).to(FollowRequestExchange).with(FOLLOW_REQUEST_ROUTING_KEY);
    }

    // follow Response Configuration
    public static final String FOLLOW_RESPONSE_EXCHANGE = "followResponseExchange";
    public static final String FOLLOW_RESPONSE_QUEUE = "followResponseQueue";
    public static final String FOLLOW_RESPONSE_ROUTING_KEY = "followResponseKey";

    @Bean
    public DirectExchange followEventExchange() {
        return new DirectExchange(FOLLOW_RESPONSE_EXCHANGE);
    }

    @Bean
    public Queue followEventQueue() {
        return new Queue(FOLLOW_RESPONSE_QUEUE);
    }

    @Bean
    public Binding bindingResponse(Queue followEventQueue, DirectExchange followEventExchange) {
        return BindingBuilder.bind(followEventQueue).to(followEventExchange).with(FOLLOW_RESPONSE_ROUTING_KEY);
    }

    public static final String USER_DELETE_EXCHANGE = "userDeleteExchange";
    public static final String USER_DELETE_QUEUE = "followUserDeleteQueue";
    public static final String USER_DELETE_ROUTING_KEY = "userDeleteKey";

    public static final String USER_DELETE_EXCHANGE_DLQ = "userDeleteExchange.dlq";
    public static final String USER_DELETE_QUEUE_DLQ = "postUserDeleteQueue.dlq";
    public static final String USER_DELETE_ROUTING_KEY_DLQ = "userDeleteKey.dlq";

    @Bean
    public DirectExchange userDeleteExchange() {
        return new DirectExchange(USER_DELETE_EXCHANGE);
    }

    @Bean
    public Queue userDeleteQueue() {
        return QueueBuilder.durable(USER_DELETE_QUEUE)
                .withArgument("x-dead-letter-exchange", USER_DELETE_EXCHANGE_DLQ) // Route to DLX on failure
                .withArgument("x-dead-letter-routing-key", USER_DELETE_ROUTING_KEY_DLQ) // Routing key for DLQ
                .withArgument("x-message-ttl", 60000) // Retry after 60 seconds
                .build();
    }

    @Bean
    public Binding bindingDelete(Queue userDeleteQueue, DirectExchange userDeleteExchange) {
        return BindingBuilder.bind(userDeleteQueue).to(userDeleteExchange).with(USER_DELETE_ROUTING_KEY);
    }

    @Bean
    public DirectExchange userDeleteExchangeDLQ() {
        return new DirectExchange(USER_DELETE_EXCHANGE_DLQ);
    }

    // Dead Letter Queue (DLQ)
    @Bean
    public Queue userDeleteQueueDLQ() {
        return QueueBuilder.durable(USER_DELETE_QUEUE_DLQ).build();
    }

    // Binding for DLQ
    @Bean
    public Binding bindingDeleteDLQ(Queue userDeleteQueueDLQ, DirectExchange userDeleteExchangeDLQ) {
        return BindingBuilder.bind(userDeleteQueueDLQ).to(userDeleteExchangeDLQ).with(USER_DELETE_ROUTING_KEY_DLQ);
    }

    public static final String USER_UPDATE_EXCHANGE = "userUpdateExchange";
    public static final String USER_UPDATE_QUEUE = "followUserUpdateQueue";
    public static final String USER_UPDATE_ROUTING_KEY = "userUpdateKey";

    @Bean
    public DirectExchange userUpdateExchange() {
        return new DirectExchange(USER_UPDATE_EXCHANGE);
    }

    @Bean
    public Queue userUpdateQueue() {
        return new Queue(USER_UPDATE_QUEUE);
    }

    @Bean
    public Binding bindingUpdate(Queue userUpdateQueue, DirectExchange userUpdateExchange) {
        return BindingBuilder.bind(userUpdateQueue).to(userUpdateExchange).with(USER_UPDATE_ROUTING_KEY);
    }
}
