package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent2.ModifyDiagnosticValueEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Overlap;

public class ModifyDiagnosticValueEvent2 extends GwtEvent<ModifyDiagnosticValueEventHandler> implements PrintableEvent {

	public interface ModifyDiagnosticValueEventHandler extends EventHandler {
		void onModify(ModifyDiagnosticValueEvent2 event);
	}
	
	public static Type<ModifyDiagnosticValueEventHandler> TYPE = new Type<ModifyDiagnosticValueEventHandler>();
	private Overlap overlap;
	private DiagnosticValue newDiagnosticValue;
	
	public ModifyDiagnosticValueEvent2(Overlap overlap, DiagnosticValue newDiagnosticValue) {
		this.overlap = overlap;
		this.newDiagnosticValue = newDiagnosticValue;
	}

	@Override
	public GwtEvent.Type<ModifyDiagnosticValueEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ModifyDiagnosticValueEventHandler handler) {
		handler.onModify(this);
	}

	public static Type<ModifyDiagnosticValueEventHandler> getTYPE() {
		return TYPE;
	}

	public Overlap getOverlap() {
		return overlap;
	}
	
	public DiagnosticValue getNewDiagnosticValue() {
		return newDiagnosticValue;
	}

	@Override
	public String print() {
		return "Modifying evidence: \n" + overlap.toString();
	}

}
