package com.example.demo.restservice;

public class Greeting {

    private final String name;
    private final String question;

    public Greeting( String name, String question) {
        this.name = name;
        this.question = question;
    }


    public String getName() {
        return name;
    }

    public String getQuestion(){
        return question;
    }
}