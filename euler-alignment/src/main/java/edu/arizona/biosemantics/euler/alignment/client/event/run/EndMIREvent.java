package edu.arizona.biosemantics.euler.alignment.client.event.run;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

import edu.arizona.biosemantics.euler.alignment.client.event.run.EndMIREvent.EndMIREventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class EndMIREvent extends GwtEvent<EndMIREventHandler> {

	public interface EndMIREventHandler extends EventHandler {
		void onShow(EndMIREvent event);
	}
	
    public static Type<EndMIREventHandler> TYPE = new Type<EndMIREventHandler>();
	private String resultURL;

    public EndMIREvent(String resultURL) {
    	this.resultURL = resultURL;
    }
    
	@Override
	public Type<EndMIREventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EndMIREventHandler handler) {
		handler.onShow(this);
	}

	public String getResultURL() {
		return resultURL;
	}

}
