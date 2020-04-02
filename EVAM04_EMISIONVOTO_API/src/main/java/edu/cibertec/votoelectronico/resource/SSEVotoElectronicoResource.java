package edu.cibertec.votoelectronico.resource;

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.SseEventSink;

import org.springframework.context.event.EventListener;

import edu.cibertec.votoelectronico.application.event.EmitirVotoEvent;

@Path("/votoelectronico/subscribe")
public interface SSEVotoElectronicoResource {

	@GET
	@Path("/resultado")
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void obtenerResultados(@HeaderParam(HttpHeaders.LAST_EVENT_ID_HEADER) @DefaultValue("-1") int lastEventId,
			@Context SseEventSink eventSink) throws IOException;

	@EventListener
	public void onEmitirVotoEvent(EmitirVotoEvent domainEvent);

}
