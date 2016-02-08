package edu.arizona.biosemantics.euler.alignment.shared;

import java.io.IOException;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.biosemantics.euler.alignment.server.db.Query.QueryException;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

@RemoteServiceRelativePath("alignment")
public interface IEulerAlignmentService extends RemoteService {

	public Model getModel();
	
	public Articulations getArticulations(String text, Model model) throws Exception;
	
	public SafeHtml getHighlighted(String content, Set<Highlight> highlights);

	public Collection createCollection(Collection collection) throws Exception;
	
}
