package edu.cibertec.votoelectronico.util;

public interface SpringContextBridgedServices {

	<T> T getService(Class<T> serviceType);

}
