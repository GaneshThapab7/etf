package com.infodev.eft_rtgs.globalResponse;

import com.infodev.eft_rtgs.pojo.UserPojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Scope("prototype")
public class APIResponse {
    @NotNull
    private boolean status;
    @NotNull
    private String message;

    private Object data;

    private Object errors;


}
