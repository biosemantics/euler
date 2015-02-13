package edu.arizona.biosemantics.euler.alignment.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.biosemantics.euler.alignment.client.event.DownloadEvent.DownloadHandler;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class DownloadEvent extends GwtEvent<DownloadHandler> {

	public interface DownloadHandler extends EventHandler {
		void onDownload(DownloadEvent event);
	}
	
    public static Type<DownloadHandler> TYPE = new Type<DownloadHandler>();
	private Model model;

    public DownloadEvent(Model model) {
    	this.model = model;
    }
    
	@Override
	public Type<DownloadHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DownloadHandler handler) {
		handler.onDownload(this);
	}

	public Model getModel() {
		return model;
	}
	
}
