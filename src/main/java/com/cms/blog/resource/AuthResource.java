package com.cms.blog.resource;

import java.util.Collections;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.management.openmbean.OpenType;
import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.cms.blog.dto.AuthReponseDto;
import com.cms.blog.dto.AuthRequestDto;
import com.cms.blog.dto.MessageReponseDto;
import com.cms.blog.entity.User;
import com.cms.blog.repository.UserRepository;
import com.cms.blog.security.Hashing;
import com.cms.blog.security.Role;
import com.cms.blog.security.TokenUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/auth")
@RequestScoped
public class AuthResource {

    private TokenUtils tokenUtils;
    private UserRepository userRepository;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    private String issuer;


    @Inject
    public AuthResource(UserRepository userRepository, TokenUtils tokenUtils){
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
    }



    @PermitAll
    @Transactional
    @POST
    @Path("signup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signup(@Valid AuthRequestDto authRequest){

        if(userRepository.findByUsername(authRequest.getUsername()).isPresent()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MessageReponseDto("Error: Email/Username is already in use!")).build();
        }

        User user = new User();
        user.setId(null);
        user.setUsername(authRequest.getUsername());
        user.setRole(Role.USER);
        user.setPassword(Hashing.sha256(authRequest.getPassword()));

        userRepository.persist(user);

        return Response.status(Response.Status.CREATED)
                .entity(new MessageReponseDto("User registered successfully!")).build();

    }

    @PermitAll
    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid AuthRequestDto authRequest){


        Optional<User> user = userRepository.findByUsername(authRequest.getUsername());

        if(user.isPresent() && user.get().getPassword().equals(Hashing.sha256(authRequest.getPassword()))){

            String token  = tokenUtils.generateToken(user.get().getUsername(),
                    Collections.singleton(Role.USER),
                    issuer);

            return Response.ok(new AuthReponseDto(token)).build();
        }
        else{
            return Response.status(Response.Status.UNAUTHORIZED).entity(new MessageReponseDto("No authorized")).build();
        }
    }
}
