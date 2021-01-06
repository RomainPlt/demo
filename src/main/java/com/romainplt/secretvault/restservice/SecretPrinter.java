package com.romainplt.secretvault.restservice;

public class SecretPrinter {

    private final String secret;
    private final String key;
    private final String message;


    public SecretPrinter(String secret, String key) {
        this.secret = secret;
        this.key = key;
        this.message = "";
    }

    public SecretPrinter( String secret, String key, String message) {
        this.secret = secret;
        this.key = key;
        this.message = message;
    }

    public String getSecret() {
        return secret;
    }

    public String getKey(){
        return key;
    }

    public String getMessage(){
        return message;
    }


}
