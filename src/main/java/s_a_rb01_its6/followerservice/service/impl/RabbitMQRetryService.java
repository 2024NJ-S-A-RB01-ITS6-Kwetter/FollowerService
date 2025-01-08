package s_a_rb01_its6.followerservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RabbitMQRetryService {

    private static final String DLQ_NAME = "followUserDeleteQueue.dlq";
    private static final String MAIN_EXCHANGE = "userDeleteExchange";
    private static final String MAIN_ROUTING_KEY = "userDeleteKey";

    private final RabbitTemplate rabbitTemplate;

    // Scheduler polls the DLQ every minute
    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void processFailedMessages() {
        System.out.println("Checking follow DLQ for failed messages...");

        // Continuously check for messages in the DLQ
        Object failedMessage = rabbitTemplate.receiveAndConvert(DLQ_NAME);

        while (failedMessage != null) {
            System.out.println("Re-publishing failed message from DLQ: " + failedMessage);

            try {
                // Re-publish message to the main exchange
                rabbitTemplate.convertAndSend(MAIN_EXCHANGE, MAIN_ROUTING_KEY, failedMessage);
                System.out.println("Message successfully re-published: " + failedMessage);
            } catch (Exception e) {
                System.err.println("Failed to re-publish message: " + failedMessage);
                e.printStackTrace();
                break; // Exit loop if re-publishing fails to avoid infinite retries
            }

            // Fetch the next message, if any
            failedMessage = rabbitTemplate.receiveAndConvert(DLQ_NAME);
        }
    }
}