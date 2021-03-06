package edu.arizona.biosemantics.euler.alignment.client.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
import com.sencha.gxt.data.shared.Converter;
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
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.HeaderMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import edu.arizona.biosemantics.euler.alignment.client.articulate.EvidenceDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell.CommentColorizableObjectsProvider;
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

public class ArticulationsGridView extends SimpleContainer /* extends ContentPanel*/ {

	protected EventBus eventBus;
	protected Collection collection;
	
	protected ListStore<Articulation> articulationsStore;
	protected Grid<Articulation> grid;
	protected ListStore<Relation> allRelationsStore;
	protected ListStore<Type> allTypesStore;
	protected ListStore<Relation> availableRelationsStore;
	private boolean relationEditEnabled;
	private boolean removeEnabled;
	private ColumnConfig<Articulation, String> taxonACol;
	private ColumnConfig<Articulation, String> taxonBCol;
	
	public ArticulationsGridView(EventBus eventBus, Collection collection, boolean relationEditEnabled, boolean removeEnabled) {
		this.eventBus = eventBus;
		this.collection = collection;
		
		this.relationEditEnabled = relationEditEnabled;
		this.removeEnabled = removeEnabled;

		allTypesStore = new ListStore<Type>(new ModelKeyProvider<Type>() {
			@Override
			public String getKey(Type item) {
				return item.toString();
			}
		});
		allTypesStore.addAll(Arrays.asList(Type.values()));
		availableRelationsStore = new ListStore<Relation>(new ModelKeyProvider<Relation>() {
			@Override
			public String getKey(Relation item) {
				return item.toString();
			}
		});
		allRelationsStore = new ListStore<Relation>(new ModelKeyProvider<Relation>() {
			@Override
			public String getKey(Relation item) {
				return item.toString();
			}
		});
		allRelationsStore.addAll(Arrays.asList(Relation.values()));
		
		//setHeadingText("Articulations");
		add(createArticulationsGrid());
	}

	private void updateTaxonColumnHeaders() {
		if(collection != null) {
			grid.getView().getHeader().getHead(1).setHeader(SafeHtmlUtils.fromSafeConstant("Taxonomic Concept " + collection.getModel().getTaxonomies().get(0).getSecString()));
			grid.getView().getHeader().getHead(3).setHeader(SafeHtmlUtils.fromSafeConstant("Taxonomic Concept " + collection.getModel().getTaxonomies().get(1).getSecString()));
			taxonACol.setHeader("Taxonomic Concept " + collection.getModel().getTaxonomies().get(0).getSecString());
			taxonBCol.setHeader("Taxonomic Concept " + collection.getModel().getTaxonomies().get(1).getSecString());
		}
	}

