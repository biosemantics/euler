package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent.ModifyArticulationEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;

public class ModifyArticulationEvent extends GwtEvent<ModifyArticulationEventHandler> implements PrintableEvent {

	public interface ModifyArticulationEventHandler extends EventHandler {
		void onModify(ModifyArticulationEvent event);
	}
	
	public static Type<ModifyArticulationEventHandler> TYPE = new Type<ModifyArticulationEventHandler>();
	private Articulation articulation;
	private Relation newType;
	
	public ModifyArticulationEvent(Articulation articulation, Relation newType) {
		this.articulation = articulation;
		this.newType = newType;
	}
	
	@Override
	public GwtEvent.Type<ModifyArticulationEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ModifyArticulationEventHandler handler) {
		handler.onModify(this);
	}

	public static Type<ModifyArticulationEventHandler> getTYPE() {
		return TYPE;
	}

	public Articulation getArticulation() {
		return articulation;
	}
	
	public Relation getNewType() {
		return newType;
	}

	@Override
	public String print() {
		return "Modifying articulation: \n" + articulation.toString();
	}

}
