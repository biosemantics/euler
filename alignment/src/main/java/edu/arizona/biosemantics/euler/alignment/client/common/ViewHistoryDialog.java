package edu.arizona.biosemantics.euler.alignment.client.common;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;
import edu.arizona.biosemantics.euler.alignment.shared.model.RunProperties;

public class ViewHistoryDialog extends Dialog {

	private EventBus eventBus;
	private Model model;
	private ListStore<Run> runStore;
	private ListView<Run, String> runList;
	private ArticulationsGridView articulationsGridView;
	private RunConfigPanel runConfigPanel = new RunConfigPanel();
	private TextButton viewResultButton = new TextButton("View Result");
	
	private RunProperties runProperties = GWT.create(RunProperties.class);

	public ViewHistoryDialog(final EventBus eventBus, final Model model) {
		this.eventBus = eventBus;
		this.model = model;
		articulationsGridView = new ArticulationsGridView(eventBus, model);

		runStore = new ListStore<Run>(runProperties.key());
		runStore.addAll(model.getRunHistory());
		runList = new ListView<Run, String>(runStore,
				new RunProperties.DisplayNameValueProvider());
		runList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		runList.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Run>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<Run> event) {
				Run choice = null;
				if(!event.getSelection().isEmpty())
					choice = event.getSelection().get(0);
				setRun(choice);
			}
		});
		
		viewResultButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Run run = runList.getSelectionModel().getSelectedItem();
				ViewResultsDialog dialog = new ViewResultsDialog(eventBus, model);
				dialog.setRun(run);
				dialog.show();
			}
		});
		viewResultButton.setEnabled(false);
		runConfigPanel.setEnabled(false);

		BorderLayoutContainer layout = new BorderLayoutContainer();
		add(layout);

		ContentPanel panel = new ContentPanel();
		panel.setHeadingText("Run History");
		BorderLayoutContainer.BorderLayoutData data = new BorderLayoutContainer.BorderLayoutData(
				300);
		data.setMargins(new Margins(0, 5, 0, 0));
		panel.setLayoutData(data);
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(runList,
				new VerticalLayoutData(1, 1, new Margins(5)));
		verticalLayoutContainer.add(viewResultButton, new VerticalLayoutData(1,
				-1, new Margins(5)));
		panel.add(verticalLayoutContainer);
		layout.setWestWidget(panel);

		// Layout - center
		panel = new ContentPanel();
		verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(articulationsGridView,
				new VerticalLayoutData(1, 0.5, new Margins(5)));
		verticalLayoutContainer.add(runConfigPanel, new VerticalLayoutData(1,
				0.5, new Margins(5)));
		panel.add(verticalLayoutContainer);
		panel.setHeadingText("Run Info");
		layout.setCenterWidget(panel);

		setBodyBorder(false);
		setHeadingText("Run History");
		setWidth(1000);
		setHeight(600);
		setHideOnButtonClick(true);
		setModal(true);
		setPredefinedButtons(PredefinedButton.CLOSE);
	}

	protected void setRun(Run run) {
		runConfigPanel.setRunConfig(run.getRunConfig());
		articulationsGridView.setArticulations(run.getArticulations());
		viewResultButton.setEnabled(run.hasOutput());
	}
}
