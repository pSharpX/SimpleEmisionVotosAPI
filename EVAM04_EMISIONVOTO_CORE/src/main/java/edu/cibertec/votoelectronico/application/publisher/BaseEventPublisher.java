package edu.cibertec.votoelectronico.application.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public abstract class BaseEventPublisher {

	@Autowired
	protected ApplicationEventPublisher applicationEventPublisher;
}
