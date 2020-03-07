package com.sample.controller;

import com.sample.model.User;
import org.javers.core.Javers;
import org.javers.core.diff.Change;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditController {
    Javers javers;

    @Autowired
    public AuditController(Javers javers) {
        this.javers = javers;
    }

    @GetMapping("/user")
    public String getUserChanges() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(User.class);
        List<Change> changes =
            javers.findChanges(jqlQuery.withChildValueObjects().build());
        return javers.getJsonConverter().toJson(changes);
    }

}
