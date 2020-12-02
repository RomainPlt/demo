package com.example.demo.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private static final String questTemplate = "%s ?";
    private final AtomicLong counter = new AtomicLong();
    private String poids;
    private String taille;
    private String spoids;
    private String staille;


    @GetMapping("/imc")
    public Imc imc(){
        poids=System.getenv("POIDS");
        taille=System.getenv("TAILLE");

        return new Imc(poids, taille);
    }

    //@GetMapping("/secretimc")
    //public Imc secretImc(){
    //    spoids=System.getenv("SECRET_POIDS");
    //    staille=System.getenv("SECRET_TAILLE");

    //    return new Imc(spoids, staille);

    //}

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name,
                             @RequestParam(value = "question", defaultValue = "Comment ça va ?") String question) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name), String.format(questTemplate, question));
    }
}