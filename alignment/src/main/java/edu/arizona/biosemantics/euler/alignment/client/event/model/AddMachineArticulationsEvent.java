package edu.arizona.biosemantics.euler.alignment.client.event.model;

import java.util.Collection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.model.AddMachineArticulationsEvent.AddMachineArticulationsHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;

public class AddMachineArticulationsEvent extends GwtEvent<AddMachineArticulationsHandler> {

	public interface AddMachineArticulationsHandler extends EventHandler {
		void onAdd(AddMachineArticulationsEvent event);
	}
	
    public static Type<AddMachineArticulationsHandler> TYPE = new Type<AddMachineArticulationsHandler>();
	private Collection<Articulation> articulations;

    public AddMachineArticulationsEvent(Collection<Articulation> articulations) {
    	this.articulations = articulations;
    }
    
    public Collection<Articulation> getArticulations() {
    	return this.articulations;
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
