package com.example.model_predict.RabbitMQ;

/**
 * @author Dr.Awe
 * @date 2024-07-25 22:45
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String EXCHANGE_NAME = "my_exchange";
    private static final String ROUTING_KEY = "my_routing_key";

    public void sendMessage(Object message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            amqpTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
}
