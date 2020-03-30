package edu.cibertec.votoelectronico.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.cibertec.votoelectronico.application.event.EmitirVotoEvent;
import edu.cibertec.votoelectronico.domain.complex.VotoResumen;
import edu.cibertec.votoelectronico.dto.VotoResumenDto;
import edu.cibertec.votoelectronico.mapping.MapperFactoryRegistry;
import edu.cibertec.votoelectronico.resource.communication.ResumenProcesoResponse;
import edu.cibertec.votoelectronico.service.VotoService;

@Component
@Path("/votoelectronico/subscribe")
public class SimpleSSEVotoElectronicoResource implements SSEVotoElectronicoResource {

	private final Logger LOG = LoggerFactory.getLogger(SimpleSSEVotoElectronicoResource.class);

	Sse sse;

	@Autowired
	private VotoService service;
	@Autowired
	private MapperFactoryRegistry mapper;

	private SseBroadcaster sseBroadcaster;
	private int lastEventId;
	private List<ResumenProcesoResponse> messages = new ArrayList<ResumenProcesoResponse>();

	@PostConstruct
	public void init() {
		LOG.info("On Init method...");
	}

	@Context
	public void setSse(Sse sse) {
		this.sse = sse;
		this.sseBroadcaster = this.sse.newBroadcaster();
		this.sseBroadcaster.onError((o, e) -> {
			LOG.error("Ocurred an error on broadcasting. " + e.getMessage());
		});
	}

	@Override
	public void obtenerResultados(int lastEventId, SseEventSink eventSink) {
		if (lastEventId >= 0)
			this.replyLastMessage(lastEventId, eventSink);
		sseBroadcaster.register(eventSink);
	}

	@Override
	public void onEmitirVotoEvent(EmitirVotoEvent domainEvent) {
		LOG.info("EmitirVoto Event received");
		ResumenProcesoResponse response = this.fetchResumenProceso();
		OutboundSseEvent event = createEvent(response, ++this.lastEventId);
		LOG.info("Server about to send Event");
		this.sseBroadcaster.broadcast(event);
	}

	private void replyLastMessage(int lastEventId, SseEventSink eventSink) {
		try {
			for (int i = lastEventId; i < messages.size(); i++) {
				eventSink.send(createEvent(messages.get(i), i + 1));
			}
		} catch (Exception e) {
			throw new InternalServerErrorException("Could not reply messages ", e);
		}
	}

	private ResumenProcesoResponse fetchResumenProceso() {
		ResumenProcesoResponse response = null;
		try {
			LOG.info("Fetching proccess resume..");
			Collection<VotoResumen> resultados = this.service.results();
			Function<VotoResumen, VotoResumenDto> convert = (VotoResumen object) -> this.mapper.convertFrom(object,
					VotoResumenDto.class);
			Collection<VotoResumenDto> collection = resultados.stream().map(convert).collect(Collectors.toList());
			response = new ResumenProcesoResponse(collection);
		} catch (Exception e) {
			LOG.error("Ocurred an error while trying to get resume. " + e.getMessage());
			response = new ResumenProcesoResponse(e.getMessage());
		}
		return response;
	}

	private OutboundSseEvent createEvent(ResumenProcesoResponse response, int id) {
		return this.sse.newEventBuilder().id(String.valueOf(id)).data(response).build();
	}

}