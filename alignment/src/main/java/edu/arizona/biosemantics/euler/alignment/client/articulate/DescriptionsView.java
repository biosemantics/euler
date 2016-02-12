package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextArea;

import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class DescriptionsView extends SimpleContainer {

	private EventBus eventBus;
	private Collection collection;
	private HTML taxonADescriptionArea;
	private HTML taxonBDescriptionArea;
	private ContentPanel taxonAContentPanel;
	private ContentPanel taxonBContentPanel;

	public DescriptionsView(EventBus eventBus, Collection collection, Taxon taxonA, Taxon taxonB) {
		this.eventBus = eventBus;
		this.collection = collection;
		
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		taxonADescriptionArea = new HTML();
		taxonBDescriptionArea = new HTML();
		taxonADescriptionArea.getElement().setAttribute("testmy", "A");
		taxonBDescriptionArea.getElement().setAttribute("testmy", "B");
		
		taxonAContentPanel = new ContentPanel();
		FlowLayoutContainer taxonAContainer = new FlowLayoutContainer();
		taxonAContentPanel.setWidget(taxonAContainer);
		taxonAContainer.setScrollMode(ScrollMode.AUTOY);
		taxonAContainer.add(taxonADescriptionArea);
		taxonBContentPanel = new ContentPanel();
		FlowLayoutContainer taxonBContainer = new FlowLayoutContainer();
		taxonBContentPanel.setWidget(taxonBContainer);
		taxonBContainer.add(taxonBDescriptionArea);
		taxonBContainer.setScrollMode(ScrollMode.AUTOY);
		
		hlc.add(taxonAContentPanel, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
		hlc.add(taxonBContentPanel, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
		this.setWidget(hlc);
		this.setTaxonA(taxonA);
		this.setTaxonB(taxonB);	
		
		bindEvents();
	}

	private void bindEvents() {
		eventBus.addHandler(LoadCollectionEvent.TYPE, new LoadCollectionEvent.LoadCollectionEventHandler() {
			@Override
			public void onLoad(LoadCollectionEvent event) {
				DescriptionsView.this.collection = collection;
			}
		});
	}
	
	public void setTaxonADescription(SafeHtml description) {
		taxonADescriptionArea.setHTML(description);
	}
	
	public void setTaxonBDescription(SafeHtml description) {
		taxonBDescriptionArea.setHTML(description);
	}

	public void setTaxonA(Taxon taxonA) {
		taxonAContentPanel.setHeadingText("Description of " + taxonA.getBiologicalName());
		this.setTaxonADescription(SafeHtmlUtils.fromTrustedString(taxonA.getDescription()));
	}
	
	public void setTaxonB(Taxon taxonB) {
		taxonBContentPanel.setHeadingText("Description of " + taxonB.getBiologicalName());
		this.setTaxonBDescription(SafeHtmlUtils.fromTrustedString(taxonB.getDescription()));
	}

}
