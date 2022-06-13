package com.infodev.eft_rtgs.Controller.UserController;


import com.google.gson.Gson;
import com.infodev.eft_rtgs.Model.userManagement.User;

import com.infodev.eft_rtgs.globalResponse.APIResponse;
import com.infodev.eft_rtgs.pojo.UserPojo;

import com.infodev.eft_rtgs.view.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService implements UserInterface{

    @Autowired
    private UserRepo userRepo;
  @Autowired
    private PasswordEncoder passwordEncoder;

    Gson gson=new Gson();

    @Override
    public ResponseEntity<APIResponse> registerUser(UserPojo userpojo) {
  if(userRepo.findByUserName(userpojo.getUserName())!=null){
      return new ResponseEntity<APIResponse>(new APIResponse(false,"failed", "Username Already Exist!!", userpojo.getUserName()), new HttpHeaders(), HttpStatus.BAD_REQUEST);

  }
    User user=gson.fromJson(gson.toJson(userpojo),User.class);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
        userpojo.setPassword("");

        userRepo.save(user);

        return new ResponseEntity<APIResponse>(new APIResponse(true,"success", "User register success", userpojo), new HttpHeaders(), HttpStatus.OK);
    }
}
