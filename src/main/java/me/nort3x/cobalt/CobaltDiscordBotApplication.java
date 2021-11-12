package me.nort3x.cobalt;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CobaltDiscordBotApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(CobaltDiscordBotApplication.class)
				.web(WebApplicationType.NONE)
				.run( args);
	}

}
