package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent.ModifyDiagnosticValueEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;

public class ModifyDiagnosticValueEvent extends GwtEvent<ModifyDiagnosticValueEventHandler> implements PrintableEvent {

	public interface ModifyDiagnosticValueEventHandler extends EventHandler {
		void onModify(ModifyDiagnosticValueEvent event);
	}
	
	public static Type<ModifyDiagnosticValueEventHandler> TYPE = new Type<ModifyDiagnosticValueEventHandler>();
	private Evidence evidence;
	private DiagnosticValue newDiagnosticValue;
	
	public ModifyDiagnosticValueEvent(Evidence evidence, DiagnosticValue newDiagnosticValue) {
		this.evidence = evidence;
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

	public Evidence getEvidence() {
		return evidence;
	}
	
	public DiagnosticValue getNewDiagnosticValue() {
		return newDiagnosticValue;
	}

	@Override
	public String print() {
		return "Modifying evidence: \n" + evidence.toString();
	}

}
