package com.sprint.questai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class QuestAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestAiApplication.class, args);
	}

}
