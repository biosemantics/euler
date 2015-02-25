package edu.arizona.biosemantics.euler.alignment.client.event.run;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.run.StartInputVisualizationEvent.StartInputVisualizationEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class StartInputVisualizationEvent extends GwtEvent<StartInputVisualizationEventHandler> {

	public interface StartInputVisualizationEventHandler extends EventHandler {
		void onShow(StartInputVisualizationEvent event);
	}
	
    public static Type<StartInputVisualizationEventHandler> TYPE = new Type<StartInputVisualizationEventHandler>();
	private Model model;

    public StartInputVisualizationEvent(Model model) {
    	this.model = model;
    }
    
	@Override
	public Type<StartInputVisualizationEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(StartInputVisualizationEventHandler handler) {
		handler.onShow(this);
	}

	public Model getModel() {
		return model;
	}

}
