package edu.cibertec.votoelectronico.client;

import java.util.Map;
import java.util.function.Consumer;

public interface HttpClientWithSSE {

	public <T> void listenOn(String path, Map<String, Object> header, Consumer<T> consumer, Class<T> responseType);

}
