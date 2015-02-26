package edu.arizona.biosemantics.euler.alignment.client.common;

import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import edu.arizona.biosemantics.euler.alignment.client.event.run.StartMIREvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class RunDialog extends CommonDialog {
	
	private EventBus eventBus;
	private Model model;
	private RunConfigPanel runConfigPanel = new RunConfigPanel();
	
	public RunDialog(final EventBus eventBus, final Model model) {
		this.eventBus = eventBus;
		this.model = model;
		
		add(runConfigPanel);
		
		this.setWidth(400);
		this.setHeight(200);
		this.setHideOnButtonClick(true);
		
		this.setPredefinedButtons(PredefinedButton.OK);
		this.getButton(PredefinedButton.OK).setText("Run");
		this.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				
				eventBus.fireEvent(new StartMIREvent(model.getTaxonomies(), model.getArticulations(), runConfigPanel.getRunConfig()));
			}
		});
		
	}
	
}
