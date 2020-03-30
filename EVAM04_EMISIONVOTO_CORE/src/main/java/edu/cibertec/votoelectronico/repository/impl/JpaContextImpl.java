package edu.cibertec.votoelectronico.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.cibertec.votoelectronico.repository.JpaContext;

@Component
public class JpaContextImpl implements JpaContext {

	protected final EntityManager em;

	@Autowired
	public JpaContextImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public void flushAndClose() {
		this.em.flush();
		this.em.close();
	}

}
