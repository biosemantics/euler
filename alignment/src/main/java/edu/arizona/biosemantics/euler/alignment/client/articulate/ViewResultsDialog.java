package edu.arizona.biosemantics.euler.alignment.client.articulate;

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

public class ViewResultsDialog extends Dialog {
	
	private EventBus eventBus;
	private Model model;
	private ListStore<PossibleWorld> resultStore;
	private ListView<PossibleWorld, String> resultList;
	private TextButton viewButton = new TextButton("View");
	private PossibleWorldProperties possibleWorldProperties = GWT.create(PossibleWorldProperties.class);

	public ViewResultsDialog(final EventBus eventBus, final Model model) {
		this.eventBus = eventBus;
		this.model = model;
		
		resultStore = new ListStore<PossibleWorld>(possibleWorldProperties.key());
		if(!model.getRunHistory().isEmpty())
			resultStore.addAll(model.getRunHistory().getLast().getOutput().getPossibleWorlds());
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
		
		ContentPanel panel = new ContentPanel();
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(resultList, new VerticalLayoutData(1, 1));
		verticalLayoutContainer.add(viewButton, new VerticalLayoutData(1, -1, new Margins(5)));
		panel.add(verticalLayoutContainer);
		this.setPredefinedButtons(PredefinedButton.CLOSE);
		
		add(panel);
		setBodyBorder(false);
		setHeadingText("MIR Results");
		setWidth(200);
		setHeight(400);
		setHideOnButtonClick(true);
		setModal(true);
	}
}
