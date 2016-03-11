package edu.arizona.biosemantics.euler.alignment.client.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Params;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.MultiLinePromptMessageBox;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent.BeforeStartEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.HeaderMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import edu.arizona.biosemantics.euler.alignment.client.articulate.EvidenceDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell.CommentColorizableObjectsProvider;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;
import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Color;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class MinimalArticulationsGridView extends SimpleContainer /* extends ContentPanel*/ {

	protected EventBus eventBus;
	protected Collection collection;
	
	protected ListStore<Articulation> articulationsStore;
	protected Grid<Articulation> grid;
	protected ListStore<Relation> allTypesStore;
	protected ListStore<Relation> availableTypesStore;
	private Type type;
	private Articulation articulation;
	
	public MinimalArticulationsGridView(EventBus eventBus, Collection collection, Articulation articulation, Type type) {
		this.eventBus = eventBus;
		this.collection = collection;
		this.articulation = articulation;
		this.type = type;
		availableTypesStore = new ListStore<Relation>(new ModelKeyProvider<Relation>() {
			@Override
			public String getKey(Relation item) {
				return item.toString();
			}
		});
		allTypesStore = new ListStore<Relation>(new ModelKeyProvider<Relation>() {
			@Override
			public String getKey(Relation item) {
				return item.toString();
			}
		});
		allTypesStore.addAll(Arrays.asList(Relation.values()));
		
		//setHeadingText("Articulations");
		add(createArticulationsGrid());
		setArticulations(collection.getModel().getArticulations(articulation.getTaxonA(), articulation.getTaxonB(), type));
		
		bindEvents();
	}
	
	private void bindEvents() {
		eventBus.addHandler(LoadCollectionEvent.TYPE, new LoadCollectionEvent.LoadCollectionEventHandler() {
			@Override
			public void onLoad(LoadCollectionEvent event) {
				collection = event.getCollection();
			}
		});
		eventBus.addHandler(RemoveArticulationsEvent.TYPE, new RemoveArticulationsEvent.RemoveArticulationsEventHandler() {
			@Override
			public void onRemove(RemoveArticulationsEvent event) {
				for(Articulation articulation : event.getArticulations())
					if(articulation.getType().equals(type))
						articulationsStore.remove(articulation);
			}
		});
		eventBus.addHandler(AddArticulationsEvent.TYPE, new AddArticulationsEvent.AddArticulationEventHandler() {
			@Override
			public void onAdd(AddArticulationsEvent event) {
				for(Articulation articulation : event.getArticulations()) {
					if(articulation.getType().equals(type)) {
						articulationsStore.add(articulation);
					}
				}
			}
		});
	}

	public void setArticulations(List<Articulation> articulations) {
		articulationsStore.clear();
		articulationsStore.addAll(articulations);
	}

	public void removeArticulations(java.util.Collection<Articulation> articulations) {
		for(Articulation articulation : articulations)
			articulationsStore.remove(articulation);
	}

	public void addArticulations(List<Articulation> articulations) {
		articulationsStore.addAll(articulations);
	}

	private Widget createArticulationsGrid() {		
		ArticulationProperties articulationProperties = GWT.create(ArticulationProperties.class);
		articulationsStore = new ListStore<Articulation>(articulationProperties.key());
		articulationsStore.setAutoCommit(true);

		IdentityValueProvider<Articulation> identity = new IdentityValueProvider<Articulation>();
		ColorableCell colorableCell = new ColorableCell(eventBus, collection, null);
		colorableCell.setCommentColorizableObjectsStore(articulationsStore, new CommentColorizableObjectsProvider() {
			@Override
			public Object provide(Object source) {
				return source;
			}
		});

		String aHead = "Taxonomic Concept A";
		String bHead = "Taxonomic Concept B";
		if(collection != null) {
			 aHead = "Taxonomic Concept " + collection.getModel().getTaxonomies().get(0).getSecString();
			 bHead = "Taxonomic Concept " + collection.getModel().getTaxonomies().get(1).getSecString();
		}
		//ColumnConfig<Articulation, String> taxonACol = new ColumnConfig<Articulation, String>(
		//		new ArticulationProperties.TaxonAStringValueProvider(), 250, aHead);
		//taxonACol.setCell(colorableCell);
		final ColumnConfig<Articulation, Relation> relationCol = new ColumnConfig<Articulation, Relation>(
				articulationProperties.relation(), 70, "Articulation");
		relationCol.setCell(colorableCell);
		//ColumnConfig<Articulation, String> taxonBCol = new ColumnConfig<Articulation, String>(
		//		new ArticulationProperties.TaxonBStringValueProvider(), 250, bHead);
		//taxonBCol.setCell(colorableCell);
		
		ValueProvider<Articulation, String> commentValueProvider = new ValueProvider<Articulation, String>() {
			@Override
			public String getValue(Articulation object) {
				if(collection.getModel().hasComment(object))
					return collection.getModel().getComment(object);
				return "";
			}
			@Override
			public void setValue(Articulation object, String value) {
				collection.getModel().setComment(object, value);
			}
			@Override
			public String getPath() {
				return "comment";
			}
		};
		final ColumnConfig<Articulation, String> commentCol = new ColumnConfig<Articulation, String>(
				commentValueProvider, 300, "Comment");
		commentCol.setCell(colorableCell);

		List<ColumnConfig<Articulation, ?>> columns = new ArrayList<ColumnConfig<Articulation, ?>>();
		//columns.add(taxonACol);
		columns.add(relationCol);
		//columns.add(taxonBCol);
		columns.add(commentCol);
		ColumnModel<Articulation> cm = new ColumnModel<Articulation>(columns);
		
		grid = new Grid<Articulation>(articulationsStore, cm);
		grid.setBorders(false);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);

		GridInlineEditing<Articulation> editing = new GridInlineEditing<Articulation>(grid);
		
		ComboBox<Relation> relationCombo = createRelationCombo();
		
		editing.addEditor(relationCol, relationCombo);
		editing.addEditor(commentCol, new TextField());
		editing.addStartEditHandler(new StartEditHandler<Articulation>() {
			@Override
			public void onStartEdit(StartEditEvent<Articulation> event) {
				Articulation articulation = grid.getStore().get(event.getEditCell().getRow());
				java.util.Collection<Relation> availableTypes = collection.getModel().getAvailableRelations(articulation.getTaxonA(), articulation.getTaxonB(), Type.USER);
				availableTypesStore.clear();
				availableTypesStore.addAll(availableTypes);
			}
		});
		editing.addCompleteEditHandler(new CompleteEditHandler<Articulation>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Articulation> event) {			
				GridCell cell = event.getEditCell();
				Articulation articulation = grid.getStore().get(cell.getRow());
				ColumnConfig<Articulation, ?> config = grid.getColumnModel().getColumn(cell.getCol());
				if(config.equals(relationCol)) {
					Relation type = (Relation)config.getValueProvider().getValue(articulation);
					eventBus.fireEvent(new ModifyArticulationEvent(articulation, type));
				}
				if(config.equals(commentCol)) {
					String comment = (String)config.getValueProvider().getValue(articulation);
					eventBus.fireEvent(new SetCommentEvent(articulation, comment));
				}
			}
		});
		return grid;
	}
	
	private ComboBox<Relation> createRelationCombo() {
		ComboBox<Relation> relationCombo = new ComboBox<Relation>(availableTypesStore, new LabelProvider<Relation>() {
			@Override
			public String getLabel(Relation item) {
				return item.toString();
			}
		});
		relationCombo.setForceSelection(false);
		relationCombo.setTriggerAction(TriggerAction.ALL);
		relationCombo.setTypeAhead(false);
		relationCombo.setEditable(false);
		return relationCombo;
	}
	
	protected void updateStore(Articulation articulation) {
		if(articulationsStore.findModel(articulation) != null)
			articulationsStore.update(articulation);
	}

	public List<Articulation> getSelectedArticulations() {
		return grid.getSelectionModel().getSelectedItems();
	}
}
