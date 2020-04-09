package edu.cibertec.votoelectronico.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import edu.cibertec.votoelectronico.filter.CorsFilter;
import edu.cibertec.votoelectronico.filter.LoggingFilter;
import edu.cibertec.votoelectronico.validation.ValidationExceptionMapper;
import edu.cibertec.votoelectronico.resource.SimpleSSEVotoElectronicoResource;
import edu.cibertec.votoelectronico.resource.SimpleVotoElectronicoResource;
import edu.cibertec.votoelectronico.resource.SimpleVotoElectronicoResourceAsync;

@ApplicationPath("/v1")
public class JAXRSConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		HashSet<Class<?>> set = new HashSet<Class<?>>();
		set.add(SimpleVotoElectronicoResource.class);
		set.add(SimpleSSEVotoElectronicoResource.class);
		set.add(SimpleVotoElectronicoResourceAsync.class);
		set.add(ValidationExceptionMapper.class);
		return set;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<>();
		singletons.add(new CorsFilter());
		singletons.add(new LoggingFilter());
		return singletons;
	}

}
