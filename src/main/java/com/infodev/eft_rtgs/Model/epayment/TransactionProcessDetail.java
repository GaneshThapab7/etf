package com.infodev.eft_rtgs.Model.epayment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infodev.eft_rtgs.enms.TransactionStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "transaction_process_detail")
public class TransactionProcessDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String instructionId;
    private TransactionStatus transactionStatus;
    private Integer requestNumber;
    private String remarks;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name="paymentRequestDetail_id", nullable=false)
    private PaymentRequestDetail paymentRequestDetail;
}