	protected void bindEvents() {
		eventBus.addHandler(LoadCollectionEvent.TYPE, new LoadCollectionEvent.LoadCollectionEventHandler() {
			@Override
			public void onLoad(LoadCollectionEvent event) {
				collection = event.getCollection();
				updateTaxonColumnHeaders();
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
		ArticulationProperties articulationProperties = GWT
				.create(ArticulationProperties.class);
		articulationsStore = new ListStore<Articulation>(articulationProperties.key());
		articulationsStore.setAutoCommit(true);

		IdentityValueProvider<Articulation> identity = new IdentityValueProvider<Articulation>();
		final CheckBoxSelectionModel<Articulation> checkBoxSelectionModel = new CheckBoxSelectionModel<Articulation>(
				identity);

		checkBoxSelectionModel.setSelectionMode(SelectionMode.MULTI);

		ColorableCell colorableCell = new ColorableCell(eventBus, collection, null);
		colorableCell.setCommentColorizableObjectsStore(articulationsStore, new CommentColorizableObjectsProvider() {
			@Override
			public Object provide(Object source) {
				return source;
			}
		});
		final ColumnConfig<Articulation, String> createdCol = new ColumnConfig<Articulation, String>(
				new ArticulationProperties.CreatedStringValueProvder(), 200, "Created");
		createdCol.setCell(colorableCell);

		String aHead = "Taxonomic Concept A";
		String bHead = "Taxonomic Concept B";
		if(collection != null) {
			 aHead = "Taxonomic Concept " + collection.getModel().getTaxonomies().get(0).getSecString();
			 bHead = "Taxonomic Concept " + collection.getModel().getTaxonomies().get(1).getSecString();
		}
		taxonACol = new ColumnConfig<Articulation, String>(
				new ArticulationProperties.TaxonAStringValueProvider(), 250, aHead);
		taxonACol.setCell(colorableCell);
		final ColumnConfig<Articulation, Relation> relationCol = new ColumnConfig<Articulation, Relation>(
				articulationProperties.relation(), 70, "Articulation");
		relationCol.setCell(colorableCell);
		taxonBCol = new ColumnConfig<Articulation, String>(
				new ArticulationProperties.TaxonBStringValueProvider(), 250, bHead);
		taxonBCol.setCell(colorableCell);
		
		SafeStyles btnPaddingStyle = SafeStylesUtils.fromTrustedString("padding: 1px 3px 0;");
		ColumnConfig<Articulation, String> evidenceCol = new ColumnConfig<Articulation, String>(new ValueProvider<Articulation, String>() {
			@Override
			public String getValue(Articulation object) {
				return "View";
			}

			@Override
			public void setValue(Articulation object, String value) {
			}
			@Override
			public String getPath() {
				return "View";
			}
			
		}, 100, "Evidence");
		// IMPORTANT we want the text element (cell parent) to only be as wide
		// as
		// the cell and not fill the cell
		evidenceCol.setColumnTextClassName(CommonStyles.get().inlineBlock());
		evidenceCol.setColumnTextStyle(btnPaddingStyle);

		TextButtonCell button = new TextButtonCell();
		button.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Context c = event.getContext();
				int row = c.getIndex();
				Articulation articulation = articulationsStore.get(row);
				EvidenceDialog evidenceDialog = new EvidenceDialog(eventBus, collection, articulation);
				evidenceDialog.setHideOnButtonClick(true);
				evidenceDialog.show();
			}
		});
		evidenceCol.setCell(button);
		
		ColumnConfig<Articulation, Type> sourceCol = new ColumnConfig<Articulation, Type>(articulationProperties.type(), 100, "Source");
		
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
		
		ColumnConfig<Articulation, Double> confidenceCol = new ColumnConfig<Articulation, Double>(articulationProperties.confidence(), 100, "Confidence");
		
		final ColumnConfig<Articulation, String> commentCol = new ColumnConfig<Articulation, String>(
				commentValueProvider, 400, "Comment");
		commentCol.setCell(colorableCell);

		List<ColumnConfig<Articulation, ?>> columns = new ArrayList<ColumnConfig<Articulation, ?>>();
		columns.add(checkBoxSelectionModel.getColumn());
		columns.add(taxonACol);
		columns.add(relationCol);
		columns.add(taxonBCol);
		//columns.add(evidenceCol);
		//columns.add(sourceCol);
		//columns.add(confidenceCol);
		columns.add(commentCol);
		columns.add(createdCol);
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

		StringFilter<Articulation> createdFilter = new StringFilter<Articulation>(new ArticulationProperties.CreatedStringValueProvder());
		StringFilter<Articulation> taxonAFilter = new StringFilter<Articulation>(new ArticulationProperties.TaxonAStringValueProvider());
		StringFilter<Articulation> taxonBFilter = new StringFilter<Articulation>(new ArticulationProperties.TaxonBStringValueProvider());
		NumericFilter<Articulation, Double> confidenceFilter = new NumericFilter<Articulation, Double>(articulationProperties.confidence(), 
				new NumberPropertyEditor.DoublePropertyEditor());
		StringFilter<Articulation> commentFilter = new StringFilter<Articulation>(commentValueProvider);
		ListFilter<Articulation, Type> sourceFilter = new ListFilter<Articulation, Type>(
				articulationProperties.type(), this.allTypesStore);
		ListFilter<Articulation, Relation> relationFilter = new ListFilter<Articulation, Relation>(
				articulationProperties.relation(), this.allRelationsStore);

		GridFilters<Articulation> filters = new GridFilters<Articulation>();
		filters.addFilter(createdFilter);
		filters.addFilter(taxonAFilter);
		filters.addFilter(taxonBFilter);
		filters.addFilter(sourceFilter);
		filters.addFilter(confidenceFilter);
		filters.addFilter(relationFilter);
		filters.addFilter(commentFilter);
		filters.setLocal(true);
		filters.initPlugin(grid);

		GridInlineEditing<Articulation> editing = new GridInlineEditing<Articulation>(grid);
		
		ComboBox<Relation> relationCombo = createRelationCombo();
		
		if(this.relationEditEnabled) {
			editing.addEditor(relationCol, relationCombo);
			editing.addEditor(confidenceCol, new Converter<Double, String>() {
				@Override
				public Double convertFieldValue(String object) {
					return Double.valueOf(object);
				}
				@Override
				public String convertModelValue(Double object) {
					return object.toString();
				}
			}, new TextField());
		}
		editing.addEditor(commentCol, new TextField());
		editing.addStartEditHandler(new StartEditHandler<Articulation>() {
			@Override
			public void onStartEdit(StartEditEvent<Articulation> event) {
				Articulation articulation = grid.getStore().get(event.getEditCell().getRow());
				java.util.Collection<Relation> availableTypes = collection.getModel().getAvailableRelations(articulation.getTaxonA(), articulation.getTaxonB(), Type.USER);
				availableRelationsStore.clear();
				availableRelationsStore.addAll(availableTypes);
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
		ComboBox<Relation> relationCombo = new ComboBox<Relation>(availableRelationsStore, new LabelProvider<Relation>() {
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
					box.getTextArea().setValue(collection.getModel().hasComment(articulations.get(0)) ? collection.getModel().getComment(articulations.get(0)) : "");
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
				if(!collection.getModel().getColors().isEmpty()) {
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
		for(final Color color : collection.getModel().getColors()) {
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
