package com.skiba.notesmanager;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

@SpringBootApplication
@EntityScan(basePackages = "com.skiba.notesmanager")
public class NotesManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesManagerApplication.class, args);
	}

	@Bean
	public org.h2.tools.Server getWebH2Server() throws SQLException {
		final Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
		webServer.start();
		return webServer;

	}
}
