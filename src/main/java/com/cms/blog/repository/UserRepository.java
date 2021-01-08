package com.cms.blog.repository;

import com.cms.blog.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UserRepository  implements PanacheRepository<User> {

    public Optional<User> findByUsername(String username){

        PanacheQuery<User> query = find("username", username);
        if(query.count()==0){
            return Optional.empty();
        }
        return Optional.of(query.firstResult());
    }
}
