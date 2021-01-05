package com.romainplt.secretVault.restservice;

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
public class SecretPrinterController {

    public Imc test_imc;
    public Secret secret;
    public static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection("jdbc:h2:file:/tmp/secretVault/data/database", "sa", "admin");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SecretPrinterController(JdbcTemplate jdbcTemplate){
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Secrets (key VARCHAR(255), secret VARCHAR(255));");
    }

    public static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    /*
    Print the database's hash
     */
    @GetMapping("/db/print")
    public SecretPrinter printDb() throws IOException, NoSuchAlgorithmException {
        File db = new File("/tmp/secretVault/data/database.mv.db");
        MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
        String shaCheckSum = getFileChecksum(shaDigest, db);
        return new SecretPrinter(shaCheckSum , "database");
    }

    /*
    Print the fspf volume's hash
     */
    @GetMapping("/volume/print")
    public SecretPrinter printVolume() throws IOException, NoSuchAlgorithmException {
        File volume = new File("/tmp/secretVault/data/volume.fspf");
        MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
        String shaCheckSum = getFileChecksum(shaDigest, volume);
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
        secretSession.writeSecretToDB(jdbcTemplate);
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
        this.secret.writeSecretToDB(jdbcTemplate);
        return new SecretPrinter( secret.getSecret(), secret.getKey());
    }

    /*
    Allows you to get secrets from the H2 database using the corresponding key.
     */
    @GetMapping("/getsecret")
    public SecretPrinter getSecret(@RequestParam(value = "key", defaultValue = "") String key) throws SQLException {
        Secret unlockedSecret = getSecretFromDB(key);
        if (unlockedSecret.getSecret() == "") {
            return new SecretPrinter("Your secret is empty or this key doesn't exist.", key);
        } else {
            return new SecretPrinter(unlockedSecret.getSecret(), key );
        }
    }

    /*
    Will go through the H2 database to retrieve a Secret corresponding ti the given Key.
     */
    public Secret getSecretFromDB(String key) throws SQLException {
        Secret secrets = null;
        System.out.println("Creating statement...");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT secret FROM Secrets WHERE key = '" + key +"'");
        while (rs.next()) {
            secrets = new Secret(rs.getString(1), key);
        }
        return secrets;
    }
}
