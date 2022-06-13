package com.infodev.eft_rtgs.enms;

import com.infodev.eft_rtgs.exception.NotFoundException;

public enum TransactionStatus {
    INITILIZED(1,"initilized"),SUCCESS(2,"success"),FAILED(3,"failed"),
    REREQUEST(4,"rerequest");

    private Integer key;
    private String value;


    TransactionStatus(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static TransactionStatus getByKey(Integer key) throws NotFoundException {
        if (key == null)
            throw new IllegalArgumentException("transaction Status Key cannot be null");
        TransactionStatus[] Statuses = values();
        for (TransactionStatus transactionStatus : Statuses) {
            if (key.equals(transactionStatus.key)) {
                return transactionStatus;
            }
        }

        throw new NotFoundException("transaction Status Not Found");
    }
}
