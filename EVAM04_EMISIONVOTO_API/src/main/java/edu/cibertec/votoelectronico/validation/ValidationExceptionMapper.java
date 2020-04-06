package edu.cibertec.votoelectronico.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cibertec.votoelectronico.resource.communication.ValidationResponse;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	private final Logger LOG = LoggerFactory.getLogger(ValidationExceptionMapper.class);

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		LOG.warn(exception.getMessage());
		Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		Map<String, String> validations = new HashMap<String, String>();
		exception.getConstraintViolations().forEach(v -> {
			validations.put(StreamSupport.stream(v.getPropertyPath().spliterator(), false)
					.reduce((first, second) -> second).orElse(null).getName(), v.getMessage());
			builder.header("Error-Description", v.getMessage());
		});
		return builder.entity(new ValidationResponse(validations)).build();
	}

}
