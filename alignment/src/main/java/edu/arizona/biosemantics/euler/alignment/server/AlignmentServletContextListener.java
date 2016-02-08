package edu.arizona.biosemantics.euler.alignment.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler.alignment.server.db.ConnectionPool;
import edu.arizona.biosemantics.euler.alignment.server.db.Query;

public class AlignmentServletContextListener implements ServletContextListener {
	
	private ConnectionPool connectionPool;

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		log(LogLevel.INFO, "Destroy alignment context " + event.getServletContext().getContextPath());
		try {
			log(LogLevel.INFO, "Shutting down connection pool");
			connectionPool.shutdown();
		} catch (Exception e) {
			log(LogLevel.ERROR, "Exception shutting down alignment context", e);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log(LogLevel.INFO, "Initializing alignment context at context path: " + event.getServletContext().getContextPath());
		log(LogLevel.INFO, "Configuration used " + Configuration.asString());
		
		log(LogLevel.INFO, "Install Java logging to SLF4J");
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		try {
			// init connection pool
			log(LogLevel.INFO, "Initializing connection pool");
			connectionPool = new ConnectionPool();
			Query.connectionPool = connectionPool;
		} catch (Exception e) {
			log(LogLevel.ERROR, "Exception initializing alignment context", e);
		}
	}
}