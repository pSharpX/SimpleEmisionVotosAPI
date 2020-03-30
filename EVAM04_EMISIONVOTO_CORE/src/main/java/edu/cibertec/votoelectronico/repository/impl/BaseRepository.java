package edu.cibertec.votoelectronico.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRepository {

	@Autowired
	protected EntityManager em;

	public BaseRepository() {
	}

}
