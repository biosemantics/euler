package edu.arizona.biosemantics.euler.alignment.shared;


import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public interface IEulerAlignmentServiceAsync {
	
	public void getModel(AsyncCallback<Model> callback);
	
	public void getArticulations(String text, Model model, AsyncCallback<Articulations> callback);
	
}
