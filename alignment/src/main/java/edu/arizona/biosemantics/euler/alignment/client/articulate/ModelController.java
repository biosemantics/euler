package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.event.shared.EventBus;

import edu.arizona.biosemantics.euler.alignment.client.event.SwapTaxonomiesEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticScopeEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent3;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent.AddArticulationEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent.ImportArticulationsEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent.LoadCollectionEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyArticulationEvent.ModifyArticulationEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent.RemoveArticulationsEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorEvent.SetColorEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorsEvent.SetColorsEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent.SetCommentEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent.ModifyDiagnosticValueEventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.run.EndMIREvent;
import edu.arizona.biosemantics.euler.alignment.client.event.run.EndMIREvent.EndMIREventHandler;
import edu.arizona.biosemantics.euler.alignment.client.event.run.StartMIREvent;
import edu.arizona.biosemantics.euler.alignment.client.event.run.StartMIREvent.StartMIREventHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.ModelSwapper;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;
import edu.arizona.biosemantics.euler.alignment.shared.model.RunConfig;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomies;

public class ModelController implements LoadCollectionEventHandler, SetColorsEventHandler,  AddArticulationEventHandler, 
	RemoveArticulationsEventHandler, ModifyArticulationEventHandler, StartMIREventHandler, ImportArticulationsEventHandler, EndMIREventHandler, 
	SetColorEventHandler, SetCommentEventHandler, SwapTaxonomiesEvent.SwapTaxonomiesEventHandler, 
	ModifyDiagnosticScopeEvent.ModifyDiagnosticScopeEventHandler, 
	edu.arizona.biosemantics.euler.alignment.client.event.model.ModifyDiagnosticValueEvent.ModifyDiagnosticValueEventHandler, 
	ModifyDiagnosticValueEvent3.ModifyDiagnosticValueEventHandler {

	protected EventBus eventBus;
	protected Model model;
	private Collection collection;

	public ModelController(EventBus eventBus) {
		this.eventBus = eventBus;
		
		addEventHandlers();
	}

	protected void addEventHandlers() {
		eventBus.addHandler(LoadCollectionEvent.TYPE, this);
		eventBus.addHandler(SetColorsEvent.TYPE, this);
		eventBus.addHandler(SetCommentEvent.TYPE, this);
		eventBus.addHandler(SetColorEvent.TYPE, this);
		eventBus.addHandler(AddArticulationsEvent.TYPE, this);
		eventBus.addHandler(RemoveArticulationsEvent.TYPE, this);
		eventBus.addHandler(ModifyArticulationEvent.TYPE, this);
		eventBus.addHandler(StartMIREvent.TYPE, this);
		eventBus.addHandler(ImportArticulationsEvent.TYPE, this);
		eventBus.addHandler(EndMIREvent.TYPE, this);
		eventBus.addHandler(SwapTaxonomiesEvent.TYPE, this);
		eventBus.addHandler(ModifyDiagnosticScopeEvent.TYPE, this);
		eventBus.addHandler(ModifyDiagnosticValueEvent3.TYPE, this);
	}

	@Override
	public void onLoad(LoadCollectionEvent event) {
		this.collection = event.getCollection();
		this.model = collection.getModel();
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
		model.changeArticulationRelation(event.getArticulation(), event.getNewType());
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

	@Override
	public void onShow(SwapTaxonomiesEvent event) {
		ModelSwapper swapper = new ModelSwapper();
		eventBus.fireEvent(new LoadCollectionEvent(new Collection(collection.getId(), collection.getSecret(), swapper.swap(model))));
	}

	@Override
	public void onModify(ModifyDiagnosticScopeEvent event) {
		event.getEvidence().setDiagnosticScope(event.getNewDiagnosticScope());
	}

	@Override
	public void onModify(ModifyDiagnosticValueEvent event) {
		event.getEvidence().setDiagnosticValue(event.getNewDiagnosticValue());
	}

	@Override
	public void onModify(ModifyDiagnosticValueEvent3 event) {
		model.setDiagnosticValue(event.getNode(), event.getNewDiagnosticValue());
	}

}
