package edu.cibertec.votoelectronico.resource;

import java.util.concurrent.CompletionStage;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cibertec.votoelectronico.dto.EmisionVotoDto;

@Path("/votoelectronico/async")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface VotoElectronicoResourceAsync {

	public Response emitirVoto(EmisionVotoDto resource) throws ConstraintViolationException;

	@POST
	@Path("/emitir1")
	public CompletionStage<Response> emitirVotoAsync(@Valid @NotNull EmisionVotoDto resource);

	@POST
	@Path("/emitir2")
	public void emitirVotoAsync(@Valid @NotNull EmisionVotoDto resource, @Suspended AsyncResponse response);
}
