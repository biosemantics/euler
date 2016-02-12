package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.taskdefs.Javadoc.Html;
import org.sgx.raphael4gwt.raphael.Circle;
import org.sgx.raphael4gwt.raphael.Paper;
import org.sgx.raphael4gwt.raphael.Raphael;
import org.sgx.raphael4gwt.raphael.Text;
import org.sgx.raphael4gwt.raphael.event.DDListener;
import org.sgx.raphael4gwt.raphael.event.MouseEventListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ToStringValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.HtmlEditor;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
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
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.client.articulate.TaxonCharactersView.Node;
import edu.arizona.biosemantics.euler.alignment.client.articulate.TaxonCharactersView.StateNode;
import edu.arizona.biosemantics.euler.alignment.client.common.Alerter;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.QuickTipProvider;
import edu.arizona.biosemantics.euler.alignment.client.common.cell.ColorableCell.CommentColorizableObjectsProvider;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticScopeEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticScopeEvent2;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent2;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.shared.Highlight;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.EvidenceProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterOverlap;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Overlap;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.OverlapProperties;

public class EvidenceBasedCreateDialog extends Dialog {
		
	private IEulerAlignmentServiceAsync alignmentService = GWT.create(IEulerAlignmentService.class);
	private EventBus eventBus; 
	private Collection collection;
	private Taxon taxonA;
	private Taxon taxonB;
	
	private BorderLayoutContainer blc;
	private DescriptionsView descriptionsView;
	private ContentPanel taxonAContentPanel;
	private ContentPanel taxonBContentPanel;
	private ContentPanel overlapContentPanel;
	private TaxonCirclesView taxonCirclesView;
	private TaxonCharactersView taxonCharactersViewA;
	private TaxonCharactersView taxonCharactersViewB;
	private OverlapGridView overlapGridView;
	private ContentPanel descriptionsContentPanel;
	private ContentPanel circlesContentPanel;
	private Map<Relation, CheckBox> relationCheckBoxes = new HashMap<Relation, CheckBox>();

