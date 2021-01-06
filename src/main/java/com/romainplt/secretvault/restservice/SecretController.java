package com.romainplt.secretvault.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

@RestController
@RequestMapping("/vault")
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
    public ResponseEntity<String> printDb() throws IOException, NoSuchAlgorithmException {
        File db = new File("/tmp/secretVault/data/database.mv.db");
        MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
        String shaCheckSum = HashUtils.getFileChecksum(shaDigest, db);
        return new ResponseEntity<>("Database's hash : " + shaCheckSum , HttpStatus.OK);
    }

    /*
    Print the fspf volume's hash
     */
    @GetMapping("/volume/print")
    public ResponseEntity<String> printVolume() throws IOException, NoSuchAlgorithmException {
        File volume = new File("/tmp/secretVault/data/volume.fspf");
        MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
        String shaCheckSum = HashUtils.getFileChecksum(shaDigest, volume);
        return new ResponseEntity<>("Volume's hash : " + shaCheckSum , HttpStatus.OK);
    }

    /*
    Retrieve secrets and keys from session on CAS and :
        - Write them in the H2 Database
        - Print them
     */
    @PostMapping("/session/secret")
    public ResponseEntity<String> pushSecretFromSession(){
        String secret=System.getenv("SECRET");
        String key=System.getenv("KEY");
        Secret secretSession = new Secret(secret,key);
        secretService.writeSecretToDB(secretSession);
        return new ResponseEntity<>("Key : " + secretSession.getKey() + "\nSecret : " + secretSession.getSecret()
                + "\nYour secret and key have " +
                "been pushed to the database. You can see them at localhost:8080/h2-console", HttpStatus.OK);
    }

    /*
    Allows you to push secret to the database without going through session or CAS
     */
    @PostMapping("/secret")
    public ResponseEntity<String> pushSecret(@RequestBody() Secret secret) {
        secretService.writeSecretToDB(secret);
        return new ResponseEntity<>( "Key : " + secret.getKey() + "\nSecret : " + secret.getSecret() + "\nYour " +
                "secret and key have been pushed to the database.", HttpStatus.OK);
    }

    /*
    Allows you to get secrets from the H2 database using the corresponding key.
     */
    @GetMapping("/secret")
    public ResponseEntity<String> getSecret(@RequestParam(value = "key", defaultValue = "") String key) throws SQLException {
        Secret unlockedSecret = secretService.getSecretFromDB(key);
        if (unlockedSecret.getSecret() == "") {
            return new ResponseEntity<>("Your secret is empty or this key doesn't exist. You used : " + key, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Key : " + key + "\nSecret : " + unlockedSecret.getSecret(), HttpStatus.OK );
        }
    }

}
