package com.romainplt.secretvault.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SecretService {

    public static Connection conn;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /*
    Will go through the H2 database to retrieve the Secret corresponding to the given Key.
    */
    public static Secret getSecretFromDB(String key) throws SQLException {
        Secret secrets = null;
        System.out.println("Creating statement...");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT secret FROM Secrets WHERE key = '" + key +"'");
        while (rs.next()) {
            secrets = new Secret(rs.getString(1), key);
        }
        return secrets;
    }


    public static void writeSecretToDB(JdbcTemplate jdbcTemplate, Secret secret) {
        jdbcTemplate.execute("INSERT INTO Secrets(key,secret) VALUES ('" + secret.getKey() + "' ,'" + secret.getSecret() + "')");
    }




}
