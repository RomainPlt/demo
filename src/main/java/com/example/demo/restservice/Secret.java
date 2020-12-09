package com.example.demo.restservice;

import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Secret {
    private String secret;
    private String key;

    public Secret(String secret, String key){
        this.secret = secret;
        this.key = key;
    }

    public String getSecret(){
        return this.secret;
    }

    public String getKey(){
        return this.key;
    }

    public void writeSecretToDB(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("INSERT INTO Secrets(key,secret) VALUES ('"+this.getKey()+"' ,'"+this.getSecret()+"')");

    }

    public void writeSecretToFile() throws IOException {

        String path = "/home/romain/Documents/rest_app/secrets/" + this.getKey() + ".txt";
        FileWriter fileWriter = new FileWriter(path);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(this.getSecret());
        printWriter.close();

        //Files.writeString(path, contents, StandardCharsets.UTF_8);

    }







}
