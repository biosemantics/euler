package edu.arizona.biosemantics.euler.alignment.client.event.run;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.run.StartMIREvent.StartMIREventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.RunConfig;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomies;

public class StartMIREvent extends GwtEvent<StartMIREventHandler> {

	public interface StartMIREventHandler extends EventHandler {
		void onShow(StartMIREvent event);
	}
	
    public static Type<StartMIREventHandler> TYPE = new Type<StartMIREventHandler>();
	private Taxonomies taxonomies;
	private Articulations articulations;
	private RunConfig runConfig;

    public StartMIREvent(Taxonomies taxonomies, Articulations articulations, RunConfig runConfig) {
    	this.taxonomies = taxonomies;
    	this.articulations = articulations;
    	this.runConfig = runConfig;
    }
    
	@Override
	public Type<StartMIREventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(StartMIREventHandler handler) {
		handler.onShow(this);
	}

	public Taxonomies getTaxonomies() {
		return taxonomies;
	}

	public Articulations getArticulations() {
		return articulations;
	}

	public RunConfig getRunConfig() {
		return runConfig;
	}
}
