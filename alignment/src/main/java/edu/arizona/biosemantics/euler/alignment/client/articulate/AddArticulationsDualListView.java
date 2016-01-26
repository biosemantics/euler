package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;

import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.RelationProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.TaxonProperties;

public class AddArticulationsDualListView extends ContentPanel {

	private EventBus eventBus;
	private Model model;
	
	private TaxonProperties taxonProperties = GWT.create(TaxonProperties.class);
	private ArticulationProperties articulationProperties = GWT.create(ArticulationProperties.class);
	private RelationProperties articulationTypeProperties = GWT.create(RelationProperties.class);
	private ComboBox<Taxon> taxonomyACombo;
	private ComboBox<Taxon> taxonomyBCombo;
	private ListStore<Taxon> taxonomyAStore;
	private ListStore<Taxon> taxonomyBStore;
	
	private ListStore<Relation> fromRelationsStore;
	private ListStore<Relation> pickedRelationsStore;
	private MyDualListField  relationList;
	
	private ArticulateView articulateView;
	
	public AddArticulationsDualListView(EventBus eventBus, ArticulateView articulateView) {
		this.eventBus = eventBus;
		this.articulateView = articulateView;
		setHeadingText("Create Articulation");
		
		add(createArticulationButtons());
		bindEvents();
	}

