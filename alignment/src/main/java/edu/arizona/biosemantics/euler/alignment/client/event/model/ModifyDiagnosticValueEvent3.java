package edu.arizona.biosemantics.euler.alignment.client.event.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent3.ModifyDiagnosticValueEventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.Node;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.StateNode;

public class ModifyDiagnosticValueEvent3 extends GwtEvent<ModifyDiagnosticValueEventHandler> implements PrintableEvent {

	public interface ModifyDiagnosticValueEventHandler extends EventHandler {
		void onModify(ModifyDiagnosticValueEvent3 event);
	}
	
	public static Type<ModifyDiagnosticValueEventHandler> TYPE = new Type<ModifyDiagnosticValueEventHandler>();
	private Node node;
	private DiagnosticValue newDiagnosticValue;
	
	public ModifyDiagnosticValueEvent3(Node node, DiagnosticValue newDiagnosticValue) {
		this.node = node;
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

	public Node getNode() {
		return node;
	}
	
	public DiagnosticValue getNewDiagnosticValue() {
		return newDiagnosticValue;
	}

	@Override
	public String print() {
		return "Modifying statenode: \n" + node.toString();
	}

}
