package com.example.demo.restservice;

public class Greeting {

    private final String secret;
    private final String key;

    public Greeting( String secret, String key) {
        this.secret = secret;
        this.key = key;
    }


    public String getName() {
        return secret;
    }

    public String getQuestion(){
        return key;
    }
}