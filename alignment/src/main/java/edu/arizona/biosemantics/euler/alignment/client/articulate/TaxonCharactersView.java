package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.client.common.Alerter;
import edu.arizona.biosemantics.euler.alignment.shared.Highlight;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;

public class TaxonCharactersView extends SimpleContainer {

	public class Node {
		
		public String id;
		public String name;
		
		public Node(String id, String name) {
			this.id = id;
			this.name = name;
		}
	}
	
	public interface NodeProperties extends PropertyAccess<Node> {

		@Path("id")
		ModelKeyProvider<Node> id();
		
		@Path("name")
		LabelProvider<Node> label();
		
		ValueProvider<Node, String> name();
	}
	
	public interface StateNodeProperties extends PropertyAccess<StateNode> {

		@Path("id")
		ModelKeyProvider<Node> id();
		
		@Path("name")
		LabelProvider<Node> label();
		
		ValueProvider<Node, String> name();
		
		ValueProvider<Node, CharacterState> characterState();
	}
	
	public class OrganNode extends Node {
		public OrganNode(String id, String name) {
			super(id, name);
		}
	}
	public class CharacterNode extends Node {		
		public CharacterNode(String id, String name) {
			super(id, name);
		}
	}
	public class StateNode extends Node {		
		public CharacterState characterState;

		public StateNode(String id, String name, CharacterState characterState) {
			super(id, name);
			this.characterState = characterState;
		}
	}
	
	private IEulerAlignmentServiceAsync alignmentService = GWT.create(IEulerAlignmentService.class);	
	private StateNodeProperties stateNodeProperties = GWT.create(StateNodeProperties.class);
	private NodeProperties nodeProperties = GWT.create(NodeProperties.class);
	private TreeGrid<Node> taxonTree;
	private TreeStore<Node> taxonTreeStore;

	public TaxonCharactersView() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();		
		taxonTree = createTaxonTreeGrid();
		taxonTreeStore = taxonTree.getTreeStore();
		
		vlc.add(taxonTree, new VerticalLayoutData(1, 1));
		this.setWidget(vlc);
	}
	
	public void addSelectionHandler(SelectionHandler<Node> handler) {
		taxonTree.getSelectionModel().addSelectionHandler(handler);
	}
	
	private TreeGrid<Node> createTaxonTreeGrid() {
		TreeStore<Node> taxonTreeStore = new TreeStore<Node>(new ModelKeyProvider<Node>() {
			public String getKey(Node item) {
				return item.id;
			}
		});
		
		ColumnConfig<Node, String> cc1 = new ColumnConfig<Node, String>(nodeProperties.name(), 120);
	    cc1.setHeader("Name");
	 
		ColumnConfig<Node, String> cc2 = new ColumnConfig<Node, String>(
				new ValueProvider<Node, String>() {
					@Override
					public String getValue(Node object) {
						return object instanceof StateNode ? String.valueOf(((StateNode) object).characterState.getDiagnosticValue().toString()) : "";
					}
					@Override
					public void setValue(Node object, String value) {
						if (object instanceof Node) {
							((StateNode) object).characterState.setDiagnosticValue(DiagnosticValue.valueOf(value));
						}
					}
					@Override
					public String getPath() {
						return "diagnosticValue";
					}
				}, 50);
		cc2.setHeader("Value");
	      
		ColumnConfig<Node, String> cc3 = new ColumnConfig<Node, String>(
				new ValueProvider<Node, String>() {
					@Override
					public String getValue(Node object) {
						return object instanceof StateNode ? String.valueOf(((StateNode) object).characterState.getDiagnosticScope().toString()) : "";
					}

					@Override
					public void setValue(Node object, String value) {
						if (object instanceof Node) {
							((StateNode) object).characterState.setDiagnosticScope(Rank.valueOf(value));
						}
					}

					@Override
					public String getPath() {
						return "diagnosticScope";
					}
				}, 60);
		cc3.setHeader("Scope");
		
		List<ColumnConfig<Node, ?>> l = new ArrayList<ColumnConfig<Node, ?>>();
	    l.add(cc1);
	    l.add(cc2);
	    l.add(cc3);
	    ColumnModel<Node> cm = new ColumnModel<Node>(l);
		TreeGrid<Node> taxonTreeGrid = new TreeGrid<Node>(taxonTreeStore, cm, cc1);
		return taxonTreeGrid;
	}
	
	protected void update(java.util.Collection<CharacterState> characterStates) {
		taxonTreeStore.clear();
		for(CharacterState characterState : characterStates) {
			String organ = characterState.getOrgan();
			String character = characterState.getCharacter();
			String state = characterState.getState();
			
			OrganNode organNode = new OrganNode(organ, organ);
			Node existingNode = taxonTreeStore.findModel(organNode);
			if(existingNode == null)
				taxonTreeStore.add(organNode);
			Node characterNode = new CharacterNode(organ + "-" + character, character);
			existingNode = taxonTreeStore.findModel(characterNode);
			if(existingNode == null)
				taxonTreeStore.add(organNode, characterNode);
			Node stateNode = new StateNode(organ + "-" + character + "-" + state, state, characterState);
			existingNode = taxonTreeStore.findModel(stateNode);
			if(existingNode == null)
				taxonTreeStore.add(characterNode, stateNode);
		}
		taxonTree.expandAll();
	}
	
}
