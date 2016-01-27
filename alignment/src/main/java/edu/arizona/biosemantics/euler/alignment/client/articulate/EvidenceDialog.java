package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import edu.arizona.biosemantics.euler.alignment.client.common.Alerter;
import edu.arizona.biosemantics.euler.alignment.client.common.CommonDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.MinimalArticulationsGridView;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.shared.Highlight;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
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
	private Label aggreementLabel;
	private ContentPanel agreementPanel;
	private ListStore<Relation> availableRelationsStore;
	private ComboBox<Relation> relationCombo;
	

	public EvidenceDialog(EventBus eventBus, Model model, Articulation articulation) {
		this.eventBus = eventBus;
		this.model = model;
		this.articulation = articulation;
		
		this.setWidth(800);
		this.setHeight(800);
		this.setHeadingText("Character Evidence for Articulations");
		this.borderLayoutContainer = new BorderLayoutContainer();
		this.setWidget(borderLayoutContainer);
		borderLayoutContainer.setCenterWidget(createUserVsMachinePanel());
		
		southBorderLayoutContainer = createSouthBorderLayoutContainer();
		BorderLayoutData southData = new BorderLayoutData(0.66);
		southData.setCollapseMini(true);
		southData.setCollapsible(true);
		southData.setSplit(true);
		ContentPanel south = new ContentPanel();
		south.setWidget(southBorderLayoutContainer);
		south.setHeadingText("Character Comparison");
		borderLayoutContainer.setSouthWidget(south, southData);
				
		availableRelationsStore = new ListStore<Relation>(new ModelKeyProvider<Relation>() {
			@Override
			public String getKey(Relation item) {
				return item.toString();
			}
		});
		availableRelationsStore.addAll(model.getAvailableRelations(articulation.getTaxonA(), articulation.getTaxonB(), Type.USER));
		relationCombo = createRelationCombo();
		
		bindEvents();
	}
	
	private BorderLayoutContainer createSouthBorderLayoutContainer() {
		BorderLayoutContainer southBorderLayoutContainer = new BorderLayoutContainer();
		BorderLayoutData eastData = new BorderLayoutData(0.5);
		eastData.setCollapseMini(true);
		eastData.setCollapsible(true);
		eastData.setSplit(true);
		eastData.setCollapsed(false);
		ContentPanel east = new ContentPanel();
		east.setWidget(createDescriptionPanel());
		east.setHeadingText("Descriptions");
		
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		ToolBar toolBar = new ToolBar();
		final ToolButton addButton = new ToolButton(ToolButton.PLUS);
		addButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				
			}
		});
		toolBar.add(addButton);
		
		ToolButton removeButton = new ToolButton(ToolButton.MINUS);
		removeButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				
			}
		});
		toolBar.add(removeButton);	
		vlc.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1.0, -1));
		vlc.add(createCharaterComparisonPanel(), new VerticalLayoutContainer.VerticalLayoutData(1.0, 1.0));
		southBorderLayoutContainer.setCenterWidget(vlc);
		southBorderLayoutContainer.setEastWidget(east, eastData);
		return southBorderLayoutContainer;
	}

	private void bindEvents() {
		eventBus.addHandler(AddArticulationsEvent.TYPE, new AddArticulationsEvent.AddArticulationEventHandler() {
			@Override
			public void onAdd(AddArticulationsEvent event) {
				for(Articulation articulation : event.getArticulations())
					availableRelationsStore.remove(articulation.getRelation());
				refreshAgreement();
			}
		});
		eventBus.addHandler(RemoveArticulationsEvent.TYPE, new RemoveArticulationsEvent.RemoveArticulationsEventHandler() {
			@Override
			public void onRemove(RemoveArticulationsEvent event) {
				for(Articulation articulation : event.getArticulations())
					availableRelationsStore.add(articulation.getRelation());
				refreshAgreement();
			}
		});
		
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
		machineArticulationsGridView = 	new MinimalArticulationsGridView(eventBus, model, articulation, Type.MACHINE);
		ContentPanel panel = new ContentPanel();
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		ToolBar toolBar = new ToolBar();
		vlc.add(toolBar);
		vlc.add(machineArticulationsGridView);
		toolBar.add(new ToolButton(ToolButton.REFRESH));
		panel.setHeadingText("Machine");
		panel.setWidget(vlc);
		return panel;
	}
	
	private ComboBox<Relation> createRelationCombo() {
		ComboBox<Relation> relationCombo = new ComboBox<Relation>(availableRelationsStore, new LabelProvider<Relation>() {
			@Override
			public String getLabel(Relation item) {
				return item.toString();
			}
		});
		relationCombo.setForceSelection(false);
		relationCombo.setTriggerAction(TriggerAction.ALL);
		relationCombo.setTypeAhead(false);
		relationCombo.setEditable(false);
		relationCombo.addSelectionHandler(new SelectionHandler<Relation>() {
			@Override
			public void onSelection(SelectionEvent<Relation> event) {
				List<Articulation> articulations = new LinkedList<Articulation>();
				articulations.add(new Articulation(articulation.getTaxonA(), articulation.getTaxonB(), event.getSelectedItem(), Type.USER));
				eventBus.fireEvent(new AddArticulationsEvent(articulations));
			}
		});
		return relationCombo;
	}

	private Widget createUserArticulationsGrid() {
		userArticulationsGridView = new MinimalArticulationsGridView(eventBus, model, articulation, Type.USER);
		
		ContentPanel panel = new ContentPanel();
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		ToolBar toolBar = new ToolBar();
		vlc.add(toolBar);
		vlc.add(userArticulationsGridView, new VerticalLayoutData(1, 1));
		final ToolButton addButton = new ToolButton(ToolButton.PLUS);
		addButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Menu menu = new Menu();
				MenuItem menuItem = new MenuItem();
				menuItem.setWidget(relationCombo);
				menu.add(menuItem);
				menu.show(addButton);
			}
		});
		toolBar.add(addButton);
		
		ToolButton removeButton = new ToolButton(ToolButton.MINUS);
		removeButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				List<Articulation> articulation = userArticulationsGridView.getSelectedArticulations();
				if(!articulation.isEmpty())
					eventBus.fireEvent(new RemoveArticulationsEvent(articulation));
			}
		});
		toolBar.add(removeButton);	
		panel.setHeadingText("User");
		panel.setWidget(vlc);
		return panel;
	}

	private IsWidget createUserVsMachinePanel() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		hlc.add(createUserArticulationsGrid(), new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
		hlc.add(createMachineArticulationsGrid(), new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
		agreementPanel = new ContentPanel();
		refreshAgreement();
		
		vlc.add(hlc, new VerticalLayoutContainer.VerticalLayoutData(1.0, 1.0));
		vlc.add(agreementPanel, new VerticalLayoutContainer.VerticalLayoutData(1.0, -1));
		
		ContentPanel center = new ContentPanel();
		center.setWidget(vlc);
		center.setHeadingHtml(SafeHtmlUtils.fromTrustedString("Articulations: "
				+ "<b>" + articulation.getTaxonA().getBiologicalName() + " <-> " + articulation.getTaxonB().getBiologicalName() + "</b>"));
		return center;
	}

	private void refreshAgreement() {
		List<Articulation> users = model.getArticulations(articulation.getTaxonA(), articulation.getTaxonB(), Type.USER);
		int initialUserSize = users.size();
		List<Articulation> machines = model.getArticulations(articulation.getTaxonA(), articulation.getTaxonB(), Type.MACHINE);
		int initialMachineSize = machines.size();
		
		users.retainAll(machines);
		machines.retainAll(users);
		double agreement = (double)(users.size() + machines.size()) / (initialUserSize + initialMachineSize); 
		
		agreementPanel.setHeadingText(agreement * 100 + "% Agreement");
	}
	
}
