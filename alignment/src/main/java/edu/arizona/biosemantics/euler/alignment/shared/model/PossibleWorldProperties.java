package edu.arizona.biosemantics.euler.alignment.shared.model;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface PossibleWorldProperties extends PropertyAccess<PossibleWorld> {

	  @Path("id")
	  ModelKeyProvider<PossibleWorld> key();
	   
	  @Path("displayName")
	  LabelProvider<PossibleWorld> nameLabel();
	 
	  ValueProvider<PossibleWorld, String> url();
	
	  ValueProvider<PossibleWorld, String> displayName();
}