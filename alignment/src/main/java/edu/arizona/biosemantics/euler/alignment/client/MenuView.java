package edu.arizona.biosemantics.euler.alignment.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.menu.HeaderMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuBar;
import com.sencha.gxt.widget.core.client.menu.MenuBarItem;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import edu.arizona.biosemantics.euler.alignment.client.common.ColorSettingsDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.ColorsDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.CommentsDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.CommonDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.ImportDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.RunDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.ViewHistoryDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.ViewResultsDialog;
import edu.arizona.biosemantics.euler.alignment.client.event.DownloadEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.SaveEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.SwapTaxonomiesEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadMachineArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveMachineArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveUserArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.run.StartInputVisualizationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.run.StartMIREvent;
import edu.arizona.biosemantics.euler.alignment.client.settings.MachineArticulationSettingsDialog;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class MenuView extends MenuBar {

	protected Collection collection;
	protected EventBus eventBus;
	private IEulerAlignmentServiceAsync eulerAlignmentService = GWT.create(IEulerAlignmentService.class);

	public MenuView(EventBus eventBus) {
		this.eventBus = eventBus;
		addStyleName(ThemeStyles.get().style().borderBottom());
		addItems();
		
		bindEvents();
	}

	private void bindEvents() {
		eventBus.addHandler(LoadCollectionEvent.TYPE, new LoadCollectionEvent.LoadCollectionEventHandler() {
			@Override
			public void onLoad(LoadCollectionEvent event) {
				collection = event.getCollection();
			}
		});
	}

	protected void addItems() {
		add(createArticulationsItem());
		add(createRunItem());
		add(createAnnotationsItem());
		add(createViewItem());
		add(createQuestionItem());
	}

	private Widget createArticulationsItem() {
		Menu sub = new Menu();
		
		MenuBarItem articulationsItem = new MenuBarItem("Articulations", sub);
		MenuItem settingsItem = new MenuItem("Machine Articulation Settings");
		settingsItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				MachineArticulationSettingsDialog dialog = new MachineArticulationSettingsDialog();
				dialog.show();
				//dialog.setWidget(new EqualSizeCircleOverlap(200, 200, 400, 200, 100, "green", "red"));
			}
		});
		MenuItem importItem = new MenuItem("Import Articulations");
		importItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				ImportDialog importDialog = new ImportDialog(eventBus, collection);
				importDialog.show();
			}
		});
		MenuItem addMachineGeneratedArticulations = new MenuItem("Add Machine-generated Articulations");
		addMachineGeneratedArticulations.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new LoadMachineArticulationsEvent());
			}
		});
		
		MenuItem removeMachineGeneratedArticulations = new MenuItem("Remove Machine-generated Articulations");
		removeMachineGeneratedArticulations.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new RemoveMachineArticulationsEvent());
			}
		});
		
		MenuItem removeUserCreatedArticulations = new MenuItem("Remove User-created Articulations");
		removeUserCreatedArticulations.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new RemoveUserArticulationsEvent());
			}
		});		
		MenuItem downloadItem = new MenuItem("Download Articulations");
		downloadItem.setTitle("please set your browser to allow popup windows to use this function");
		downloadItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new DownloadEvent(collection));
			}
		});

		// sub.add(subMatrixItem);
		sub.add(settingsItem);
		sub.add(importItem);
		sub.add(addMachineGeneratedArticulations);
		sub.add(removeMachineGeneratedArticulations);
		sub.add(removeUserCreatedArticulations);
		sub.add(downloadItem);
		return articulationsItem;
	}

	protected Widget createRunItem() {
		final Menu sub = new Menu();
		MenuBarItem runItem = new MenuBarItem("Runs", sub);
		MenuItem showInputVisualizationItem = new MenuItem("Input Visualization");
		showInputVisualizationItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new StartInputVisualizationEvent(collection));
			}
		});
		
		final MenuItem runEulerItem = new MenuItem("Run Euler");
		runEulerItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				RunDialog runDialog = new RunDialog(eventBus, collection);
				runDialog.show();
			}
		});
		final MenuItem showEulerResult = new MenuItem("Show Last Euler Result");
		showEulerResult.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				ViewResultsDialog dialog = new ViewResultsDialog(eventBus, collection);
				if(!collection.getModel().getRunHistory().isEmpty() && collection.getModel().getRunHistory().getLast().hasOutput()) 
					dialog.setRun(collection.getModel().getRunHistory().getLast());
				dialog.show();
			}
		});
		sub.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				if(collection.getModel().getRunHistory().isEmpty() || !collection.getModel().getRunHistory().getLast().hasOutput()) {
					sub.remove(showEulerResult);
					showEulerResult.removeFromParent();
				} else {
					int index = sub.getWidgetIndex(runEulerItem);
					sub.insert(showEulerResult, index + 1);
				}
			}
		});
		final MenuItem showEulerRunHistory = new MenuItem("Show Run Euler History");
		showEulerRunHistory.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				ViewHistoryDialog dialog = new ViewHistoryDialog(eventBus, collection);
				dialog.show();
			}
		});
		MenuItem saveItem = new MenuItem("Save Run Euler History");
		saveItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new SaveEvent(collection));
			}
		});
		
		sub.add(showInputVisualizationItem);
		sub.add(runEulerItem);
		sub.add(showEulerRunHistory);
		sub.add(saveItem);
		return runItem;
	}

	protected Widget createAnnotationsItem() {
		Menu sub = new Menu();
		MenuBarItem annotationsItem = new MenuBarItem("Annotation", sub);
		sub.add(new HeaderMenuItem("Configure"));
		MenuItem colorSettingsItem = new MenuItem("Color Settings");
		colorSettingsItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> arg0) {
				ColorSettingsDialog dialog = new ColorSettingsDialog(eventBus, collection);
				dialog.show();
			}
		});
		sub.add(colorSettingsItem);
		sub.add(new HeaderMenuItem("Show"));
		MenuItem colorsItem = new MenuItem("Color Use");
		colorsItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> arg0) {
				ColorsDialog dialog = new ColorsDialog(eventBus, collection);
				dialog.show();
			}
		});
		sub.add(colorsItem);
		MenuItem commentsItem = new MenuItem("Comments");
		commentsItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> arg0) {
				CommentsDialog dialog = new CommentsDialog(eventBus, collection);
				dialog.show();
			}
		});
		sub.add(commentsItem);
		return annotationsItem;
	}
	
	private Widget createViewItem() {
		Menu sub = new Menu();
		MenuBarItem viewItem = new MenuBarItem("View", sub);
		MenuItem swapItem = new MenuItem("Swap Taxonomies");
		swapItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> arg0) {
				eventBus.fireEvent(new SwapTaxonomiesEvent());
			}
		});
		sub.add(swapItem);
		return viewItem;
	}

	protected Widget createQuestionItem() {
		Menu sub = new Menu();
		MenuBarItem questionsItem = new MenuBarItem("Instructions", sub);
		MenuItem helpItem = new MenuItem("Help");
		helpItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> arg0) {
				final Dialog dialog = new CommonDialog();
				dialog.setBodyBorder(false);
				dialog.setHeadingText("Help");
				dialog.setHideOnButtonClick(true);
				dialog.setWidget(new HelpView());
				dialog.setWidth(600);
				dialog.setHeight(600);
				dialog.setResizable(true);
				dialog.setShadow(true);
				dialog.show();
			}
		});
		sub.add(helpItem);
		return questionsItem;
	}
}
