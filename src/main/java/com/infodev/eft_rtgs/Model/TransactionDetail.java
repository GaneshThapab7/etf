package com.infodev.eft_rtgs.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.infodev.eft_rtgs.enms.STPRequestType;
import lombok.Data;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = TransactionDetail.class)
@Data
public class TransactionDetail {

    private String transactionId;
    private String amount;
    private STPRequestType stpRequestType;

}