	public EvidenceBasedCreateDialog(final EventBus eventBus, Collection collection, final Taxon taxonA, final Taxon taxonB) {
		this.eventBus = eventBus;
		this.collection = collection;
		this.taxonA = taxonA;
		this.taxonB = taxonB;
		
		blc = new BorderLayoutContainer();
		BorderLayoutData northData = new BorderLayoutData(230);
		northData.setCollapsible(true);
		northData.setFloatable(true);
		northData.setSplit(true);
		circlesContentPanel = new ContentPanel();
		taxonCirclesView = new TaxonCirclesView(eventBus, collection, taxonA, taxonB);
		circlesContentPanel.setWidget(taxonCirclesView);
		blc.setNorthWidget(circlesContentPanel, northData);
		
		BorderLayoutData westData = new BorderLayoutData(250);
		westData.setCollapsible(true);
		westData.setFloatable(true);
		westData.setSplit(true);
		taxonAContentPanel = new ContentPanel();
		taxonAContentPanel.setHeadingText(taxonA.getBiologicalName());
		taxonCharactersViewA = new TaxonCharactersView();
		taxonCharactersViewA.addSelectionHandler(new SelectionHandler<Node>() {
			@Override
			public void onSelection(SelectionEvent<Node> event) {
				if(event.getSelectedItem() instanceof StateNode) {
					StateNode stateNode = (StateNode)event.getSelectedItem();
					Set<Highlight> highlight = new HashSet<Highlight>();
					highlight.add(new Highlight(stateNode.characterState.toString(), "D84840"));
					alignmentService.getHighlighted(taxonA.getDescription(), highlight, new AsyncCallback<SafeHtml>() {
						@Override
						public void onFailure(Throwable caught) {
							Alerter.failedToHighlight();
						}
						@Override
						public void onSuccess(SafeHtml result) {
							descriptionsView.setTaxonADescription(result);
						}
					});
				}
			}
		});
		taxonAContentPanel.setWidget(taxonCharactersViewA);
		blc.setWestWidget(taxonAContentPanel, westData);
		
		BorderLayoutData eastData = new BorderLayoutData(250);
		eastData.setCollapsible(true);
		eastData.setFloatable(true);
		eastData.setSplit(true);
		taxonBContentPanel = new ContentPanel();
		taxonBContentPanel.setHeadingText(taxonB.getBiologicalName());
		taxonCharactersViewB = new TaxonCharactersView();
		taxonCharactersViewB.addSelectionHandler(new SelectionHandler<Node>() {
			@Override
			public void onSelection(SelectionEvent<Node> event) {
				if(event.getSelectedItem() instanceof StateNode) {
					StateNode stateNode = (StateNode)event.getSelectedItem();
					Set<Highlight> highlight = new HashSet<Highlight>();
					highlight.add(new Highlight(stateNode.characterState.toString(), "2763A1"));
					alignmentService.getHighlighted(taxonB.getDescription(), highlight, new AsyncCallback<SafeHtml>() {
						@Override
						public void onFailure(Throwable caught) {
							Alerter.failedToHighlight();
						}
						@Override
						public void onSuccess(SafeHtml result) {
							descriptionsView.setTaxonBDescription(result);
						}
					});
				}
			}
		});
		taxonBContentPanel.setWidget(taxonCharactersViewB);
		blc.setEastWidget(taxonBContentPanel, eastData);
		
		BorderLayoutData southData = new BorderLayoutData(60);
		southData.setCollapsed(true);
		southData.setCollapsible(true);
		southData.setFloatable(true);
		southData.setSplit(true);
		descriptionsContentPanel = new ContentPanel();
		descriptionsView = new DescriptionsView(eventBus, collection, taxonA, taxonB);
		descriptionsContentPanel.setWidget(descriptionsView);
		blc.setSouthWidget(descriptionsContentPanel, southData);
		this.setWidget(blc);
		
		overlapContentPanel = new ContentPanel();
		overlapGridView = new OverlapGridView(eventBus, collection, taxonA, taxonB);
		overlapGridView.addSelectionHandler(new SelectionHandler<Overlap>() {
			@Override
			public void onSelection(SelectionEvent<Overlap> event) {
				Set<Highlight> highlight = new HashSet<Highlight>();
				highlight.add(new Highlight(event.getSelectedItem().getTaxonACharacter().toString(), "D84840"));
				highlight.add(new Highlight(event.getSelectedItem().getTaxonBCharacter().toString(), "2763A1"));
				alignmentService.getHighlighted(taxonA.getDescription(), highlight, new AsyncCallback<SafeHtml>() {
					@Override
					public void onFailure(Throwable caught) {
						Alerter.failedToHighlight();
					}
					@Override
					public void onSuccess(SafeHtml result) {
						descriptionsView.setTaxonADescription(result);
					}
				});
				alignmentService.getHighlighted(taxonB.getDescription(), highlight, new AsyncCallback<SafeHtml>() {
					@Override
					public void onFailure(Throwable caught) {
						Alerter.failedToHighlight();
					}
					@Override
					public void onSuccess(SafeHtml result) {
						descriptionsView.setTaxonBDescription(result);
					}
				});
			}
		});
		overlapGridView.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				update();
			}
		});
		overlapContentPanel.setHeadingText("Character overlap");
		overlapContentPanel.setWidget(overlapGridView);
		blc.setCenterWidget(overlapContentPanel);		

		Relation[] relations = Relation.values();
		for(int i=0; i<relations.length; i++) {
			final Relation relation = relations[i];
			CheckBox articulationCheckBox = new CheckBox();
			relationCheckBoxes.put(relation, articulationCheckBox);
			
			articulationCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if(event.getValue()) {
						eventBus.fireEvent(new AddArticulationsEvent(new Articulation(taxonA, taxonB, relation, 1.0, Type.USER)));
					} else {
						eventBus.fireEvent(new RemoveArticulationsEvent(new Articulation(taxonA, taxonB, relation, 1.0, Type.USER)));
					}
				}
			});
			
			articulationCheckBox.setBoxLabel(SafeHtmlUtils.fromString(relation.displayName()).asString());
			this.getButtonBar().insert(articulationCheckBox, i);
		}
		
		this.setHideOnButtonClick(true);
		this.setHeight(800);
		this.setWidth(1000);
		this.setMaximizable(true);

		this.update();	
	}	
	
	protected void update() {
		List<Articulation> articulations = collection.getModel().getArticulations(taxonA, taxonB, Type.USER);
		for(Articulation articulation : articulations) {
			if(relationCheckBoxes.containsKey(articulation.getRelation())) {
				relationCheckBoxes.get(articulation.getRelation()).setValue(true);
			}
		}
		
		final MessageBox box = Alerter.startLoading();
		alignmentService.getCharacterOverlap(collection, taxonA, taxonB, overlapGridView.getThreshold(), new AsyncCallback<CharacterOverlap>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				Alerter.stopLoading(box);
			}
			@Override
			public void onSuccess(CharacterOverlap characterOverlap) {
				taxonAContentPanel.setHeadingText(taxonA.getBiologicalName() + " unique characters: " + characterOverlap.getCharacterStatesA().size());
				taxonBContentPanel.setHeadingText(taxonB.getBiologicalName() + " unique characters: " + characterOverlap.getCharacterStatesB().size());
				overlapContentPanel.setHeadingText("Character overlap: " + characterOverlap.getOverlap().size());
				taxonCirclesView.update(characterOverlap);
				taxonCharactersViewA.update(characterOverlap.getCharacterStatesA());
				taxonCharactersViewB.update(characterOverlap.getCharacterStatesB());
				overlapGridView.update(characterOverlap.getOverlap());
				
				Alerter.stopLoading(box);
			}
		});		
	}

	public Taxon getTaxonA() {
		return taxonA;
	}

	public Taxon getTaxonB() {
		return taxonB;
	}
	
}
