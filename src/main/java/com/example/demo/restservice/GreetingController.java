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
    public Imc test_imc;
    private String string_imc;


    @GetMapping("/secretimc")
    public Greeting greeting1(@RequestParam(value = "name", defaultValue = "World") String name,
                             @RequestParam(value = "question", defaultValue = "Comment ça va ?") String question) {
        spoids=System.getenv("POIDS");
        staille=System.getenv("TAILLE");
        string_imc = "poids : " + spoids + " et taille : " + staille;
        return new Greeting(counter.incrementAndGet(), string_imc, String.format(questTemplate, question));
    }

    
    @GetMapping("/imc")
    public Imc imc(){
        poids=System.getenv("POIDS");
        taille=System.getenv("TAILLE");
        System.out.print("System.getenv('POIDS') = ");
        System.out.println(System.getenv("POIDS"));
        System.out.print("System.getenv('TAILLE') = ");
        System.out.println(System.getenv("TAILLE"));
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
                             @RequestParam(value = "question", defaultValue = "Est-ceque ça marche ?") String question) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name), String.format(questTemplate, question));
    }
}