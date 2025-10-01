package com.ipaas.tasks.application.resource;

import com.ipaas.tasks.application.dto.SubtaskRequest;
import com.ipaas.tasks.application.dto.SubtaskResponse;
import com.ipaas.tasks.application.dto.SubtaskStatusUpdateRequest;
import com.ipaas.tasks.domain.service.SubtaskService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/api/subtasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SubtaskResource {

    @Inject
    SubtaskService service;

    @GET
    public List<SubtaskResponse> list() {
        return service.list();
    }

    @GET
    @Path("/{id}")
    public SubtaskResponse get(@PathParam("id") UUID id) {
        return service.get(id);
    }

    @GET
    @Path("/task/{taskId}")
    public List<SubtaskResponse> listByTask(@PathParam("taskId") UUID taskId) {
        return service.listByTask(taskId);
    }

    @POST
    public Response create(@Valid SubtaskRequest req) {
        return Response.status(Response.Status.CREATED)
                .entity(service.create(req))
                .build();
    }

    @PUT
    @Path("/{id}")
    public SubtaskResponse update(@PathParam("id") UUID id, @Valid SubtaskRequest req) {
        return service.update(id, req);
    }

    @PATCH
    @Path("/{id}/status")
    public SubtaskResponse updateStatus(@PathParam("id") UUID id, @Valid SubtaskStatusUpdateRequest req) {
        return service.updateStatus(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
