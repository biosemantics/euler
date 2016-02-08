package edu.arizona.biosemantics.euler.alignment.client.articulate;

import org.sgx.raphael4gwt.raphael.Circle;
import org.sgx.raphael4gwt.raphael.Paper;
import org.sgx.raphael4gwt.raphael.Raphael;
import org.sgx.raphael4gwt.raphael.event.DDListener;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class EvidenceBasedCreateDialog extends Dialog {

	private Taxon taxonA;
	private Taxon taxonB;
	
	private int minR = 10;
	private int maxR = 110;
	private int defaultR = 60;
	private Circle circle1;
	private Circle circle2;	

	public EvidenceBasedCreateDialog(Taxon taxonA, Taxon taxonB) {
		this.taxonA = taxonA;
		this.taxonB = taxonB;
		
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(createCircles(), new VerticalLayoutData(1, -1));
		vlc.add(createResizeSliders(), new VerticalLayoutData(1, -1));
		this.setWidget(vlc);
		
		this.setWidget(vlc);
	}
	
	private IsWidget createCircles() {
		final SimpleContainer container = new SimpleContainer();
		Paper paper = Raphael.paper(container.getElement(), 370, 300);
		circle1 = paper.circle(150, 130, defaultR);
		circle1.setAttribute("fill", "red");
		circle1.setAttribute("fill-opacity", "0.5");
		circle2 = paper.circle(220, 130, defaultR);
		circle2.setAttribute("fill", "green");
		circle2.setAttribute("fill-opacity", "0.5");
		return container;
	}
	
	private Widget createResizeSliders() {
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		Slider slider1 = new Slider();
	    slider1.setMinValue(minR);
	    slider1.setMaxValue(maxR);
	    slider1.setValue(defaultR);
	    slider1.setIncrement(2);
	    slider1.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				circle1.setAttribute("r", event.getValue());
			}
	    });
	    Slider slider2 = new Slider();
	    slider2.setMinValue(minR);
	    slider2.setMaxValue(maxR);
	    slider2.setValue(defaultR);
	    slider2.setIncrement(2);
	    slider2.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				circle2.setAttribute("r", event.getValue());
			}
	    });
	    hlc.add(slider1, new HorizontalLayoutData(0.5, -1, new Margins(5)));
	    hlc.add(slider2, new HorizontalLayoutData(0.5, -1, new Margins(5)));
		return hlc;
	}
	
	

	public Taxon getTaxonA() {
		return taxonA;
	}

	public Taxon getTaxonB() {
		return taxonB;
	}
	
}
