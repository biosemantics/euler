package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent.SetCommentEventHandler;

public class SetCommentEvent extends GwtEvent<SetCommentEventHandler> {

	public interface SetCommentEventHandler extends EventHandler {
		void onSet(SetCommentEvent event);
	}
	
	public static Type<SetCommentEventHandler> TYPE = new Type<SetCommentEventHandler>();
	private Object object;
	private String comment;
	
	public SetCommentEvent(Object object, String comment) {
		this.object = object;
		this.comment = comment;
	}
	
	@Override
	public GwtEvent.Type<SetCommentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SetCommentEventHandler handler) {
		handler.onSet(this);
	}

	public static Type<SetCommentEventHandler> getTYPE() {
		return TYPE;
	}

	public Object getObject() {
		return object;
	}

	public String getComment() {
		return comment;
	}
	
}