package com.infodev.eft_rtgs.enms;

import com.infodev.eft_rtgs.exception.NotFoundException;

public enum ResponseFileType {
    ERROR(1,"logon"),SUCCESS(2,"transfer"),FAILED(3,"logout"),
    OUTPUT(4,"output");

    private Integer key;
    private String value;


    ResponseFileType(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static ResponseFileType getByKey(Integer key) throws NotFoundException {
        if (key == null)
            throw new IllegalArgumentException("responseFileType Status Key cannot be null");
        ResponseFileType[] Statuses = values();
        for (ResponseFileType responseFileType : Statuses) {
            if (key.equals(responseFileType.key)) {
                return responseFileType;
            }
        }

        throw new NotFoundException("Acknowledgement Status Not Found");
    }
}
