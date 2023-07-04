package org.martns.dto;

import jakarta.validation.constraints.NotBlank;

public class UserPromptDto {
    
    @NotBlank
    String userMessage;

    @NotBlank
    String userRole = "user";

    public String getUserMessage() {
        return userMessage;
    }

    public String getUserRole() {
        return userRole;
    }
    
}
