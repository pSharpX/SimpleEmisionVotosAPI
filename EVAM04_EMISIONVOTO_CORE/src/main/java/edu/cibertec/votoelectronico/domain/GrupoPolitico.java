package edu.cibertec.votoelectronico.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
@Table(name = "grupoPoliticos")
@NamedNativeQueries({
		@NamedNativeQuery(name = "QN_GRUPO_POLITICO_DELETE_ALL", query = "db.grupoPoliticos.drop()", resultClass = GrupoPolitico.class) })
public class GrupoPolitico {

	@Id
	@DocumentId
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "objectid")
	private String grupoPoliticoId;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String nombre;

	private String descripcion;

	@OneToMany(mappedBy = "grupoPolitico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@ContainedIn
	private Set<Voto> votos = new HashSet<>();

	public GrupoPolitico() {
		super();
	}

	public String getGrupoPoliticoId() {
		return grupoPoliticoId;
	}

	public void setGrupoPoliticoId(String grupoPoliticoId) {
		this.grupoPoliticoId = grupoPoliticoId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set<Voto> getVotos() {
		return votos;
	}

	public void setVotos(Set<Voto> votos) {
		this.votos = votos;
	}

	@Override
	public String toString() {
		return "GrupoPolitico [grupoPoliticoId=" + grupoPoliticoId + ", nombre=" + nombre + ", descripcion="
				+ descripcion + ", votos=" + votos + "]";
	}

}
