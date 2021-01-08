package com.cms.blog.resource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
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


@Path("/posts")
public class PostResource {


    private PostRepository postRepository;

    @Inject
    public PostResource(PostRepository postRepository){
        this.postRepository = postRepository;
    }


    @RolesAllowed("USER")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
        @DefaultValue("0")  @QueryParam("page") int pageIndex,
        @DefaultValue("10") @QueryParam("size") int pageSize,
        @Context UriInfo request)
    {
        PanacheQuery<Post> query = postRepository.findAll();

        if(query.count() == 0){
            throw new NotFoundException();
        }

        query.page(pageIndex, pageSize);
        Map<String, Object> payload = generateHateoas(query,request);
        return Response.status(Status.OK).entity(payload).build();
    }


    @RolesAllowed("USER")
    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") Long id){
        Optional<Post> optional = postRepository.findByIdOptional(id);
        Post post = optional.orElseThrow(() -> new NotFoundException());
        return Response.status(Status.OK).entity(post).build();
    }


    @RolesAllowed("USER")
    @Transactional
    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") Long id){
        boolean hasBeenDeleted = postRepository.deleteById(id);

        if(!hasBeenDeleted){
            throw new NotFoundException();
        }
        return Response.status(Status.NO_CONTENT).build();
    }

    @RolesAllowed("USER")
    @Transactional
    @PUT
    @Path("{id}")
    public Response updateById(@PathParam("id") Long id,@Valid PostDto postDto){

        Optional<Post> optional = postRepository.findByIdOptional(id);
        Post post = optional.orElseThrow(() -> new NotFoundException());

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdated(LocalDateTime.now());
        postRepository.persist(post);

        return Response.status(Status.CREATED).entity(post).build();
    }


    @RolesAllowed("USER")
    @Transactional
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submit(@Valid PostDto postDto) {
        
        Post post = mapToPost(postDto);

        postRepository.persist(post);
        return Response.status(Status.CREATED).entity(post).build();
    }


    private Post mapToPost(PostDto postDto) {
        Post post = new Post();
        post.setId(null);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdated(LocalDateTime.now());
        return post;
    }

    private Map<String, Object> generateHateoas(PanacheQuery<Post> query,  UriInfo  request) {
        Map<String,Object> payload = new HashMap<>();
        payload.put("posts",query.list());
        payload.put("_links", Link.of(request,query,Post.class));
        payload.put("page", com.cms.blog.hateoas.Page.of(query,Post.class));
        return payload;
    }

}