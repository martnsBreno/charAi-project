package org.martns.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.martns.dto.ExceptionDto;
import org.martns.dto.UserBackResponseDto;
import org.martns.dto.UserPromptDto;
import org.martns.exception.MuitasRequisicoesException;
import org.martns.exception.SemAutorizacaoException;
import org.martns.service.AiCallService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonProcessingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class AiControllerTest {

    @Mock
    private AiCallService aiCallServiceMock;

    @InjectMocks
    private AiController aiController;

    UserPromptDto userPromptDto;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        userPromptDto = new UserPromptDto();
    }

    @Test
    void testConsumeAi_Success() throws JsonProcessingException {
        UserBackResponseDto responseUser = new UserBackResponseDto("assistant", "Im your assistant");

        when(aiCallServiceMock.realizarChamada(userPromptDto)).thenReturn(responseUser);

        Response response = aiController.consumeAi(userPromptDto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(responseUser, response.getEntity());
        verify(aiCallServiceMock, times(1)).realizarChamada(userPromptDto);
    }

    @Test
    void testConsumeAi_SemAutorizacaoException() throws JsonProcessingException {
        when(aiCallServiceMock.realizarChamada(userPromptDto)).thenThrow(new SemAutorizacaoException("Sem autorização"));

        Response response = aiController.consumeAi(userPromptDto);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Sem autorização", ((ExceptionDto) response.getEntity()).getMensagemDeErro());
        verify(aiCallServiceMock, times(1)).realizarChamada(userPromptDto);
    }

    @Test
    void testConsumeAi_MuitasRequisicoesException() throws JsonProcessingException {
        when(aiCallServiceMock.realizarChamada(userPromptDto)).thenThrow(new MuitasRequisicoesException("Muitas requisições"));

        Response response = aiController.consumeAi(userPromptDto);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Muitas requisições", ((ExceptionDto) response.getEntity()).getMensagemDeErro());
        verify(aiCallServiceMock, times(1)).realizarChamada(userPromptDto);
    }
}
