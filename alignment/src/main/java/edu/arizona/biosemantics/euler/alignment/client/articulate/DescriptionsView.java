package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextArea;

import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class DescriptionsView extends SimpleContainer {

	private EventBus eventBus;
	private Model model;
	private HTML taxonADescriptionArea;
	private HTML taxonBDescriptionArea;

	public DescriptionsView(EventBus eventBus, Model model) {
		this.eventBus = eventBus;
		this.model = model;
		
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		taxonADescriptionArea = new HTML();
		taxonBDescriptionArea = new HTML();
		taxonADescriptionArea.getElement().setAttribute("testmy", "A");
		taxonBDescriptionArea.getElement().setAttribute("testmy", "B");
		
		FlowLayoutContainer taxonAContainer = new FlowLayoutContainer();
		taxonAContainer.setScrollMode(ScrollMode.AUTOY);
		taxonAContainer.add(taxonADescriptionArea);
		FlowLayoutContainer taxonBContainer = new FlowLayoutContainer();
		taxonBContainer.add(taxonBDescriptionArea);
		taxonBContainer.setScrollMode(ScrollMode.AUTOY);
		
		hlc.add(taxonAContainer, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
		hlc.add(taxonBContainer, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
		this.setWidget(hlc);
		
		bindEvents();
	}

	private void bindEvents() {
		eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
			@Override
			public void onLoad(LoadModelEvent event) {
				DescriptionsView.this.model = model;
			}
		});
	}
	
	public void setTaxonADescription(SafeHtml description) {
		taxonADescriptionArea.setHTML(description);
	}
	
	public void setTaxonBDescription(SafeHtml description) {
		taxonBDescriptionArea.setHTML(description);
	}

}
