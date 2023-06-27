package org.martns.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CharAiService {

    @ConfigProperty(name = "API_KEY")
    private String API_SECRET;

    @ConfigProperty(name = "AI_MODEL")
    private String AI_MODEL;

    @ConfigProperty(name = "OPENAI_API_URL")
    private String API_URL;

    public void prepararRequisicaoAi() {

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost(API_URL);

        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_SECRET);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        realizarRequisicaoAi(request, httpClient);
        
    }
    
    private void realizarRequisicaoAi(HttpPost request, HttpClient httpClient) {

        try {
            StringEntity params = new StringEntity(prepararJsonRequisicao());
            
            request.setEntity(params);
    
            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();

            String reponseBody = EntityUtils.toString(entity);

            System.out.println(reponseBody);
            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private String prepararJsonRequisicao() {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        ObjectNode systemNode = mapper.createObjectNode();
        systemNode.put("role", "system");
        systemNode.put("content", "You are Luffy from One Piece");

        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("role", "user");
        userNode.put("content", "Hello!");

        rootNode.set("model", mapper.valueToTree(AI_MODEL));
        rootNode.set("messages", mapper.valueToTree(new ObjectNode[] { systemNode, userNode }));

        return rootNode.toString();

    }
}
