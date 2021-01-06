package com.romainplt.secretvault.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

@RestController
@RequestMapping("/secretvault")
public class SecretController {

    public Secret secret;
    public SecretService secretService;

    public SecretController(SecretService secretService){
        this.secretService = secretService;
    }

    /*
    Print the database's hash
     */
    @GetMapping("/db/print")
    public SecretPrinter printDb() throws IOException, NoSuchAlgorithmException {
        File db = new File("/tmp/secretVault/data/database.mv.db");
        MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
        String shaCheckSum = HashUtils.getFileChecksum(shaDigest, db);
        return new SecretPrinter(shaCheckSum , "database");
    }

    /*
    Print the fspf volume's hash
     */
    @GetMapping("/volume/print")
    public SecretPrinter printVolume() throws IOException, NoSuchAlgorithmException {
        File volume = new File("/tmp/secretVault/data/volume.fspf");
        MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
        String shaCheckSum = HashUtils.getFileChecksum(shaDigest, volume);
        return new SecretPrinter(shaCheckSum , "volume");
    }

    /*
    Retrieve secrets and keys from session on CAS and :
        - Write them in the H2 Database
        - Print them
     */
    @GetMapping("/secret")
    public SecretPrinter pushSecretFromSession(){
        String secret=System.getenv("SECRET");
        String key=System.getenv("KEY");
        Secret secretSession = new Secret(secret,key);
        secretService.writeSecretToDB(secretSession);
        return new SecretPrinter(secretSession.getSecret() , secretSession.getKey(), "Your secret and key have " +
                "been pushed to the database. You can see them at localhost:8080/h2-console");
    }

    /*
    Allows you to push secret to the database without going through session or CAS
     */
    @GetMapping("/pushsecret")
    public SecretPrinter pushSecret(@RequestParam(value = "secret", defaultValue = "mySecret") String pushedSecret,
                               @RequestParam(value = "key", defaultValue = "mickey") String pushedKey) {
        this.secret  = new Secret(pushedSecret, pushedKey);
        secretService.writeSecretToDB(secret);
        return new SecretPrinter( secret.getSecret(), secret.getKey());
    }

    /*
    Allows you to get secrets from the H2 database using the corresponding key.
     */
    @GetMapping("/getsecret")
    public SecretPrinter getSecret(@RequestParam(value = "key", defaultValue = "") String key) throws SQLException {
        Secret unlockedSecret = secretService.getSecretFromDB(key);
        if (unlockedSecret.getSecret() == "") {
            return new SecretPrinter("Your secret is empty or this key doesn't exist.", key);
        } else {
            return new SecretPrinter(unlockedSecret.getSecret(), key );
        }
    }

}
