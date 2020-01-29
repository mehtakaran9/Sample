package com.sample.controller;

import com.sample.dal.UserDAL;
import com.sample.dal.UserRepository;
import com.sample.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDAL userDAL;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Mono<User> addNewUsers(@RequestBody User user) {
		LOG.info("Saving user.");
		return userRepository.save(user);
	}

	@RequestMapping(value = "/update/{userId}", method = RequestMethod.GET)
	public Mono<User> updateCount(@PathVariable String userId) {
		Mono<User> userMono = userRepository.findById(userId);
		return userMono.doOnSuccess(user -> {
			user.setCount(user.getCount() + 1);
			userRepository.save(user)
				.subscribe(null, null, () -> LOG.info("Updated count for userId {}", userId));
		});
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Flux<User> getAllUsers() {
		LOG.info("Getting all users.");
		return userRepository.findAll();
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public Mono<User> getUser(@PathVariable String userId) {
		LOG.info("Getting user with ID: {}.", userId);
		return userRepository.findById(userId);
	}

	@RequestMapping(value = "/settings/{userId}", method = RequestMethod.GET)
	public Map<String, String> getAllUserSettings(@PathVariable String userId) {
		Mono<User> userMono = userRepository.findById(userId);
		return Objects.requireNonNull(userMono.block()).getUserSettings();
	}

	@RequestMapping(value = "/settings/{userId}/{key}", method = RequestMethod.GET)
	public String getUserSetting(@PathVariable String userId, @PathVariable String key) {
		return userDAL.getUserSetting(userId, key);
	}

	@RequestMapping(value = "/settings/{userId}/{key}/{value}", method = RequestMethod.GET)
	public Mono<User> addUserSetting(@PathVariable String userId, @PathVariable String key,
		@PathVariable String value) {
		Mono<User> userMono = userRepository.findById(userId);
		return userMono.doOnSuccess(user -> {
			user.getUserSettings().put(key, value);
			userRepository.save(user).subscribe(null, null, () -> LOG.info(user.toString()));
		}).doOnError(throwable -> LOG.error(throwable.getMessage()));
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public boolean updateCountByUserId(@RequestParam String userId) {
		return userDAL.updateCount(userId);
	}
}
