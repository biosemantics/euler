package edu.arizona.biosemantics.euler.alignment.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.AddMachineArticulationsEvent.AddMachineArticulationsHandler;

public class AddMachineArticulationsEvent extends GwtEvent<AddMachineArticulationsHandler> {

	public interface AddMachineArticulationsHandler extends EventHandler {
		void onAdd(AddMachineArticulationsEvent event);
	}
	
    public static Type<AddMachineArticulationsHandler> TYPE = new Type<AddMachineArticulationsHandler>();

    public AddMachineArticulationsEvent() {
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
