package edu.cibertec.votoelectronico.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import edu.cibertec.votoelectronico.dto.validation.DniNotExist;
import edu.cibertec.votoelectronico.dto.validation.GrupoPoliticoMustExist;
import edu.cibertec.votoelectronico.dto.validation.ValidDate;

@NotNull
public class EmisionVotoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "{emisionvoto.grupoPolitico.notblank}")
	@GrupoPoliticoMustExist(message = "{emisionvoto.grupoPolitico.grupopoliticomustexist}")
	private String grupoPolitico;

	@NotBlank(message = "{emisionvoto.dni.notblank}")
	@Size(min = 8, max = 8, message = "{emisionvoto.dni.size}")
	@DniNotExist(message = "{emisionvoto.dni.dninotexist}")
	private String dni;

	@NotNull(message = "{emisionvoto.fecha.notnull}")
	@ValidDate(message = "{emisionvoto.fecha.validdate}")
	private String fecha;

	public String getGrupoPolitico() {
		return grupoPolitico;
	}

	public void setGrupoPolitico(String grupoPolitico) {
		this.grupoPolitico = grupoPolitico;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

}
