package com.infodev.eft_rtgs.globalResponse;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private Long id;
    private String instructionId;
    private String status;
}
