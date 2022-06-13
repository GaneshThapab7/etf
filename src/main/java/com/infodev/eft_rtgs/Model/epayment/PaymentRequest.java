package com.infodev.eft_rtgs.Model.epayment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "EFT_PAYMENT_REQUEST")
@IdClass(PaymentRequestId.class)
public class PaymentRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Id
    private String poCode;
    @Id
    private String accountCd;
    @Id
    private String poRequestNo;
    @Column(unique = true,name = "TRANSACTION_ID")
    private String trasnctionID;


    private String debtorAgent;
    private String debtorBranch;
    private String categoryPurpose;
    private String debtorName;
    private String debtorAccount;
    private String debtorIdType;
    private String debtorIdValue;
    private String debtorAddress;
    private String debtorPhone;
    private String debtorMobile;
    private String debtorEmail;
    private String batchId;
    private String debitStatus;
    private String debitReasonDesc;
    private Date entryNdate;
    private Date entryEdate;
    private String status;
    @Column(nullable = true)
    private char approveFlag;
    private Date transferNdate;
    private Date transferEdate;
    private String responseCode;
    private String debtorNrbBankCode;
    private String debtorNrbBranchCode;
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)

    @JoinColumns({
            @JoinColumn(name = "id", referencedColumnName = "id"),
            @JoinColumn(name = "poCode", referencedColumnName = "poCode"),
            @JoinColumn(name = "accountCd", referencedColumnName = "accountCd"),
            @JoinColumn(name = "poRequestNo", referencedColumnName = "poRequestNo")
    })
    private List<PaymentRequestDetail> paymentRequestDetails = new ArrayList<>();

}