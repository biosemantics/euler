package edu.arizona.biosemantics.euler.alignment.client.event.model;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent.RemoveArticulationsEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;

public class RemoveArticulationsEvent extends GwtEvent<RemoveArticulationsEventHandler> implements PrintableEvent {

	public interface RemoveArticulationsEventHandler extends EventHandler {
		void onRemove(RemoveArticulationsEvent event);
	}
	
	public static Type<RemoveArticulationsEventHandler> TYPE = new Type<RemoveArticulationsEventHandler>();
	private List<Articulation> articulations;
	
	public RemoveArticulationsEvent(Articulation articulation) {
		this.articulations = new LinkedList<Articulation>();
		this.articulations.add(articulation);
	}
	
	public RemoveArticulationsEvent(List<Articulation> articulations) {
		this.articulations = articulations;
	}
	
	@Override
	public GwtEvent.Type<RemoveArticulationsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RemoveArticulationsEventHandler handler) {
		handler.onRemove(this);
	}

	public static Type<RemoveArticulationsEventHandler> getTYPE() {
		return TYPE;
	}

	public List<Articulation> getArticulations() {
		return articulations;
	}

	@Override
	public String print() {
		return "Removeing articulations: \n" + articulations.toString();
	}

}
