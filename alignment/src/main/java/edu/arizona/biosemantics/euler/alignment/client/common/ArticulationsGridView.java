package edu.arizona.biosemantics.euler.alignment.client.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Params;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.MultiLinePromptMessageBox;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent.BeforeStartEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
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

import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell.CommentColorizableObjectsProvider;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationType;
import edu.arizona.biosemantics.euler.alignment.shared.model.Color;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class ArticulationsGridView extends ContentPanel {

	protected EventBus eventBus;
	protected Model model;
	
	protected ListStore<Articulation> articulationsStore;
	protected Grid<Articulation> grid;
	protected ListStore<ArticulationType> allTypesStore;
	protected ListStore<ArticulationType> availableTypesStore;
	private boolean relationEditEnabled;
	private boolean removeEnabled;
	
	public ArticulationsGridView(EventBus eventBus, Model model, boolean relationEditEnabled, boolean removeEnabled) {
		this.eventBus = eventBus;
		this.model = model;
		
		this.relationEditEnabled = relationEditEnabled;
		this.removeEnabled = removeEnabled;

		availableTypesStore = new ListStore<ArticulationType>(new ModelKeyProvider<ArticulationType>() {
			@Override
			public String getKey(ArticulationType item) {
				return item.toString();
			}
		});
		allTypesStore = new ListStore<ArticulationType>(new ModelKeyProvider<ArticulationType>() {
			@Override
			public String getKey(ArticulationType item) {
				return item.toString();
			}
		});
		allTypesStore.addAll(Arrays.asList(ArticulationType.values()));
		
		setHeadingText("Articulations");
		add(createArticulationsGrid());
	}

	protected void bindEvents() {
		eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
			@Override
			public void onLoad(LoadModelEvent event) {
				model = event.getModel();
			}
		});
	}

	public void setArticulations(List<Articulation> articulations) {
		articulationsStore.clear();
		articulationsStore.addAll(articulations);
	}

	public void removeArticulations(List<Articulation> articulations) {
		for(Articulation articulation : articulations)
			articulationsStore.remove(articulation);
	}

	public void addArticulations(List<Articulation> articulations) {
		articulationsStore.addAll(articulations);
	}

	private Widget createArticulationsGrid() {		
		ArticulationProperties articulationProperties = GWT
				.create(ArticulationProperties.class);
		articulationsStore = new ListStore<Articulation>(articulationProperties.key());
		articulationsStore.setAutoCommit(true);

		IdentityValueProvider<Articulation> identity = new IdentityValueProvider<Articulation>();
		final CheckBoxSelectionModel<Articulation> checkBoxSelectionModel = new CheckBoxSelectionModel<Articulation>(
				identity);

		checkBoxSelectionModel.setSelectionMode(SelectionMode.MULTI);

		ColorableCell colorableCell = new ColorableCell(eventBus, model);
		colorableCell.setCommentColorizableObjectsStore(articulationsStore, new CommentColorizableObjectsProvider() {
			@Override
			public Object provide(Object source) {
				return source;
			}
		});
		final ColumnConfig<Articulation, String> taxonACol = new ColumnConfig<Articulation, String>(
				new ArticulationProperties.TaxonAStringValueProvider(), 100, "Taxon A");
		taxonACol.setCell(colorableCell);
		final ColumnConfig<Articulation, ArticulationType> relationCol = new ColumnConfig<Articulation, ArticulationType>(
				articulationProperties.type(), 100, "Relation");
		relationCol.setCell(colorableCell);
		final ColumnConfig<Articulation, String> taxonBCol = new ColumnConfig<Articulation, String>(
				new ArticulationProperties.TaxonBStringValueProvider(), 100, "Taxon B");
		taxonBCol.setCell(colorableCell);
		
		ValueProvider<Articulation, String> commentValueProvider = new ValueProvider<Articulation, String>() {
			@Override
			public String getValue(Articulation object) {
				if(model.hasComment(object))
					return model.getComment(object);
				return "";
			}
			@Override
			public void setValue(Articulation object, String value) {
				model.setComment(object, value);
			}
			@Override
			public String getPath() {
				return "comment";
			}
		};
		
		final ColumnConfig<Articulation, String> commentCol = new ColumnConfig<Articulation, String>(
				commentValueProvider, 400, "Comment");
		commentCol.setCell(colorableCell);

		List<ColumnConfig<Articulation, ?>> columns = new ArrayList<ColumnConfig<Articulation, ?>>();
		columns.add(checkBoxSelectionModel.getColumn());
		columns.add(taxonACol);
		columns.add(relationCol);
		columns.add(taxonBCol);
		columns.add(commentCol);
		ColumnModel<Articulation> cm = new ColumnModel<Articulation>(columns);
		
		//final GroupingView<Articulation> groupingView = new GroupingView<Articulation>();
		//groupingView.setShowGroupedColumn(false);
		//groupingView.setForceFit(true);
		//groupingView.groupBy(relationCol);

		grid = new Grid<Articulation>(articulationsStore, cm);
		//grid.setView(groupingView);
		grid.setContextMenu(createArticulationsContextMenu());
		grid.setSelectionModel(checkBoxSelectionModel);
		//grid.getView().setAutoExpandColumn(taxonBCol);
		grid.setBorders(false);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);

		StringFilter<Articulation> taxonAFilter = new StringFilter<Articulation>(new ArticulationProperties.TaxonAStringValueProvider());
		StringFilter<Articulation> taxonBFilter = new StringFilter<Articulation>(new ArticulationProperties.TaxonBStringValueProvider());
		StringFilter<Articulation> commentFilter = new StringFilter<Articulation>(commentValueProvider);
		
		ListFilter<Articulation, ArticulationType> relationFilter = new ListFilter<Articulation, ArticulationType>(
				articulationProperties.type(), this.allTypesStore);

		GridFilters<Articulation> filters = new GridFilters<Articulation>();
		filters.addFilter(taxonAFilter);
		filters.addFilter(taxonBFilter);
		filters.addFilter(relationFilter);
		filters.addFilter(commentFilter);
		filters.setLocal(true);
		filters.initPlugin(grid);

		GridInlineEditing<Articulation> editing = new GridInlineEditing<Articulation>(grid);
		
		ComboBox<ArticulationType> relationCombo = createRelationCombo();
		
		if(this.relationEditEnabled)
			editing.addEditor(relationCol, relationCombo);
		editing.addEditor(commentCol, new TextField());
		editing.addStartEditHandler(new StartEditHandler<Articulation>() {
			@Override
			public void onStartEdit(StartEditEvent<Articulation> event) {
				Articulation articulation = grid.getStore().get(event.getEditCell().getRow());
				List<ArticulationType> availableTypes = getAvailableTypes(articulation);
				availableTypesStore.clear();
				availableTypesStore.addAll(availableTypes);
			}
		});
		/*editing.addBeforeStartEditHandler(new BeforeStartEditHandler<Articulation>() {

			@Override
			public void onBeforeStartEdit(
					BeforeStartEditEvent<Articulation> event) {
				event.get
			}
			
		}); */
		editing.addCompleteEditHandler(new CompleteEditHandler<Articulation>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Articulation> event) {			
				GridCell cell = event.getEditCell();
				Articulation articulation = grid.getStore().get(cell.getRow());
				ColumnConfig<Articulation, ?> config = grid.getColumnModel().getColumn(cell.getCol());
				if(config.equals(relationCol)) {
					ArticulationType type = (ArticulationType)config.getValueProvider().getValue(articulation);
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

	protected List<ArticulationType> getAvailableTypes(Articulation fromArticulation) {
		List<ArticulationType> types = new ArrayList<ArticulationType>(allTypesStore.getAll());
		for(Articulation articulation : model.getArticulations()) {
			if(articulation.getTaxonA().equals(fromArticulation.getTaxonA()) && 
					articulation.getTaxonB().equals(fromArticulation.getTaxonB())) {
				types.remove(articulation.getType());
			}
		}
		return types;
	}

	private ComboBox<ArticulationType> createRelationCombo() {
		ComboBox<ArticulationType> relationCombo = new ComboBox<ArticulationType>(availableTypesStore, new LabelProvider<ArticulationType>() {
			@Override
			public String getLabel(ArticulationType item) {
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
		//if(articulationsStore.hasRecord(articulation))
		if(articulationsStore.findModel(articulation) != null)
			articulationsStore.update(articulation);
	}

	protected Menu createArticulationsContextMenu() {
		final Menu menu = new Menu();
		menu.add(new HeaderMenuItem("Annotation"));
		
		if(this.removeEnabled) {
			MenuItem deleteItem = new MenuItem("Remove");
			menu.add(deleteItem);
			deleteItem.addSelectionHandler(new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					eventBus.fireEvent(new RemoveArticulationsEvent(grid.getSelectionModel().getSelectedItems()));
				}
			});
		}
		final MenuItem commentItem = new MenuItem("Comment");
		commentItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				final List<Articulation> articulations = getSelectedArticulations();
				final MultiLinePromptMessageBox box = new MultiLinePromptMessageBox("Comment", "");

				if(articulations.size() == 1)
					box.getTextArea().setValue(model.hasComment(articulations.get(0)) ? model.getComment(articulations.get(0)) : "");
				else 
					box.getTextArea().setValue("");
				
				box.addHideHandler(new HideHandler() {

					@Override
					public void onHide(HideEvent event) {
						for(Articulation articulation : articulations) { 
							eventBus.fireEvent(new SetCommentEvent(articulation, box.getValue()));
							updateStore(articulation);
						}
						String comment = Format.ellipse(box.getValue(), 80);
						String message = Format.substitute("'{0}' saved", new Params(comment));
						Info.display("Comment", message);
					}
				});
				box.show();
			}
		});
		menu.add(commentItem);
		
		final MenuItem colorizeItem = new MenuItem("Colorize");
		menu.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				if(!model.getColors().isEmpty()) {
					menu.insert(colorizeItem, menu.getWidgetIndex(commentItem));
					//colors can change, refresh
					colorizeItem.setSubMenu(createColorizeMenu());
				} else {
					menu.remove(colorizeItem);	
				}
			}
		});
		
		return menu;
	}
	
	protected Menu createColorizeMenu() {
		Menu colorMenu = new Menu();
		MenuItem offItem = new MenuItem("None");
		offItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				final List<Articulation> articulations = getSelectedArticulations();
				for(Articulation articulation : articulations) {
					eventBus.fireEvent(new SetColorEvent(articulation, null));
					updateStore(articulation);
				}
			}
		});
		colorMenu.add(offItem);
		for(final Color color : model.getColors()) {
			MenuItem colorItem = new MenuItem(color.getUse());
			colorItem.getElement().getStyle().setProperty("backgroundColor", "#" + color.getHex());
			colorItem.addSelectionHandler(new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					final List<Articulation> articulations = getSelectedArticulations();
					for(Articulation articulation : articulations) {
						eventBus.fireEvent(new SetColorEvent(articulation, color));
						updateStore(articulation);
					}
				}
			});
			colorMenu.add(colorItem);
		}
		return colorMenu;
	}

	protected List<Articulation> getSelectedArticulations() {
		return grid.getSelectionModel().getSelectedItems();
	}
	
}
