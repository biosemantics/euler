package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.client.common.Alerter;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell.CommentColorizableObjectsProvider;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.QuickTipProvider;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticScopeEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.EvidenceProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;

public class CharacterEvidenceGridView extends SimpleContainer {

	private IEulerAlignmentServiceAsync alignmentService = GWT.create(IEulerAlignmentService.class);
	private EventBus eventBus;
	private Collection collection;
	private Articulation articulation;
	private ListStore<Evidence> evidenceStore;
	private Grid<Evidence> grid;
	private ListStore<DiagnosticValue> diagnosticValueStore;
	private ListStore<Rank> rankStore;

	public CharacterEvidenceGridView(EventBus eventBus, Collection collection, Articulation articulation) {
		this.eventBus = eventBus;
		this.collection = collection;
		this.articulation = articulation;
		
		add(createEvidenceGrid());
	}

	private Widget createEvidenceGrid() {
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
		
		
		EvidenceProperties evidenceProperties = GWT.create(EvidenceProperties.class);
		evidenceStore = new ListStore<Evidence>(evidenceProperties.key());
		evidenceStore.setAutoCommit(true);

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
		colorableCell.setCommentColorizableObjectsStore(evidenceStore, commentColorizableObjectsProvider);
		
		ColumnConfig<Evidence, String> taxonACol = new ColumnConfig<Evidence, String>(
				evidenceProperties.taxonACharacter(), 100, articulation.getTaxonA().getBiologicalName());
		taxonACol.setCell(colorableCell);
		taxonACol.setToolTip(SafeHtmlUtils.fromTrustedString(articulation.getTaxonA().getBiologicalName()));
		ColumnConfig<Evidence, String> taxonBCol = new ColumnConfig<Evidence, String>(
				evidenceProperties.taxonBCharacter(), 100, articulation.getTaxonB().getBiologicalName());
		taxonBCol.setCell(colorableCell);
		taxonBCol.setToolTip(SafeHtmlUtils.fromTrustedString(articulation.getTaxonB().getBiologicalName()));
		
		ValueProvider<Evidence, Double> similarityUniquenessProvider = new ValueProvider<Evidence, Double>() {
			@Override
			public Double getValue(Evidence object) {
				if(object.getSimilarity() < 0)
					return object.getUniqueness();
				return object.getSimilarity();
			}
			@Override
			public void setValue(Evidence object, Double value) { }
			@Override
			public String getPath() {
				return "similarityUniqueness";
			}
		};
		ColumnConfig<Evidence, Double> similarityUniquenessCol = new ColumnConfig<Evidence, Double>(
				similarityUniquenessProvider, 50, "Similarity/Uniqueness");
		similarityUniquenessCol.setCell(colorableCell);
		similarityUniquenessCol.setToolTip(SafeHtmlUtils.fromTrustedString("Similarity/Uniqueness"));
		final ColumnConfig<Evidence, DiagnosticValue> diagnosticValueCol = new ColumnConfig<Evidence, DiagnosticValue>(
				evidenceProperties.diagnosticValue(), 70, "Diagnostic Value");
		diagnosticValueCol.setCell(colorableCell);
		diagnosticValueCol.setToolTip(SafeHtmlUtils.fromTrustedString("Diagnostic Value"));
		final ColumnConfig<Evidence, Rank> diagnosticScopeCol = new ColumnConfig<Evidence, Rank>(
				evidenceProperties.diagnosticScope(), 70, "Diagnostic Scope");
		diagnosticScopeCol.setCell(colorableCell);
		diagnosticScopeCol.setToolTip(SafeHtmlUtils.fromTrustedString("Diagnostic Scope"));
		
		List<ColumnConfig<Evidence, ?>> columns = new ArrayList<ColumnConfig<Evidence, ?>>();
		columns.add(taxonACol);
		columns.add(taxonBCol);
		columns.add(similarityUniquenessCol);
		columns.add(diagnosticValueCol);
		columns.add(diagnosticScopeCol);
		ColumnModel<Evidence> cm = new ColumnModel<Evidence>(columns);
		
		grid = new Grid<Evidence>(evidenceStore, cm);
		grid.setBorders(false);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		QuickTip t = new QuickTip(grid);
		
		StringFilter<Evidence> taxonACharacterFilter = new StringFilter<Evidence>(evidenceProperties.taxonACharacter());
		StringFilter<Evidence> taxonBCharacterFilter = new StringFilter<Evidence>(evidenceProperties.taxonBCharacter());
		NumericFilter<Evidence, Double> similarityUniquenessFilter = new NumericFilter<Evidence, Double>(similarityUniquenessProvider, 
				new NumberPropertyEditor.DoublePropertyEditor());
		
		ListFilter<Evidence, DiagnosticValue> diagnosticValueFilter = new ListFilter<Evidence, DiagnosticValue>(
				evidenceProperties.diagnosticValue(), this.diagnosticValueStore);
		ListFilter<Evidence, Rank> diagnosticScopeFilter = new ListFilter<Evidence, Rank>(
				evidenceProperties.diagnosticScope(), this.rankStore);
		GridFilters<Evidence> filters = new GridFilters<Evidence>();
		filters.addFilter(taxonACharacterFilter);
		filters.addFilter(taxonBCharacterFilter);
		filters.addFilter(similarityUniquenessFilter);
		filters.addFilter(diagnosticValueFilter);
		filters.addFilter(diagnosticScopeFilter);
		filters.setLocal(true);
		filters.initPlugin(grid);

		GridInlineEditing<Evidence> editing = new GridInlineEditing<Evidence>(grid);
		
		editing.addEditor(diagnosticScopeCol, createDiagnosticScopeCombo());
		editing.addEditor(diagnosticValueCol, createDiagnosticValueCombo());
		editing.addCompleteEditHandler(new CompleteEditHandler<Evidence>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Evidence> event) {			
				GridCell cell = event.getEditCell();
				Evidence evidence = grid.getStore().get(cell.getRow());
				ColumnConfig<Evidence, ?> config = grid.getColumnModel().getColumn(cell.getCol());
				if(config.equals(diagnosticValueCol)) {
					DiagnosticValue diagnosticValue = (DiagnosticValue)config.getValueProvider().getValue(evidence);
					eventBus.fireEvent(new ModifyDiagnosticValueEvent(evidence, diagnosticValue));
				}
				if(config.equals(diagnosticScopeCol)) {
					Rank rank = (Rank)config.getValueProvider().getValue(evidence);
					eventBus.fireEvent(new ModifyDiagnosticScopeEvent(evidence, rank));
				}
			}
		});
		return grid;
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

	public void setArticulation(Articulation articulation) {
		this.articulation = articulation;	
		final MessageBox box = Alerter.startLoading();
		alignmentService.getEvidence(collection, articulation.getTaxonA(), articulation.getTaxonB(), new AsyncCallback<List<Evidence>>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				Alerter.stopLoading(box);
			}
			@Override
			public void onSuccess(List<Evidence> result) {
				evidenceStore.addAll(result);
				Alerter.stopLoading(box);
			}
		});
	}
	
	public GridSelectionModel<Evidence> getSelectionModel() {
		return grid.getSelectionModel();
	}

	
}
