package com.cms.blog.dto;

public class AuthReponseDto {

    private String token;

    public AuthReponseDto(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
