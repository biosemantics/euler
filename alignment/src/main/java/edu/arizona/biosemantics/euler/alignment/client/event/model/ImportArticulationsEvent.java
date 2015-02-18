package edu.arizona.biosemantics.euler.alignment.client.event.model;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent.ImportArticulationsEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;

public class ImportArticulationsEvent  extends GwtEvent<ImportArticulationsEventHandler> implements PrintableEvent {

	public interface ImportArticulationsEventHandler extends EventHandler {
		void onImport(ImportArticulationsEvent event);
	}
	
	public static Type<ImportArticulationsEventHandler> TYPE = new Type<ImportArticulationsEventHandler>();
	private List<Articulation> articulations;
	
	public ImportArticulationsEvent(List<Articulation> articulations) {
		this.articulations = articulations;
	}
	
	@Override
	public GwtEvent.Type<ImportArticulationsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ImportArticulationsEventHandler handler) {
		handler.onImport(this);
	}

	public static Type<ImportArticulationsEventHandler> getTYPE() {
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
