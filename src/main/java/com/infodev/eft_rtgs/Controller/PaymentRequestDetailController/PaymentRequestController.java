package com.infodev.eft_rtgs.Controller.PaymentRequestDetailController;


import com.infodev.eft_rtgs.pojo.PaymentRequestTransferPojo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentRequestController {

    @Autowired
     PaymentRequestInterface paymentRequestInterface;


    @PostMapping("/addPaymentRequestDetail")
    public ResponseEntity<?> addPaymentRequestDetail(@Valid @RequestBody PaymentRequestTransferPojo paymentRequestTransferPojo/*, BindingResult bindingResult*/) throws Exception {
        return paymentRequestInterface.requestPayment(paymentRequestTransferPojo/*, bindingResult*/);
    }
}
