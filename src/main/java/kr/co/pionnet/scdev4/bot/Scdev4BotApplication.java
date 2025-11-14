package kr.co.pionnet.scdev4.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "kr.co.pionnet")
public class Scdev4BotApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("GMT+9")));
		SpringApplication.run(Scdev4BotApplication.class, args);
	}

}
