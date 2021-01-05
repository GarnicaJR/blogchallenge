package com.cms.blog.repository;

import javax.enterprise.context.ApplicationScoped;

import com.cms.blog.entity.Post;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post>{

    
    
}
