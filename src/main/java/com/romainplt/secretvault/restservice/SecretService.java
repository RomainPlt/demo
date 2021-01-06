package com.romainplt.secretvault.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class SecretService {

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

    public SecretService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createDatabase(){
        new SecretService(jdbcTemplate);
    }
    /*
    Will go through the H2 database to retrieve the Secret corresponding to the given Key.
    */
    public Secret getSecretFromDB(String key) throws SQLException {
        Secret secrets = null;
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Secrets (key VARCHAR(255), secret VARCHAR(255));");
        System.out.println("Creating statement...");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT secret FROM Secrets WHERE key = '" + key +"'");
        while (rs.next()) {
            secrets = new Secret(rs.getString(1), key);
        }
        return secrets;
    }

    public void writeSecretToDB(Secret secret) {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Secrets (key VARCHAR(255), secret VARCHAR(255));");
        jdbcTemplate.execute("INSERT INTO Secrets(key,secret) VALUES ('" + secret.getKey() + "' ,'" + secret.getSecret() + "')");
    }




}
