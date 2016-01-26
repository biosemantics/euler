package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveUserArticulationsEvent.RemoveUserArticulationsHandler;

public class RemoveUserArticulationsEvent extends GwtEvent<RemoveUserArticulationsHandler> {

	public interface RemoveUserArticulationsHandler extends EventHandler {
		void onRemove(RemoveUserArticulationsEvent event);
	}
	
    public static Type<RemoveUserArticulationsHandler> TYPE = new Type<RemoveUserArticulationsHandler>();

    public RemoveUserArticulationsEvent() {
    }
    
	@Override
	public Type<RemoveUserArticulationsHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RemoveUserArticulationsHandler handler) {
		handler.onRemove(this);
	}

}
