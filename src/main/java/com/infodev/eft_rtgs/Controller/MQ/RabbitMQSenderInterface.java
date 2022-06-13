package com.infodev.eft_rtgs.Controller.MQ;


import com.infodev.eft_rtgs.Model.TransactionDetail;

public interface RabbitMQSenderInterface {


    public void send(TransactionDetail transaction);
}
