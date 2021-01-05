package com.romainplt.secretVault.restservice;

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

    public Secret(String secret, String key) {
        this.secret = secret;
        this.key = key;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getKey() {
        return this.key;
    }

    public void writeSecretToDB(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("INSERT INTO Secrets(key,secret) VALUES ('" + this.getKey() + "' ,'" + this.getSecret() + "')");

    }

}
