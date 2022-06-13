package com.infodev.eft_rtgs.view;

import com.infodev.eft_rtgs.Model.epayment.EftNchlRbbBankMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EftNchlRbbBankMappingRepo extends CrudRepository<EftNchlRbbBankMapping, String> {
    //@Query(value="select * from tsa_debtors_bank_detail k  WHERE  k.tsa_bank_account_no = ?1 order by k.tsa_bank_account_no DESC ", nativeQuery=true)
  //  EftNchlRbbBankMapping bankCodeByRbbBankCode(String rbbBankCode);


    EftNchlRbbBankMapping getByBankCode(String bankcode);
}