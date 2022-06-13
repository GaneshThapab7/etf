package com.infodev.eft_rtgs.Controller.MQConsumer;

import com.infodev.eft_rtgs.Model.TransactionDetail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
public class QueueConsumer {

    @RabbitListener(queues = {"${rtgs.rabbitmq.queue}"})
    public void receive(@Payload TransactionDetail fileBody) {
        System.out.println("Message " + fileBody);
    }

}