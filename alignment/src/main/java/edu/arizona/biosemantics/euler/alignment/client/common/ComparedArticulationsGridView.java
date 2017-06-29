package edu.arizona.biosemantics.euler.alignment.client.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell.CommentColorizableObjectsProvider;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;

public class ComparedArticulationsGridView extends ContentPanel {

	private static enum ComparisonResult {
		added, removed;
	}
	
	private static class ComparedArticulation {
		private ComparisonResult comparisonResult;
		private Articulation articulation;
		public ComparedArticulation(ComparisonResult comparisonResult, Articulation articulation) {
			this.articulation = articulation;
			this.comparisonResult = comparisonResult;
		}
		public Articulation getArticulation() {
			return articulation;
		}
		public ComparisonResult getComparisonResult() {
			return comparisonResult;
		}
	}
	
	protected EventBus eventBus;
	protected Collection collection;

	protected ListStore<ComparedArticulation> store;
	protected Grid<ComparedArticulation> grid;
	protected ListStore<Relation> typesStore;
	protected ListStore<ComparisonResult> comparisonResultStore;
	
	public ComparedArticulationsGridView(EventBus eventBus, Collection collection) {
		this.eventBus = eventBus;
		this.collection = collection;

		typesStore = new ListStore<Relation>(
				new ModelKeyProvider<Relation>() {
					@Override
					public String getKey(Relation item) {
						return item.toString();
					}
				});
		typesStore.addAll(Arrays.asList(Relation.values()));
		comparisonResultStore = new ListStore<ComparisonResult>(new ModelKeyProvider<ComparisonResult>() {
			@Override
			public String getKey(ComparisonResult item) {
				return item.toString();
			}
		});

		setHeading("Articulation Changes");
		add(createComparedArticulationsGrid());
	}

	protected void bindEvents() {
		eventBus.addHandler(LoadCollectionEvent.TYPE,
				new LoadCollectionEvent.LoadCollectionEventHandler() {
					@Override
					public void onLoad(LoadCollectionEvent event) {
						collection = event.getCollection();
					}
				});
	}
	
	public void clear() {
		store.clear();
	}

	public void update(Run run1, Run run2) {
		store.clear();
		store.addAll(createComparedArticulations(run1, run2));
	}

	private java.util.Collection<? extends ComparedArticulation> createComparedArticulations(
			Run run1, Run run2) {
		List<ComparedArticulation> result = new LinkedList<ComparedArticulation>();
		
		List<Articulation> removedArticulations = new LinkedList<Articulation>();
		List<Articulation> addedArticulations = new LinkedList<Articulation>(run2.getArticulations());
		
		for(Articulation a1 : run1.getArticulations()) {
			boolean foundEqual = false;
			for(Articulation a2 : run2.getArticulations()) {
				if(a1.equals(a2)) {
					foundEqual = true;
					addedArticulations.remove(a2);
				}
			}
			if(!foundEqual)
				removedArticulations.add(a1);
		}
		
		for(Articulation removed : removedArticulations) 
			result.add(new ComparedArticulation(ComparisonResult.removed, removed));
		for(Articulation added : addedArticulations) 
			result.add(new ComparedArticulation(ComparisonResult.added, added));
		return result;
	}

