package org.polik.springjms.listener;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.polik.springjms.config.JmsConfig;
import org.polik.springjms.model.HelloWorldMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.Message;
import java.util.UUID;

/**
 * Created by Polik on 5/23/2022
 */
@Component
@AllArgsConstructor
public class HelloMessageListener {
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers,
                       Message message) {
        System.out.println(helloWorldMessage);
    }

    @SneakyThrows
    @JmsListener(destination = JmsConfig.MY_SEND_RCV_QUEUE)
    public void receive(@Payload HelloWorldMessage helloWorldMessage,
                        @Headers MessageHeaders headers,
                        Message message) {
        HelloWorldMessage helloWorldMessage1 = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("received")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), helloWorldMessage);


    }
}
