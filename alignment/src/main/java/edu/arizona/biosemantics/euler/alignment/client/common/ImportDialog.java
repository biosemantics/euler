package edu.arizona.biosemantics.euler.alignment.client.common;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;

import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;

public class ImportDialog extends CommonDialog {
	
	private EventBus eventBus;
	private Collection collection;
	private IEulerAlignmentServiceAsync eulerAlignmentService = GWT.create(IEulerAlignmentService.class);

	public ImportDialog(final EventBus eventBus, final Collection collection) {
		this.eventBus = eventBus;
		this.collection = collection;
				
		ContentPanel panel = new ContentPanel();
		HTML html = new HTML();
		
		Taxonomy taxonomy1 = collection.getModel().getTaxonomies().get(0);
		Taxonomy taxonomy2 = collection.getModel().getTaxonomies().get(0);
		html.setHTML(SafeHtmlUtils.fromTrustedString("Taxonomy sec. " + 
				taxonomy1.getAuthor() + " " + taxonomy1.getYear() +  " ID: " + taxonomy1.getId() + " </br>" + 
				"Taxonomy sec. " + 
				taxonomy2.getAuthor() + " " + taxonomy2.getYear() +  " ID: " + taxonomy2.getId()));
		
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(html,  new VerticalLayoutData(1, -1));
		final TextArea textArea = new TextArea();
		vlc.add(textArea, new VerticalLayoutData(1, 1));
		panel.add(vlc);
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
