package com.sample;

import com.sample.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
@EnableSwagger2
public class BootMongoDBApp {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Bean CommandLineRunner users(ReactiveMongoTemplate reactiveMongoTemplate) {
		Query query = new Query();
		return args -> reactiveMongoTemplate.findAllAndRemove(query, User.class)
			.subscribe(null, null, () -> {
				Stream
					.of(new User(UUID.randomUUID().toString(), "Karan", 10000, new Date(), new HashMap<>()),
						new User(UUID.randomUUID().toString(), "Lovish", 50000, new Date(), new HashMap<>()),
						new User(UUID.randomUUID().toString(), "Apoorv", 40000, new Date(), new HashMap<>()),
						new User(UUID.randomUUID().toString(), "Sumeet", 30000, new Date(), new HashMap<>()),
						new User(UUID.randomUUID().toString(), "Malati", 20000, new Date(), new HashMap<>()))
					.forEach(user -> {
						reactiveMongoTemplate.save(user).subscribe(User::toString);
						LOG.info(user.toString());
					});
			});
	}

	public static void main(String[] args) {
		SpringApplication.run(BootMongoDBApp.class, args);
	}
}
