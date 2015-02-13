package edu.arizona.biosemantics.euler.alignment.client.event.run;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

import edu.arizona.biosemantics.euler.alignment.client.event.run.StartMIREvent.StartMIREventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class StartMIREvent extends GwtEvent<StartMIREventHandler> {

	public interface StartMIREventHandler extends EventHandler {
		void onShow(StartMIREvent event);
	}
	
    public static Type<StartMIREventHandler> TYPE = new Type<StartMIREventHandler>();
	private Model model;

    public StartMIREvent(Model model) {
    	this.model = model;
    }
    
	@Override
	public Type<StartMIREventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(StartMIREventHandler handler) {
		handler.onShow(this);
	}

	public Model getModel() {
		return model;
	}

}
