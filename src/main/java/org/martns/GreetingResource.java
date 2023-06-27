package org.martns;

import org.martns.service.CharAiService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @Inject
    CharAiService charAiService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        charAiService.prepararRequisicaoAi();
        return "Hello from RESTEasy Reactive";

    }
}
