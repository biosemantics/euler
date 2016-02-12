package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.shared.EventBus;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.client.common.Alerter;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.QuickTipProvider;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell.CommentColorizableObjectsProvider;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticScopeEvent2;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent2;
import edu.arizona.biosemantics.euler.alignment.shared.Highlight;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Overlap;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.OverlapProperties;

public class OverlapGridView extends SimpleContainer {

	private IEulerAlignmentServiceAsync alignmentService = GWT.create(IEulerAlignmentService.class);
	private Grid<Overlap> overlapGrid;
	private TextField thresholdField;
	//private Slider thresholdSlider;
	private EventBus eventBus;
	private Collection collection;
	private Taxon taxonA;
	private Taxon taxonB;

	public OverlapGridView(final EventBus eventBus, Collection collection, final Taxon taxonA, final Taxon taxonB) {
		this.eventBus = eventBus;
		this.collection = collection;
		this.taxonA = taxonA;
		this.taxonB = taxonB;
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		
		OverlapProperties overlapProperties = GWT.create(OverlapProperties.class);
		ListStore<Overlap> store = new ListStore<Overlap>(overlapProperties.key());
		store.setAutoCommit(true);
		
		ListStore<DiagnosticValue> diagnosticValueStore = new ListStore<DiagnosticValue>(new ModelKeyProvider<DiagnosticValue>() {
			@Override
			public String getKey(DiagnosticValue item) {
				return item.toString();
			}
		});
		for(DiagnosticValue diagnosticValue : DiagnosticValue.values()) 
			diagnosticValueStore.add(diagnosticValue);
		ListStore<Rank> rankStore = new ListStore<Rank>(new ModelKeyProvider<Rank>() {
			@Override
			public String getKey(Rank item) {
				return item.toString();
			}
		});
		rankStore.addAll(Arrays.asList(Rank.values()));
		
		CommentColorizableObjectsProvider commentColorizableObjectsProvider = new CommentColorizableObjectsProvider() {
			@Override
			public Object provide(Object source) {
				return source;
			}
		};
		ColorableCell colorableCell = new ColorableCell(eventBus, collection, new QuickTipProvider<Object>() {
			@Override
			public String getQuickTip(Object value) {
				return value.toString();
			}
		});
		colorableCell.setCommentColorizableObjectsStore(store, commentColorizableObjectsProvider);
		
		ColumnConfig<Overlap, String> taxonACol = new ColumnConfig<Overlap, String>(
				new OverlapProperties.TaxonACharacterString(), 180, taxonA.getBiologicalName());
		taxonACol.setCell(colorableCell);
		taxonACol.setToolTip(SafeHtmlUtils.fromTrustedString(taxonA.getBiologicalName()));
		ColumnConfig<Overlap, String> taxonBCol = new ColumnConfig<Overlap, String>(
				new OverlapProperties.TaxonBCharacterString(), 180, taxonB.getBiologicalName());
		taxonBCol.setCell(colorableCell);
		taxonBCol.setToolTip(SafeHtmlUtils.fromTrustedString(taxonB.getBiologicalName()));
		
		ValueProvider<Overlap, Double> similarityProvider = new ValueProvider<Overlap, Double>() {
			@Override
			public Double getValue(Overlap object) {
				return object.getSimilarity();
			}
			@Override
			public void setValue(Overlap object, Double value) { }
			@Override
			public String getPath() {
				return "similarity";
			}
		};
		ColumnConfig<Overlap, Double> similarityCol = new ColumnConfig<Overlap, Double>(
				similarityProvider, 100, "Similarity");
		similarityCol.setCell(colorableCell);
		similarityCol.setToolTip(SafeHtmlUtils.fromTrustedString("Similarity/Uniqueness"));
		final ColumnConfig<Overlap, DiagnosticValue> diagnosticValueCol = new ColumnConfig<Overlap, DiagnosticValue>(
				overlapProperties.diagnosticValue(), 70, "Diagnostic Value");
		diagnosticValueCol.setCell(colorableCell);
		diagnosticValueCol.setToolTip(SafeHtmlUtils.fromTrustedString("Diagnostic Value"));
		final ColumnConfig<Overlap, Rank> diagnosticScopeCol = new ColumnConfig<Overlap, Rank>(
				overlapProperties.diagnosticScope(), 70, "Diagnostic Scope");
		diagnosticScopeCol.setCell(colorableCell);
		diagnosticScopeCol.setToolTip(SafeHtmlUtils.fromTrustedString("Diagnostic Scope"));
		
		List<ColumnConfig<Overlap, ?>> columns = new ArrayList<ColumnConfig<Overlap, ?>>();
		columns.add(taxonACol);
		columns.add(taxonBCol);
		columns.add(similarityCol);
		//columns.add(diagnosticValueCol);
		//columns.add(diagnosticScopeCol);
		ColumnModel<Overlap> cm = new ColumnModel<Overlap>(columns);
		
		overlapGrid = new Grid<Overlap>(store, cm);
		overlapGrid.setBorders(false);
		overlapGrid.getView().setStripeRows(true);
		overlapGrid.getView().setColumnLines(true);
		QuickTip t = new QuickTip(overlapGrid);
		
		StringFilter<Overlap> taxonACharacterFilter = new StringFilter<Overlap>(new OverlapProperties.TaxonACharacterString());
		StringFilter<Overlap> taxonBCharacterFilter = new StringFilter<Overlap>(new OverlapProperties.TaxonBCharacterString());
		NumericFilter<Overlap, Double> similarityUniquenessFilter = new NumericFilter<Overlap, Double>(similarityProvider, 
				new NumberPropertyEditor.DoublePropertyEditor());
		
		ListFilter<Overlap, DiagnosticValue> diagnosticValueFilter = new ListFilter<Overlap, DiagnosticValue>(
				overlapProperties.diagnosticValue(), diagnosticValueStore);
		ListFilter<Overlap, Rank> diagnosticScopeFilter = new ListFilter<Overlap, Rank>(
				overlapProperties.diagnosticScope(), rankStore);
		GridFilters<Overlap> filters = new GridFilters<Overlap>();
		filters.addFilter(taxonACharacterFilter);
		filters.addFilter(taxonBCharacterFilter);
		filters.addFilter(similarityUniquenessFilter);
		filters.addFilter(diagnosticValueFilter);
		filters.addFilter(diagnosticScopeFilter);
		filters.setLocal(true);
		filters.initPlugin(overlapGrid);

		GridInlineEditing<Overlap> editing = new GridInlineEditing<Overlap>(overlapGrid);
		
		editing.addEditor(diagnosticScopeCol, createDiagnosticScopeCombo(rankStore));
		editing.addEditor(diagnosticValueCol, createDiagnosticValueCombo(diagnosticValueStore));
		editing.addCompleteEditHandler(new CompleteEditHandler<Overlap>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Overlap> event) {			
				GridCell cell = event.getEditCell();
				Overlap overlap = overlapGrid.getStore().get(cell.getRow());
				ColumnConfig<Overlap, ?> config = overlapGrid.getColumnModel().getColumn(cell.getCol());
				if(config.equals(diagnosticValueCol)) {
					DiagnosticValue diagnosticValue = (DiagnosticValue)config.getValueProvider().getValue(overlap);
					eventBus.fireEvent(new ModifyDiagnosticValueEvent2(overlap, diagnosticValue));
				}
				if(config.equals(diagnosticScopeCol)) {
					Rank rank = (Rank)config.getValueProvider().getValue(overlap);
					eventBus.fireEvent(new ModifyDiagnosticScopeEvent2(overlap, rank));
				}
			}
		});
		
		SimpleContainer simpleContainer = new SimpleContainer();
		//FlowLayoutContainer flowLayoutContainer = new FlowLayoutContainer();
		//flowLayoutContainer.setScrollMode(ScrollMode.AUTOY);
		simpleContainer.add(overlapGrid);
		vlc.add(simpleContainer, new VerticalLayoutData(1, 1));//0.9999));
		//vlc.add(createResizeSlider(), new VerticalLayoutData(1, 40, new Margins(5)));
		this.setWidget(vlc);
	}
	
	/*private Widget createResizeSlider() {
	    thresholdField = new TextField();
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		thresholdSlider = new Slider();
		thresholdSlider.setMessage("");
	    thresholdSlider.setMinValue(0);
	    thresholdSlider.setMaxValue(100);
	    thresholdSlider.setValue(50);
	    thresholdSlider.setIncrement(2);
	    thresholdSlider.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateThresholdField();
			}
	    });
	    hlc.add(thresholdSlider, new HorizontalLayoutData(0.5, -1, new Margins(5)));
	    hlc.add(thresholdField, new HorizontalLayoutData(0.5, -1, new Margins(5)));
	    updateThresholdField();
		return hlc;
	}*/
	
	/*protected void updateThresholdField() {
		thresholdField.setValue(String.valueOf(getThreshold()));
	}*/

	public void addSelectionHandler(SelectionHandler<Overlap> handler) {
		this.overlapGrid.getSelectionModel().addSelectionHandler(handler);
	}
	
	/*public void addValueChangeHandler(ValueChangeHandler<Integer> valueChangeHandler) {
		thresholdSlider.addValueChangeHandler(valueChangeHandler);
	};*/
		
	private ComboBox<Rank> createDiagnosticScopeCombo(ListStore<Rank> rankStore) {
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

	private ComboBox<DiagnosticValue> createDiagnosticValueCombo(ListStore<DiagnosticValue> diagnosticValueStore) {
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

	/*public double getThreshold() {
		return (double)thresholdSlider.getValue()/100;
	}*/

	public void update(List<Overlap> overlap) {
		overlapGrid.getStore().clear();
		overlapGrid.getStore().addAll(overlap);
	}	
}
