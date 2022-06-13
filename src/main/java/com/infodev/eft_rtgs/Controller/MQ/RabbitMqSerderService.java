package com.infodev.eft_rtgs.Controller.MQ;

import com.infodev.eft_rtgs.Model.TransactionDetail;
import com.infodev.eft_rtgs.fileChangerAnotation.FileChangeListner;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqSerderService implements RabbitMQSenderInterface {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${rtgs.rabbitmq.exchange}")
    private String exchange;

    @Value("${rtgs.rabbitmq.routingkey}")
    private String routingkey;



    @FileChangeListner
    @Override
    public void send(TransactionDetail transaction) {
        rabbitTemplate.convertAndSend(exchange, routingkey, transaction);
        System.out.println("Send msg = " + transaction);
    }
}
