package com.iot.IotSink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:sensors.xml")
public class IotSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotSinkApplication.class, args);
	}
}
