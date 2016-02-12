package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent.LoadCollectionEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;

public class LoadCollectionEvent extends GwtEvent<LoadCollectionEventHandler> implements PrintableEvent {

	public interface LoadCollectionEventHandler extends EventHandler {
		void onLoad(LoadCollectionEvent event);
	}
	
	public static Type<LoadCollectionEventHandler> TYPE = new Type<LoadCollectionEventHandler>();
	private Collection collection;
	
	public LoadCollectionEvent(Collection collection) {
		this.collection = collection;
	}
	
	@Override
	public GwtEvent.Type<LoadCollectionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoadCollectionEventHandler handler) {
		handler.onLoad(this);
	}

	public static Type<LoadCollectionEventHandler> getTYPE() {
		return TYPE;
	}

	public Collection getCollection() {
		return collection;
	}

	@Override
	public String print() {
		return "Loading collection: \n" + collection.toString();
	}
}