package com.cms.blog.resource;

import java.util.Collections;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.cms.blog.dto.AuthReponseDto;
import com.cms.blog.security.Role;
import com.cms.blog.security.TokenUtils;

@Path("/auth")
@RequestScoped
public class AuthResource {

    private TokenUtils tokenUtils;

    @Inject
    public AuthResource(TokenUtils tokenUtils){
        this.tokenUtils = tokenUtils;
    }

    @PermitAll
    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(){
        
        String token  = tokenUtils.generateToken("michel@gmail.com",
                                    Collections.singleton(Role.USER));

        return Response.ok(new AuthReponseDto(token)).build();
    }
}
