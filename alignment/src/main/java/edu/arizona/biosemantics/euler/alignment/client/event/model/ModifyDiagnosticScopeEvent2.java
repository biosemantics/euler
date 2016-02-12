package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticScopeEvent2.ModifyDiagnosticScopeEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Overlap;

public class ModifyDiagnosticScopeEvent2 extends GwtEvent<ModifyDiagnosticScopeEventHandler> implements PrintableEvent {

	public interface ModifyDiagnosticScopeEventHandler extends EventHandler {
		void onModify(ModifyDiagnosticScopeEvent2 event);
	}
	
	public static Type<ModifyDiagnosticScopeEventHandler> TYPE = new Type<ModifyDiagnosticScopeEventHandler>();
	private Overlap overlap;
	private Rank newScopeValue;
	
	public ModifyDiagnosticScopeEvent2(Overlap overlap, Rank newScopeValue) {
		this.overlap = overlap;
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

	public Overlap getOverlap() {
		return overlap;
	}
	
	public Rank getNewDiagnosticScope() {
		return newScopeValue;
	}

	@Override
	public String print() {
		return "Modifying evidence: \n" + overlap.toString();
	}

}
