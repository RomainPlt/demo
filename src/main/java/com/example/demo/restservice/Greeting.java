package com.example.demo.restservice;

public class Greeting {

    private final long id;
    private final String name;
    private final String question;

    public Greeting(long id, String name, String question) {
        this.id = id;
        this.question = question;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return name;
    }

    public String getQuestion(){
        return question;
    }
}