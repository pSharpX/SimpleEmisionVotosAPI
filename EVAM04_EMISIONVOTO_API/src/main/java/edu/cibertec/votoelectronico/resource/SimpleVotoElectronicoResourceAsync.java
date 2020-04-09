package edu.cibertec.votoelectronico.resource;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.cibertec.votoelectronico.domain.GrupoPolitico;
import edu.cibertec.votoelectronico.domain.Voto;
import edu.cibertec.votoelectronico.dto.EmisionVotoDto;
import edu.cibertec.votoelectronico.dto.VotoDto;
import edu.cibertec.votoelectronico.mapping.MapperFactoryRegistry;
import edu.cibertec.votoelectronico.resource.communication.BaseResponse;
import edu.cibertec.votoelectronico.resource.communication.EmisionVotoResponse;
import edu.cibertec.votoelectronico.service.GrupoPoliticoService;
import edu.cibertec.votoelectronico.service.VotoService;

@Component
@Path("/votoelectronico/async")
public class SimpleVotoElectronicoResourceAsync implements VotoElectronicoResourceAsync {

	private final Logger LOG = LoggerFactory.getLogger(SimpleVotoElectronicoResourceAsync.class);

	@Autowired
	private VotoService votoService;
	@Autowired
	private GrupoPoliticoService grupoPoliticoService;
	@Autowired
	private MapperFactoryRegistry mapper;

	private ExecutorService executor;

	@PostConstruct
	public void init() {
		LOG.info("After Construct...");
		this.executor = Executors.newFixedThreadPool(10);
	}

	@PreDestroy
	public void destroy() {
		LOG.info("Before Destroy...");
		if (this.executor != null && !this.executor.isShutdown())
			executor.shutdown();
	}

	@Override
	public Response emitirVoto(EmisionVotoDto resource) throws ConstraintViolationException {
		try {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<EmisionVotoDto>> violations = validator.validate(resource);
			if (violations.size() > 0)
				throw new ConstraintViolationException(violations);

			GrupoPolitico grupoPolitico = this.grupoPoliticoService.findByName(resource.getGrupoPolitico());
			if (Objects.isNull(grupoPolitico)) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new EmisionVotoResponse("No existe grupo politico.")).build();
			}
			Voto voto = this.mapper.convertTo(resource, Voto.class);
			voto.setGrupoPolitico(grupoPolitico);
			this.votoService.emitirVoto(voto);
			VotoDto votoDto = this.mapper.convertFrom(voto, VotoDto.class);
			return Response.status(Response.Status.CREATED).entity(new EmisionVotoResponse(votoDto)).build();
		} catch (ConstraintViolationException e) {
			throw e;
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new EmisionVotoResponse(e.getMessage())).build();
		}
	}

	@Override
	public CompletionStage<Response> emitirVotoAsync(EmisionVotoDto resource) {
		return CompletableFuture.supplyAsync(() -> emitirVoto(resource), this.executor);
	}

	@Override
	public void emitirVotoAsync(EmisionVotoDto resource, AsyncResponse response) {
		response.setTimeout(20, TimeUnit.SECONDS);
		response.setTimeoutHandler(r -> r.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
				.entity(new BaseResponse(false, "")).type(MediaType.APPLICATION_JSON).build()));
		this.executor.execute(() -> response.resume(emitirVoto(resource)));
	}

}
