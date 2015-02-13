package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.model.SetArticulationColorEvent.SetArticulationColorEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Color;

public class SetArticulationColorEvent extends GwtEvent<SetArticulationColorEventHandler> {

	public interface SetArticulationColorEventHandler extends EventHandler {
		void onSet(SetArticulationColorEvent event);
	}
	
	public static Type<SetArticulationColorEventHandler> TYPE = new Type<SetArticulationColorEventHandler>();
	private Articulation articulation;
	private Color color;
	
	public SetArticulationColorEvent(Articulation articulation, Color color) {
		this.articulation = articulation;
		this.color = color;
	}
	
	@Override
	public GwtEvent.Type<SetArticulationColorEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SetArticulationColorEventHandler handler) {
		handler.onSet(this);
	}

	public static Type<SetArticulationColorEventHandler> getTYPE() {
		return TYPE;
	}

	public Articulation getArticulation() {
		return articulation;
	}

	public Color getColor() {
		return color;
	}

}
