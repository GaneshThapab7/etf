package com.infodev.eft_rtgs.Controller.PaymentRequestDetailController.bankDetailMappingService;

import com.infodev.eft_rtgs.Model.epayment.EftNchlRbbBankMapping;

public interface EftNchlRbbBankMappingInterface {
    EftNchlRbbBankMapping findById();
    EftNchlRbbBankMapping findByNrbCode(String nrbBankCode);
}
