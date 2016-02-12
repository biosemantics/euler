package edu.arizona.biosemantics.euler.alignment.client.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Params;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.box.MultiLinePromptMessageBox;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
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
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Color;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;
import edu.arizona.biosemantics.euler.alignment.shared.model.RunOutputType;
import edu.arizona.biosemantics.euler.alignment.shared.model.RunProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.RunProperties.DisplayNameValueProvider;

public class RunGridView implements IsWidget {

	private EventBus eventBus;
	private Collection collection;
	private ListStore<Run> runStore;
	private ListStore<RunOutputType> runOutputTypesStore;
	private Grid<Run> runGrid;

	private RunProperties runProperties = GWT.create(RunProperties.class);

	public RunGridView(final EventBus eventBus, final Collection collection) {
		this.eventBus = eventBus;
		this.collection = collection;

		runStore = new ListStore<Run>(runProperties.key());
		runStore.addAll(collection.getModel().getRunHistory());
		runStore.setAutoCommit(true);

		runOutputTypesStore = new ListStore<RunOutputType>(
				new ModelKeyProvider<RunOutputType>() {
					@Override
					public String getKey(RunOutputType item) {
						return item.toString();
					}
				});
		runOutputTypesStore.addAll(Arrays.asList(RunOutputType.values()));

		IdentityValueProvider<Run> identity = new IdentityValueProvider<Run>();
		final CheckBoxSelectionModel<Run> checkBoxSelectionModel = new CheckBoxSelectionModel<Run>(
				identity);
		checkBoxSelectionModel.setSelectionMode(SelectionMode.MULTI);

		ColorableCell colorableCell = new ColorableCell(eventBus, collection, null);
		colorableCell.setCommentColorizableObjectsStore(runStore,
				new CommentColorizableObjectsProvider() {
					@Override
					public Object provide(Object source) {
						return source;
					}
				});
		final ColumnConfig<Run, String> nameCol = new ColumnConfig<Run, String>(
				new RunProperties.DisplayNameValueProvider(), 200, "Run");
		nameCol.setCell(colorableCell);
		final ColumnConfig<Run, String> resultCol = new ColumnConfig<Run, String>(
				new RunProperties.ResultValueProvider(), 70, "Result");
		resultCol.setCell(colorableCell);

		ValueProvider<Run, String> runCommentValueProvider = new ValueProvider<Run, String>() {
			@Override
			public String getValue(Run object) {
				if (collection.getModel().hasComment(object))
					return collection.getModel().getComment(object);
				return "";
			}

			@Override
			public void setValue(Run object, String value) {
				collection.getModel().setComment(object, value);
			}

			@Override
			public String getPath() {
				return "comment";
			}

		};
		final ColumnConfig<Run, String> commentCol = new ColumnConfig<Run, String>(
				runCommentValueProvider, 100, "Comment");
		commentCol.setCell(colorableCell);

		List<ColumnConfig<Run, ?>> columns = new ArrayList<ColumnConfig<Run, ?>>();
		columns.add(checkBoxSelectionModel.getColumn());
		columns.add(nameCol);
		columns.add(resultCol);
		columns.add(commentCol);
		ColumnModel<Run> cm = new ColumnModel<Run>(columns);

		// final GroupingView<Articulation> groupingView = new
		// GroupingView<Articulation>();
		// groupingView.setShowGroupedColumn(false);
		// groupingView.setForceFit(true);
		// groupingView.groupBy(relationCol);

		runGrid = new Grid<Run>(runStore, cm);
		// grid.setView(groupingView);
		runGrid.setContextMenu(createRunsContextMenu());
		runGrid.setSelectionModel(checkBoxSelectionModel);
		// grid.getView().setAutoExpandColumn(taxonBCol);
		runGrid.setBorders(false);
		runGrid.getView().setStripeRows(true);
		runGrid.getView().setColumnLines(true);

		StringFilter<Run> nameFilter = new StringFilter<Run>(
				new RunProperties.DisplayNameValueProvider());
		StringFilter<Run> commentFilter = new StringFilter<Run>(
				runCommentValueProvider);

		ListFilter<Run, RunOutputType> resultFilter = new ListFilter<Run, RunOutputType>(
				new RunProperties.RunOutputTypeValueProvider(),
				this.runOutputTypesStore);

		GridFilters<Run> filters = new GridFilters<Run>();
		filters.addFilter(nameFilter);
		filters.addFilter(resultFilter);
		filters.addFilter(commentFilter);
		filters.setLocal(true);
		filters.initPlugin(runGrid);

		GridInlineEditing<Run> editing = new GridInlineEditing<Run>(runGrid);

		/*
		 * ComboBox<ArticulationType> relationCombo = createRelationCombo();
		 * 
		 * if(this.relationEditEnabled) editing.addEditor(relationCol,
		 * relationCombo);
		 */
		editing.addEditor(commentCol, new TextField());

		editing.addCompleteEditHandler(new CompleteEditHandler<Run>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Run> event) {
				GridCell cell = event.getEditCell();
				Run run = runGrid.getStore().get(cell.getRow());
				ColumnConfig<Run, ?> config = runGrid.getColumnModel()
						.getColumn(cell.getCol());
				if (config.equals(commentCol)) {
					String comment = (String) config.getValueProvider()
							.getValue(run);
					eventBus.fireEvent(new SetCommentEvent(run, comment));
				}
			}
		});

		bindEvents();
	}

	private void bindEvents() {
		eventBus.addHandler(LoadCollectionEvent.TYPE,
				new LoadCollectionEvent.LoadCollectionEventHandler() {
					@Override
					public void onLoad(LoadCollectionEvent event) {
						collection = event.getCollection();
					}
				});
		eventBus.addHandler(SetCommentEvent.TYPE,
				new SetCommentEvent.SetCommentEventHandler() {
					@Override
					public void onSet(SetCommentEvent event) {
						if (event.getObject() instanceof Run) {
							Run run = (Run) event.getObject();
							updateStore(run);
						}
					}
				});
		eventBus.addHandler(SetColorEvent.TYPE,
				new SetColorEvent.SetColorEventHandler() {
					@Override
					public void onSet(SetColorEvent event) {
						if (event.getObject() instanceof Run) {
							Run run = (Run) event.getObject();
							updateStore(run);
						}
					}
				});
	}

	public boolean contains(Run run) {
		return runStore.findModel(run) != null;
	}
	
	private void updateStore(Run run) {
		if(contains(run))
			runStore.update(run);
	}

	private Menu createRunsContextMenu() {
		final Menu menu = new Menu();
		menu.add(new HeaderMenuItem("Annotation"));

		final MenuItem commentItem = new MenuItem("Comment");
		commentItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				final List<Run> runs = runGrid.getSelectionModel().getSelectedItems();
				final MultiLinePromptMessageBox box = new MultiLinePromptMessageBox(
						"Comment", "");

				if (runs.size() == 1)
					box.getTextArea().setValue(
							collection.getModel().hasComment(runs.get(0)) ? collection.getModel()
									.getComment(runs.get(0)) : "");
				else
					box.getTextArea().setValue("");

				box.addHideHandler(new HideHandler() {

					@Override
					public void onHide(HideEvent event) {
						for (Run run : runs) {
							eventBus.fireEvent(new SetCommentEvent(
									run, box.getValue()));
							updateStore(run);
						}
						String comment = Format.ellipse(box.getValue(), 80);
						String message = Format.substitute("'{0}' saved",
								new Params(comment));
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
				if (!collection.getModel().getColors().isEmpty()) {
					menu.insert(colorizeItem, menu.getWidgetIndex(commentItem));
					// colors can change, refresh
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
				final List<Run> runs = runGrid.getSelectionModel().getSelectedItems();
				for (Run run : runs) {
					eventBus.fireEvent(new SetColorEvent(run, null));
					updateStore(run);
				}
			}
		});
		colorMenu.add(offItem);
		for (final Color color : collection.getModel().getColors()) {
			MenuItem colorItem = new MenuItem(color.getUse());
			colorItem.getElement().getStyle()
					.setProperty("backgroundColor", "#" + color.getHex());
			colorItem.addSelectionHandler(new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					final List<Run> runs = runGrid.getSelectionModel().getSelectedItems();
					for (Run run : runs) {
						eventBus.fireEvent(new SetColorEvent(run,
								color));
						updateStore(run);
					}
				}
			});
			colorMenu.add(colorItem);
		}
		return colorMenu;
	}

	@Override
	public Widget asWidget() {
		return runGrid.asWidget();
	}

	public GridSelectionModel<Run> getSelectionModel() {
		return runGrid.getSelectionModel();
	}

}
