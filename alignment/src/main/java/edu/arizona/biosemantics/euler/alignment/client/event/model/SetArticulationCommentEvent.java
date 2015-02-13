package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.model.SetArticulationCommentEvent.SetArticulationCommentEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;

public class SetArticulationCommentEvent extends GwtEvent<SetArticulationCommentEventHandler> {

	public interface SetArticulationCommentEventHandler extends EventHandler {
		void onSet(SetArticulationCommentEvent event);
	}
	
	public static Type<SetArticulationCommentEventHandler> TYPE = new Type<SetArticulationCommentEventHandler>();
	private Articulation articulation;
	private String comment;
	
	public SetArticulationCommentEvent(Articulation articulation, String comment) {
		this.articulation = articulation;
		this.comment = comment;
	}
	
	@Override
	public GwtEvent.Type<SetArticulationCommentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SetArticulationCommentEventHandler handler) {
		handler.onSet(this);
	}

	public static Type<SetArticulationCommentEventHandler> getTYPE() {
		return TYPE;
	}

	public Articulation getArticulation() {
		return articulation;
	}

	public String getComment() {
		return comment;
	}
	
}