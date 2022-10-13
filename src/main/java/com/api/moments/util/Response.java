package com.api.moments.util;

import java.util.List;

import org.springframework.http.HttpStatus;

public class Response<T> {

    private HttpStatus status;
    private T body;
    private List<String> messages;

    public Response() {

    }

    public Response(HttpStatus status, T body, List<String> messages) {
        this.status = status;
        this.body = body;
        this.messages = messages;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    
}
