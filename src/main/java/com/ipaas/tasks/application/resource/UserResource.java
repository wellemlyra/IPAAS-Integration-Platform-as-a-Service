package com.ipaas.tasks.application.resource;

import com.ipaas.tasks.application.dto.*;
import com.ipaas.tasks.domain.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.UUID;

@Path("/api/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService service;

    @GET
    public java.util.List<UserResponse> list() {
        return service.list();
    }

    @GET
    @Path("/{id}")
    public UserResponse get(@PathParam("id") java.util.UUID id) {
        return service.get(id);
    }

    @POST
    public Response create(@Valid UserRequest req) {
        return Response.status(Response.Status.CREATED).entity(service.create(req)).build();
    }

    @PUT
    @Path("/{id}")
    public UserResponse update(@PathParam("id") java.util.UUID id, @Valid UserRequest req) {
        return service.update(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") java.util.UUID id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
