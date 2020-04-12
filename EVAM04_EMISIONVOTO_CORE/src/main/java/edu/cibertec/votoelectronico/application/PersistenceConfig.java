package edu.cibertec.votoelectronico.application;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("edu.cibertec.votoelectronico")
@PropertySource("classpath:database.properties")
public class PersistenceConfig {

	@Bean
	public EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("ogm-mongodb");
	}

	@Bean
	@Primary
	public EntityManager createEntityManager(EntityManagerFactory emf) {
		return emf.createEntityManager();
	}

	@Bean
	@Qualifier("fullTextEntityManager")
	public FullTextEntityManager createFullTextEntityManager(EntityManager em) {
		return Search.getFullTextEntityManager(em);
	}

}
