package com.sample.dal;

import java.util.Map;

import com.sample.model.User;
import org.javers.spring.annotation.JaversAuditable;
import reactor.core.publisher.Flux;

public interface UserDAL {

	Flux<User> getAllUsers();

	User getUserById(String userId);

	User addNewUser(User user);

	Flux<Map> getAllUserSettings(String userId);

	String getUserSetting(String userId, String key);

	String addUserSetting(String userId, String key, String value);

	boolean updateCount(String name);
}
