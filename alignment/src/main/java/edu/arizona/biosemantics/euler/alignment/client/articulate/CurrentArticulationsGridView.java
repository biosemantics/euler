package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.event.shared.EventBus;
import com.sencha.gxt.widget.core.client.info.Info;

import edu.arizona.biosemantics.euler.alignment.client.common.ArticulationsGridView;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;

public class CurrentArticulationsGridView extends ArticulationsGridView {


	public CurrentArticulationsGridView(EventBus eventBus) {
		super(eventBus);		
		bindEvents();
	}

	protected void bindEvents() {
		super.bindEvents();
		eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
			@Override
			public void onLoad(LoadModelEvent event) {
				setArticulations(event.getModel().getArticulations());
			}
		});
		eventBus.addHandler(AddArticulationsEvent.TYPE, new AddArticulationsEvent.AddArticulationEventHandler() {
			@Override
			public void onAdd(AddArticulationsEvent event) {
				addArticulations(event.getArticulations());
			}
		});
		eventBus.addHandler(RemoveArticulationsEvent.TYPE, new RemoveArticulationsEvent.RemoveArticulationsEventHandler() {
			@Override
			public void onRemove(RemoveArticulationsEvent event) {
				removeArticulations(event.getArticulations());
			}
		});
		eventBus.addHandler(ImportArticulationsEvent.TYPE, new ImportArticulationsEvent.ImportArticulationsEventHandler() {
			@Override
			public void onImport(ImportArticulationsEvent event) {
				setArticulations(event.getArticulations());
				Info.display("Imoprt successful", event.getArticulations().size() + " articulations successfully imported");
			}
		});
	}	
}
