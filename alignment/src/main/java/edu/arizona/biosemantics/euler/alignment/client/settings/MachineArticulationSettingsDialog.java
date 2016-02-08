package edu.arizona.biosemantics.euler.alignment.client.settings;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;

public class MachineArticulationSettingsDialog extends Dialog {

	private ContentPanel panel;

	public MachineArticulationSettingsDialog() {
		final TextButton congruentDisjointButton = new TextButton("== / !");
		final TextButton includedButton = new TextButton(">");
		final TextButton overlapButton = new TextButton("><");

		final SimpleContainer congruentDisjointCard = new CongruentDisjointParameterView();
		final SimpleContainer includedCard = new IncludedParameterView();
		final SimpleContainer overlapCard = new OverlapParameterView(); 
		
		final CardLayoutContainer cardLayout = new CardLayoutContainer();
		cardLayout.add(congruentDisjointCard);
		cardLayout.add(includedCard);
		cardLayout.add(overlapCard);
		cardLayout.setActiveWidget(congruentDisjointCard);

		panel = new ContentPanel();
		panel.add(cardLayout, new MarginData(20));
		panel.addButton(new LabelToolItem("Switch Cards:"));
		panel.addButton(congruentDisjointButton);
		panel.addButton(includedButton);
		panel.addButton(overlapButton);

		congruentDisjointButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				cardLayout.setActiveWidget(congruentDisjointCard);
			}
		});
		includedButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				cardLayout.setActiveWidget(includedCard);
			}
		});
		overlapButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				cardLayout.setActiveWidget(overlapCard);
			}
		});
		this.setWidget(panel);
		this.setHideOnButtonClick(true);
		this.setWidth(800);
		this.setHeight(500);
	}
}
