package com.sample.dal;

import com.sample.model.User;
import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Objects;

@Repository public class UserDALImpl implements UserDAL {


    @Autowired private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired private MongoTemplate mongoTemplate;

    @Autowired private Javers javers;

    @Autowired
    public UserDALImpl(ReactiveMongoTemplate reactiveMongoTemplate, MongoTemplate mongoTemplate,
        Javers javers) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.mongoTemplate = mongoTemplate;
        this.javers = javers;
    }

    @Override public Flux<User> getAllUsers() {
        return reactiveMongoTemplate.findAll(User.class);
    }

    @Override public User getUserById(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override public User addNewUser(User user) {
        mongoTemplate.save(user);
        javers.commit("Added", user);
        return user;
    }

    @Override public Flux<Map> getAllUserSettings(String userId) {
        Query query = Query.query(Criteria.where("_id").is(userId));
        return reactiveMongoTemplate.findDistinct(query, "userSettings", User.class, Map.class);
    }

    @Override public String getUserSetting(String userId, String key) {
        Query query = new Query();
        query.fields().include("userSettings");
        query.addCriteria(Criteria.where("_id").is(userId)
            .andOperator(Criteria.where("userSettings." + key).exists(true)));
        User user = mongoTemplate.findOne(query, User.class);
        return user != null ? user.getUserSettings().get(key) : "Not found";
    }

    @Override public String addUserSetting(String userId, String key, String value) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));
        User user = mongoTemplate.findOne(query, User.class);
        if (user != null) {
            user.getUserSettings().put(key, value);
            mongoTemplate.save(user);
            javers.commit("Updated User Settings", user);
            return "Key added.";
        } else {
            return "User not found.";
        }
    }

    @Override public boolean updateCount(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        query.addCriteria(Criteria.where("count").gt(0));
        Update update = new Update();
        update.inc("count", -1);
        return Objects
            .requireNonNull(reactiveMongoTemplate.updateFirst(query, update, User.class).block())
            .getModifiedCount() == 1;
    }
}
