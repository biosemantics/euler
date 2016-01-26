package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import edu.arizona.biosemantics.euler.alignment.client.common.Alerter;
import edu.arizona.biosemantics.euler.alignment.client.common.CommonDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.MinimalArticulationsGridView;
import edu.arizona.biosemantics.euler.alignment.shared.Highlight;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;

public class EvidenceDialog extends CommonDialog {

	private IEulerAlignmentServiceAsync alignmentService = GWT.create(IEulerAlignmentService.class);
	private EventBus eventBus;
	private Model model;
	private BorderLayoutContainer borderLayoutContainer;
	private MinimalArticulationsGridView machineArticulationsGridView;
	private MinimalArticulationsGridView userArticulationsGridView;
	private Articulation articulation;
	private CharacterEvidenceGridView characterEvidenceGridView;
	private DescriptionsView descriptionsView;
	private BorderLayoutContainer southBorderLayoutContainer;

	public EvidenceDialog(EventBus eventBus, Model model, Articulation articulation) {
		this.eventBus = eventBus;
		this.model = model;
		this.articulation = articulation;
		borderLayoutContainer = new BorderLayoutContainer();
		
		this.setWidth(800);
		this.setHeight(800);
		this.setHeadingText("Character Evidence for Articulations");
		
		borderLayoutContainer.setCenterWidget(createUserVsMachinePanel());
		
		southBorderLayoutContainer = new BorderLayoutContainer();
		BorderLayoutData southData = new BorderLayoutData(0.66);
		southData.setCollapseMini(true);
		southData.setCollapsible(true);
		southData.setSplit(true);
		ContentPanel south = new ContentPanel();
		south.setWidget(southBorderLayoutContainer);
		south.setHeadingText("Character Comparison");
		borderLayoutContainer.setSouthWidget(south, southData);
		
		BorderLayoutData eastData = new BorderLayoutData(0.5);
		eastData.setCollapseMini(true);
		eastData.setCollapsible(true);
		eastData.setSplit(true);
		eastData.setCollapsed(false);
		ContentPanel east = new ContentPanel();
		east.setWidget(createDescriptionPanel());
		east.setHeadingText("Descriptions");
		southBorderLayoutContainer.setCenterWidget(createCharaterComparisonPanel());
		southBorderLayoutContainer.setEastWidget(east, eastData);
		this.setWidget(borderLayoutContainer);
	}
	
	private Widget createDescriptionPanel() {
		descriptionsView = new DescriptionsView(eventBus, model);
		descriptionsView.setTaxonADescription(SafeHtmlUtils.fromTrustedString(articulation.getTaxonA().getDescription()));
		descriptionsView.setTaxonBDescription(SafeHtmlUtils.fromTrustedString(articulation.getTaxonB().getDescription()));
		return descriptionsView;
	}

	@Override
	public void onResize(int width, int height) {
		super.onResize(width, height);
		//borderLayoutContainer.onResize();
	}

	private IsWidget createCharaterComparisonPanel() {
		characterEvidenceGridView = new CharacterEvidenceGridView(eventBus, model, articulation);
		characterEvidenceGridView.setArticulation(articulation);
		characterEvidenceGridView.getSelectionModel().addSelectionHandler(new SelectionHandler<Evidence>() {
			@Override
			public void onSelection(SelectionEvent<Evidence> event) {
				Set<Highlight> highlight = new HashSet<Highlight>();
				highlight.add(new Highlight(event.getSelectedItem().getTaxonACharacter(), "D84840"));
				highlight.add(new Highlight(event.getSelectedItem().getTaxonBCharacter(), "2763A1"));
				alignmentService.getHighlighted(articulation.getTaxonA().getDescription(), highlight, new AsyncCallback<SafeHtml>() {
					@Override
					public void onFailure(Throwable caught) {
						Alerter.failedToHighlight();
					}
					@Override
					public void onSuccess(SafeHtml result) {
						descriptionsView.setTaxonADescription(result);
					}
				});
				alignmentService.getHighlighted(articulation.getTaxonB().getDescription(), highlight, new AsyncCallback<SafeHtml>() {
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
		return characterEvidenceGridView;
	}

	private Widget createMachineArticulationsGrid() {
		ContentPanel panel = new ContentPanel();
		machineArticulationsGridView = 	new MinimalArticulationsGridView(eventBus, model);
		panel.setHeadingText("Machine");
		panel.setWidget(userArticulationsGridView);
		machineArticulationsGridView.setArticulations(model.getArticulations(articulation.getTaxonA(), articulation.getTaxonB(), Type.MACHINE));
		return panel;
	}

	private Widget createUserArticulationsGrid() {
		ContentPanel panel = new ContentPanel();
		userArticulationsGridView = new MinimalArticulationsGridView(eventBus, model);
		panel.setHeadingText("User");
		panel.setWidget(userArticulationsGridView);
		userArticulationsGridView.setArticulations(model.getArticulations(articulation.getTaxonA(), articulation.getTaxonB(), Type.USER));
		return panel;
	}

	private IsWidget createUserVsMachinePanel() {
		BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(createUserArticulationsGrid(), new VerticalLayoutContainer.VerticalLayoutData(1, 0.5));
		vlc.add(createMachineArticulationsGrid(), new VerticalLayoutContainer.VerticalLayoutData(1, 0.5));
		
		ContentPanel center = new ContentPanel();
		center.setWidget(vlc);
		center.setHeadingHtml(SafeHtmlUtils.fromTrustedString("Articulations: "
				+ "<b>" + articulation.getTaxonA().getBiologicalName() + " <-> " + articulation.getTaxonB().getBiologicalName() + "</b>"));
		borderLayoutContainer.setCenterWidget(center);
		
		BorderLayoutData eastData = new BorderLayoutData(0.5);
		eastData.setCollapseMini(true);
		eastData.setCollapsible(true);
		eastData.setSplit(true);
		ContentPanel east = new ContentPanel();
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		Label aggreementLabel = new Label("Agreement: 33.33%");
		TextButton refreshButton = new TextButton("Refresh");
		hlc.add(aggreementLabel, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
		hlc.add(refreshButton, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, -1));
		east.setWidget(hlc);
		east.setHeadingText("Agreement");
		
		borderLayoutContainer.setEastWidget(east, eastData);
		return borderLayoutContainer;
	}
	
}
