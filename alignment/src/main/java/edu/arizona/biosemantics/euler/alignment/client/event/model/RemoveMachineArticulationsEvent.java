package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveMachineArticulationsEvent.RemoveMachineArticulationsHandler;

public class RemoveMachineArticulationsEvent extends GwtEvent<RemoveMachineArticulationsHandler> {

	public interface RemoveMachineArticulationsHandler extends EventHandler {
		void onRemove(RemoveMachineArticulationsEvent event);
	}
	
    public static Type<RemoveMachineArticulationsHandler> TYPE = new Type<RemoveMachineArticulationsHandler>();

    public RemoveMachineArticulationsEvent() {
    }
    
	@Override
	public Type<RemoveMachineArticulationsHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RemoveMachineArticulationsHandler handler) {
		handler.onRemove(this);
	}

}
