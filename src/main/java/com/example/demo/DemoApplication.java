package com.example.demo;

import com.example.demo.restservice.GreetingController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@SpringBootApplication
public class DemoApplication {


	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

		Path path = Paths.get("/tmp/demo/data/database.mv.db");
		if (Files.exists(path)){
			printDbHash();
		}
		SpringApplication.run(DemoApplication.class, args);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Shutdown Hook is running !");
			}
		});
		System.out.println("Application Terminating ...");


	}

	public static void printDbHash() throws NoSuchAlgorithmException, IOException {

		File db = new File("/tmp/demo/data/database.mv.db");
		MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
		String shaCheckSum = GreetingController.getFileChecksum(shaDigest, db);
		System.out.println("HASH DE LA DB : " + shaCheckSum);


	}



}