package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sencha.gxt.widget.core.client.menu.Item;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.menu.HeaderMenuItem;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.client.common.Alerter;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticScopeEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticScopeEvent3;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent3;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;
import edu.arizona.biosemantics.euler.alignment.shared.Highlight;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.CharacterNode;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.Node;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.NodeProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.OrganNode;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.StateNode;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.StateNodeProperties;

public class TaxonCharactersView extends SimpleContainer {
	
	public class CharacterStatesMenu extends Menu implements BeforeShowHandler {

		public CharacterStatesMenu() {
			this.setWidth(200);
			
			this.add(new HeaderMenuItem("View"));
			MenuItem expand = new MenuItem("Expand All");
			expand.addSelectionHandler(new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					expandAll();
				}
			});
			this.add(expand);
			MenuItem collapse = new MenuItem("Collapse All");
			collapse.addSelectionHandler(new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					collapseAll();
				}
			});
			this.add(collapse);
			
			MenuItem expandOrgans = new MenuItem("Expand Organs");
			expandOrgans.addSelectionHandler(new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					expandOrgans();
				}
			});
			this.add(expandOrgans);
			
			MenuItem expandCharacters = new MenuItem("Expand Characters");
			expandOrgans.addSelectionHandler(new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					expandCharacters();
				}
			});
			this.add(expandCharacters);
			
			MenuItem collapseOrgans = new MenuItem("Collapse Organs");
			expandOrgans.addSelectionHandler(new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					collapseOrgans();
				}
			});
			this.add(collapseOrgans);
			
			MenuItem collapseCharacters = new MenuItem("Collapse Characters");
			expandOrgans.addSelectionHandler(new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					collapseCharacters();
				}
			});
			this.add(collapseCharacters);
			
			this.addBeforeShowHandler(this);
		}

		@Override
		public void onBeforeShow(BeforeShowEvent event) {
			this.clear();
		}
	}
	
	private IEulerAlignmentServiceAsync alignmentService = GWT.create(IEulerAlignmentService.class);	
	private StateNodeProperties stateNodeProperties = GWT.create(StateNodeProperties.class);
	private NodeProperties nodeProperties = GWT.create(NodeProperties.class);
	private EventBus eventBus;
	private Collection collection;
	
	private TreeGrid<Node> taxonTree;
	private TreeStore<Node> taxonTreeStore;
	private ListStore<DiagnosticValue> diagnosticValueStore;
	private ListStore<Rank> rankStore;

	public TaxonCharactersView(EventBus eventBus, Collection collection) {
		this.eventBus = eventBus;
		this.collection = collection;
		diagnosticValueStore = new ListStore<DiagnosticValue>(new ModelKeyProvider<DiagnosticValue>() {
			@Override
			public String getKey(DiagnosticValue item) {
				return item.toString();
			}
		});
		for(DiagnosticValue diagnosticValue : DiagnosticValue.values()) 
			diagnosticValueStore.add(diagnosticValue);
		rankStore = new ListStore<Rank>(new ModelKeyProvider<Rank>() {
			@Override
			public String getKey(Rank item) {
				return item.toString();
			}
		});
		rankStore.addAll(Arrays.asList(Rank.values()));
		
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();		
		taxonTree = createTaxonTreeGrid();
		taxonTreeStore = taxonTree.getTreeStore();
		vlc.add(taxonTree, new VerticalLayoutData(1, 1));
		this.setWidget(vlc);
		this.setContextMenu(new CharacterStatesMenu());
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
		taxonTreeStore.setAutoCommit(true);
		ColumnConfig<Node, String> nameColumn = new ColumnConfig<Node, String>(nodeProperties.name(), 150);
	    nameColumn.setHeader("Name");
	 
	    ValueProvider<Node, DiagnosticValue> diagnosticValueValueProvider = new ValueProvider<Node, DiagnosticValue>() {
			@Override
			public DiagnosticValue getValue(Node object) {
				return object.diagnosticValue;
			}
			@Override
			public void setValue(Node object, DiagnosticValue value) {
				object.diagnosticValue = value;
			}
			@Override
			public String getPath() {
				return "diagnosticValue";
			}
		};
		final ColumnConfig<Node, DiagnosticValue> diagnosticValueColumn = new ColumnConfig<Node, DiagnosticValue>(diagnosticValueValueProvider, 70);
		diagnosticValueColumn.setHeader("Weight");
	      
		/*ValueProvider<Node, Rank> diagnosticScopeValueProvider = new ValueProvider<Node, Rank>() {
			@Override
			public Rank getValue(Node object) {
				return object instanceof StateNode ? ((StateNode) object).characterState.getDiagnosticScope() : null;
			}
			@Override
			public void setValue(Node object, Rank value) {
				if (object instanceof Node) {
					((StateNode) object).characterState.setDiagnosticScope(value);
				}
			}
			@Override
			public String getPath() {
				return "diagnosticScope";
			}
		};
		final ColumnConfig<Node, Rank> diagnosticScopeColumn = new ColumnConfig<Node, Rank>(diagnosticScopeValueProvider, 60);
		diagnosticScopeColumn.setHeader("Scope");*/
		
		List<ColumnConfig<Node, ?>> l = new ArrayList<ColumnConfig<Node, ?>>();
	    l.add(nameColumn);
	    l.add(diagnosticValueColumn);
	    //l.add(diagnosticScopeColumn);
	    ColumnModel<Node> cm = new ColumnModel<Node>(l);
		final TreeGrid<Node> taxonTreeGrid = new TreeGrid<Node>(taxonTreeStore, cm, nameColumn);
		
		GridInlineEditing<Node> editing = new GridInlineEditing<Node>(taxonTreeGrid);
		//rowEditing.addEditor(diagnosticScopeColumn, createDiagnosticScopeCombo());
		editing.addEditor(diagnosticValueColumn, createDiagnosticValueCombo());
		editing.addCompleteEditHandler(new CompleteEditHandler<Node>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Node> event) {
				GridCell cell = event.getEditCell();
				Node node = taxonTreeGrid.getStore().get(cell.getRow());
				ColumnConfig<Node, ?> config = taxonTreeGrid.getColumnModel().getColumn(cell.getCol());
				DiagnosticValue diagnosticValue = (DiagnosticValue)config.getValueProvider().getValue(node);
				eventBus.fireEvent(new ModifyDiagnosticValueEvent3(node, diagnosticValue));
			}
		});
		
		
		StringFilter<Node> nameFilter = new StringFilter<Node>(nodeProperties.name());
		ListFilter<Node, DiagnosticValue> diagnosticValueFilter = new ListFilter<Node, DiagnosticValue>(
				diagnosticValueValueProvider, this.diagnosticValueStore) {
			@Override
			public boolean validateModel(Node model) {
				if(!(model instanceof StateNode))
					return true;
				return super.validateModel(model);/*
				Object value = getValueProvider().getValue(model);
				List<?> values = (List<?>) getValue();
				return values.size() == 0 || values.contains(value);*/
			}
		};
		//ListFilter<Node, Rank> diagnosticScopeFilter = new ListFilter<Node, Rank>(
		//		diagnosticScopeValueProvider, this.rankStore);
		GridFilters<Node> filters = new GridFilters<Node>();
		filters.addFilter(nameFilter);
		filters.addFilter(diagnosticValueFilter);
		//filters.addFilter(diagnosticScopeFilter);
		filters.setLocal(true);
		filters.initPlugin(taxonTreeGrid);
		return taxonTreeGrid;
	}

	private ComboBox<Rank> createDiagnosticScopeCombo() {
		ComboBox<Rank> diagnosticValueCombo = new ComboBox<Rank>(rankStore, new LabelProvider<Rank>() {
			@Override
			public String getLabel(Rank item) {
				return item.name();
			}
		});
		diagnosticValueCombo.setForceSelection(false);
		diagnosticValueCombo.setTriggerAction(TriggerAction.ALL);
		diagnosticValueCombo.setTypeAhead(false);
		diagnosticValueCombo.setEditable(false);
		return diagnosticValueCombo;
	}

	private ComboBox<DiagnosticValue> createDiagnosticValueCombo() {
		ComboBox<DiagnosticValue> diagnosticValueCombo = new ComboBox<DiagnosticValue>(diagnosticValueStore, new LabelProvider<DiagnosticValue>() {
			@Override
			public String getLabel(DiagnosticValue item) {
				return item.getDisplayName();
			}
		});
		diagnosticValueCombo.setForceSelection(false);
		diagnosticValueCombo.setTriggerAction(TriggerAction.ALL);
		diagnosticValueCombo.setTypeAhead(false);
		diagnosticValueCombo.setEditable(false);
		return diagnosticValueCombo;
	}

	protected void expandAll() {
		taxonTree.expandAll();
	}
	
	protected void collapseAll() {
		taxonTree.collapseAll();
	}
	
	protected void collapseCharacters() {
		for(Node node : this.taxonTreeStore.getRootItems()) {
			for(Node characterNode : this.taxonTreeStore.getChildren(node)) {
				this.taxonTree.setExpanded(characterNode, false, true);
			}
		}
	}

	protected void collapseOrgans() {
		for(Node node : this.taxonTreeStore.getRootItems()) {
			this.taxonTree.setExpanded(node, false, true);
		}
	}

	protected void expandOrgans() {
		for(Node node : this.taxonTreeStore.getRootItems()) {
			this.taxonTree.setExpanded(node, false, true);
			this.taxonTree.setExpanded(node, true, false);
		}
	}

	protected void expandCharacters() {
		for(Node node : this.taxonTreeStore.getRootItems()) {
			this.taxonTree.setExpanded(node, true, true);
		}
	}
	
	protected void update(java.util.Collection<CharacterState> characterStates) {
		taxonTreeStore.clear();
		for(CharacterState characterState : characterStates) {
			String organ = characterState.getOrgan();
			String character = characterState.getCharacter();
			String state = characterState.getState();
			
			OrganNode organNode = new OrganNode(organ, organ);
			Node existingNode = taxonTreeStore.findModel(organNode);
			if(existingNode == null) {
				organNode.diagnosticValue = collection.getModel().getDiagnosticValue(organNode);
				taxonTreeStore.add(organNode);
			}
			
			Node characterNode = new CharacterNode(organ + "-" + character, character);
			existingNode = taxonTreeStore.findModel(characterNode);
			if(existingNode == null) {
				characterNode.diagnosticValue = collection.getModel().getDiagnosticValue(characterNode);
				taxonTreeStore.add(organNode, characterNode);
			}
			
			Node stateNode = new StateNode(organ + "-" + character + "-" + state, state, characterState);
			existingNode = taxonTreeStore.findModel(stateNode);
			if(existingNode == null) {
				stateNode.diagnosticValue = collection.getModel().getDiagnosticValue(stateNode);
				taxonTreeStore.add(characterNode, stateNode);
			}
		}
		taxonTreeStore.addSortInfo(new StoreSortInfo<Node>(nodeProperties.name(), SortDir.DESC));
	}

	public void select(Node node) {
		if(node instanceof OrganNode) {
			for(Node organNode : taxonTreeStore.getRootItems()) {
				if(organNode.name.equals(node.name))
					this.taxonTree.getSelectionModel().select(false, organNode);
			}
		}
		if(node instanceof CharacterNode) {
			for(Node organNode : taxonTreeStore.getRootItems()) {
				for(Node characterNode : taxonTreeStore.getChildren(organNode)) {
					if(characterNode.name.equals(node.name))
						this.taxonTree.getSelectionModel().select(false, characterNode);
				}
			}
		}
		if(node instanceof StateNode) {
			for(Node organNode : taxonTreeStore.getRootItems()) {
				for(Node characterNode : taxonTreeStore.getChildren(organNode)) {
					for(Node stateNode : taxonTreeStore.getChildren(characterNode)) {
						if(stateNode.name.equals(node.name))
							this.taxonTree.getSelectionModel().select(false, stateNode);
					}
				}
			}
		}
	}
}
