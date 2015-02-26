package edu.arizona.biosemantics.euler.alignment.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import edu.arizona.biosemantics.euler.alignment.client.articulate.ArticulateMenuView;
import edu.arizona.biosemantics.euler.alignment.client.articulate.ArticulateView;
import edu.arizona.biosemantics.euler.alignment.client.articulate.ModelControler;
import edu.arizona.biosemantics.euler.alignment.client.common.ViewDiagnosisDialog;
import edu.arizona.biosemantics.euler.alignment.client.common.ViewResultsDialog;
import edu.arizona.biosemantics.euler.alignment.client.desktop.DesktopView;
import edu.arizona.biosemantics.euler.alignment.client.event.ShowDesktopEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.ToggleDesktopEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.run.EndInputVisualizationEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.run.EndMIREvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class EulerAlignmentView extends SplitLayoutPanel {
	
	private Model model;
	private SimpleEventBus eventBus;
	private ModelControler modelControler;
	
	private SimpleContainer contentContainer = new SimpleContainer();
	private SimpleContainer menuContainer = new SimpleContainer();
	private ArticulateMenuView alignmentMenuView;
	private ArticulateView alignmentView;
	private DesktopView desktopView;
	private int desktopHeight = 300;
	
	public EulerAlignmentView() {	
		eventBus = new SimpleEventBus();
		modelControler = new ModelControler(eventBus);
		
		alignmentView = new ArticulateView(eventBus);
		desktopView = new DesktopView(eventBus);
		alignmentMenuView = new ArticulateMenuView(eventBus, alignmentView);
		
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(menuContainer, new VerticalLayoutData(1,-1));
		verticalLayoutContainer.add(contentContainer, new VerticalLayoutData(1,1));
		setContent(alignmentView);
		setMenu(alignmentMenuView);
		addSouth(desktopView, 0);
		add(verticalLayoutContainer);
		
		addKeyHandling();
		bindEvents();
		
		/*desktopView.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			toggleFooter();
		}
		});*/
	}

	private void addKeyHandling() {
		/*this.addDomHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_S) {
					//fullModelBus.fireEvent(new SaveEvent(fullModel));
					//System.out.println("fire save");
				}
			}
		}, KeyDownEvent.getType());*/
	}

	private void setMenu(MenuView menuView) {
		menuContainer.setWidget(menuView);
	}
	
	private void setContent(IsWidget content) {
		contentContainer.setWidget(content);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				contentContainer.forceLayout();
				forceLayout();
			}
		}); 
	}
		
	private void bindEvents() {
		eventBus.addHandler(ShowDesktopEvent.TYPE, new ShowDesktopEvent.ShowDesktopEventHandler() {
			@Override
			public void onShow(ShowDesktopEvent showDesktopEvent) {
				showDesktop();
			}
		});
		eventBus.addHandler(ToggleDesktopEvent.TYPE, new ToggleDesktopEvent.ToggleDesktopEventHandler() {
			@Override
			public void onToggle(ToggleDesktopEvent toggleDesktopEvent) {
				toggleDesktop();
			}
		});
		eventBus.addHandler(EndMIREvent.TYPE, new EndMIREvent.EndMIREventHandler() {
			@Override
			public void onEnd(EndMIREvent event) {
				switch(event.getOutput().getType()) {
				case CONFLICT:
					ViewDiagnosisDialog dialog = new ViewDiagnosisDialog(eventBus, model);
					if(!model.getRunHistory().isEmpty() && model.getRunHistory().getLast().hasOutput()) 
						dialog.setRun(model.getRunHistory().getLast());
					dialog.show();
					break;
				case MULTIPLE:
				case ONE:
					ViewResultsDialog viewResultsDialog = new ViewResultsDialog(eventBus, model);
					if(!model.getRunHistory().isEmpty() && model.getRunHistory().getLast().hasOutput()) 
						viewResultsDialog.setRun(model.getRunHistory().getLast());
					viewResultsDialog.show();
					break;
				}
			}
		});
		eventBus.addHandler(EndInputVisualizationEvent.TYPE, new EndInputVisualizationEvent.EndInputVisualizationEventHandler() {
			@Override
			public void onShow(EndInputVisualizationEvent event) {
				Window.open(event.getResultURL(), "_blank", "");
			}
		});
		eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
			@Override
			public void onLoad(LoadModelEvent event) {
				model = event.getModel();
			}
		});
	}

	protected void showDesktop() {
		forceLayout();
		if(getWidgetSize(desktopView) < desktopHeight) 
			setWidgetSize(desktopView, desktopHeight);
		animate(500);
	}

	protected void toggleDesktop() {
		forceLayout();
		if(getWidgetSize(desktopView) == desktopHeight) 
			setWidgetSize(desktopView, 0);
		else if(getWidgetSize(desktopView) == 0) 
			setWidgetSize(desktopView, desktopHeight);
		animate(500);
	}

	public void setModel(Model model) {
		this.model = model;
		eventBus.fireEvent(new LoadModelEvent(model));
	}
		
	public EventBus getEventBus() {
		return eventBus;
	}
		
}