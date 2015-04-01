package edu.arizona.biosemantics.euler.alignment.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.SwapTaxonomiesEvent.SwapTaxonomiesEventHandler;

public class SwapTaxonomiesEvent extends GwtEvent<SwapTaxonomiesEventHandler> {

	public interface SwapTaxonomiesEventHandler extends EventHandler {
		void onShow(SwapTaxonomiesEvent event);
	}
	
	public static Type<SwapTaxonomiesEventHandler> TYPE = new Type<SwapTaxonomiesEventHandler>();
	
	public SwapTaxonomiesEvent() {
	}
	
	@Override
	public GwtEvent.Type<SwapTaxonomiesEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SwapTaxonomiesEventHandler handler) {
		handler.onShow(this);
	}

	public static Type<SwapTaxonomiesEventHandler> getTYPE() {
		return TYPE;
	}
}