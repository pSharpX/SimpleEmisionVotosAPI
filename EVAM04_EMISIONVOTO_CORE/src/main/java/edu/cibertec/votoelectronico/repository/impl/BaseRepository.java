package edu.cibertec.votoelectronico.repository.impl;

import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseRepository {

	@Autowired
	protected EntityManager em;

	@Autowired
	@Qualifier("fullTextEntityManager")
	protected FullTextEntityManager fullTextEntityManager;

	public BaseRepository() {
	}

}
