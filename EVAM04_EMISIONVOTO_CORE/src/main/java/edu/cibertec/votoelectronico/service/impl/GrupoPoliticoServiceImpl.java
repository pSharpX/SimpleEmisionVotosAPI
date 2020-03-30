package edu.cibertec.votoelectronico.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.cibertec.votoelectronico.domain.GrupoPolitico;
import edu.cibertec.votoelectronico.repository.GrupoPoliticoRepository;
import edu.cibertec.votoelectronico.service.GrupoPoliticoService;

@Service
public class GrupoPoliticoServiceImpl implements GrupoPoliticoService {

	@Autowired
	private GrupoPoliticoRepository repository;

	@Override
	public List<GrupoPolitico> list() {
		return this.repository.getAll();
	}

	@Override
	public GrupoPolitico findByName(String name) throws Exception {
		return this.repository.findByName(name);
	}

}
