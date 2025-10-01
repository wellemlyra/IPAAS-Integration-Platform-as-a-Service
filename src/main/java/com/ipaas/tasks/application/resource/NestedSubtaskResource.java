package com.ipaas.tasks.application.resource;

import com.ipaas.tasks.application.dto.SubtaskRequest;
import com.ipaas.tasks.application.dto.SubtaskResponse;
import com.ipaas.tasks.application.dto.SubtaskStatusUpdateRequest;
import com.ipaas.tasks.application.exception.BusinessException;
import com.ipaas.tasks.domain.service.SubtaskService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

/**
 * Nested endpoints to match the Postman collection:
 * POST   /api/tasks/{taskId}/subtasks
 * GET    /api/tasks/{taskId}/subtasks
 * PATCH  /api/tasks/{taskId}/subtasks/{id}/status
 * DELETE /api/tasks/{taskId}/subtasks/{id}
 * <p>
 * Kept existing /api/subtasks endpoints intact (in SubtaskResource).
 */
@Path("/api/tasks/{taskId}/subtasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NestedSubtaskResource {

    @Inject
    SubtaskService service;

    @GET
    public List<SubtaskResponse> listByTask(@PathParam("taskId") UUID taskId) {
        return service.listByTask(taskId);
    }

    @POST
    public Response create(@PathParam("taskId") UUID taskId, @Valid SubtaskRequest req) {
        // If the body already brings taskId, ensure it matches the path to avoid silent inconsistencies
        if (req.getTaskId() != null && !req.getTaskId().equals(taskId)) {
            throw new BusinessException("taskId do corpo não confere com o path", Response.Status.BAD_REQUEST);
        }
        req.setTaskId(taskId);
        return Response.status(Response.Status.CREATED).entity(service.create(req)).build();
    }

    @PATCH
    @Path("/{id}/status")
    public SubtaskResponse updateStatus(@PathParam("taskId") UUID taskId,
                                        @PathParam("id") UUID id,
                                        @Valid SubtaskStatusUpdateRequest req) {
        // Optionally, could validate that subtask belongs to taskId. For now we delegate to service.
        return service.updateStatus(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("taskId") UUID taskId, @PathParam("id") UUID id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
