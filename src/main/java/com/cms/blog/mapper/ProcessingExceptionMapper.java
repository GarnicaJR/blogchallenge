package com.cms.blog.mapper;

import javax.json.Json;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ProcessingExceptionMapper implements ExceptionMapper<ProcessingException> {

    @Override
    public Response toResponse(ProcessingException exception) {
        int code = 400;
        return Response.status(code)
                .entity(Json.createObjectBuilder().add("error", getMessage()).add("code", code).build())
                .build();
    }

    private String getMessage(){
        return "Invalid Json please fix it";
    }
}