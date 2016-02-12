package edu.arizona.biosemantics.euler.alignment.client.common;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;

import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class ImportDialog extends CommonDialog {
	
	private EventBus eventBus;
	private Collection collection;
	private IEulerAlignmentServiceAsync eulerAlignmentService = GWT.create(IEulerAlignmentService.class);

	public ImportDialog(final EventBus eventBus, final Collection collection) {
		this.eventBus = eventBus;
		this.collection = collection;
				
		ContentPanel panel = new ContentPanel();
		final TextArea textArea = new TextArea();
		panel.add(textArea);
		this.setPredefinedButtons(PredefinedButton.CLOSE, PredefinedButton.CANCEL);
		TextButton importButton = this.getButton(PredefinedButton.CLOSE);
		importButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				eulerAlignmentService.getArticulations(collection, textArea.getValue(), new AsyncCallback<Articulations>() {
					@Override
					public void onFailure(Throwable caught) {
						Alerter.failedToImportArticulations(caught);								
					}
					@Override
					public void onSuccess(Articulations result) {
						eventBus.fireEvent(new ImportArticulationsEvent(result));
					}
				});
			}
		});
		importButton.setText("Import");
		
		add(panel);
		setBodyBorder(false);
		setHeadingText("Import Articulations");
		setWidth(600);
		setHeight(400);
		setHideOnButtonClick(true);
		setModal(true);
	}
}
