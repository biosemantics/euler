package edu.arizona.biosemantics.euler.alignment.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

@RemoteServiceRelativePath("alignment")
public interface IEulerAlignmentService extends RemoteService {

	public Model getModel();
	
	public Articulations getArticulations(String text, Model model) throws Exception;
	
}
