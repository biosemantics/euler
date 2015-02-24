package edu.arizona.biosemantics.euler.alignment.shared.model;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface RunProperties extends PropertyAccess<Run> {

	@Path("id")
	ModelKeyProvider<Run> key();

	public static class DisplayNameValueProvider implements
			ValueProvider<Run, String> {

		@Override
		public String getValue(Run object) {
			return object.getCreated().toString();
		}

		@Override
		public void setValue(Run object, String value) {
		}

		@Override
		public String getPath() {
			return "displayName";
		}

	}

}