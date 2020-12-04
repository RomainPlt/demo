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

    public Imc test_imc;


    @GetMapping("/secretimc")
    public Greeting greeting1(@RequestParam(value = "name", defaultValue = "World") String name) {
        String spoids=System.getenv("SECRET_POIDS");
        String staille=System.getenv("SECRET_TAILLE");
        test_imc = new Imc(spoids, staille);

        return new Greeting(counter.incrementAndGet(), String.format(template, name), test_imc.getImc());
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name,
                             @RequestParam(value = "question", defaultValue = "Est-ceque ça marche ?") String question) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name), String.format(questTemplate, question));
    }
}