package com.infodev.eft_rtgs.view;

import com.infodev.eft_rtgs.Model.epayment.EftNchlRbbBankMapping;
import com.infodev.eft_rtgs.Model.epayment.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRequestRepo extends CrudRepository<PaymentRequest,String> {

    PaymentRequest findFirstByTrasnctionID(String rbbBankCode);
}
