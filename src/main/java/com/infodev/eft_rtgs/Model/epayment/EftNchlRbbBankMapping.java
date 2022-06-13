package com.infodev.eft_rtgs.Model.epayment;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "eft_nchl_rbb_bank_mapping")
public class EftNchlRbbBankMapping {
    @Id
    @Basic
    @Column(name = "bank_code")
    private String bankCode;
    @Basic
    @Column(name = "bank_name")
    private String bankName;
    @Basic
    @Column(name = "branch_code")
    private String branchCode;
    @Basic
    @Column(name = "branch_name")
    private String branchName;
    @Basic
    @Column(name = "nrb_bank_code")
    private String nrbBankCode;
    @Basic
    @Column(name = "nrb_branch_code")
    private String nrbBranchCode;
    @Basic
    @Column(name = "npi_bank_code")
    private String npiBankCode;
    @Basic
    @Column(name = "npi_branch_code")
    private String npiBranchCode;
    @Basic
    @Column(name = "rtg_bank_number")
    private String rtgBankNumber;
}