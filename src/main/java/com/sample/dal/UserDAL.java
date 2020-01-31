package com.sample.dal;

import java.util.List;

import com.sample.model.User;
import reactor.core.publisher.Flux;

public interface UserDAL {

	Flux<User> getAllUsers();

	User getUserById(String userId);

	User addNewUser(User user);

	Object getAllUserSettings(String userId);

	String getUserSetting(String userId, String key);

	String addUserSetting(String userId, String key, String value);

	boolean updateCount(String name);
}
