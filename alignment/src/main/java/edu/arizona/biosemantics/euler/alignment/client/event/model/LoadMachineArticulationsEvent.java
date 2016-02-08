package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadMachineArticulationsEvent.AddMachineArticulationsHandler;

public class LoadMachineArticulationsEvent extends GwtEvent<AddMachineArticulationsHandler> {

	public interface AddMachineArticulationsHandler extends EventHandler {
		void onAdd(LoadMachineArticulationsEvent event);
	}
	
    public static Type<AddMachineArticulationsHandler> TYPE = new Type<AddMachineArticulationsHandler>();

    public LoadMachineArticulationsEvent() {
    }
    
	@Override
	public Type<AddMachineArticulationsHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddMachineArticulationsHandler handler) {
		handler.onAdd(this);
	}

}
