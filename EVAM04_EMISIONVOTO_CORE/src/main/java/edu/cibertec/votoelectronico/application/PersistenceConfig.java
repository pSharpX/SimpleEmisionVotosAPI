package edu.cibertec.votoelectronico.application;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
	public EntityManager createEntityManager(EntityManagerFactory emf) {
		return emf.createEntityManager();
	}

}
