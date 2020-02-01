package com.sample.dal;

import java.util.List;
import java.util.Map;

import com.sample.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserDAL {

	Flux<User> getAllUsers();

	User getUserById(String userId);

	User addNewUser(User user);

	Mono<Map> getAllUserSettings(String userId);

	String getUserSetting(String userId, String key);

	String addUserSetting(String userId, String key, String value);

	boolean updateCount(String name);
}
