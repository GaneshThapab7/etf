package com.infodev.eft_rtgs.globalResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TsaApiResponse<T> {
    private String status;
    private String message;
    private String transctionID;
    private PaymentReq paymentRequest;


    public TsaApiResponse(String status, String message, String transctionID) {
        this.status = status;
        this.message = message;
        this.transctionID = transctionID;
    }
}