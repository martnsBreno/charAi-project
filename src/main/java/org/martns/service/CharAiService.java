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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

    ObjectMapper mapper = new ObjectMapper();

    public String prepararRequisicaoAi(String userPrompt) {

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost(API_URL);

        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_SECRET);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        return realizarRequisicaoAi(request, httpClient, userPrompt);

    }

    private String realizarRequisicaoAi(HttpPost request, HttpClient httpClient, String userPrompt) {

        String respostaAi = "";

        try {
            StringEntity params = new StringEntity(prepararJsonRequisicao(userPrompt));

            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();

            respostaAi = retornarRespostaAi(EntityUtils.toString(entity));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return respostaAi;
    }

    private String retornarRespostaAi(String jsonString) throws JsonMappingException, JsonProcessingException {

        JsonNode jsonNode = mapper.readTree(jsonString);

        JsonNode messagesNode = jsonNode.get("choices").get(0).get("message");

        String roleText = messagesNode.get("role").asText();

        String contentText = messagesNode.get("content").asText();

        ObjectNode rootNode = mapper.createObjectNode();

        ArrayNode arrayNode = mapper.createArrayNode();

        ObjectNode userMessageNode = mapper.createObjectNode();
        userMessageNode.put("role", roleText);
        userMessageNode.put("content", contentText);

        arrayNode.add(userMessageNode);

        rootNode.set("messages", messagesNode);

        return mapper.writeValueAsString(rootNode);
    }

    private String prepararJsonRequisicao(String userPrompt) {

        ObjectNode rootNode = mapper.createObjectNode();

        ObjectNode systemNode = mapper.createObjectNode();
        systemNode.put("role", "system");
        systemNode.put("content", "Youre Darth Vader");

        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("role", "user");
        userNode.put("content", userPrompt);

        rootNode.set("model", mapper.valueToTree(AI_MODEL));
        rootNode.set("messages", mapper.valueToTree(new ObjectNode[] { systemNode, userNode }));

        return rootNode.toString();

    }
}
