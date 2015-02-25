package edu.arizona.biosemantics.euler.alignment.client.desktop.widget;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import edu.arizona.biosemantics.euler.alignment.client.desktop.Window;
import edu.arizona.biosemantics.euler.alignment.client.event.PrintableEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.ShowDesktopEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.ToggleDesktopEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;

public class ConsoleManager extends AbstractWindowManager {

	private class ConsoleEventsHandler implements LoadModelEvent.LoadModelEventHandler, SetColorsEvent.SetColorsEventHandler,
		SetColorEvent.SetColorEventHandler, SetCommentEvent.SetCommentEventHandler, 
		ShowDesktopEvent.ShowDesktopEventHandler, ToggleDesktopEvent.ToggleDesktopEventHandler {
		
		public ConsoleEventsHandler() {
			addEventHandlers();
		}
	
		private void addEventHandlers() {
			eventBus.addHandler(SetColorsEvent.TYPE, this);
			eventBus.addHandler(LoadModelEvent.TYPE, this);
			eventBus.addHandler(SetColorsEvent.TYPE, this);
			eventBus.addHandler(SetColorEvent.TYPE, this);
			eventBus.addHandler(SetCommentEvent.TYPE, this);
			eventBus.addHandler(ShowDesktopEvent.TYPE, this);
			eventBus.addHandler(ToggleDesktopEvent.TYPE, this);
		}

		private void printToConsole(GwtEvent event) {
			if(event instanceof PrintableEvent)
				appendConsole(((PrintableEvent) event).print());
			else
				appendConsole(event.toDebugString());
		}
		
		private void appendConsole(String text) {
			if(instance != null)
				instance.append(text);
		}
	
		@Override
		public void onToggle(ToggleDesktopEvent event) {
			printToConsole(event);
		}
	
		@Override
		public void onShow(ShowDesktopEvent event) {
			printToConsole(event);
		}
		
		@Override
		public void onLoad(LoadModelEvent event) {
			printToConsole(event);
		}
	
		@Override
		public void onSet(SetColorsEvent event) {
			printToConsole(event);
		}

		@Override
		public void onSet(SetCommentEvent event) {
			printToConsole(event);
		}

		@Override
		public void onSet(SetColorEvent event) {
			printToConsole(event);
		}		
	}
	
	public ConsoleManager(EventBus eventBus, Window window) {
		super(eventBus, window);
		init();
	}

	public class Console extends TextArea {
		
		public void append(String text) {
			String oldText = this.getText();
			this.setText(oldText + "\n" + text);
		}
	}

	private Console instance;

	@Override
	public void refreshContent() {
		//if(instance == null)
		instance = new Console();
		window.setWidget(instance);
	}

	@Override
	protected void addEventHandlers() {
		ConsoleEventsHandler consoleEventsHandler = new ConsoleEventsHandler();
	}
	
	@Override
	public void refreshContextMenu() {
		Menu menu = new Menu();
		MenuItem closeItem = new MenuItem("Close");
		menu.add(closeItem);
		closeItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				window.hide();
			}
		});
		window.setContextMenu(menu);
	}

	@Override
	public void refreshTitle() {
		window.setHeadingText("Activity Log");
	}

}
