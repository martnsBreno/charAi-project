package org.martns.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.martns.dto.ApiResponseDto;
import org.martns.exception.MuitasRequisicoesException;
import org.martns.exception.SemAutorizacaoException;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(configKey="client-api")
public interface AiRestClient {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    ApiResponseDto getRespostaAi(@HeaderParam("Authorization")String apiKey, String userPromptDto);

    @ClientExceptionMapper
    static RuntimeException tException(Response response) {


        if(response.getStatus() == 401) {
            throw new SemAutorizacaoException("Credenciais Invalidas. Impossivel realizar a requisicao");
        } else if (response.getStatus() == 429) {
            throw new MuitasRequisicoesException("Muitas requisicoes sendo realizadas para a OPEN AI em um curto espaço de tempo.");
        }

        return null;
    }   

} 