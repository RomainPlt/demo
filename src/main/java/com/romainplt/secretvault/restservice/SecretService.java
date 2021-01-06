package com.romainplt.secretvault.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Date;

@Service
public class SecretService {

    public static Connection conn;
    public static final String createTable = "CREATE TABLE IF NOT EXISTS Secrets (key VARCHAR(255), secret VARCHAR(255));";

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

    /*
    Will go through the H2 database to retrieve the Secret corresponding to the given Key.
    */
    public Secret getSecretFromDB(String key) throws SQLException {
        Secret secrets = new Secret("","");
        jdbcTemplate.execute(createTable);
        System.out.println("Creating statement...");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT secret FROM Secrets WHERE key = '" + key +"'");
        while (rs.next()) {
            secrets = new Secret(rs.getString(1), key);
        }
        return secrets;
    }

    public void writeSecretToDB(Secret secret) {
        jdbcTemplate.execute(createTable);
        jdbcTemplate.execute("INSERT INTO Secrets(key,secret) VALUES ('" + secret.getKey() + "' ,'" + secret.getSecret() + "')");
    }


    public static class JsonResponse{
        private String key;
        private String hash;
        private java.util.Date date;

        public String getHash() {
            return this.hash;
        }
        public String getKey() {
            return this.key;
        }
        public java.util.Date getDate() { return this.date; }
        public void setHash(String hash) { this.hash = hash; }
        public void setKey(String key) { this.key = key; }
        public void setDate(Date date) { this.date = date; }
    }




}
