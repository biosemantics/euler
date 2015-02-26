package edu.arizona.biosemantics.euler.alignment.client.common;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.PossibleWorld;
import edu.arizona.biosemantics.euler.alignment.shared.model.PossibleWorldProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;

public class ViewResultsDialog extends CommonDialog {
	
	private EventBus eventBus;
	private Model model;
	private ListStore<PossibleWorld> resultStore;
	private ListView<PossibleWorld, String> resultList;
	private TextButton viewButton = new TextButton("View");
	private TextButton aggregateButton = new TextButton("Aggregate");
	private PossibleWorldProperties possibleWorldProperties = GWT.create(PossibleWorldProperties.class);
	private Run run;
	
	public void setRun(Run run) {
		this.run = run;
		if(run.hasOutput())
			resultStore.addAll(run.getOutput().getPossibleWorlds());
	}

	public ViewResultsDialog(final EventBus eventBus, final Model model) {
		this.eventBus = eventBus;
		this.model = model;
		
		resultStore = new ListStore<PossibleWorld>(possibleWorldProperties.key());
		resultList = new ListView<PossibleWorld, String>(resultStore, possibleWorldProperties.displayName());
		resultList.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		viewButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				for(PossibleWorld item : resultList.getSelectionModel().getSelectedItems()) {
					Window.open(item.getUrl(), "_blank", "");
				}
			}
		});
		
		aggregateButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if(model.getRunHistory().getLast().hasOutput())
					Window.open(run.getOutput().getAggregateUrl(), "_blank", "");
			}
		});
		
		ContentPanel panel = new ContentPanel();
		panel.setHeadingText("Possible Worlds");
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(resultList, new VerticalLayoutData(1, 1));
		verticalLayoutContainer.add(viewButton, new VerticalLayoutData(1, -1, new Margins(5)));
		verticalLayoutContainer.add(aggregateButton, new VerticalLayoutData(1, -1, new Margins(5)));
		panel.add(verticalLayoutContainer);
		add(panel);
		
		setBodyBorder(false);
		setHeadingText("MIR Results");
		setWidth(200);
		setHeight(400);
		setHideOnButtonClick(true);
		setModal(true);
		setPredefinedButtons(PredefinedButton.CLOSE);
	}

}
