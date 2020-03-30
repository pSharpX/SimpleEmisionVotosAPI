package edu.cibertec.votoelectronico.application.publisher;

import org.springframework.context.ApplicationEvent;

public interface GenericEventPublisher<P, E extends ApplicationEvent> {

	public E createEvent(P payload);

	public void publish(P payload);
}