	private void bindEvents() {
		eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
			@Override
			public void onLoad(LoadModelEvent event) {
				model = event.getModel();
				taxonomyAStore.clear();
				taxonomyBStore.clear();
				taxonomyAStore.addAll(model.getTaxonomies().get(0).getTaxaDFS());
				taxonomyBStore.addAll(model.getTaxonomies().get(1).getTaxaDFS());
			}
		});
		
		eventBus.addHandler(RemoveArticulationsEvent.TYPE, new RemoveArticulationsEvent.RemoveArticulationsEventHandler() {
			@Override
			public void onRemove(RemoveArticulationsEvent event) {
				updateRelations();
			}
		});
		eventBus.addHandler(AddArticulationsEvent.TYPE, new AddArticulationsEvent.AddArticulationEventHandler() {
			@Override
			public void onAdd(AddArticulationsEvent event) {
				updateRelations();
			}
		});
		taxonomyACombo.addSelectionHandler(new SelectionHandler<Taxon>() {
			@Override
			public void onSelection(SelectionEvent<Taxon> event) {
				Taxon selected = event.getSelectedItem();
				articulateView.setTaxonA(selected);
				updateRelations();
			}
		});
		taxonomyBCombo.addSelectionHandler(new SelectionHandler<Taxon>() {
			@Override
			public void onSelection(SelectionEvent<Taxon> event) {
				Taxon selected = event.getSelectedItem();
				articulateView.setTaxonB(selected);
				updateRelations();
			}
		});
	}

	protected void updateRelations() {
		Taxon taxonA = this.taxonomyACombo.getValue();
		Taxon taxonB = this.taxonomyBCombo.getValue();
		this.fromRelationsStore.clear();
		Set<Relation> existing = model.getArticulationTypes(taxonA, taxonB);
		for(Relation articulationType : Relation.values()) {
			if(!existing.contains(articulationType))
				this.fromRelationsStore.add(articulationType);
		}
		this.pickedRelationsStore.clear();
		this.pickedRelationsStore.addAll(existing);
		
	}

	private MyDualListField createRelationWidget() {
		fromRelationsStore = new ListStore<Relation>(new ModelKeyProvider<Relation>() {
			@Override
			public String getKey(Relation item) {
				return item.toString();
			}
		});
		pickedRelationsStore = new ListStore<Relation>(new ModelKeyProvider<Relation>() {
			@Override
			public String getKey(Relation item) {
				return item.toString();
			}
		});
		MyDualListField relationList = new MyDualListField(fromRelationsStore, 
				pickedRelationsStore, articulationTypeProperties.displayName(),
		          new TextCell(), eventBus, model, taxonomyACombo, taxonomyBCombo);
		
		/*DualListField<ArticulationType, String> relationList = new DualListField<ArticulationType, String>(fromRelationsStore, 
				pickedRelationsStore, articulationTypeProperties.displayName(),
		          new TextCell()) {
			
			@Override
			public void onAllRight() {
				List<Articulation> articulations = new LinkedList<Articulation>();
				for(ArticulationType type : getFromStore().getAll()) {
					Articulation articulation = new Articulation(taxonomyACombo.getValue(), taxonomyBCombo.getValue(), type);
					articulations.add(articulation);
				}
				eventBus.fireEvent(new AddArticulationsEvent(articulations));
				
				super.onAllRight();
			}
			@Override
			public void onAllLeft() {
				Collection<Articulation> articulations = model.getArticulations(taxonomyACombo.getValue(), taxonomyBCombo.getValue());
				eventBus.fireEvent(new RemoveArticulationsEvent(articulations));

				super.onAllLeft();
			}
			@Override
			public void onLeft() {
				List<Articulation> articulations = new LinkedList<Articulation>();

				List<ArticulationType> sel = getToView().getSelectionModel()
						.getSelectedItems();
				for (ArticulationType m : sel) {
					getToStore().remove(m);
					articulations.add(model.getArticulation(taxonomyACombo.getValue(), taxonomyBCombo.getValue(), m));
				}
				getFromStore().addAll(sel);
				getFromView().getSelectionModel().select(sel, false);

				eventBus.fireEvent(new RemoveArticulationsEvent(articulations));
			}
			@Override
			public void onRight() {
				List<Articulation> articulations = new LinkedList<Articulation>();
				
				List<ArticulationType> sel = this.getFromView().getSelectionModel().getSelectedItems();
				for (ArticulationType m : sel) {
					this.getFromStore().remove(m);
					articulations.add(new Articulation(taxonomyACombo.getValue(), taxonomyBCombo.getValue(), m));
					
				}
				getToStore().addAll(sel);
				getToView().getSelectionModel().select(sel, false);
				
				eventBus.fireEvent(new AddArticulationsEvent(articulations));
			}
		};*/ // doesn't support the dnd, and overwriting the enablednd won't have access to some fields.. hence MyDualListField
		relationList.setWidth(300);
		relationList.setEnableDnd(true);
		return relationList;
	}

	private ComboBox<Taxon> createTaxonomyACombo() {
		taxonomyAStore = new ListStore<Taxon>(taxonProperties.key());
		ComboBox<Taxon> result = new ComboBox<Taxon>(taxonomyAStore, taxonProperties.nameLabel());
		result.setForceSelection(false);
		result.setTriggerAction(TriggerAction.ALL);
		result.setTypeAhead(false);
		result.setEditable(false);
		return result;
	}
	
	private ComboBox<Taxon> createTaxonomyBCombo() {
		taxonomyBStore = new ListStore<Taxon>(taxonProperties.key());
		ComboBox<Taxon> result = new ComboBox<Taxon>(taxonomyBStore, taxonProperties.nameLabel());
		result.setForceSelection(false);
		result.setTriggerAction(TriggerAction.ALL);
		result.setTypeAhead(false);
		result.setEditable(false);
		return result;
	}
	
	private Widget createArticulationButtons() {	
		taxonomyACombo = createTaxonomyACombo();
		taxonomyBCombo = createTaxonomyBCombo();
		relationList = createRelationWidget();
		
		ToolBar toolBar = new ToolBar();
		// toolBar.setSpacing(2);
		toolBar.setBorders(true);
		toolBar.add(new FillToolItem());
		toolBar.add(taxonomyACombo);
		toolBar.add(relationList);
		toolBar.add(taxonomyBCombo);
		toolBar.add(new FillToolItem());
		return toolBar;
	}

	public void setTaxonA(Taxon taxon) {
		taxonomyACombo.setValue(taxon);
		updateRelations();
	}

	public void setTaxonB(Taxon taxon) {
		taxonomyBCombo.setValue(taxon);
		updateRelations();
	}

	public void addSelectionHandlerA(SelectionHandler<Taxon> handler) {
		taxonomyACombo.addSelectionHandler(handler);
	}

	public void addSelectionHandlerB(SelectionHandler<Taxon> handler) {
		taxonomyBCombo.addSelectionHandler(handler);
	}

}
