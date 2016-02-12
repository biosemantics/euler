package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;

public interface StateNodeProperties extends PropertyAccess<StateNode> {

		@Path("id")
		ModelKeyProvider<Node> id();
		
		@Path("name")
		LabelProvider<Node> label();
		
		ValueProvider<Node, String> name();
		
		ValueProvider<Node, CharacterState> characterState();
	}
	
	