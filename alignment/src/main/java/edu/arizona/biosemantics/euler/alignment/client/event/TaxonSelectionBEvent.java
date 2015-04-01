package edu.arizona.biosemantics.euler.alignment.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.TaxonSelectionBEvent.TaxonSelectionEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class TaxonSelectionBEvent extends GwtEvent<TaxonSelectionEventHandler> {

	public interface TaxonSelectionEventHandler extends EventHandler {
		void onSelect(TaxonSelectionBEvent event);
	}
	
	public static Type<TaxonSelectionEventHandler> TYPE = new Type<TaxonSelectionEventHandler>();
	private Taxon taxon;
	private Object source;
	
	public TaxonSelectionBEvent(Taxon taxon, Object source) {
		this.taxon = taxon;
		this.source = source;
	}
		
	public Taxon getTaxon() {
		return taxon;
	}
	
	public Object getSource() {
		return source;
	}

	@Override
	public GwtEvent.Type<TaxonSelectionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TaxonSelectionEventHandler handler) {
		handler.onSelect(this);
	}

	public static Type<TaxonSelectionEventHandler> getTYPE() {
		return TYPE;
	}
}