package edu.arizona.biosemantics.euler.alignment.shared.model;

import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;

public interface ArticulationProperties extends PropertyAccess<Articulation> {

	  @Path("id")
	  ModelKeyProvider<Articulation> key();
	   
	  @Path("text")
	  LabelProvider<Articulation> nameLabel();
	 
	  ValueProvider<Articulation, Relation> relation();
	
	  ValueProvider<Articulation, Taxon> taxonA();
	  
	  ValueProvider<Articulation, Type> type();
	  
	  ValueProvider<Articulation, Double> confidence();
	  
	  public static class ConfidenceStringValueProvider implements ValueProvider<Articulation, String> {
		@Override
		public String getValue(Articulation object) {
			return String.valueOf(object.getConfidence());
		}
		@Override
		public void setValue(Articulation object, String value) {
			object.setConfidence(Double.valueOf(value));
		}
		@Override
		public String getPath() {
			return "confidence";
		}
	  }
	  
	  ValueProvider<Articulation, Taxon> taxonB();
	  
		public static class CreatedStringValueProvder implements ValueProvider<Articulation, String> {
			private DateTimeFormat format = DateTimeFormat
					.getFormat("MM/dd/yyyy HH:mm:ss");
			@Override
			public String getValue(Articulation object) {
				return format.format(object.getCreated());
			}

			@Override
			public void setValue(Articulation object, String value) {
				
			}

			@Override
			public String getPath() {
				return "created";
			}
	
		}
	
	  public static class TypeStringValueProvider implements ValueProvider<Articulation, String> {

		@Override
		public String getValue(Articulation object) {
			return object.getRelation().displayName();
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
				return object.getTaxonA().getBiologicalName();
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
				return object.getTaxonB().getBiologicalName();
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