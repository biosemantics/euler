package edu.arizona.biosemantics.euler.alignment.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class EulerAlignment implements EntryPoint {

	@Override
	public void onModuleLoad() {		  
		final EulerAlignmentView view = new EulerAlignmentView();

		// simulate etc site 
		DockLayoutPanel dock = new DockLayoutPanel(Unit.EM);
		dock.addNorth(new HTML("header"), 2);
		HTML footer = new HTML("footer");
		dock.addSouth(footer, 2);
		dock.add(view.asWidget());
		RootLayoutPanel.get().add(dock);

		IEulerAlignmentServiceAsync eulerAlignmentService = GWT.create(IEulerAlignmentService.class);
		// TaxonMatrix taxonMatrix = createSampleMatrix();
		eulerAlignmentService.getModel(new AsyncCallback<Model>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(final Model result) {
				view.setModel(result);
			}
		});
	}
}
