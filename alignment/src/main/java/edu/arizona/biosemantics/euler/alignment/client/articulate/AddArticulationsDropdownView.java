//package edu.arizona.biosemantics.euler.alignment.client.articulate;
//
//import java.util.Arrays;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.event.logical.shared.SelectionEvent;
//import com.google.gwt.event.logical.shared.SelectionHandler;
//import com.google.gwt.event.shared.EventBus;
//import com.google.gwt.user.client.ui.Widget;
//import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
//import com.sencha.gxt.core.client.IdentityValueProvider;
//import com.sencha.gxt.core.client.Style.SelectionMode;
//import com.sencha.gxt.data.shared.LabelProvider;
//import com.sencha.gxt.data.shared.ListStore;
//import com.sencha.gxt.data.shared.ModelKeyProvider;
//import com.sencha.gxt.widget.core.client.ContentPanel;
//import com.sencha.gxt.widget.core.client.ListView;
//import com.sencha.gxt.widget.core.client.button.TextButton;
//import com.sencha.gxt.widget.core.client.event.SelectEvent;
//import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
//import com.sencha.gxt.widget.core.client.form.ComboBox;
//import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
//import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
//
//import edu.arizona.biosemantics.euler.alignment.client.common.Alerter;
//import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
//import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
//import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
//import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationEntry;
//import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationProperties;
//import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationType;
//import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
//import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
//import edu.arizona.biosemantics.euler.alignment.shared.model.TaxonProperties;
//
//public class AddArticulationsDropdownView extends ContentPanel {
//
//	private EventBus eventBus;
//	private Model model;
//	
//	private TaxonProperties taxonProperties = GWT.create(TaxonProperties.class);
//	private ArticulationProperties articulationProperties = GWT.create(ArticulationProperties.class);
//	//private ComboBox<Type> relationCombo;
//	private ComboBox<Taxon> taxonomyACombo;
//	private ComboBox<Taxon> taxonomyBCombo;
//	private ListStore<Taxon> taxonomyAStore;
//	private ListStore<Taxon> taxonomyBStore;
//	private TextButton addArticulationButton = new TextButton("Add");
//	private ListStore<ArticulationType> relationStore;
//	private ArticulateView articulateView;
//	private ListView<ArticulationType, ArticulationType> relationList;
//	
//	public AddArticulationsDropdownView(EventBus eventBus, ArticulateView articulateView) {
//		this.eventBus = eventBus;
//		this.articulateView = articulateView;
//		setHeadingText("Create Articulation");
//		
//		add(createArticulationButtons());
//		bindEvents();
//	}
//
//
//
//	private void bindEvents() {
//		eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
//			@Override
//			public void onLoad(LoadModelEvent event) {
//				model = event.getModel();
//				taxonomyAStore.clear();
//				taxonomyBStore.clear();
//				taxonomyAStore.addAll(model.getTaxonomies().get(0).getTaxaDFS());
//				taxonomyBStore.addAll(model.getTaxonomies().get(1).getTaxaDFS());
//			}
//		});
//		addArticulationButton.addSelectHandler(new SelectHandler() {
//			@Override
//			public void onSelect(SelectEvent event) {
//				if(taxonomyACombo.getValue() != null && 
//						taxonomyBCombo.getValue() != null && 
//						//relationCombo.getValue() != null) {
//						!relationList.getSelectionModel().getSelectedItems().isEmpty()) {
//					
//					Taxon taxonA = taxonomyACombo.getValue();
//					Taxon taxonB = taxonomyBCombo.getValue();
//					ArticulationEntry articulationEntry = new ArticulationEntry(taxonA, taxonB);
//					if(!model.containsArticulationEntry(articulationEntry)) {
//						for(ArticulationType type : relationList.getSelectionModel().getSelectedItems()) {
//							Articulation articulation = new Articulation(taxonA, taxonB, type);
//							eventBus.fireEvent(new AddArticulationsEvent(articulation));
//						} 
//						taxonomyACombo.clear();
//						taxonomyBCombo.clear();
//						relationList.getSelectionModel().deselectAll();
//						//relationCombo.clear();
//					} else {
//						taxonomyACombo.clear();
//						taxonomyBCombo.clear();
//						relationList.getSelectionModel().deselectAll();
//						//relationCombo.clear();
//						Alerter.articulationAlreadyExists();
//					}
//				} else {
//					Alerter.missingItemToCreateArticulation();
//				}
//			}
//		});
//		taxonomyACombo.addSelectionHandler(new SelectionHandler<Taxon>() {
//			@Override
//			public void onSelection(SelectionEvent<Taxon> event) {
//				Taxon selected = event.getSelectedItem();
//				articulateView.setTaxonA(selected);
//			}
//		});
//		taxonomyBCombo.addSelectionHandler(new SelectionHandler<Taxon>() {
//			@Override
//			public void onSelection(SelectionEvent<Taxon> event) {
//				Taxon selected = event.getSelectedItem();
//				articulateView.setTaxonB(selected);
//			}
//		});
//	}
//
//	private ListView<ArticulationType, ArticulationType> createRelationWidget() {
//		relationStore = new ListStore<ArticulationType>(new ModelKeyProvider<ArticulationType>() {
//			@Override
//			public String getKey(ArticulationType item) {
//				return item.toString();
//			}
//		});
//		relationStore.addAll(Arrays.asList(ArticulationType.values()));
//		ComboBox<ArticulationType> relationCombo = new ComboBox<ArticulationType>(relationStore, new LabelProvider<ArticulationType>() {
//			@Override
//			public String getLabel(ArticulationType item) {
//				return item.toString();
//			}
//		});
//		
//		relationList = new ListView<ArticulationType, ArticulationType>(relationStore, new IdentityValueProvider<ArticulationType>());
//		relationList.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
//	    
//		/*relationCombo.setForceSelection(false);
//		relationCombo.setTriggerAction(TriggerAction.ALL);
//		relationCombo.setTypeAhead(false);
//		relationCombo.setEditable(false);
//		return relationCombo;*/
//		return relationList;
//	}
//
//	private ComboBox<Taxon> createTaxonomyACombo() {
//		taxonomyAStore = new ListStore<Taxon>(taxonProperties.key());
//		ComboBox<Taxon> result = new ComboBox<Taxon>(taxonomyAStore, taxonProperties.nameLabel());
//		result.setForceSelection(false);
//		result.setTriggerAction(TriggerAction.ALL);
//		result.setTypeAhead(false);
//		result.setEditable(false);
//		return result;
//	}
//	
//	private ComboBox<Taxon> createTaxonomyBCombo() {
//		taxonomyBStore = new ListStore<Taxon>(taxonProperties.key());
//		ComboBox<Taxon> result = new ComboBox<Taxon>(taxonomyBStore, taxonProperties.nameLabel());
//		result.setForceSelection(false);
//		result.setTriggerAction(TriggerAction.ALL);
//		result.setTypeAhead(false);
//		result.setEditable(false);
//		return result;
//	}
//	
//	private Widget createArticulationButtons() {	
//		taxonomyACombo = createTaxonomyACombo();
//		taxonomyBCombo = createTaxonomyBCombo();
//		//relationCombo = createRelationCombo();
//		relationList = createRelationWidget();
//		
//		ToolBar toolBar = new ToolBar();
//		// toolBar.setSpacing(2);
//		toolBar.setBorders(true);
//		toolBar.add(new FillToolItem());
//		toolBar.add(taxonomyACombo);
//		toolBar.add(relationList);
//		toolBar.add(taxonomyBCombo);
//		toolBar.add(new FillToolItem());
//		toolBar.add(addArticulationButton);
//		return toolBar;
//	}
//
//	public void setTaxonA(Taxon taxon) {
//		taxonomyACombo.setValue(taxon);
//	}
//
//	public void setTaxonB(Taxon taxon) {
//		taxonomyBCombo.setValue(taxon);
//	}
//
//}
