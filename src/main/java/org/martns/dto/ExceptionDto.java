package org.martns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ExceptionDto {
    
    @JsonProperty("errorMessage")
    String mensagemDeErro;

    public ExceptionDto(String mensagemDeErro) {
        this.mensagemDeErro = mensagemDeErro;
    }

    
}
