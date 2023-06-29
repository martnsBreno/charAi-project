package org.martns.rest;

import java.util.Arrays;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.martns.client.AiRestClient;
import org.martns.dto.ApiResponseDto;
import org.martns.dto.MessageDto;
import org.martns.dto.RequestDto;
import org.martns.dto.UserBackResponseDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/test")
@ApplicationScoped
public class AiCallRest {

    @Inject
    @RestClient
    AiRestClient aiRestClient;

    @ConfigProperty(name = "API_KEY")
    private String API_KEY_AUTH;

    @ConfigProperty(name = "AI_MODEL")
    private String AI_MODEL;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response consumeAi() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        MessageDto systemMessage = new MessageDto("system", "You are a helpful assistant.");

        MessageDto userMessage = new MessageDto("user", "Hello");

        RequestDto requestDTO = new RequestDto(AI_MODEL, Arrays.asList(systemMessage, userMessage));

        ApiResponseDto respostaAi = aiRestClient.getRespostaAi("Bearer " + API_KEY_AUTH, mapper.writeValueAsString(requestDTO));

        String contentAi = respostaAi.getChoices().get(0).getMessage().getContent();
        String roleAi = respostaAi.getChoices().get(0).getMessage().getRole();

        UserBackResponseDto responseUser = new UserBackResponseDto(roleAi, contentAi);

        return Response.ok().entity(responseUser).build();
    }
}
