package edu.arizona.biosemantics.euler.alignment.shared.model;

import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface RunProperties extends PropertyAccess<Run> {

	@Path("id")
	ModelKeyProvider<Run> key();

	public static class DisplayNameValueProvider implements
			ValueProvider<Run, String> {
		private DateTimeFormat format = DateTimeFormat
				.getFormat("MM/dd/YYYY HH:mm:ss");

		@Override
		public String getValue(Run run) {
			String runName = format.format(run.getCreated());
			if (run.hasOutput()) {
				runName += " " + run.getOutput().getType().toString();
				switch (run.getOutput().getType()) {
				case CONFLICT:
					break;
				case MULTIPLE:
					runName += " ("
							+ run.getOutput().getPossibleWorlds().size()
							+ "PWs )";
					break;
				case ONE:
					break;
				default:
					break;
				}

			}
			return runName;
		}

		@Override
		public void setValue(Run object, String value) {
		}

		@Override
		public String getPath() {
			return "displayName";
		}

	}
	

	public static class RunOutputTypeValueProvider implements ValueProvider<Run, RunOutputType> {
		@Override
		public RunOutputType getValue(Run run) {
			if(!run.hasOutput())
				return null;
			return run.getOutput().getType();
		}

		@Override
		public void setValue(Run object, RunOutputType value) {
		}

		@Override
		public String getPath() {
			return "runOutputType";
		}
	}


	public static class ResultValueProvider implements ValueProvider<Run, String> {
		@Override
		public String getValue(Run run) {
			if(!run.hasOutput())
				return "";
			return run.getOutput().getType().toString();
		}

		@Override
		public void setValue(Run object, String value) {
		}

		@Override
		public String getPath() {
			return "result";
		}
	}

}