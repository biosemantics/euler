package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Params;
import com.sencha.gxt.widget.core.client.box.MultiLinePromptMessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.HeaderMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuBarItem;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import edu.arizona.biosemantics.euler.alignment.client.common.ArticulationsGridView;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadMachineArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.ImportArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveMachineArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveUserArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;

public class CurrentArticulationsGridView extends ArticulationsGridView {

	public CurrentArticulationsGridView(EventBus eventBus) {
		super(eventBus, null, true, true);
		bindEvents();

		grid.setContextMenu(createContextMenu());
	}

	private Menu createContextMenu() {
		Menu menu = new Menu();
		MenuItem addMachineGeneratedArticulations = new MenuItem(
				"Add Machine-generated Articulations");
		addMachineGeneratedArticulations
				.addSelectionHandler(new SelectionHandler<Item>() {
					@Override
					public void onSelection(SelectionEvent<Item> event) {
						eventBus.fireEvent(new LoadMachineArticulationsEvent());
					}
				});

		MenuItem removeMachineGeneratedArticulations = new MenuItem(
				"Remove Machine-generated Articulations");
		removeMachineGeneratedArticulations
				.addSelectionHandler(new SelectionHandler<Item>() {
					@Override
					public void onSelection(SelectionEvent<Item> event) {
						eventBus.fireEvent(new RemoveMachineArticulationsEvent());
					}
				});

		MenuItem removeUserCreatedArticulations = new MenuItem(
				"Remove User-created Articulations");
		removeUserCreatedArticulations
				.addSelectionHandler(new SelectionHandler<Item>() {
					@Override
					public void onSelection(SelectionEvent<Item> event) {
						eventBus.fireEvent(new RemoveUserArticulationsEvent());
					}
				});

		// sub.add(subMatrixItem);
		menu.add(addMachineGeneratedArticulations);
		menu.add(removeMachineGeneratedArticulations);
		menu.add(removeUserCreatedArticulations);
		return menu;
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
		eventBus.addHandler(RemoveMachineArticulationsEvent.TYPE, new RemoveMachineArticulationsEvent.RemoveMachineArticulationsHandler() {
			@Override
			public void onRemove(RemoveMachineArticulationsEvent event) {
				removeArticulations(Type.MACHINE);
			}
		});
		eventBus.addHandler(RemoveUserArticulationsEvent.TYPE, new RemoveUserArticulationsEvent.RemoveUserArticulationsHandler() {
			@Override
			public void onRemove(RemoveUserArticulationsEvent event) {
				removeArticulations(Type.USER);
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

	protected void removeArticulations(Type type) {
		for(Articulation articulation : new LinkedList<Articulation>(articulationsStore.getAll()))
			if(articulation.getType().equals(type))
				articulationsStore.remove(articulation);
	}
}
