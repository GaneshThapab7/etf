package com.infodev.eft_rtgs.Model.epayment;

import lombok.Data;

import javax.persistence.*;





@Entity
@Data
@Table(name = "tsa_debtors_bank_detail")
public class TsaDebtorsBankDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    @Column(name = "tsa_bank_code")
    private String tsaBankCode;

    @Basic
    @Column(name = "tsa_bank_account_no",unique = true)
    private String tsaBankAccountNo;
    @Basic
    @Column(name = "nchl_debtor_bank_code")
    private String nchlDebtorBankCode;
    @Basic
    @Column(name = "nchl_debtor_branch_code")
    private String nchlDebtorBranchCode;
}

