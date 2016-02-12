package edu.arizona.biosemantics.euler.alignment.shared;

import java.util.List;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterOverlap;

public interface IEulerAlignmentServiceAsync {
	
	public void getArticulations(Collection collection, String text, AsyncCallback<Articulations> callback);
	
	public void getHighlighted(String content, Set<Highlight> highlights, AsyncCallback<SafeHtml> callback);
	
	public void createCollection(Collection collection, AsyncCallback<Collection> callback);

	public void getCollection(int id, String secret, AsyncCallback<Collection> callback);

	public void getEvidence(Collection collection, Taxon taxonA, Taxon taxonB, AsyncCallback<List<Evidence>> callback);

	public void getCharacterOverlap(Collection collection, Taxon taxonA, Taxon taxonB, double threshold, AsyncCallback<CharacterOverlap> callback);
	
}

