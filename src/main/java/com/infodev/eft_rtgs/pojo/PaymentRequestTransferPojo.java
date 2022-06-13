package com.infodev.eft_rtgs.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentRequestTransferPojo {
    @Valid
    private EftPaymentRequest paymentRequest;
    @Valid
    private List<EftPaymentRequestDetail> eftPaymentRequestDetail;


}
