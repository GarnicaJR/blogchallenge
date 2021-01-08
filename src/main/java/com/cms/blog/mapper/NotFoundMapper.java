package com.cms.blog.mapper;

import javax.json.Json;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        int code = 404;
        return Response.status(code)
                .entity(Json.createObjectBuilder().add("error", getMessage()).add("code", code).build())
                .build();
    }

    private String getMessage(){
        return "Resource no found";
    }
}