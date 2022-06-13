package com.infodev.eft_rtgs.Controller.PaymentRequestDetailController.bankDetailMappingService;

import com.infodev.eft_rtgs.Model.epayment.EftNchlRbbBankMapping;
import com.infodev.eft_rtgs.view.EftNchlRbbBankMappingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EftNchlRbbBankMappingService implements EftNchlRbbBankMappingInterface {


    @Autowired
    private EftNchlRbbBankMappingRepo nchlRbbBankMappingRepo;


    @Override
    public EftNchlRbbBankMapping findById() {
        return null;
    }

    @Override
    public EftNchlRbbBankMapping findByNrbCode(String nrbBankCode) {

        EftNchlRbbBankMapping mapping= nchlRbbBankMappingRepo.getByBankCode(nrbBankCode);
        if(mapping == null){
            throw new RuntimeException("NCHL Bank Code Not Found");
        }
        return mapping;
    }
}
