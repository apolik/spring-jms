package org.polik.springjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.polik.springjms.config.JmsConfig;
import org.polik.springjms.model.HelloWorldMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Polik on 5/23/2022
 */
@Component
@AllArgsConstructor
public class HelloSender  {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.SECONDS)
    public void sendMessage() {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("something")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
    }

    @SneakyThrows
    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.SECONDS)
    public void sendAndReceiveMessage() {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("sent")
                .build();

        Message receivedMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, session -> {
            try {
                Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                helloMessage.setStringProperty("_type", "org.polik.springjms.model.HelloWorldMessage");

                System.out.println("Sending Hello");

                return helloMessage;
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
        });

        HelloWorldMessage received = objectMapper.readValue(receivedMessage.getBody(String.class), HelloWorldMessage.class);

        System.out.println(received);
    }

}
