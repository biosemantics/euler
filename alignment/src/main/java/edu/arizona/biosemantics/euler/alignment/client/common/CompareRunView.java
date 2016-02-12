package edu.arizona.biosemantics.euler.alignment.client.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewSelectionModel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;
import edu.arizona.biosemantics.euler.alignment.shared.model.RunProperties;

public class CompareRunView  extends BorderLayoutContainer {

	private EventBus eventBus;
	private Collection collection;
	//private ListStore<Run> runStore;
	//private ListView<Run, String> runList;
	//private ArticulationsGridView articulationsGridView;
	//private RunConfigPanel runConfigPanel = new RunConfigPanel();
	//private TextButton viewResultButton = new TextButton("View Result");
	
	//private RunProperties runProperties = GWT.create(RunProperties.class);
	private RunConfigPanel runConfigPanel1;
	private RunConfigPanel runConfigPanel2;
	private HorizontalLayoutContainer runConfigsPanel;
	private ComparedArticulationsGridView comparedArticulationsGridView;
	private RunGridView runGridView;
	
	public CompareRunView(final EventBus eventBus, final Collection collection) {
		this.eventBus = eventBus;
		this.collection = collection;
		
		comparedArticulationsGridView = new ComparedArticulationsGridView(eventBus, collection);
		
		runGridView = new RunGridView(eventBus, collection);
		/*runStore = new ListStore<Run>(runProperties.key());
		runStore.addAll(model.getRunHistory());
		runList = new ListView<Run, String>(runStore,
				new RunProperties.DisplayNameValueProvider());*/
		runGridView.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		runGridView.getSelectionModel().addBeforeSelectionHandler(new BeforeSelectionHandler<Run>() {
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Run> event) {
				if(runGridView.getSelectionModel().getSelection().size() >= 2)
					event.cancel();
			}
		});
		runGridView.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Run>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<Run> event) {
				List<Run> selection = event.getSelection();
				if(selection.size() == 2) {
					List<Run> sortedSelection = new ArrayList<Run>(selection);
					Collections.sort(sortedSelection, new Comparator<Run>() {
						@Override
						public int compare(Run arg0, Run arg1) {
							return arg0.getCreated().compareTo(arg1.getCreated());
						}
					});
					updateComparison(sortedSelection.get(0), sortedSelection.get(1));
				} else
					removeComparison();
			}
		});
		/*runList.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Run>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<Run> event) {
				Run choice = null;
				if(!event.getSelection().isEmpty())
					choice = event.getSelection().get(0);
				setRun(choice);
			}
		});*/
		
		/*viewResultButton.addSelectHandler(new SelectHandler() {
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
		
		*/
		ContentPanel panel = new ContentPanel();
		panel.setHeadingText("Run History");
		
		BorderLayoutData westData = new BorderLayoutData(400);
		westData.setMargins(new Margins(1));
		westData.setCollapsible(true);
		westData.setSplit(true);
		panel.setLayoutData(westData);
		
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(runGridView,
				new VerticalLayoutData(1, 1, new Margins(5)));
		//verticalLayoutContainer.add(viewResultButton, new VerticalLayoutData(1,
		//		-1, new Margins(5)));
		panel.add(verticalLayoutContainer);
		setWestWidget(panel);

		
		// Layout - center
		panel = new ContentPanel();
		
		BorderLayoutData centerData = new BorderLayoutData(500);
		centerData.setMargins(new Margins(1));
		centerData.setCollapsible(true);
		centerData.setSplit(true);
		panel.setLayoutData(centerData);
		
		verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(comparedArticulationsGridView,
				new VerticalLayoutData(1, 0.5, new Margins(5)));
		verticalLayoutContainer.add(createCompareRunConfigPanel(), new VerticalLayoutData(1,
				0.5, new Margins(5)));
		panel.add(verticalLayoutContainer);
		panel.setHeadingText("Run Comparison");
		setCenterWidget(panel); 
		
		removeComparison();
	}

	protected void removeComparison() {
		runConfigsPanel.hide();
		comparedArticulationsGridView.clear();
	}

	protected void updateComparison(Run run1, Run run2) {
		runConfigsPanel.show();
		runConfigPanel1.setRunConfig(run1.getRunConfig());
		runConfigPanel2.setRunConfig(run2.getRunConfig());
		runConfigsPanel.forceLayout();
		comparedArticulationsGridView.update(run1, run2);
	}

	private IsWidget createCompareRunConfigPanel() {
		runConfigsPanel = new HorizontalLayoutContainer();
		runConfigPanel1 = new RunConfigPanel();
		runConfigPanel1.setEnabled(false);
		runConfigsPanel.add(runConfigPanel1);
		runConfigPanel2 = new RunConfigPanel();
		runConfigPanel2.setEnabled(false);
		runConfigsPanel.add(runConfigPanel2);
		return runConfigsPanel;
	}
}
