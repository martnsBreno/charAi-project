package org.martns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserBackResponseDto {

    @JsonProperty("roleResponse")
    String role;

    @JsonProperty("aiResponse")
    String messageAi;

    public UserBackResponseDto(String role, String messageAi) {
        this.role = role;
        this.messageAi = messageAi;
    }

    public String getRole() {
        return role;
    }

    public String getMessageAi() {
        return messageAi;
    }

    
    
}
