package com.example.demo.restservice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/demo")
public class GreetingController {

    private static final String template = "Hello, %s!";
    private static final String questTemplate = "%s ?";
    private final AtomicLong counter = new AtomicLong();
    public Imc test_imc;
    public Secret secret;

    @GetMapping("/pushsecret")
    public Greeting pushSecret(@RequestParam(value = "secret", defaultValue = "mySecret") String pushedSecret,
                               @RequestParam(value = "key", defaultValue = "mickey") String pushedKey) throws IOException {

        this.secret  = new Secret(pushedSecret, pushedKey);

        secret.writeSecret();

        return new Greeting(counter.incrementAndGet(), "Your secret is : " + secret.getSecret(), "Your key for your secret is : " + secret.getKey());
    }

    @GetMapping("/getsecret")
    public Greeting getSecret(@RequestParam(value = "key", defaultValue = "") String key) throws IOException {

        //get secret from file using key
        String unlockedSecret = getSecretFromKey(key);

        return new Greeting(counter.incrementAndGet(), "You used this key : " + key, "Your secret is : " + unlockedSecret);
    }


    public String getSecretFromKey(String key) throws IOException {
        Path pathToFile = Path.of("/home/romain/Documents/rest_app/secrets/" + key + ".txt");
        String content = Files.readString(pathToFile);
        return content;
    }


    @GetMapping("/secretimc")
    public Greeting greeting1(@RequestParam(value = "name", defaultValue = "World") String name) {
        String sWeight=System.getenv("SECRET_WEIGHT");
        String sHeight=System.getenv("SECRET_HEIGHT");
        test_imc = new Imc(sWeight, sHeight);
        return new Greeting(counter.incrementAndGet(), String.format(template, name), test_imc.getImc());
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name,
                             @RequestParam(value = "question", defaultValue = "Est-ceque ça marche ?") String question) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name), String.format(questTemplate, question));
    }
}