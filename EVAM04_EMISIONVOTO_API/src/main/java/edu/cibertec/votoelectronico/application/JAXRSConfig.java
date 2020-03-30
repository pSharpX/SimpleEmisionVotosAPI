package edu.cibertec.votoelectronico.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import edu.cibertec.votoelectronico.resource.SimpleVotoElectronicoResource;

@ApplicationPath("/v1")
public class JAXRSConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		HashSet<Class<?>> set = new HashSet<Class<?>>();
		set.add(SimpleVotoElectronicoResource.class);
		return set;
	}

}
