package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent.ModifyEvidenceEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;

public class ModifyDiagnosticValueEvent extends GwtEvent<ModifyEvidenceEventHandler> implements PrintableEvent {

	public interface ModifyEvidenceEventHandler extends EventHandler {
		void onModify(ModifyDiagnosticValueEvent event);
	}
	
	public static Type<ModifyEvidenceEventHandler> TYPE = new Type<ModifyEvidenceEventHandler>();
	private Evidence evidence;
	private DiagnosticValue newDiagnosticValue;
	
	public ModifyDiagnosticValueEvent(Evidence evidence, DiagnosticValue newDiagnosticValue) {
		this.evidence = evidence;
		this.newDiagnosticValue = newDiagnosticValue;
	}

	@Override
	public GwtEvent.Type<ModifyEvidenceEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ModifyEvidenceEventHandler handler) {
		handler.onModify(this);
	}

	public static Type<ModifyEvidenceEventHandler> getTYPE() {
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
