package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.event.shared.EventBus;

import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent.ImportArticulationsEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetArticulationColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetArticulationCommentEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetRunColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetRunColorEvent.SetRunColorEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetRunCommentEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetRunCommentEvent.SetRunCommentEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetTaxonColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetTaxonCommentEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent.AddArticulationEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent.LoadModelEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent.ModifyArticulationEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent.RemoveArticulationsEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetArticulationColorEvent.SetArticulationColorEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetArticulationCommentEvent.SetArticulationCommentEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorsEvent.SetColorsEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetTaxonColorEvent.SetTaxonColorEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetTaxonCommentEvent.SetTaxonCommentEventHandler;
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

public class ModelControler implements LoadModelEventHandler, SetColorsEventHandler,  
	SetTaxonCommentEventHandler, SetTaxonColorEventHandler, AddArticulationEventHandler, 
	SetArticulationCommentEventHandler, SetArticulationColorEventHandler, RemoveArticulationsEventHandler, 
	ModifyArticulationEventHandler, StartMIREventHandler, ImportArticulationsEventHandler, EndMIREventHandler, SetRunCommentEventHandler, SetRunColorEventHandler {

	protected EventBus eventBus;
	protected Model model;

	public ModelControler(EventBus eventBus) {
		this.eventBus = eventBus;
		
		addEventHandlers();
	}

	protected void addEventHandlers() {
		eventBus.addHandler(LoadModelEvent.TYPE, this);
		eventBus.addHandler(SetColorsEvent.TYPE, this);
		eventBus.addHandler(SetTaxonCommentEvent.TYPE, this);
		eventBus.addHandler(SetTaxonColorEvent.TYPE, this);
		eventBus.addHandler(SetRunCommentEvent.TYPE, this);
		eventBus.addHandler(SetRunColorEvent.TYPE, this);
		eventBus.addHandler(SetArticulationColorEvent.TYPE, this);
		eventBus.addHandler(SetArticulationCommentEvent.TYPE, this);
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
	public void onSet(SetTaxonCommentEvent event) {
		model.setComment(event.getTaxon(), event.getComment());
	}

	@Override
	public void onSet(SetTaxonColorEvent event) {
		model.setColor(event.getTaxon(), event.getColor());
	}

	@Override
	public void onAdd(AddArticulationsEvent event) {
		model.addArticulations(event.getArticulations());
	}

	@Override
	public void onSet(SetArticulationColorEvent event) {
		model.setColor(event.getArticulation(), event.getColor());
	}

	@Override
	public void onSet(SetArticulationCommentEvent event) {
		model.setComment(event.getArticulation(), event.getComment());
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
		RunConfig runConfig = new RunConfig();
		model.addRun(new Run(taxonomies, clonedArticulations, runConfig));
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
	public void onSet(SetRunColorEvent event) {
		model.setColor(event.getRun(), event.getColor());
	}

	@Override
	public void onSet(SetRunCommentEvent event) {
		model.setComment(event.getRun(), event.getComment());
	}

}
