package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticScopeEvent.ModifyDiagnosticScopeEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;

public class ModifyDiagnosticScopeEvent extends GwtEvent<ModifyDiagnosticScopeEventHandler> implements PrintableEvent {

	public interface ModifyDiagnosticScopeEventHandler extends EventHandler {
		void onModify(ModifyDiagnosticScopeEvent event);
	}
	
	public static Type<ModifyDiagnosticScopeEventHandler> TYPE = new Type<ModifyDiagnosticScopeEventHandler>();
	private Evidence evidence;
	private Rank newScopeValue;
	
	public ModifyDiagnosticScopeEvent(Evidence evidence, Rank newScopeValue) {
		this.evidence = evidence;
		this.newScopeValue = newScopeValue;
	}

	@Override
	public GwtEvent.Type<ModifyDiagnosticScopeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ModifyDiagnosticScopeEventHandler handler) {
		handler.onModify(this);
	}

	public static Type<ModifyDiagnosticScopeEventHandler> getTYPE() {
		return TYPE;
	}

	public Evidence getEvidence() {
		return evidence;
	}
	
	public Rank getNewDiagnosticScope() {
		return newScopeValue;
	}

	@Override
	public String print() {
		return "Modifying evidence: \n" + evidence.toString();
	}

}
