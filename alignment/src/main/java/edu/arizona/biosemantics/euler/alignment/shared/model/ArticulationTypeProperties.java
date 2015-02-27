package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ArticulationTypeProperties extends PropertyAccess<ArticulationType> {

	   
	  @Path("displayName")
	  LabelProvider<ArticulationType> nameLabel();
	
	  ValueProvider<ArticulationType, String> displayName();
}
