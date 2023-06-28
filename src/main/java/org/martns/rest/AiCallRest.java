package org.martns.rest;

import org.martns.dto.UserPromptDto;
import org.martns.service.CharAiService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/ai")
public class AiCallRest {

    @Inject
    CharAiService charAiService;

    @GET
    @Path("/call")
    @Produces(MediaType.APPLICATION_JSON)
    public String reqAi(UserPromptDto userPromptDto) {

        return charAiService.prepararRequisicaoAi(userPromptDto.getUserMessage());

    }
}
