package edu.arizona.biosemantics.euler.alignment.client.articulate;

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
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.MultiLinePromptMessageBox;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
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
import com.sencha.gxt.data.shared.ModelKeyProvider;

import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetArticulationColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetArticulationCommentEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetTaxonColorEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Color;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationType;

public class ArticulationsGridView extends ContentPanel {

	private EventBus eventBus;
	private Model model;
	
	private ListStore<Articulation> articulationsStore;
	private Grid<Articulation> grid;
	private ListStore<ArticulationType> typesStore;

	public ArticulationsGridView(EventBus eventBus) {
		this.eventBus = eventBus;

		typesStore = new ListStore<ArticulationType>(new ModelKeyProvider<ArticulationType>() {
			@Override
			public String getKey(ArticulationType item) {
				return item.toString();
			}
		});
		typesStore.addAll(Arrays.asList(ArticulationType.values()));
		
		setHeadingText("Articulations");
		add(createArticulationsGrid());
		
		bindEvents();
	}

	private void bindEvents() {
		eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
			@Override
			public void onLoad(LoadModelEvent event) {
				model = event.getModel();
				articulationsStore.addAll(model.getArticulations());
			}
		});
		eventBus.addHandler(AddArticulationsEvent.TYPE, new AddArticulationsEvent.AddArticulationEventHandler() {
			@Override
			public void onAdd(AddArticulationsEvent event) {
				articulationsStore.addAll(event.getArticulations());
			}
		});
		eventBus.addHandler(RemoveArticulationsEvent.TYPE, new RemoveArticulationsEvent.RemoveArticulationsEventHandler() {
			@Override
			public void onRemove(RemoveArticulationsEvent event) {
				for(Articulation articulation : event.getArticulations())
					articulationsStore.remove(articulation);
			}
		});
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

		ColorableCell colorableCell = new ColorableCell(eventBus);
		colorableCell.setArticulationsStore(articulationsStore);
		final ColumnConfig<Articulation, String> taxonACol = new ColumnConfig<Articulation, String>(
				new ArticulationProperties.TaxonAStringValueProvider(), 400, "Taxon A");
		taxonACol.setCell(colorableCell);
		final ColumnConfig<Articulation, ArticulationType> relationCol = new ColumnConfig<Articulation, ArticulationType>(
				articulationProperties.type(), 190, "Relation");
		relationCol.setCell(colorableCell);
		final ColumnConfig<Articulation, String> taxonBCol = new ColumnConfig<Articulation, String>(
				new ArticulationProperties.TaxonBStringValueProvider(), 400, "Taxon B");
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
				articulationProperties.type(), this.typesStore);

		GridFilters<Articulation> filters = new GridFilters<Articulation>();
		filters.addFilter(taxonAFilter);
		filters.addFilter(taxonBFilter);
		filters.addFilter(relationFilter);
		filters.addFilter(commentFilter);
		filters.setLocal(true);
		filters.initPlugin(grid);

		GridInlineEditing<Articulation> editing = new GridInlineEditing<Articulation>(grid);
		
		ComboBox<ArticulationType> relationCombo = createRelationCombo();
		
		editing.addEditor(relationCol, relationCombo);
		editing.addEditor(commentCol, new TextField());
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
			}
		});
		return grid;
	}

	private ComboBox<ArticulationType> createRelationCombo() {
		ComboBox<ArticulationType> relationCombo = new ComboBox<ArticulationType>(typesStore, new LabelProvider<ArticulationType>() {
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

	private Menu createArticulationsContextMenu() {
		final Menu menu = new Menu();
		menu.add(new HeaderMenuItem("Annotation"));
		MenuItem deleteItem = new MenuItem("Remove");
		menu.add(deleteItem);
		deleteItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new RemoveArticulationsEvent(grid.getSelectionModel().getSelectedItems()));
			}
		});
		MenuItem commentItem = new MenuItem("Comment");
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
							eventBus.fireEvent(new SetArticulationCommentEvent(articulation, box.getValue()));
							articulationsStore.update(articulation);
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
		menu.add(colorizeItem);
		
		menu.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				if(!model.getColors().isEmpty()) {
					//refresh colors, they may have changed since last show
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
					eventBus.fireEvent(new SetArticulationColorEvent(articulation, null));
					articulationsStore.update(articulation);
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
						eventBus.fireEvent(new SetArticulationColorEvent(articulation, color));
						articulationsStore.update(articulation);
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
