package edu.arizona.biosemantics.euler.alignment.shared;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.ListStore;

import edu.arizona.biosemantics.euler.alignment.server.db.Query.QueryException;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterOverlap;

@RemoteServiceRelativePath("alignment")
public interface IEulerAlignmentService extends RemoteService {
	
	public Articulations getArticulations(Collection collection, String text) throws Exception;
	
	public SafeHtml getHighlighted(String content, Set<Highlight> highlights);

	public Collection createCollection(Collection collection) throws Exception;

	public Collection getCollection(int id, String secret) throws Exception;

	public List<Evidence> getEvidence(Collection collection, Taxon taxonA, Taxon taxonB) throws Exception;

	public CharacterOverlap getCharacterOverlap(Collection collection, Taxon taxonA, Taxon taxonB, double threshold);
	
}
