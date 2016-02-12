package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import edu.arizona.biosemantics.euler.alignment.client.common.Alerter;
import edu.arizona.biosemantics.euler.alignment.client.event.TaxonSelectionAEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.TaxonSelectionBEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.TaxonProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;

public class AddArticulationsCheckboxesView extends SimpleContainer {

	private EventBus eventBus;
	private Collection collection;
	
	private TaxonProperties taxonProperties = GWT.create(TaxonProperties.class);
	private ArticulationProperties articulationProperties = GWT.create(ArticulationProperties.class);
	private ComboBox<Taxon> taxonomyACombo;
	private ComboBox<Taxon> taxonomyBCombo;
	private ListStore<Taxon> taxonomyAStore;
	private ListStore<Taxon> taxonomyBStore;
	private Map<Relation, CheckBox> articulationCheckBoxes = new HashMap<Relation, CheckBox>();
	
	public AddArticulationsCheckboxesView(EventBus eventBus) {
		this.eventBus = eventBus;
		//setHeadingText("Create Articulation");
		
		add(createArticulationButtons());
		bindEvents();
	}

	private void bindEvents() {
		eventBus.addHandler(LoadCollectionEvent.TYPE, new LoadCollectionEvent.LoadCollectionEventHandler() {
			@Override
			public void onLoad(LoadCollectionEvent event) {
				collection = event.getCollection();
				taxonomyAStore.clear();
				taxonomyBStore.clear();
				taxonomyAStore.addAll(collection.getModel().getTaxonomies().get(0).getTaxaDFS());
				taxonomyBStore.addAll(collection.getModel().getTaxonomies().get(1).getTaxaDFS());
				clearSelections();
			}
		});
		eventBus.addHandler(TaxonSelectionAEvent.TYPE, new TaxonSelectionAEvent.TaxonSelectionEventHandler() {
			@Override
			public void onSelect(TaxonSelectionAEvent event) {
				if(!event.getSource().equals(AddArticulationsCheckboxesView.this)) {
					taxonomyACombo.setValue(event.getTaxon());
					updateArticulationCheckBoxes();
				}
			}
		});
		eventBus.addHandler(TaxonSelectionBEvent.TYPE, new TaxonSelectionBEvent.TaxonSelectionEventHandler() {
			@Override
			public void onSelect(TaxonSelectionBEvent event) {
				if(!event.getSource().equals(AddArticulationsCheckboxesView.this)) {
					taxonomyBCombo.setValue(event.getTaxon());
					updateArticulationCheckBoxes();
				}
			}
		});
		taxonomyACombo.addSelectionHandler(new SelectionHandler<Taxon>() {
			@Override
			public void onSelection(SelectionEvent<Taxon> event) {
				Taxon selected = event.getSelectedItem();
				eventBus.fireEvent(new TaxonSelectionAEvent(selected, AddArticulationsCheckboxesView.this));
				updateArticulationCheckBoxes();
			}
		});
		taxonomyBCombo.addSelectionHandler(new SelectionHandler<Taxon>() {
			@Override
			public void onSelection(SelectionEvent<Taxon> event) {
				Taxon selected = event.getSelectedItem();
				eventBus.fireEvent(new TaxonSelectionBEvent(selected, AddArticulationsCheckboxesView.this));
				updateArticulationCheckBoxes();
			}
		});
	}

	protected void clearSelections() {
		taxonomyACombo.clear();
		taxonomyBCombo.clear();
		clearArticulationCheckBoxes();
	}

	private void clearArticulationCheckBoxes() {
		for(Relation type : articulationCheckBoxes.keySet())
			articulationCheckBoxes.get(type).setValue(false);
	}

	protected void updateArticulationCheckBoxes() {
		clearArticulationCheckBoxes();
		Taxon taxonA = taxonomyACombo.getValue();
		Taxon taxonB = taxonomyBCombo.getValue();
		
		if(taxonA != null && taxonB != null) {
			List<Articulation> articulations = collection.getModel().getArticulations(taxonA, taxonB, Type.USER);
			for(Articulation articulation : articulations) {
				articulationCheckBoxes.get(articulation.getRelation()).setValue(true);
			}
		}
	}

