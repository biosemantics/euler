package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

import edu.arizona.biosemantics.euler.alignment.shared.model.RunConfig;

public class RunConfigPanel extends VerticalLayoutContainer  {

	private CheckBox simplifyAggregateCheckBox = new CheckBox();
	private FieldLabel simplifyAggregateFieldLabel = new FieldLabel(simplifyAggregateCheckBox, "Simplify Aggregate View: ");
	
	public RunConfigPanel() {
		//simplifyAggregateCheckBox.setBoxLabel("Simplify Aggregate View");
		simplifyAggregateFieldLabel.setLabelWidth(200);
		this.add(simplifyAggregateFieldLabel);
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
}
