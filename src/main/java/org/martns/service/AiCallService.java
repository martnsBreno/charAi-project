package org.martns.service;

import java.util.Arrays;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.martns.dto.ApiResponseDto;
import org.martns.dto.MessageDto;
import org.martns.dto.RequestDto;
import org.martns.dto.UserBackResponseDto;
import org.martns.dto.UserPromptDto;
import org.martns.restclient.AiRestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiCallService {

    @Inject
    @RestClient
    AiRestClient aiRestClient;

    @ConfigProperty(name = "API_KEY")
    private String API_KEY_AUTH;

    @ConfigProperty(name = "AI_MODEL")
    private String AI_MODEL;

    public UserBackResponseDto realizarChamada(UserPromptDto userPromptDto) throws JsonProcessingException {

        RequestDto requestDTO = buildaRequestDto(userPromptDto);

        ObjectMapper mapper = new ObjectMapper();

        ApiResponseDto respostaAi = new ApiResponseDto();

        respostaAi = aiRestClient.getRespostaAi("Bearer " + API_KEY_AUTH,
                mapper.writeValueAsString(requestDTO));

        String contentAi = respostaAi.getChoices().get(0).getMessage().getContent();
        String roleAi = respostaAi.getChoices().get(0).getMessage().getRole();

        return new UserBackResponseDto(roleAi, contentAi);
    }

    private RequestDto buildaRequestDto(UserPromptDto userPromptDto) {

        MessageDto systemMessage = new MessageDto("system", "You are a helpful assistant.");

        MessageDto userMessage = new MessageDto(userPromptDto.getUserRole(), userPromptDto.getUserMessage());

        return new RequestDto(AI_MODEL, Arrays.asList(systemMessage, userMessage));

    }

}