	private Widget createComparedArticulationsGrid() {
		store = new ListStore<ComparedArticulation>(new ModelKeyProvider<ComparedArticulation>() {
			@Override
			public String getKey(ComparedArticulation item) {
				return String.valueOf(item.getArticulation().getId());
			}
		});
		store.setAutoCommit(true);

		ColorableCell colorableCell = new ColorableCell(eventBus, collection, null);
		colorableCell.setCommentColorizableObjectsStore(store, new CommentColorizableObjectsProvider() {
			@Override
			public Object provide(Object source) {
				if(source instanceof ComparedArticulation)
					return ((ComparedArticulation)source).getArticulation();
				return null;
			}
		});
		final ColumnConfig<ComparedArticulation, String> comparisonCol = new ColumnConfig<ComparedArticulation, String>(new ValueProvider<ComparedArticulation, String>() {
			@Override
			public String getValue(ComparedArticulation object) {
				return object.getComparisonResult().toString();
			}
			@Override
			public void setValue(ComparedArticulation object, String value) {	}
			@Override
			public String getPath() {
				return "comparison";
			}
		}, 100,	"Change");
		
		final ColumnConfig<ComparedArticulation, String> taxonACol = new ColumnConfig<ComparedArticulation, String>(new ValueProvider<ComparedArticulation, String>() {
			@Override
			public String getValue(ComparedArticulation object) {
				return object.getArticulation().getTaxonA().getBiologicalName();
			}
			@Override
			public void setValue(ComparedArticulation object, String value) {	}
			@Override
			public String getPath() {
				return "taxonA";
			}
		}, 100,	"Taxonomic Concept A");
		taxonACol.setCell(colorableCell);
		final ColumnConfig<ComparedArticulation, Relation> relationCol = new ColumnConfig<ComparedArticulation, Relation>(
				new ValueProvider<ComparedArticulation, Relation>() {

					@Override
					public Relation getValue(ComparedArticulation object) {
						return object.getArticulation().getRelation();
					}

					@Override
					public void setValue(ComparedArticulation object,
							Relation value) {
					}

					@Override
					public String getPath() {
						return "type";
					}
					
				}, 100, "Articulation");
		relationCol.setCell(colorableCell);
		final ColumnConfig<ComparedArticulation, String> taxonBCol = new ColumnConfig<ComparedArticulation, String>(
				new ValueProvider<ComparedArticulation, String>() {
					@Override
					public String getValue(ComparedArticulation object) {
						return object.getArticulation().getTaxonB().getBiologicalName();
					}
					@Override
					public void setValue(ComparedArticulation object, String value) {	}
					@Override
					public String getPath() {
						return "taxonB";
					}
				}, 100,
				"Taxonomic Concept B");
		taxonBCol.setCell(colorableCell);

		ValueProvider<ComparedArticulation, String> commentValueProvider = new ValueProvider<ComparedArticulation, String>() {
			@Override
			public String getValue(ComparedArticulation object) {
				if (collection.getModel().hasComment(object))
					return collection.getModel().getComment(object);
				return "";
			}

			@Override
			public void setValue(ComparedArticulation object, String value) {
			}

			@Override
			public String getPath() {
				return "comment";
			}
		};

		final ColumnConfig<ComparedArticulation, String> commentCol = new ColumnConfig<ComparedArticulation, String>(
				commentValueProvider, 400, "Comment");
		commentCol.setCell(colorableCell);

		IdentityValueProvider<ComparedArticulation> identity = new IdentityValueProvider<ComparedArticulation>();
		final CheckBoxSelectionModel<ComparedArticulation> checkBoxSelectionModel = new CheckBoxSelectionModel<ComparedArticulation>(
				identity);
		
		List<ColumnConfig<ComparedArticulation, ?>> columns = new ArrayList<ColumnConfig<ComparedArticulation, ?>>();
		//columns.add(checkBoxSelectionModel.getColumn());
		columns.add(comparisonCol);
		columns.add(taxonACol);
		columns.add(relationCol);
		columns.add(taxonBCol);
		columns.add(commentCol);
		ColumnModel<ComparedArticulation> cm = new ColumnModel<ComparedArticulation>(columns);

		// final GroupingView<Articulation> groupingView = new
		// GroupingView<Articulation>();
		// groupingView.setShowGroupedColumn(false);
		// groupingView.setForceFit(true);
		// groupingView.groupBy(relationCol);

		grid = new Grid<ComparedArticulation>(store, cm);
		// grid.setView(groupingView);
		//grid.setContextMenu(createArticulationsContextMenu());
		grid.setSelectionModel(checkBoxSelectionModel);
		// grid.getView().setAutoExpandColumn(taxonBCol);
		grid.setBorders(false);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);

		/*StringFilter<Articulation> taxonAFilter = new StringFilter<Articulation>(
				new ArticulationProperties.TaxonAStringValueProvider());
		StringFilter<Articulation> taxonBFilter = new StringFilter<Articulation>(
				new ArticulationProperties.TaxonBStringValueProvider());
		StringFilter<Articulation> commentFilter = new StringFilter<Articulation>(
				commentValueProvider);
		*/

		//ListFilter<Articulation, ArticulationType> relationFilter = new ListFilter<Articulation, ArticulationType>(
		//		articulationProperties.type(), this.typesStore);

		/*GridFilters<Articulation> filters = new GridFilters<Articulation>();
		filters.addFilter(taxonAFilter);
		filters.addFilter(taxonBFilter);
		filters.addFilter(relationFilter);
		filters.addFilter(commentFilter);
		filters.setLocal(true);
		filters.initPlugin(grid);
		*/

		/*GridInlineEditing<Articulation> editing = new GridInlineEditing<Articulation>(
		/		grid);

		ComboBox<ArticulationType> relationCombo = createRelationCombo();

		editing.addEditor(relationCol, relationCombo);
		editing.addEditor(commentCol, new TextField());
		editing.addCompleteEditHandler(new CompleteEditHandler<Articulation>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Articulation> event) {
				GridCell cell = event.getEditCell();
				Articulation articulation = grid.getStore().get(cell.getRow());
				ColumnConfig<Articulation, ?> config = grid.getColumnModel()
						.getColumn(cell.getCol());
				if (config.equals(relationCol)) {
					ArticulationType type = (ArticulationType) config
							.getValueProvider().getValue(articulation);
					eventBus.fireEvent(new ModifyArticulationEvent(
							articulation, type));
				}
				if (config.equals(commentCol)) {
					String comment = (String) config.getValueProvider()
							.getValue(articulation);
					eventBus.fireEvent(new SetCommentEvent(articulation,
							comment));
				}
			}
		});*/
		return grid;
	}
}
