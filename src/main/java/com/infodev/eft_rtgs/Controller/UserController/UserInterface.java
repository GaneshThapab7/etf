package com.infodev.eft_rtgs.Controller.UserController;

import com.infodev.eft_rtgs.globalResponse.APIResponse;
import com.infodev.eft_rtgs.pojo.UserPojo;
import org.springframework.http.ResponseEntity;

public interface UserInterface {

    ResponseEntity<APIResponse> registerUser(UserPojo userpojo);



}
