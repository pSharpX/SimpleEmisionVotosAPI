package edu.cibertec.votoelectronico.application.event;

import org.springframework.context.ApplicationEvent;

import edu.cibertec.votoelectronico.domain.Voto;

public class EmitirVotoEvent extends ApplicationEvent implements PayloadEvent<Voto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Voto payload;

	public EmitirVotoEvent(Object source) {
		super(source);
	}

	public EmitirVotoEvent(Object source, Voto payload) {
		super(source);
		this.payload = payload;
	}

	@Override
	public Voto getPayload() {
		return this.payload;
	}

}
