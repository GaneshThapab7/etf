package com.infodev.eft_rtgs.pojo;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class EftPaymentRequestDetail {
    @NotNull(message = "Instruction Id Cannot Be Null")
    private String instructionId;
    @NotNull(message = "Cheque Number Cannot Be Null")
    private int chequeNo;
    @NotNull(message = "End To End Id Cannot Be Null")
    private String endToEndId;
    @NotNull(message = "Amount Cannot Be Null")
    private BigDecimal amount;

    private String purpose;
    @NotNull(message = "Creditor Agent Cannot Be Null")
    private String creditorAgent;

    private String creditorBranch;
    @NotNull(message = "Creditor Name Cannot Be Null")
    private String creditorName;
    @NotNull(message = "Creditor Account Cannot Be Null")
    @Size(max = 20 , message = "Creditor Account Cannot Be More than 20")
    private String creditorAccount;
    private String creditorType;
    private String creditorIdValue;
    private String creditorIdType;
    private String creditorAddress;
    private String creditorPhone;

    private String creditorMobile;
    private String creditorEmail;

    //to provide extra information abt the transaction..
    private int addenda1;
    private Date addenda2;
    private String addenda3;
    private String addenda4;
    //to identify from where the transaction is being initiated..
    private String channelId;
    //extra info abt transaction to be more specific..
    private String freeCode1;
    private String freeCode2;
    private String freeText1;
    private String freeText2;
    private Date entryDate;
    private Date transferDate;
    private String creditStatus;
    private String creditReasonDesc;
    private String paymentStatus;
    private String paymentMessage;
    private int paymentGatewayId;
    private String settlementDate;
    private Long id;
    private String batchId;
    private Long paymentDetailId;
}
