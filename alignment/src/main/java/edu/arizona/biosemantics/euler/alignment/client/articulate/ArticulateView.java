package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import edu.arizona.biosemantics.euler.alignment.client.event.TaxonSelectionAEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.TaxonSelectionBEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class ArticulateView extends SplitLayoutPanel {
	
	private EventBus eventBus;
	private Model model;
	
	private TaxonomyView taxaViewA;
	private TaxonomyView taxaViewB;
	private CurrentArticulationsGridView articulationsGridView;
	private AddArticulationsCheckboxesView addArticulationsView;
	
	public ArticulateView(final EventBus eventBus) {
		this.eventBus = eventBus;
		
		taxaViewA = new TaxonomyView(eventBus);
		taxaViewB = new TaxonomyView(eventBus);
		HorizontalLayoutContainer horizontalLayoutContainer = new HorizontalLayoutContainer();
		horizontalLayoutContainer.add(taxaViewA, new HorizontalLayoutData(0.5, 1.0));
		horizontalLayoutContainer.add(taxaViewB, new HorizontalLayoutData(0.5, 1.0));	
		
		VerticalLayoutContainer verticalLayoutPanel = new VerticalLayoutContainer();
		addArticulationsView = new AddArticulationsCheckboxesView(eventBus);
		verticalLayoutPanel.add(addArticulationsView, new VerticalLayoutData(1.0, -1.0));
		
		articulationsGridView = new CurrentArticulationsGridView(eventBus);
		verticalLayoutPanel.add(articulationsGridView, new VerticalLayoutData(1.0, 1.0));
		addSouth(verticalLayoutPanel, 0);
		add(horizontalLayoutContainer);
		this.setWidgetSize(verticalLayoutPanel, 400.0);
		
		bindEvents();
	}


	private void bindEvents() {
		eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
			@Override
			public void onLoad(LoadModelEvent event) {
				model = event.getModel();
				taxaViewA.loadModel(model.getTaxonomies().get(0));
				taxaViewB.loadModel(model.getTaxonomies().get(1));
			}
		});
		taxaViewA.addSelectionChangeHandler(new SelectionChangedHandler<Taxon>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<Taxon> event) {
				if(!event.getSelection().isEmpty())
					eventBus.fireEvent(new TaxonSelectionAEvent(event.getSelection().get(0), ArticulateView.this));
			}
		});
		taxaViewB.addSelectionChangeHandler(new SelectionChangedHandler<Taxon>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<Taxon> event) {
				if(!event.getSelection().isEmpty())
					eventBus.fireEvent(new TaxonSelectionBEvent(event.getSelection().get(0), ArticulateView.this));
			}
		});
		eventBus.addHandler(TaxonSelectionAEvent.TYPE, new TaxonSelectionAEvent.TaxonSelectionEventHandler() {
			@Override
			public void onSelect(TaxonSelectionAEvent event) {
				if(!event.getSource().equals(ArticulateView.this)) {
					taxaViewA.setSelected(event.getTaxon());
				}
			}
		});
		eventBus.addHandler(TaxonSelectionBEvent.TYPE, new TaxonSelectionBEvent.TaxonSelectionEventHandler() {
			@Override
			public void onSelect(TaxonSelectionBEvent event) {
				if(!event.getSource().equals(ArticulateView.this)) {
					taxaViewB.setSelected(event.getTaxon());
				}
			}
		});
	}

	
	public List<Taxon> getSelectedTaxaA() {
		return taxaViewA.getSelectedTaxa();
	}
	
	public List<Taxon> getSelectedTaxaB() {
		return taxaViewB.getSelectedTaxa();
	}


	public void setTaxonA(Taxon taxon) {
		this.taxaViewA.setSelected(taxon);
	}


	public void setTaxonB(Taxon taxon) {
		this.taxaViewB.setSelected(taxon);
	}
		
}
