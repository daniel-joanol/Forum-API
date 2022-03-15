package com.secondcommit.forum;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ForumApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ForumApplication.class, args);
		BasicConfigurator.configure();
	}

}
