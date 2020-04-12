package edu.cibertec.votoelectronico.application;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import edu.cibertec.votoelectronico.domain.GrupoPolitico;
import edu.cibertec.votoelectronico.domain.Voto;

@Component
public class HibernateSearchInit {

	private static final Logger LOG = LoggerFactory.getLogger(HibernateSearchInit.class);

	@Autowired
	@Qualifier("fullTextEntityManager")
	protected FullTextEntityManager fullTextEntityManager;

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		LOG.info("On Starup...");
		try {
			fullTextEntityManager.createIndexer(Voto.class, GrupoPolitico.class).startAndWait();
		} catch (InterruptedException e) {
			LOG.error("Ocurred an error: [{}]", e);
			e.printStackTrace();
		}
	}

}
