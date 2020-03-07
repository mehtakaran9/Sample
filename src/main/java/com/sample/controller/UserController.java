package com.sample.controller;

import com.sample.dal.UserDAL;
import com.sample.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserDAL userDAL;

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	@PostMapping(value = "/create")
	public Mono<User> addNewUsers(@RequestBody User user) {
		LOG.info("Saving user.");
		return reactiveMongoTemplate.save(user);
	}

	@GetMapping(value = "/")
	public Flux<User> getAllUsers() {
		LOG.info("Getting all users.");
		return userDAL.getAllUsers();
	}

	@GetMapping(value = "/{userId}")
	public Mono<User> getUser(@PathVariable String userId) {
		LOG.info("Getting user with ID: {}.", userId);
		return reactiveMongoTemplate.findById(userId, User.class);
	}

	@GetMapping(value = "/settings/{userId}")
	public Flux<Map> getAllUserSettings(@PathVariable String userId) {
		return userDAL.getAllUserSettings(userId);
	}

	@GetMapping(value = "/settings/{userId}/{key}")
	public String getUserSetting(@PathVariable String userId, @PathVariable String key) {
		return userDAL.getUserSetting(userId, key);
	}

	@GetMapping(value = "/settings/{userId}/{key}/{value}")
	public boolean addUserSetting(@PathVariable String userId, @PathVariable String key,
		@PathVariable String value) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(userId));
		Update update = new Update();
		update.set("userSettings.".concat(key), value);
		AtomicBoolean result = new AtomicBoolean(false);
		reactiveMongoTemplate.updateFirst(query, update, User.class).doOnSuccess(updateResult -> {
			if (updateResult != null) {
				result.set(updateResult.getModifiedCount() == 1);
			}
		}).block();
		return result.get();
	}

	@GetMapping(value = "/count")
	public boolean updateCountByName(@RequestParam String name) {
		return userDAL.updateCount(name);
	}
}
