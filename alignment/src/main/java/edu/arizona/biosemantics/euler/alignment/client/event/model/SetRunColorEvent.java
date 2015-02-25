package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.model.SetRunColorEvent.SetRunColorEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Color;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;

public class SetRunColorEvent extends GwtEvent<SetRunColorEventHandler> {

	public interface SetRunColorEventHandler extends EventHandler {
		void onSet(SetRunColorEvent event);
	}
	
	public static Type<SetRunColorEventHandler> TYPE = new Type<SetRunColorEventHandler>();
	private Run run;
	private Color color;
	
	public SetRunColorEvent(Run run, Color color) {
		this.run = run;
		this.color = color;
	}
	
	@Override
	public GwtEvent.Type<SetRunColorEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SetRunColorEventHandler handler) {
		handler.onSet(this);
	}

	public static Type<SetRunColorEventHandler> getTYPE() {
		return TYPE;
	}

	public Run getRun() {
		return run;
	}

	public Color getColor() {
		return color;
	}
	
}