package edu.arizona.biosemantics.euler.alignment.shared;

import java.util.Set;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

@RemoteServiceRelativePath("alignment")
public interface IEulerAlignmentService extends RemoteService {

	public Model getModel();
	
	public Articulations getArticulations(String text, Model model) throws Exception;
	
	public SafeHtml getHighlighted(String content, Set<Highlight> highlights);
}
