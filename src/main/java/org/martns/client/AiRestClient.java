package org.martns.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.martns.dto.ApiResponseDto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey="client-api")
@RegisterForReflection
public interface AiRestClient {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    ApiResponseDto getRespostaAi(@HeaderParam("Authorization")String apiKey, String userPromptDto);

} 