package com.cloud.paas.taskmanager;

import com.cloud.paas.taskmanager.websocket.TaskWebsocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.cloud.paas")
public class TaskmanagerApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(TaskmanagerApplication.class, args);
		TaskWebsocket.setApplicationContext(configurableApplicationContext);
	}
}
