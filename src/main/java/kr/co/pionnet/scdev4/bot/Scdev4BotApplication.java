package kr.co.pionnet.scdev4.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "kr.co.pionnet.scdev4.bot")
public class Scdev4BotApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Scdev4BotApplication.class);
	}
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("GMT+9")));
		SpringApplication.run(Scdev4BotApplication.class, args);
	}

}
