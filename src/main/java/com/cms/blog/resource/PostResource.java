package com.cms.blog.resource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.cms.blog.dto.PostDto;
import com.cms.blog.entity.Post;
import com.cms.blog.hateoas.Link;
import com.cms.blog.repository.PostRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

@Path("/posts")
public class PostResource {


    private PostRepository postRepository;

    @Context UriInfo uriInfo;


    @Inject
    public PostResource(PostRepository postRepository){
        this.postRepository = postRepository;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts(
        @DefaultValue("0")  @QueryParam("page") int pageIndex,
        @DefaultValue("25") @QueryParam("size") int pageSize)
    {
        PanacheQuery<Post> query = postRepository.findAll();
        query.page(pageIndex, pageSize);

        Map<String, Object> payload = generateHateoas(query);        
        return Response.status(Status.OK).entity(payload).build();
    }


    private Map<String, Object> generateHateoas(PanacheQuery<Post> query) {
        Map<String,Object> payload = new HashMap<>();
        payload.put("posts",query.list());
        payload.put("_links", Link.of(uriInfo,query,Post.class));
        payload.put("page", com.cms.blog.hateoas.Page.of(query,Post.class));
        return payload;
    }


    @Transactional
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitPost(@Valid PostDto postDto) {
        
        Post post = createPost(postDto);

        postRepository.persist(post);
        return Response.status(Status.CREATED).entity(post).build();
    }


    private Post createPost(PostDto postDto) {
        Post post = new Post();
        post.setId(null);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCreateAt(LocalDateTime.now());
        return post;
    }
}