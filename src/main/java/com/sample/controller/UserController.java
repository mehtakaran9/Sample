package com.sample.controller;

import java.util.List;

import com.sample.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.sample.dal.UserDAL;
import com.sample.dal.UserRepository;

@RestController
@RequestMapping(value = "/")
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private final UserRepository userRepository;

	private final UserDAL userDAL;

	public UserController(UserRepository userRepository, UserDAL userDAL) {
		this.userRepository = userRepository;
		this.userDAL = userDAL;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public User addNewUsers(@RequestBody User user) {
		LOG.info("Saving user.");
		return userRepository.save(user);
	}

	@RequestMapping(value = "/update/{userId}", method = RequestMethod.POST)
	public String updateCount(@PathVariable String userId) {

		return userDAL.updateCount(userId) + " ";

	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		LOG.info("Getting all users.");
		return userRepository.findAll();
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public User getUser(@PathVariable String userId) {
		LOG.info("Getting user with ID: {}.", userId);
		return userRepository.findOne(userId);
	}

	// @RequestMapping(value = "/settings/{userId}", method = RequestMethod.GET)
	// public Object getAllUserSettings(@PathVariable String userId) {
	// User user = userRepository.findOne(userId);
	// if (user != null) {
	// return user.getUserSettings();
	// } else {
	// return "User not found.";
	// }
	// }

	@RequestMapping(value = "/settings/{userId}", method = RequestMethod.GET)
	public Object getAllUserSettings(@PathVariable String userId) {
		User user = userRepository.findOne(userId);
		if (user != null) {
			return userDAL.getAllUserSettings(userId);
		} else {
			return "User not found.";
		}
	}

	// @RequestMapping(value = "/settings/{userId}/{key}", method =
	// RequestMethod.GET)
	// public String getUserSetting(@PathVariable String userId, @PathVariable
	// String key) {
	// //User user = userRepository.findOne(userId);
	// String setting = userDAL.getUserSetting(userId, key);
	// LOG.info("Setting = "+setting);
	// if (setting != null) {
	// return setting;
	// } else {
	// return "Setting not found.";
	// }
	// }

	@RequestMapping(value = "/settings/{userId}/{key}", method = RequestMethod.GET)
	public String getUserSetting(@PathVariable String userId, @PathVariable String key) {
		return userDAL.getUserSetting(userId, key);
	}

	@RequestMapping(value = "/settings/{userId}/{key}/{value}", method = RequestMethod.GET)
	public String addUserSetting(@PathVariable String userId, @PathVariable String key, @PathVariable String value) {
		User user = userRepository.findOne(userId);
		if (user != null) {
			user.getUserSettings().put(key, value);
			userRepository.save(user);
			return "Key added";
		} else {
			return "User not found.";
		}
	}
}
