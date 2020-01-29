package com.sample.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document
public class User {

	@Id
	private String userId;
	private String name;
	private int count;
	private Date creationDate = new Date();
	private Map<String, String> userSettings = new HashMap<>();

	public String getUserId() {
		return userId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Map<String, String> getUserSettings() {
		return userSettings;
	}

	public void setUserSettings(Map<String, String> userSettings) {
		this.userSettings = userSettings;
	}

	public User(String userId, String name, int count, Date creationDate,
		Map<String, String> userSettings) {
		this.userId = userId;
		this.name = name;
		this.count = count;
		this.creationDate = creationDate;
		this.userSettings = userSettings;
	}

	@Override public String toString() {
		return "User{" + "userId='" + userId + '\'' + ", name='" + name + '\'' + ", count=" + count
			+ ", creationDate=" + creationDate + ", userSettings=" + userSettings + '}';
	}
}
