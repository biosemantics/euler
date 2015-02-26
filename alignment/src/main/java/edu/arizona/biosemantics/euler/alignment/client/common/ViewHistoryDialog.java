package edu.arizona.biosemantics.euler.alignment.client.common;

import com.google.gwt.event.shared.EventBus;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;

import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class ViewHistoryDialog extends CommonDialog {

	private EventBus eventBus;
	private Model model;

	public ViewHistoryDialog(final EventBus eventBus, final Model model) {
		this.eventBus = eventBus;
		this.model = model;
		
		TabPanel panel = new TabPanel();
		panel.setTabScroll(true);
		panel.setAnimScroll(true);
		panel.add(new SingleRunView(eventBus, model), new TabItemConfig("Single", false));
		panel.add(new CompareRunView(eventBus, model), new TabItemConfig("Compare", false));

		add(panel);
		
		setBodyBorder(false);
		setHeadingText("Run History");
		setWidth(1000);
		setHeight(600);
		setHideOnButtonClick(true);
		setModal(true);
		setPredefinedButtons(PredefinedButton.CLOSE);
		this.setResizable(true);
		this.setMaximizable(true);
		//this.setMinimizable(true);
		this.setBlinkModal(true);
		//this.setCollapsible(true);
	}

}
