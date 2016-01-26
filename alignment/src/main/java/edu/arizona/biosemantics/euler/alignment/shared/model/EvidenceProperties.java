package edu.arizona.biosemantics.euler.alignment.shared.model;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import edu.arizona.biosemantics.common.taxonomy.Rank;

public interface EvidenceProperties extends PropertyAccess<Evidence> {

	  @Path("id")
	  ModelKeyProvider<Evidence> key();
	 
	  ValueProvider<Evidence, String> taxonACharacter();
	
	  ValueProvider<Evidence, String> taxonBCharacter();
	  
	  ValueProvider<Evidence, Double> similarity();
	  
	  ValueProvider<Evidence, Double> uniqueness();

	  ValueProvider<Evidence, DiagnosticValue> diagnosticValue();

	  ValueProvider<Evidence, Rank> diagnosticScope();
	  
}