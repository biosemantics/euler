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
import com.sencha.gxt.widget.core.client.ContentPanel.ContentPanelAppearance;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
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
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

import edu.arizona.biosemantics.common.taxonomy.Rank;
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
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.CharacterNode;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.Node;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.OrganNode;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.StateNode;

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
	private TaxonCharactersView uniqueTaxonCharactersViewA;
	private TaxonCharactersView uniqueTaxonCharactersViewB;
	private OverlapGridView overlapGridView;
	private ContentPanel descriptionsContentPanel;
	private ContentPanel circlesContentPanel;
	private TaxonCharactersView allTaxonCharactersViewA;
	private TaxonCharactersView allTaxonCharactersViewB;
	private TabPanel tabPanelA;
	private TabPanel tabPanelB;

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
		taxonCirclesView.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				update();
			}
		});
		circlesContentPanel.setWidget(taxonCirclesView);
		blc.setNorthWidget(circlesContentPanel, northData);
		
		BorderLayoutData westData = new BorderLayoutData(250);
		westData.setCollapsible(true);
		westData.setFloatable(true);
		westData.setSplit(true);
		taxonAContentPanel = new ContentPanel((ContentPanelAppearance) GWT.create(RedContentPanelAppearance.class));
		taxonAContentPanel.getHeader().getElement().getStyle().setBackgroundColor("red");
		taxonAContentPanel.setHeadingText(taxonA.getBiologicalName());
		tabPanelA = new TabPanel();
		uniqueTaxonCharactersViewA = new TaxonCharactersView(eventBus, collection);
		uniqueTaxonCharactersViewA.addSelectionHandler(new SelectionHandler<Node>() {
			@Override
			public void onSelection(SelectionEvent<Node> event) {
				selectCharacter(event.getSelectedItem(), uniqueTaxonCharactersViewA);
				highlightCharacterSelectionA(event.getSelectedItem());
			}
		});
		allTaxonCharactersViewA = new TaxonCharactersView(eventBus, collection);
		allTaxonCharactersViewA.addSelectionHandler(new SelectionHandler<Node>() {
			@Override
			public void onSelection(SelectionEvent<Node> event) {
				selectCharacter(event.getSelectedItem(), allTaxonCharactersViewA);
				highlightCharacterSelectionA(event.getSelectedItem());
			}
		});
		allTaxonCharactersViewA.update(taxonA.getCharacterStates());
		tabPanelA.add(uniqueTaxonCharactersViewA, "Unique characters (" + taxonA.getCharacterStates().size() + ")");
		tabPanelA.add(allTaxonCharactersViewA, "All characters (" + taxonA.getCharacterStates().size() + ")");
		taxonAContentPanel.setWidget(tabPanelA);
		blc.setWestWidget(taxonAContentPanel, westData);
		
		BorderLayoutData eastData = new BorderLayoutData(250);
		eastData.setCollapsible(true);
		eastData.setFloatable(true);
		eastData.setSplit(true);
		taxonBContentPanel = new ContentPanel((ContentPanelAppearance) GWT.create(GreenContentPanelAppearance.class));
		taxonBContentPanel.setHeadingText(taxonB.getBiologicalName());
		tabPanelB = new TabPanel();
		uniqueTaxonCharactersViewB = new TaxonCharactersView(eventBus, collection);
		uniqueTaxonCharactersViewB.addSelectionHandler(new SelectionHandler<Node>() {
			@Override
			public void onSelection(SelectionEvent<Node> event) {
				selectCharacter(event.getSelectedItem(), uniqueTaxonCharactersViewB);
				highlightCharacterSelectionB(event.getSelectedItem());
			}
		});
		allTaxonCharactersViewB = new TaxonCharactersView(eventBus, collection);
		allTaxonCharactersViewB.addSelectionHandler(new SelectionHandler<Node>() {
			@Override
			public void onSelection(SelectionEvent<Node> event) {
				selectCharacter(event.getSelectedItem(), allTaxonCharactersViewB);
				highlightCharacterSelectionB(event.getSelectedItem());
			}
		});
		allTaxonCharactersViewB.update(taxonB.getCharacterStates());
		tabPanelB.add(uniqueTaxonCharactersViewB, "Unique characters (" + taxonB.getCharacterStates().size() + ")");
		tabPanelB.add(allTaxonCharactersViewB, "All characters (" + taxonB.getCharacterStates().size() + ")");
		taxonBContentPanel.setWidget(tabPanelB);
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
		
		overlapContentPanel = new ContentPanel((ContentPanelAppearance) GWT.create(MixedContentPanelAppearance.class));
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
		/*overlapGridView.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				update();
			}
		});*/
		overlapContentPanel.setHeadingText("Character overlap");
		overlapContentPanel.setWidget(overlapGridView);
		blc.setCenterWidget(overlapContentPanel);		
		
		this.setHideOnButtonClick(true);
		this.setHeight(800);
		this.setWidth(1000);
		this.setMaximizable(true);
		TextButton button = this.getButton(PredefinedButton.OK);
		button.setText("Close");
		this.setHeadingText("Character Comparison");
		this.setModal(true);
	}	
	
	protected void selectCharacter(Node node, TaxonCharactersView source) {
		OrganNode organNode = null;
		CharacterNode characterNode = null;
		StateNode stateNode = null;
		if(node instanceof OrganNode)
			organNode = (OrganNode)node;
		if(node instanceof CharacterNode)
			characterNode = (CharacterNode)node;
		if(node instanceof StateNode) 
			stateNode = (StateNode)node;
		if(stateNode != null) {
			characterNode = (CharacterNode)source.getStore().getParent(stateNode);
			organNode = (OrganNode)source.getStore().getParent(characterNode);
		} else if(characterNode != null) {
			organNode = (OrganNode)source.getStore().getParent(characterNode);
		} 
		
		if(!source.equals(allTaxonCharactersViewA)) 
			this.allTaxonCharactersViewA.select(organNode, characterNode, stateNode);
		if(!source.equals(allTaxonCharactersViewB))
			this.allTaxonCharactersViewB.select(organNode, characterNode, stateNode);
		if(!source.equals(uniqueTaxonCharactersViewA))
			this.uniqueTaxonCharactersViewA.select(organNode, characterNode, stateNode);
		if(!source.equals(uniqueTaxonCharactersViewB))
			this.uniqueTaxonCharactersViewB.select(organNode, characterNode, stateNode);
	}

	protected void highlightCharacterSelectionA(Node selectedItem) {
		if(selectedItem instanceof StateNode) {
			StateNode stateNode = (StateNode)selectedItem;
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
	
	protected void highlightCharacterSelectionB(Node selectedItem) {
		if(selectedItem instanceof StateNode) {
			StateNode stateNode = (StateNode)selectedItem;
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

	protected void update() {
		final MessageBox box = Alerter.startLoading();
		alignmentService.getCharacterOverlap(collection, taxonA, taxonB, taxonCirclesView.getThreshold()/*overlapGridView.getThreshold()*/, new AsyncCallback<CharacterOverlap>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				Alerter.stopLoading(box);
			}
			@Override
			public void onSuccess(CharacterOverlap characterOverlap) {
				overlapContentPanel.setHeadingText("Character overlap (" + characterOverlap.getOverlap().size() + ")");
				taxonCirclesView.update(characterOverlap);
				uniqueTaxonCharactersViewA.update(characterOverlap.getCharacterStatesA());
				tabPanelA.getConfig(uniqueTaxonCharactersViewA).setText("Unique characters (" + characterOverlap.getCharacterStatesA().size() + ")");
				tabPanelA.update(uniqueTaxonCharactersViewA, tabPanelA.getConfig(uniqueTaxonCharactersViewA));
				uniqueTaxonCharactersViewB.update(characterOverlap.getCharacterStatesB());
				tabPanelB.getConfig(uniqueTaxonCharactersViewB).setText("Unique characters (" + characterOverlap.getCharacterStatesB().size() + ")");
				tabPanelB.update(uniqueTaxonCharactersViewB, tabPanelB.getConfig(uniqueTaxonCharactersViewB));
				
				uniqueTaxonCharactersViewA.expandAll();
				uniqueTaxonCharactersViewB.expandAll();
				allTaxonCharactersViewA.expandAll();
				allTaxonCharactersViewB.expandAll();
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
