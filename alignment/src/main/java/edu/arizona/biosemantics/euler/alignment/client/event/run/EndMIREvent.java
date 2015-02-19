package edu.arizona.biosemantics.euler.alignment.client.event.run;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

import edu.arizona.biosemantics.euler.alignment.client.event.run.EndMIREvent.EndMIREventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.RunOutput;

public class EndMIREvent extends GwtEvent<EndMIREventHandler> {

	public interface EndMIREventHandler extends EventHandler {
		void onEnd(EndMIREvent event);
	}
	
    public static Type<EndMIREventHandler> TYPE = new Type<EndMIREventHandler>();
	private RunOutput output;

    public EndMIREvent(RunOutput output) {
    	this.output = output;
    }
    
	@Override
	public Type<EndMIREventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EndMIREventHandler handler) {
		handler.onEnd(this);
	}

	public RunOutput getOutput() {
		return output;
	}

}
