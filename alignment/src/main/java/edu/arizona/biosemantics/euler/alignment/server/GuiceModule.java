package edu.arizona.biosemantics.euler.alignment.server;

import com.google.gwt.logging.server.RemoteLoggingServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import edu.arizona.biosemantics.euler.alignment.server.db.CollectionDAO;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;

public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EulerAlignmentService.class).in(Scopes.SINGLETON);
		
		bind(IEulerAlignmentService.class).to(EulerAlignmentService.class).in(Scopes.SINGLETON);
		bind(RemoteLoggingServiceImpl.class).in(Scopes.SINGLETON);
		
		bind(CollectionDAO.class).in(Scopes.SINGLETON);
		
	}

}