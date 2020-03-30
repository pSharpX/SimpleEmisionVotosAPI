//package edu.cibertec.votoelectronico.application;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//
//import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
//import org.jboss.resteasy.plugins.spring.SpringContextLoader;
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.context.ContextLoader;
//import org.springframework.web.context.ContextLoaderListener;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//
//public class AppInitializer implements WebApplicationInitializer {
//	@Override
//	public void onStartup(ServletContext container) throws ServletException {
//		container.addListener(new ResteasyBootstrap());
//
//		final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//		ContextLoaderListener springListener = new ContextLoaderListener(rootContext) {
//			@SuppressWarnings("unused")
//			protected ContextLoader createContextLoader() {
//				return new SpringContextLoader();
//			}
//		};
//		rootContext.register(AppConfig.class);
//		container.addListener(springListener);
//	}
//
//}
