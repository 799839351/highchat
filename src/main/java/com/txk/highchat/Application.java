package com.txk.highchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


/*@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})*/
@SpringBootApplication()
@MapperScan(basePackages = {"com.txk.highchat.mapper"})
@ComponentScan(basePackages= {"com.txk.highchat","org.n3r.idworker"})
public class Application {
	@Bean
	public com.txk.highchat.SpringUtil getSpingUtil() {
		return new com.txk.highchat.SpringUtil();
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
