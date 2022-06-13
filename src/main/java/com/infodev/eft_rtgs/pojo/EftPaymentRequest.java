package com.infodev.eft_rtgs.pojo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EftPaymentRequest {
    @NotNull(message = "POCODE cannot be NULL")
    @NotEmpty(message = "POCODE Cannot be Empty")
    private String poCode;
    private String accountCd;
    @NotNull(message = "PO REQUEST NO cannot be NULL")
    private String poRequestNo;
    private String batchId;
    @NotNull(message = "Debtor Agent Cannot be NULL")
    private String debtorAgent;
    @NotEmpty(message="Category Purpose Cannot be EMPTY")
    @NotNull(message = "Category Purpose Cannot be NULL")
    private String categoryPurpose;

    private String debtorBranch;
    @NotNull(message = "Debtor Name Cannot Be NULL")
    private String debtorName;
    @NotNull(message = "Debtor Account Cannot Be NULL")
    @Size(max = 20, message = "Debtors Account Number Cannot Be More then 20")
    private String debtorAccount;
    private String debtorIdType;
    private String debtorIdValue;
    private String debtorAddress;
    private String debtorPhone;
    private String debtorMobile;
    private String debtorEmail;

}
