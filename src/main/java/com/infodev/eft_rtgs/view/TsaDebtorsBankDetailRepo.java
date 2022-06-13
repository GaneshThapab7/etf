package com.infodev.eft_rtgs.view;

import com.infodev.eft_rtgs.Model.epayment.TsaDebtorsBankDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TsaDebtorsBankDetailRepo extends CrudRepository<TsaDebtorsBankDetail, Integer> {

 //   @Query("select c from TsaDebtorsBankDetail c where c.tsaBankAccountNo = :account",nativeQuery=true)
    TsaDebtorsBankDetail findFirstByTsaBankAccountNoOrderByTsaBankAccountNo(String account);

}