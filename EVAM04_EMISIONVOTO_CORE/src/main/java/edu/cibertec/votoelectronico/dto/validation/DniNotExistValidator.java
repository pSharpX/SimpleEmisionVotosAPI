package edu.cibertec.votoelectronico.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cibertec.votoelectronico.helper.BeanHelper;
import edu.cibertec.votoelectronico.service.VotoService;

public class DniNotExistValidator implements ConstraintValidator<DniNotExist, String> {

	private final Logger LOG = LoggerFactory.getLogger(DniNotExistValidator.class);

	private VotoService service;

	@Override
	public void initialize(DniNotExist constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		this.service = BeanHelper.services().getService(VotoService.class);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			return !this.service.exist(value);
		} catch (Exception e) {
			LOG.error("Ocurred an error when trying validate dni. " + e.getMessage());
			return false;
		}
	}

}
