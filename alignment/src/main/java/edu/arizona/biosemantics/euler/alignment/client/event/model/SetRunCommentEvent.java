package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.model.SetRunCommentEvent.SetRunCommentEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class SetRunCommentEvent extends GwtEvent<SetRunCommentEventHandler> {

	public interface SetRunCommentEventHandler extends EventHandler {
		void onSet(SetRunCommentEvent event);
	}
	
	public static Type<SetRunCommentEventHandler> TYPE = new Type<SetRunCommentEventHandler>();
	private Run run;
	private String comment;
	
	public SetRunCommentEvent(Run run, String comment) {
		this.run = run;
		this.comment = comment;
	}
	
	@Override
	public GwtEvent.Type<SetRunCommentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SetRunCommentEventHandler handler) {
		handler.onSet(this);
	}

	public static Type<SetRunCommentEventHandler> getTYPE() {
		return TYPE;
	}

	public Run getRun() {
		return run;
	}

	public String getComment() {
		return comment;
	}
	
}