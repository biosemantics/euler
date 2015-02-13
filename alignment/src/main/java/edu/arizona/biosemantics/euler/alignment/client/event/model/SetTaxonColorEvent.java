package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.model.SetTaxonColorEvent.SetTaxonColorEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Color;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class SetTaxonColorEvent extends GwtEvent<SetTaxonColorEventHandler> {

	public interface SetTaxonColorEventHandler extends EventHandler {
		void onSet(SetTaxonColorEvent event);
	}
	
	public static Type<SetTaxonColorEventHandler> TYPE = new Type<SetTaxonColorEventHandler>();
	private Taxon taxon;
	private Color color;
	
	public SetTaxonColorEvent(Taxon taxon, Color color) {
		this.taxon = taxon;
		this.color = color;
	}
	
	@Override
	public GwtEvent.Type<SetTaxonColorEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SetTaxonColorEventHandler handler) {
		handler.onSet(this);
	}

	public static Type<SetTaxonColorEventHandler> getTYPE() {
		return TYPE;
	}

	public Taxon getTaxon() {
		return taxon;
	}

	public Color getColor() {
		return color;
	}
	
}