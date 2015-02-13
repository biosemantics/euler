package edu.arizona.biosemantics.euler.alignment.client.event.model;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent.AddArticulationEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class AddArticulationsEvent extends GwtEvent<AddArticulationEventHandler> implements PrintableEvent {

	public interface AddArticulationEventHandler extends EventHandler {
		void onAdd(AddArticulationsEvent event);
	}
	
	public static Type<AddArticulationEventHandler> TYPE = new Type<AddArticulationEventHandler>();
	private List<Articulation> articulations;
	
	public AddArticulationsEvent(Articulation articulation) {
		this.articulations = new LinkedList<Articulation>();
		this.articulations.add(articulation);
	}
	
	public AddArticulationsEvent(List<Articulation> articulations) {
		this.articulations = articulations;
	}
	
	@Override
	public GwtEvent.Type<AddArticulationEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddArticulationEventHandler handler) {
		handler.onAdd(this);
	}

	public static Type<AddArticulationEventHandler> getTYPE() {
		return TYPE;
	}

	public List<Articulation> getArticulations() {
		return articulations;
	}

	@Override
	public String print() {
		return "Adding articulations: \n" + articulations.toString();
	}

}
