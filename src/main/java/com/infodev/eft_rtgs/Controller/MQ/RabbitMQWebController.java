package com.infodev.eft_rtgs.Controller.MQ;

import com.infodev.eft_rtgs.Model.TransactionDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/rtgs-rabbitmq")
public class RabbitMQWebController {

    @Autowired
    RabbitMQSenderInterface rabbitMQSenderInterface;


    @PostMapping(value = "/producer")

    public String producer(@RequestBody TransactionDetail transactionDetail, HttpServletRequest servletRequest) {
        rabbitMQSenderInterface.send(transactionDetail);
        return "Message sent to the RabbitMQ JavaInUse Successfully";
    }
}
