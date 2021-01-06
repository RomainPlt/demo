package com.romainplt.secretvault.restservice;

import org.springframework.jdbc.core.JdbcTemplate;

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

}
