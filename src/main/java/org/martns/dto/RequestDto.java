package org.martns.dto;

import java.util.List;

public class RequestDto {

    String model;

    List<MessageDto> messages;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }

    public RequestDto(String model, List<MessageDto> messages) {
        this.model = model;
        this.messages = messages;
    }

    
    
}
