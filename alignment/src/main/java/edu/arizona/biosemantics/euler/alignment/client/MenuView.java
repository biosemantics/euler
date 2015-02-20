package edu.arizona.biosemantics.euler.alignment.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Params;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.MultiLinePromptMessageBox;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.HeaderMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuBar;
import com.sencha.gxt.widget.core.client.menu.MenuBarItem;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;

import edu.arizona.biosemantics.euler.alignment.client.articulate.ViewResultsDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.Alerter;
import edu.arizona.biosemantics.euler.alignment.client.common.ColorSettingsDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.ColorsDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.CommentsDialog;
import edu.arizona.biosemantics.euler.alignment.client.event.DownloadEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.SaveEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetTaxonCommentEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.run.StartInputVisualizationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.run.StartMIREvent;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.io.ArticulationsReader;

public class MenuView extends MenuBar {

	protected Model model;
	protected EventBus eventBus;
	private IEulerAlignmentServiceAsync eulerAlignmentService = GWT.create(IEulerAlignmentService.class);

	public MenuView(EventBus eventBus) {
		this.eventBus = eventBus;
		addStyleName(ThemeStyles.get().style().borderBottom());
		addItems();
		
		bindEvents();
	}

	private void bindEvents() {
		eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
			@Override
			public void onLoad(LoadModelEvent event) {
				model = event.getModel();
			}
		});
	}

	protected void addItems() {
		add(createTaxonomiesItem());
		add(createRunItem());
		add(createAnnotationsItem());
		add(createQuestionItem());
	}

	protected Widget createRunItem() {
		final Menu sub = new Menu();
		MenuBarItem runItem = new MenuBarItem("Run", sub);
		final MenuItem runEulerItem = new MenuItem("Run Euler");
		runEulerItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new StartMIREvent(model.getTaxonomies(), model.getArticulations()));
			}
		});
		final MenuItem showEulerResult = new MenuItem("Show Euler Result");
		showEulerResult.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				ViewResultsDialog dialog = new ViewResultsDialog(eventBus, model);
				dialog.show();
			}
		});
		sub.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				if(model.getRunHistory().isEmpty()) {
					sub.remove(showEulerResult);
					showEulerResult.removeFromParent();
				} else {
					int index = sub.getWidgetIndex(runEulerItem);
					sub.insert(showEulerResult, index + 1);
				}
			}
		});
		
		MenuItem showInputVisualizationItem = new MenuItem("Input Visualization");
		showInputVisualizationItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new StartInputVisualizationEvent(model));
			}
		});

		sub.add(runEulerItem);
		sub.add(showInputVisualizationItem);
		return runItem;
	}

	protected Widget createTaxonomiesItem() {
		Menu sub = new Menu();
		
		MenuBarItem taxonomiesItem = new MenuBarItem("Taxonomies", sub);
		MenuItem saveItem = new MenuItem("Save Progress");
		saveItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new SaveEvent(model));
			}
		});
		
		MenuItem importItem = new MenuItem("Import Articulations");
		importItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				final MultiLinePromptMessageBox box = new MultiLinePromptMessageBox("Articulations", "");				
				box.addHideHandler(new HideHandler() {
					@Override
					public void onHide(HideEvent event) {
						eulerAlignmentService.getArticulations(box.getValue(), model, new AsyncCallback<Articulations>() {
							@Override
							public void onFailure(Throwable caught) {
								Alerter.failedToImportArticulations(caught);								
							}
							@Override
							public void onSuccess(Articulations result) {
								eventBus.fireEvent(new ImportArticulationsEvent(result));
								Info.display("Imoprt successful", 4 + "articulations successfully imported");
							}
						});
					}
				});
				box.show();
			}
		});
		
		MenuItem downloadItem = new MenuItem("Download Articulations");
		downloadItem.setTitle("please set your browser to allow popup windows to use this function");
		downloadItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				eventBus.fireEvent(new DownloadEvent(model));
			}
		});

		// sub.add(subMatrixItem);
		sub.add(saveItem);
		sub.add(importItem);
		sub.add(downloadItem);
		return taxonomiesItem;
	}

	protected Widget createAnnotationsItem() {
		Menu sub = new Menu();
		MenuBarItem annotationsItem = new MenuBarItem("Annotation", sub);
		sub.add(new HeaderMenuItem("Configure"));
		MenuItem colorSettingsItem = new MenuItem("Color Settings");
		colorSettingsItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> arg0) {
				ColorSettingsDialog dialog = new ColorSettingsDialog(
						eventBus, model);
				dialog.show();
			}
		});
		sub.add(colorSettingsItem);
		sub.add(new HeaderMenuItem("Show"));
		MenuItem colorsItem = new MenuItem("Color Use");
		colorsItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> arg0) {
				ColorsDialog dialog = new ColorsDialog(eventBus, model);
				dialog.show();
			}
		});
		sub.add(colorsItem);
		MenuItem commentsItem = new MenuItem("Comments");
		commentsItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> arg0) {
				CommentsDialog dialog = new CommentsDialog(eventBus, model);
				dialog.show();
			}
		});
		sub.add(commentsItem);
		return annotationsItem;
	}

	protected Widget createQuestionItem() {
		Menu sub = new Menu();
		MenuBarItem questionsItem = new MenuBarItem("Instructions", sub);
		MenuItem helpItem = new MenuItem("Help");
		helpItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> arg0) {
				final Dialog dialog = new Dialog();
				dialog.setBodyBorder(false);
				dialog.setHeadingText("Help");
				dialog.setHideOnButtonClick(true);
				dialog.setWidget(new HelpView());
				dialog.setWidth(400);
				dialog.setHeight(225);
				dialog.setResizable(true);
				dialog.setShadow(true);
				dialog.show();
			}
		});
		sub.add(helpItem);
		return questionsItem;
	}
}
