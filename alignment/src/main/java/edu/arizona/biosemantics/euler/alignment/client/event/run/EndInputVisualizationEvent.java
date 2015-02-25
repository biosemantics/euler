package edu.arizona.biosemantics.euler.alignment.client.event.run;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.run.EndInputVisualizationEvent.EndInputVisualizationEventHandler;

public class EndInputVisualizationEvent extends GwtEvent<EndInputVisualizationEventHandler> {

	public interface EndInputVisualizationEventHandler extends EventHandler {
		void onShow(EndInputVisualizationEvent event);
	}
	
    public static Type<EndInputVisualizationEventHandler> TYPE = new Type<EndInputVisualizationEventHandler>();
	private String resultURL;

    public EndInputVisualizationEvent(String resultURL) {
    	this.resultURL = resultURL;
    }
    
	@Override
	public Type<EndInputVisualizationEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EndInputVisualizationEventHandler handler) {
		handler.onShow(this);
	}

	public String getResultURL() {
		return resultURL;
	}

}
