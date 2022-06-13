package com.infodev.eft_rtgs.Model.epayment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infodev.eft_rtgs.enms.TransactionStatus;
import com.infodev.eft_rtgs.enms.TransactionType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

@Getter
@Setter
@Entity
@Table(name = "EFT_PAYMENT_REQUEST_DETAIL")
public class PaymentRequestDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PAYMENT_DETAIL_ID")
    @JsonProperty("id")
    private long paymentDetailId;
    private int chequeNo;
    @Column(unique = true,name = "INSTRUCTION_ID")
    private String instructionId;
    private String endToEndId;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    private String purpose;
    private String creditorAgent;
    private String creditorBranch;
    @Column(name = "CREDITOR_NAME")
    private String creditorName;
    private String creditorAccount;
    private String creditorType;
    private String creditorIdValue;
    private String creditorAddress;
    private String creditorPhone;
    private String creditorMobile;
    private String creditorEmail;

    private String nRemarks;
    private String status;
    private char approvedFlag;
  //  private String nchlTransactionType;
    private TransactionType transactionType;
    private Date entryNdate;
    private Date entryEdate;
    private Date transferEdate;
    private Date transferNdate;
    private String batchId;
    private String reasonCode;
    private String creditReasonDesc;
    private String nchlResponseCode;
    private String nchlResponseMessage;
    private String nchlCreditStatus;
    private String recDate;
    private String creditorNrbBankCode;
    private String creditorNrbBranchCode;
    private int addenda1;
    @Column(nullable = true)
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
    private Integer nchlBatchId;
    private Integer nchlId;
    private TransactionStatus transactionStatus;


    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumns({
            @JoinColumn(name = "id", referencedColumnName = "id"),
            @JoinColumn(name = "poCode", referencedColumnName = "poCode"),
            @JoinColumn(name = "accountCd", referencedColumnName = "accountCd"),
            @JoinColumn(name = "poRequestNo", referencedColumnName = "poRequestNo")
    })
    private PaymentRequest paymentRequest;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "paymentRequestDetail")
    })
    private List<TransactionProcessDetail> transactionProcessDetails = new ArrayList<>();

}

