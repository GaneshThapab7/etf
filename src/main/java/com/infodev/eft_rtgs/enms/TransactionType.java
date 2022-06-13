package com.infodev.eft_rtgs.enms;

import com.infodev.eft_rtgs.exception.NotFoundException;

public enum  TransactionType {
    RTGS(1,"rtgs"),ONUS(2,"onus"),OFFUS(3,"offus");

    private Integer key;
    private String value;


    TransactionType(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static TransactionType getByKey(Integer key) throws NotFoundException {
        if (key == null)
            throw new IllegalArgumentException("stpRequestType Status Key cannot be null");
        TransactionType[] Statuses = values();
        for (TransactionType transactionType : Statuses) {
            if (key.equals(transactionType.key)) {
                return transactionType;
            }
        }

        throw new NotFoundException("Acknowledgement Status Not Found");
    }
}
