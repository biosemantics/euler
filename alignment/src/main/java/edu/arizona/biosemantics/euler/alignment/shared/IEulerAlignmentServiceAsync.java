package edu.arizona.biosemantics.euler.alignment.shared;

import java.util.Set;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public interface IEulerAlignmentServiceAsync {
	
	public void getModel(AsyncCallback<Model> callback);
	
	public void getArticulations(String text, Model model, AsyncCallback<Articulations> callback);
	
	public void getHighlighted(String content, Set<Highlight> highlights, AsyncCallback<SafeHtml> callback);
	
	public void createCollection(Collection collection, AsyncCallback<Collection> callback);
}
