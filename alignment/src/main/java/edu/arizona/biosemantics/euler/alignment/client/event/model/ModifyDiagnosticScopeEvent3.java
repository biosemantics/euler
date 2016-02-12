package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticScopeEvent3.ModifyDiagnosticScopeEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Overlap;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.Node;

public class ModifyDiagnosticScopeEvent3 extends GwtEvent<ModifyDiagnosticScopeEventHandler> implements PrintableEvent {

	public interface ModifyDiagnosticScopeEventHandler extends EventHandler {
		void onModify(ModifyDiagnosticScopeEvent3 event);
	}
	
	public static Type<ModifyDiagnosticScopeEventHandler> TYPE = new Type<ModifyDiagnosticScopeEventHandler>();
	private Node node;
	private Rank newScopeValue;
	
	public ModifyDiagnosticScopeEvent3(Node node, Rank newScopeValue) {
		this.node = node;
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

	public Node getNode() {
		return node;
	}
	
	public Rank getNewDiagnosticScope() {
		return newScopeValue;
	}

	@Override
	public String print() {
		return "Modifying evidence: \n" + node.toString();
	}

}
