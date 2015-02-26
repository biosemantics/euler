package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.event.shared.EventBus;

import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent.AddArticulationEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent.ImportArticulationsEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent.LoadModelEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent.ModifyArticulationEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent.RemoveArticulationsEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorEvent.SetColorEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorsEvent.SetColorsEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent.SetCommentEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.run.EndMIREvent;
import edu.arizona.biosemantics.euler.alignment.client.event.run.EndMIREvent.EndMIREventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.run.StartMIREvent;
import edu.arizona.biosemantics.euler.alignment.client.event.run.StartMIREvent.StartMIREventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;
import edu.arizona.biosemantics.euler.alignment.shared.model.RunConfig;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomies;

public class ModelControler implements LoadModelEventHandler, SetColorsEventHandler,  AddArticulationEventHandler, 
	RemoveArticulationsEventHandler, ModifyArticulationEventHandler, StartMIREventHandler, ImportArticulationsEventHandler, EndMIREventHandler, 
	SetColorEventHandler, SetCommentEventHandler {

	protected EventBus eventBus;
	protected Model model;

	public ModelControler(EventBus eventBus) {
		this.eventBus = eventBus;
		
		addEventHandlers();
	}

	protected void addEventHandlers() {
		eventBus.addHandler(LoadModelEvent.TYPE, this);
		eventBus.addHandler(SetColorsEvent.TYPE, this);
		eventBus.addHandler(SetCommentEvent.TYPE, this);
		eventBus.addHandler(SetColorEvent.TYPE, this);
		eventBus.addHandler(AddArticulationsEvent.TYPE, this);
		eventBus.addHandler(RemoveArticulationsEvent.TYPE, this);
		eventBus.addHandler(ModifyArticulationEvent.TYPE, this);
		eventBus.addHandler(StartMIREvent.TYPE, this);
		eventBus.addHandler(ImportArticulationsEvent.TYPE, this);
		eventBus.addHandler(EndMIREvent.TYPE, this);
	}

	@Override
	public void onLoad(LoadModelEvent event) {
		this.model = event.getModel();
	}

	@Override
	public void onSet(SetColorsEvent event) {
		model.getColors().clear();
		model.getColors().addAll(event.getColors());
	}

	@Override
	public void onAdd(AddArticulationsEvent event) {
		model.addArticulations(event.getArticulations());
	}

	@Override
	public void onModify(ModifyArticulationEvent event) {
		model.changeArticulationType(event.getArticulation(), event.getNewType());
	}

	@Override
	public void onRemove(RemoveArticulationsEvent event) {
		model.removeArticulations(event.getArticulations());
	}

	@Override
	public void onShow(StartMIREvent event) {
		Taxonomies taxonomies = model.getTaxonomies(); //taxonomies.clone(); not necessary to clone if taxonomies can not be editted
		Articulations clonedArticulations = new Articulations();
		for(Articulation articulation : model.getArticulations()) {
			Articulation clone = articulation.getClone();
			clonedArticulations.add(clone);
			if(model.hasColor(articulation))
				model.setColor(clone, model.getColor(articulation));
			if(model.hasComment(articulation))
				model.setComment(clone, model.getComment(articulation));
		}
		model.addRun(new Run(taxonomies, clonedArticulations, event.getRunConfig()));
	}

	@Override
	public void onImport(ImportArticulationsEvent event) {
		model.clearArticulations();
		model.addArticulations(event.getArticulations());
	}

	@Override
	public void onEnd(EndMIREvent event) {
		if(!model.getRunHistory().isEmpty())
			model.getRunHistory().getLast().setOutput(event.getOutput());
	}

	@Override
	public void onSet(SetCommentEvent event) {
		model.setComment(event.getObject(), event.getComment());
	}

	@Override
	public void onSet(SetColorEvent event) {
		model.setColor(event.getObject(), event.getColor());
	}

}
