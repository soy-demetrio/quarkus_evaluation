package ru.soy_demetrio.research.quarkus.facade;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import ru.soy_demetrio.research.quarkus.domain.Person;
import ru.soy_demetrio.research.quarkus.service.PersonService;
import ru.soy_demetrio.research.quarkus.supplementary.ConstraintViolatedException;

@Path("/rest")
public class PersonResource {

	@Inject
	private PersonService service;
	
	@GET
	@Path("/persons")
    @Produces(MediaType.APPLICATION_JSON)
	public Multi<Person> getAll() {
		return service.getAll();
	}
	
    @GET
    @Path("/person/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getById(@PathParam("id") long argId) {
    	
		return service.getById(argId)
				.onItem().transform(instance -> instance == null ? Response.status(Status.NOT_FOUND) : Response.ok(instance))
				.onItem().transform(ResponseBuilder::build);
    }
    
    @POST
    @Path("/person")
    public Uni<Response> save(Person arg) {
		return service.save(arg)
				.onItem().transform(affectedRecordId -> affectedRecordId == null ? Response.status(Status.NOT_FOUND) : Response.ok(affectedRecordId))
				.onItem().transform(ResponseBuilder::build)
				.onFailure(ConstraintViolatedException.class).recoverWithItem(exception -> Response.status(Status.NOT_ACCEPTABLE).entity(exception.getMessage()).build());
    }
 
    
    @DELETE
    @Path("/person/{id}")
    public Uni<Response> delete(@PathParam("id") Long argVictimId) {
        return service.delete(argVictimId)
                .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }    
}