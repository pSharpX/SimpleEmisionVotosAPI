package edu.cibertec.votoelectronico.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.cibertec.votoelectronico.application.publisher.EmitirVotoEventPublisher;
import edu.cibertec.votoelectronico.domain.Voto;
import edu.cibertec.votoelectronico.domain.complex.VotoResumen;
import edu.cibertec.votoelectronico.repository.VotoRepository;
import edu.cibertec.votoelectronico.service.VotoService;
import edu.cibertec.votoelectronico.shared.Pagination;

@Service
public class VotoServiceImpl implements VotoService {

	private final Logger LOG = LoggerFactory.getLogger(VotoServiceImpl.class);

	@Autowired
	private VotoRepository repository;

	@Autowired
	private EmitirVotoEventPublisher eventPublisher;

	@Override
	public void emitirVoto(Voto object) throws Exception {
		try {
			this.repository.create(object);
			this.eventPublisher.publish(object);
		} catch (Exception e) {
			LOG.error("Ocurred an error while trying emit a vote. " + e.getMessage());
			throw e;
		}
	}

	@Override
	public List<Voto> list() {
		return this.repository.getAll();
	}

	@Override
	public Pagination<Voto> list(int page, int size) {
		return this.repository.getAll(page, size);
	}

	@Override
	public List<VotoResumen> results() {
		return this.repository.groupByGrupoPolitico();
	}

	@Override
	public List<VotoResumen> results(String grupoPolitico) {
		return this.repository.groupByGrupoPolitico(grupoPolitico);
	}

	@Override
	public Voto findByDni(String dni) throws Exception {
		return this.repository.findByDni(dni);
	}

}
