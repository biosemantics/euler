package edu.arizona.biosemantics.euler.alignment.shared.model;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;

public interface ArticulationProperties extends PropertyAccess<Taxon> {

	  @Path("id")
	  ModelKeyProvider<Articulation> key();
	   
	  @Path("text")
	  LabelProvider<Articulation> nameLabel();
	 
	  ValueProvider<Articulation, Type> type();
	
	  ValueProvider<Articulation, Taxon> taxonA();
	  
	  ValueProvider<Articulation, Taxon> taxonB();
	  

	  public static class TypeStringValueProvider implements ValueProvider<Articulation, String> {

		@Override
		public String getValue(Articulation object) {
			return object.getType().displayName();
		}

		@Override
		public void setValue(Articulation object, String value) {	}

		@Override
		public String getPath() {
			return "typeString";
		}
		  
	  }
	  
	  public static class TaxonAStringValueProvider implements ValueProvider<Articulation, String> {

			@Override
			public String getValue(Articulation object) {
				return object.getTaxonA().getFullName();
			}

			@Override
			public void setValue(Articulation object, String value) {
				
			}

			@Override
			public String getPath() {
				return "taxonAString";
			}
			  
		  }
	  
	  public static class TaxonBStringValueProvider implements ValueProvider<Articulation, String> {

			@Override
			public String getValue(Articulation object) {
				return object.getTaxonB().getFullName();
			}

			@Override
			public void setValue(Articulation object, String value) {
				
			}

			@Override
			public String getPath() {
				return "taxonBString";
			}
			  
		  }
}