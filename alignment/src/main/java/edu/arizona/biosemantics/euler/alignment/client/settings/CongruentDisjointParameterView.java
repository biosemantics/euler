package edu.arizona.biosemantics.euler.alignment.client.settings;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

public class CongruentDisjointParameterView extends SimpleContainer {
	
	public CongruentDisjointParameterView() {
		
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

		//similarity = overlap/((area1 + area2) / 2)
		//novelty = ((area1 + area2)/2)/overlap
		//novelty1 = area1 / overlap = 1/similarity1
		//novelty2 = area2 / overlap = 1/similarity2
		vlc1.add(circlesOverlapView1, new VerticalLayoutData(1, -1, new Margins(5)));
		vlc1.add(new FieldLabel(label1, "Minimum Similarity for Congruence (to separate from included)"), new VerticalLayoutData(1, -1, new Margins(25, 5, 5, 5)));
		hlc.add(vlc1, new HorizontalLayoutData(0.5, -1, new Margins(5)));
		
		vlc2.add(circlesOverlapView2, new VerticalLayoutData(1, -1, new Margins(5)));	
		vlc2.add(new FieldLabel(label2, "Maximum Similarity for Disjointness (to separate from overlap)"), new VerticalLayoutData(1, -1, new Margins(25, 5, 5, 5)));
		hlc.add(vlc2, new HorizontalLayoutData(0.5, -1, new Margins(5)));
		this.setWidget(hlc);
	}
}
