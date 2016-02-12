package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;

public interface OverlapProperties extends PropertyAccess<Overlap> {

	public class TaxonACharacterString implements ValueProvider<Overlap, String> {
		@Override
		public String getValue(Overlap object) {
			return object.getTaxonACharacter().toString();
		}

		@Override
		public void setValue(Overlap object, String value) {
		}

		@Override
		public String getPath() {
			return "taxonACharacterString";
		}
	}
	
	public class TaxonBCharacterString implements ValueProvider<Overlap, String> {
		@Override
		public String getValue(Overlap object) {
			return object.getTaxonBCharacter().toString();
		}

		@Override
		public void setValue(Overlap object, String value) {
		}

		@Override
		public String getPath() {
			return "taxonBCharacterString";
		}
	}
	

	@Path("id")
	  ModelKeyProvider<Overlap> key();
	 
	  ValueProvider<Overlap, CharacterState> taxonACharacter();
	
	  ValueProvider<Overlap, CharacterState> taxonBCharacter();
	  
	  ValueProvider<Overlap, String> taxonBCharacterString();
	  
	  ValueProvider<Overlap, Double> similarity();
	  
	  ValueProvider<Overlap, Double> uniqueness();

	  ValueProvider<Overlap, DiagnosticValue> diagnosticValue();

	  ValueProvider<Overlap, Rank> diagnosticScope();
	  
}