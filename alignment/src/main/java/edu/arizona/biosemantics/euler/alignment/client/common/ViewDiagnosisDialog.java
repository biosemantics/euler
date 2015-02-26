package edu.arizona.biosemantics.euler.alignment.client.common;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;

public class ViewDiagnosisDialog extends CommonDialog {
	
	private EventBus eventBus;
	private Model model;
	private TextButton viewDiagnosisLatticeButton = new TextButton("View Diagnosis Lattice");
	private Run run;
	
	public ViewDiagnosisDialog(final EventBus eventBus, final Model model) {
		this.eventBus = eventBus;
		this.model = model;
		
		viewDiagnosisLatticeButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if(model.getRunHistory().getLast().hasOutput())
					Window.open(run.getOutput().getDiagnosisUrl(), "_blank", "");
			}
		});
				
		ContentPanel panel = new ContentPanel();
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(viewDiagnosisLatticeButton, new VerticalLayoutData(1, -1, new Margins(5)));
		panel.add(verticalLayoutContainer);
		add(panel);
		
		setBodyBorder(false);
		setHeadingText("Conflicting Input");
		setWidth(200);
		setHeight(400);
		setHideOnButtonClick(true);
		setModal(true);
		setPredefinedButtons(PredefinedButton.CLOSE);
	}

	public void setRun(Run run) {
		this.run = run;
	}

}
