package com.cms.blog.dto;

public class MessageReponseDto {

    private String message;

    public MessageReponseDto(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
