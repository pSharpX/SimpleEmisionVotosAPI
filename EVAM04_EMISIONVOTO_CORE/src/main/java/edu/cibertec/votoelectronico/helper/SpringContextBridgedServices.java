package edu.cibertec.votoelectronico.helper;

public interface SpringContextBridgedServices {

	<T> T getService(Class<T> serviceType);

}
