package com.ipaas.tasks.application.resource;

import com.ipaas.tasks.application.dto.TaskRequest;
import com.ipaas.tasks.application.dto.TaskResponse;
import com.ipaas.tasks.application.dto.TaskStatusUpdateRequest;
import com.ipaas.tasks.domain.model.Status;
import com.ipaas.tasks.domain.service.TaskService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/api/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskService service;

    /**
     * Lista todas as tarefas ou filtra por status (se informado).
     */
    @GET
    public List<TaskResponse> list(@QueryParam("status") String status) {
        if (status != null && !status.isBlank()) {
            try {
                var parsed = com.ipaas.tasks.domain.model.Status
                        .valueOf(status.trim().toUpperCase(java.util.Locale.ROOT));
                return service.listByStatus(parsed);
            } catch (IllegalArgumentException ex) {
                throw new jakarta.ws.rs.WebApplicationException(
                        "Status inválido: " + status + " (use: NOVA, EM_ANDAMENTO, CONCLUIDA, ...)", 400);
            }
        }
        return service.list();
    }


    @GET
    @Path("/{id}")
    public TaskResponse get(@PathParam("id") UUID id) {
        return service.get(id);
    }

    @POST
    public Response create(@Valid TaskRequest req) {
        return Response.status(Response.Status.CREATED)
                .entity(service.create(req))
                .build();
    }

    @PUT
    @Path("/{id}")
    public TaskResponse update(@PathParam("id") UUID id, @Valid TaskRequest req) {
        return service.update(id, req);
    }

    @PATCH
    @Path("/{id}/status")
    public TaskResponse updateStatus(@PathParam("id") UUID id, @Valid TaskStatusUpdateRequest req) {
        return service.updateStatus(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
