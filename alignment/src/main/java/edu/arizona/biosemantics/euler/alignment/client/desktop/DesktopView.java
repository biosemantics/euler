package edu.arizona.biosemantics.euler.alignment.client.desktop;

import com.google.gwt.event.shared.EventBus;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;

import edu.arizona.biosemantics.euler.alignment.client.desktop.widget.ConsoleManager;
import edu.arizona.biosemantics.euler.alignment.client.desktop.widget.ConsoleManager.Console;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class DesktopView extends FlowLayoutContainer { //CssFloatLayoutContainer
	
	public class DesktopController {
				
		private Console console;
		private Model model;
		
		public DesktopController() {
			// TODO let is show all interesting events - also for debugging
			Window consoleWindow = new Window(false);
			ConsoleManager manager = new ConsoleManager(eventBus, consoleWindow);
			consoleWindow.setWindowManager(manager);
			addWindow(consoleWindow);
			
			addEventHandlers();
		}

		private void addEventHandlers() {
			eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
				@Override
				public void onLoad(LoadModelEvent event) {
					model = event.getModel();
				}
			});
		}		
	}
	
	private EventBus eventBus;
	private int defaultMargin = 5;
	private int widgetId = 0;
	private int marginIncrement = 20;

	public DesktopView(EventBus eventBus) {
		this.eventBus = eventBus;
		this.setScrollMode(ScrollMode.AUTO);
		
		DesktopController controller = new DesktopController();
	}
		
	public Window addWindow(Window window) {		
		if(this.getWidgetCount() == 0)
			this.add(window, new MarginData(defaultMargin, defaultMargin, defaultMargin, defaultMargin));
		else
			this.add(window, new MarginData(- window.getFramedPanelHeight() + marginIncrement + defaultMargin, defaultMargin, defaultMargin, ++widgetId * marginIncrement + defaultMargin));
		
		final Resizable resize = new Resizable(window, Dir.E, Dir.SE, Dir.S);
		Draggable draggablePanel = new Draggable(window, window.getHeader());
		draggablePanel.setUseProxy(false);
		draggablePanel.setContainer(this);
				
		// resize.setMinHeight(400);
		// resize.setMinWidth(400);

		/*addExpandHandler(new ExpandHandler() {
			@Override
			public void onExpand(ExpandEvent event) {
				resize.setEnabled(true);
			}
		});
		addCollapseHandler(new CollapseHandler() {
			@Override
			public void onCollapse(CollapseEvent event) {
				resize.setEnabled(false);
			}
		});*/
		return window;
	}
}
