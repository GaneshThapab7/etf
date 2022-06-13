package com.infodev.eft_rtgs.enms;

import com.infodev.eft_rtgs.exception.NotFoundException;

public enum CronStatus {
    READY(0,"logon"),PENDING(1,"pending"),SUCCESS(2,"transfer"),FAILED(3,"failed"),REUPDATE(4,"reupdate");


    private Integer key;
    private String value;


    CronStatus(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static CronStatus getByKey(Integer key) throws NotFoundException {
        if (key == null)
            throw new IllegalArgumentException("cron_status Status Key cannot be null");
        CronStatus[] Statuses = values();
        for (CronStatus cron_status : Statuses) {
            if (key.equals(cron_status.key)) {
                return cron_status;
            }
        }

        throw new NotFoundException("Acknowledgement Status Not Found");
    }
}
