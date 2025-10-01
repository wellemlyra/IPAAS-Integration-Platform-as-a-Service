package com.ipaas.tasks.application.exception;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;

import java.util.Map;

@Provider
public class GenericErrorMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        if (ex instanceof BusinessException be) {
            return Response.status(be.getStatus()).entity(Map.of("error", be.getMessage())).type(MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", "Erro interno", "detail", ex.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
}
