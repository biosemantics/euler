package edu.arizona.biosemantics.euler.alignment.shared.model;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface TaxonProperties extends PropertyAccess<Taxon> {

	  @Path("id")
	  ModelKeyProvider<Taxon> key();
	   
	  @Path("biologicalName")
	  LabelProvider<Taxon> nameLabel();
	 
	  ValueProvider<Taxon, String> description();
	
	  ValueProvider<Taxon, String> fullName();
}