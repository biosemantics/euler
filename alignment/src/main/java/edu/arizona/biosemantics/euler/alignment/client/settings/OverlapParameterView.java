package edu.arizona.biosemantics.euler.alignment.client.settings;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

public class OverlapParameterView extends SimpleContainer {
	
	public OverlapParameterView() {
		
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		VerticalLayoutContainer vlc1 = new VerticalLayoutContainer();
		VerticalLayoutContainer vlc2 = new VerticalLayoutContainer();
		CirclesOverlapView circlesOverlapView1 = new CirclesOverlapView();		
		CirclesOverlapView circlesOverlapView2 = new CirclesOverlapView();

		final Label label1 = new Label(String.valueOf(circlesOverlapView1.getOverlap()));
		final Label label2 = new Label(String.valueOf(circlesOverlapView2.getOverlap()));
		circlesOverlapView1.setOverlapListener(new CirclesOverlapView.OverlapListener() {
			@Override
			public void onOverlap(double overlap) {
				label1.setText("" + overlap);
			}
		});
		circlesOverlapView2.setOverlapListener(new CirclesOverlapView.OverlapListener() {
			@Override
			public void onOverlap(double overlap) {
				label2.setText("" + overlap);
			}
		});

		vlc1.add(circlesOverlapView1, new VerticalLayoutData(1, -1, new Margins(5)));
		vlc1.add(new FieldLabel(label1, "Minimum Similarity"), new VerticalLayoutData(1, -1, new Margins(25, 5, 5, 5)));
		hlc.add(vlc1, new HorizontalLayoutData(0.5, -1, new Margins(5)));
		
		vlc2.add(circlesOverlapView2, new VerticalLayoutData(1, -1, new Margins(5)));	
		vlc2.add(new FieldLabel(label2, "Minimum Novelty"), new VerticalLayoutData(1, -1, new Margins(25, 5, 5, 5)));
		hlc.add(vlc2, new HorizontalLayoutData(0.5, -1, new Margins(5)));
		this.setWidget(hlc);
	}

}
