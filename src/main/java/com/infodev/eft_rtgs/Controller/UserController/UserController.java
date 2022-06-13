package com.infodev.eft_rtgs.Controller.UserController;


import com.infodev.eft_rtgs.globalResponse.APIResponse;
import com.infodev.eft_rtgs.pojo.UserPojo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/register")
public class UserController {

    private UserInterface userInterface;





    @PostMapping
    public ResponseEntity<APIResponse> saveuser(@RequestBody UserPojo userPojo){
        return userInterface.registerUser(userPojo);

    }

}
