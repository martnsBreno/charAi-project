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

    public void testarApi() {

        try {

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();

            ObjectNode systemNode = mapper.createObjectNode();
            systemNode.put("role", "system");
            systemNode.put("content", "You are Luffy from One Piece");

            ObjectNode userNode = mapper.createObjectNode();
            userNode.put("role", "user");
            userNode.put("content", "Whats your dream? What do you think about Zoro?");

            rootNode.set("model", mapper.valueToTree("gpt-3.5-turbo"));
            rootNode.set("messages", mapper.valueToTree(new ObjectNode[] { systemNode, userNode }));

            String jsonString = rootNode.toString();

            HttpClient httpClient = HttpClientBuilder.create().build();

            String apiUrl = "https://api.openai.com/v1/chat/completions";

            HttpPost request = new HttpPost(apiUrl);

            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_SECRET);
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            StringEntity params = new StringEntity(jsonString);
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();

            String responseBody = EntityUtils.toString(entity);

            System.out.println(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
