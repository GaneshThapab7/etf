package com.infodev.eft_rtgs.enms;

import com.infodev.eft_rtgs.exception.NotFoundException;

public enum STPRequestType {

    LOGON(1,"logon"),TRANSFER(2,"transfer"),LOGOUT(3,"logout");

    private Integer key;
    private String value;


    STPRequestType(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static STPRequestType getByKey(Integer key) throws NotFoundException {
        if (key == null)
            throw new IllegalArgumentException("stpRequestType Status Key cannot be null");
        STPRequestType[] Statuses = values();
        for (STPRequestType stpRequestType : Statuses) {
            if (key.equals(stpRequestType.key)) {
                return stpRequestType;
            }
        }

        throw new NotFoundException("Acknowledgement Status Not Found");
    }
}
