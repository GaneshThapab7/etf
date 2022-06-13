package com.infodev.eft_rtgs.pojo;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserPojo {
    @NotNull(message = "User Name cannot be null")
    private String userName;
    private String password;
    private String poCode;
    private String userEname;
    private String userNname;
    private String userRole;
    private int subDistrictCode;
    private int districtCode;
}
