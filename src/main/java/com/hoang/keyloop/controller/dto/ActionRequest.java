package com.hoang.keyloop.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActionRequest {
    private String status;
    
    @NotBlank(message = "Type cannot be blank")
    private String type;
    
    private String note;
}
