package edu.arizona.biosemantics.euler.alignment.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.ShowDescriptionEvent.ShowDescriptionEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class ShowDescriptionEvent extends GwtEvent<ShowDescriptionEventHandler> {

	public interface ShowDescriptionEventHandler extends EventHandler {
		void onDownload(ShowDescriptionEvent event);
	}
	
    public static Type<ShowDescriptionEventHandler> TYPE = new Type<ShowDescriptionEventHandler>();
	private Taxon taxon;

 
    
	public ShowDescriptionEvent(Taxon taxon) {
		this.taxon = taxon;
	}

	@Override
	public Type<ShowDescriptionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShowDescriptionEventHandler handler) {
		handler.onDownload(this);
	}

	public Taxon getTaxon() {
		return taxon;
	}
	
	

}
