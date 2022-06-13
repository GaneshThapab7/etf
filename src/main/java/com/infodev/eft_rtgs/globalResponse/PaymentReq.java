package com.infodev.eft_rtgs.globalResponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
@Data
public class PaymentReq {
    @JsonIgnore
    private Long id;
    private String poCode;
    private String accoundCd;
    private String poRequestNo;
    private String status;
    private String transctionId;
    private List<PaymentRequestDto> paymentRequestDetails;
}