	private ComboBox<Taxon> createTaxonomyACombo() {
		taxonomyAStore = new ListStore<Taxon>(taxonProperties.key());
		ComboBox<Taxon> result = new ComboBox<Taxon>(taxonomyAStore, taxonProperties.nameLabel(), new AbstractSafeHtmlRenderer<Taxon>() {
			@Override
			public SafeHtml render(Taxon object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
			    sb.appendHtmlConstant("<div qtip=\"" + object.getBiologicalName() + "\">" + taxonProperties.nameLabel().getLabel(object) + "</div>");
			    return sb.toSafeHtml();
			}
		});
		result.setForceSelection(false);
		result.setTriggerAction(TriggerAction.ALL);
		result.setTypeAhead(false);
		result.setEditable(false);
		
		return result;
	}
	
	private ComboBox<Taxon> createTaxonomyBCombo() {
		taxonomyBStore = new ListStore<Taxon>(taxonProperties.key());
		ComboBox<Taxon> result = new ComboBox<Taxon>(taxonomyBStore, taxonProperties.nameLabel(), new AbstractSafeHtmlRenderer<Taxon>() {
			@Override
			public SafeHtml render(Taxon object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<div qtip=\"" + object.getBiologicalName() + "\">" + taxonProperties.nameLabel().getLabel(object) + "</div>");
			    return sb.toSafeHtml();
			}
		});
		result.setForceSelection(false);
		result.setTriggerAction(TriggerAction.ALL);
		result.setTypeAhead(false);
		result.setEditable(false);
		return result;
	}
	
	private Widget createArticulationButtons() {	
		taxonomyACombo = createTaxonomyACombo();
		taxonomyBCombo = createTaxonomyBCombo();

		HorizontalLayoutContainer checkBoxContainer = new HorizontalLayoutContainer();
		checkBoxContainer.add(new Label(), new HorizontalLayoutContainer.HorizontalLayoutData(0.5, -1));
		checkBoxContainer.add(taxonomyACombo, new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(5, 80, 5, 0)));
		
		Relation[] relations = Relation.values();
		for(int i=0; i<relations.length; i++) {
			final Relation relation = relations[i];
			CheckBox articulationCheckBox = new CheckBox();
			ToolButton evidenceButton = new ToolButton(ToolButton.SEARCH);
			evidenceButton.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					EvidenceBasedCreateDialog dialog = new EvidenceBasedCreateDialog(eventBus, collection, taxonomyACombo.getValue(), taxonomyBCombo.getValue());
					dialog.show();
					/*
					Articulation articulation = new Articulation(taxonomyACombo.getValue(), taxonomyBCombo.getValue(), relation, 1.0, Type.USER);
					EvidenceDialog evidenceDialog = new EvidenceDialog(eventBus, collection, articulation);
					evidenceDialog.show();*/
				}
			});
			articulationCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Taxon taxonA = taxonomyACombo.getValue();
					Taxon taxonB = taxonomyBCombo.getValue();
					
					if(taxonA != null && taxonB != null) {
						if(event.getValue()) {
							eventBus.fireEvent(new AddArticulationsEvent(new Articulation(taxonA, taxonB, relation, 1.0, Type.USER)));
						} else {
							eventBus.fireEvent(new RemoveArticulationsEvent(new Articulation(taxonA, taxonB, relation, 1.0, Type.USER)));
						}
					}
				}
			});
			
			articulationCheckBoxes.put(relation, articulationCheckBox);
			articulationCheckBox.setBoxLabel(SafeHtmlUtils.fromString(relation.displayName()).asString());
			//if(i<relations.length - 1) 
			//	evidenceButton.getElement().getStyle().setPaddingRight(50, Unit.PX);
			//System.out.println(articulationCheckBox.getElement().toString());
			//articulationCheckBox.getElement().getStyle().setPaddingRight(0, Unit.PX);
			checkBoxContainer.add(articulationCheckBox, new HorizontalLayoutData(-1, -1, new Margins(5, 0, 5, 0)));
			checkBoxContainer.add(evidenceButton, new HorizontalLayoutData(-1, -1, new Margins(5, 80, 5, 0)));
		}
				
		checkBoxContainer.add(taxonomyBCombo, new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(5, 0, 5, 0)));
		/*TextButton createButton = new TextButton("Create Evidence-Based");
		createButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				EvidenceBasedCreateDialog dialog = new EvidenceBasedCreateDialog(eventBus, collection, taxonomyACombo.getValue(), taxonomyBCombo.getValue());
				dialog.show();
			}
		});
		checkBoxContainer.add(createButton);*/
		checkBoxContainer.add(new Label(), new HorizontalLayoutContainer.HorizontalLayoutData(0.5, -1));
		return checkBoxContainer;
	}
}
	