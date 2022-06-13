package com.infodev.eft_rtgs.Controller.PaymentRequestDetailController;

import com.infodev.eft_rtgs.pojo.PaymentRequestTransferPojo;
import com.infodev.eft_rtgs.globalResponse.TsaApiResponse;
import org.springframework.http.ResponseEntity;

public interface PaymentRequestInterface {
    ResponseEntity<TsaApiResponse> requestPayment(PaymentRequestTransferPojo paymentRequestTransferPojo/*, BindingResult bindingResult*/) throws Exception;

}
