package edu.cibertec.votoelectronico.application.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import edu.cibertec.votoelectronico.domain.Voto;

@Component
public class SimpleEmitirVotoEventPublisher extends BaseEventPublisher implements EmitirVotoEventPublisher {

	private final Logger LOG = LoggerFactory.getLogger(SimpleEmitirVotoEventPublisher.class);

	@Override
	public void publish(Voto payload) {
		LOG.info("EmitirVoto Event will be published...");
		this.applicationEventPublisher.publishEvent(this.createEvent(payload));
		LOG.info("EmitirVoto Event was published...");
	}

}
