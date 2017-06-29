package edu.arizona.biosemantics.euler.alignment.client.common;

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

import edu.arizona.biosemantics.euler.alignment.shared.model.RunConfig;

public class RunConfigPanel extends ContentPanel  {

	private CheckBox simplifyAggregateCheckBox = new CheckBox();
	private FieldLabel simplifyAggregateFieldLabel = new FieldLabel(simplifyAggregateCheckBox, "Simplify Aggregate View");
	private VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
	
	public RunConfigPanel() {
		setHeading("Configuration");
		//simplifyAggregateCheckBox.setBoxLabel("Simplify Aggregate View");
		simplifyAggregateFieldLabel.setLabelWidth(200);
		verticalLayoutContainer.add(simplifyAggregateFieldLabel);
		this.add(verticalLayoutContainer);
	}
	
	public void reset() {
		simplifyAggregateCheckBox.setValue(false);
	}
	
	public void setRunConfig(RunConfig config) {
		simplifyAggregateCheckBox.setValue(config.isSimplifyAggregateView());
	}
	
	public boolean getSimplifyAggregateChecked() {
		return simplifyAggregateCheckBox.getValue();
	}
	
	public void setEnabled(boolean enabled) {
		this.simplifyAggregateCheckBox.setEnabled(enabled);
	}

	public RunConfig getRunConfig() {
		RunConfig runConfig = new RunConfig();
		runConfig.setSimplifyAggregateView(simplifyAggregateCheckBox.getValue());
		return runConfig;
	}
}
