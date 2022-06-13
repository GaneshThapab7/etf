package com.infodev.eft_rtgs.Controller.Trasnsaction;


import org.springframework.stereotype.Service;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Service
public class Transaction {

    private static final String NAMESPACE_URI = "http://localhost:8090/ws/rtgs.wsdl";

  /*  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "logon")
    @ResponsePayload
      public logonResponse getArticle(@RequestPayload logon logon) {

        logonResponse response = new logonResponse();
        data data=new data();
        response.setSession_id("89541684vsd8fa4d7a876a5z3f4a3dsaf43");
        data.setMir("145869");
        data.setRef("5994asd");
        data.setType("MRK");
        response.setData(data);
        return response;
    }*/




}
