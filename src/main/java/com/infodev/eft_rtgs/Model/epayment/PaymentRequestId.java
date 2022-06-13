package com.infodev.eft_rtgs.Model.epayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestId implements Serializable {
    private Long id;
    private String poCode;
    private String accountCd;
    private String poRequestNo;


}
