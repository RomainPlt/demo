package com.example.demo.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping("/demo")
public class GreetingController {

    private static final String template = "Hello, %s!";
    private static final String questTemplate = "%s ?";
    private final AtomicLong counter = new AtomicLong();
    public Imc test_imc;
    public Secret secret;
    public static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection("jdbc:h2:file:/home/romain/Documents/rest_app/demo/data/secretsDB", "sa", "admin");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    @Autowired
    private JdbcTemplate jdbcTemplate;

    public GreetingController(JdbcTemplate jdbcTemplate){
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Secrets (key VARCHAR(255), secret VARCHAR(255));");
    }

    @GetMapping("/pushsecret")
    public Greeting pushSecret(@RequestParam(value = "secret", defaultValue = "mySecret") String pushedSecret,
                               @RequestParam(value = "key", defaultValue = "mickey") String pushedKey) throws IOException {

        this.secret  = new Secret(pushedSecret, pushedKey);

        this.secret.writeSecretToDB(jdbcTemplate);
        //secret.writeSecretToFile();

        return new Greeting( "Your secret is : " + secret.getSecret(), "Your key for your secret is : " + secret.getKey());
    }

    @GetMapping("/getsecret")
    public Greeting getSecret(@RequestParam(value = "key", defaultValue = "") String key) throws IOException, SQLException {

        //get secret from DB
        Secret unlockedSecret = getSecretFromDB(key);

        //get secret from file using key
        //String unlockedSecret = getSecretFromFile(key);

        if (unlockedSecret.getSecret() == ""){
            return new Greeting("You used this key : " + key, "Your secret is empty or this key doesn't exist");
        }else {
            return new Greeting("You used this key : " + key, "You're secret is : " + unlockedSecret);
        }
    }

    public Secret getSecretFromDB(String key) throws SQLException {
        Secret secrets = null;
        System.out.println("Creating statement...");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT secret FROM Secrets WHERE key = '" + key +"'");
        while (rs.next()) {
            secrets = new Secret(rs.getString(1), key);
        }
        conn.close();

        return secrets;
    }

    public String getSecretFromFile(String key) throws IOException {
        Path pathToFile = Path.of("/home/romain/Documents/rest_app/secrets/" + key + ".txt");
        String content = Files.readString(pathToFile);
        return content;
    }

    public void createTable(){
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Secrets (key VARCHAR(255), secret VARCHAR(255));");

    }

    @GetMapping("/secretimc")
    public Greeting greeting1(@RequestParam(value = "name", defaultValue = "World") String name) {
        String sWeight=System.getenv("SECRET_WEIGHT");
        String sHeight=System.getenv("SECRET_HEIGHT");
        test_imc = new Imc(sWeight, sHeight);
        return new Greeting( String.format(template, name), test_imc.getImc());
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name,
                             @RequestParam(value = "question", defaultValue = "Est-ceque ça marche ?") String question) {
        return new Greeting(String.format(template, name), String.format(questTemplate, question));
    }
}