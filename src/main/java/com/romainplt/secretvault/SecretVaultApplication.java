package com.romainplt.secretvault;

import com.romainplt.secretvault.restservice.HashUtils;
import com.romainplt.secretvault.restservice.SecretController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class SecretVaultApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		Path path = Paths.get("/tmp/secretVault/data/database.mv.db");
		if (Files.exists(path)){
			printDbHashBeforeRun();
		}
		SpringApplication.run(SecretVaultApplication.class, args);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Shutdown Hook is running !");
			}
		});
		System.out.println("Application Terminating ...");
	}

	public static void printDbHashBeforeRun() throws NoSuchAlgorithmException, IOException {
		File db = new File("/tmp/secretVault/data/database.mv.db");
		MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
		String shaCheckSum = HashUtils.getFileChecksum(shaDigest, db);
		System.out.println("HASH DE LA DB : " + shaCheckSum);
	}
}