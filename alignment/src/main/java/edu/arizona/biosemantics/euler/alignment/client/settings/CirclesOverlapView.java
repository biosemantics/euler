package edu.arizona.biosemantics.euler.alignment.client.settings;

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
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import edu.arizona.biosemantics.euler.alignment.client.settings.CirclesOverlapView.OverlapListener;

public class CirclesOverlapView extends SimpleContainer {
	
	private int minR = 10;
	private int maxR = 110;
	private int defaultR = 60;
	private Circle circle1;
	private Circle circle2;	
	private OverlapListener overlapListener;
	
	public static interface OverlapListener {
		void onOverlap(double overlap);
	}
	
	public CirclesOverlapView() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(createCircles(), new VerticalLayoutData(1, -1));
		vlc.add(createResizeSliders(), new VerticalLayoutData(1, -1));
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
		
	    circle1.drag(new DDListener() {				
			@Override
			public void onStart(int x, int y, NativeEvent e) {	}				
			@Override
			public void onMove(int dx, int dy, int x, int y, NativeEvent e) {				
				circle1.setAttribute("cx", x - container.getAbsoluteLeft());
				circle1.setAttribute("cy", y - container.getAbsoluteTop());
				
				notifyOverlapListener();
			}
			@Override
			public void onEnd(NativeEvent e) {	}
		});
	    
	    circle2.drag(new DDListener() {
			@Override
			public void onMove(int dx, int dy, int x, int y, NativeEvent e) {
				circle2.setAttribute("cx", x - container.getAbsoluteLeft());
				circle2.setAttribute("cy", y - container.getAbsoluteTop());
				
				notifyOverlapListener();
			}
			@Override
			public void onStart(int x, int y, NativeEvent e) {	}
			@Override
			public void onEnd(NativeEvent e) {	}
		});
		return container;
	}

	protected void notifyOverlapListener() {
		if(overlapListener != null) {
			overlapListener.onOverlap(calculateOverlap());
		}
	}

	protected double calculateOverlap() {
		double x1 = Double.valueOf(circle1.getAttribute("cx"));
		double y1 = Double.valueOf(circle1.getAttribute("cy"));
		double r1 = Double.valueOf(circle1.getAttribute("r"));
		double x2 = Double.valueOf(circle2.getAttribute("cx"));
		double y2 = Double.valueOf(circle2.getAttribute("cy"));
		double r2 = Double.valueOf(circle2.getAttribute("r"));
		
		Double circleArea1 = Math.PI * Math.pow(r1, 2);
		Double circleArea2 = Math.PI * Math.pow(r2, 2);
		double d = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y1 - y2, 2));
		if(r1+r2 <= d) 
			return 0;
		if(d < Math.abs(r1-r2) || d == 0)
			return 1.0;
		Double r = r1;
		Double R = r2;
		if(R < r){
		    // swap
		    r = r2;
		    R = r1;
		}
		Double part1 = r*r*Math.acos((d*d + r*r - R*R)/(2*d*r));
		Double part2 = R*R*Math.acos((d*d + R*R - r*r)/(2*d*R));
		Double part3 = 0.5*Math.sqrt((-d+r+R)*(d+r-R)*(d-r+R)*(d+r+R));

		Double intersectionArea = part1 + part2 - part3;
		Double smallCircleArea = circleArea1 < circleArea2 ? circleArea1 : circleArea2;
		return (intersectionArea / smallCircleArea);
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
				notifyOverlapListener();
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
				notifyOverlapListener();
			}
	    });
	    hlc.add(slider1, new HorizontalLayoutData(0.5, -1, new Margins(5)));
	    hlc.add(slider2, new HorizontalLayoutData(0.5, -1, new Margins(5)));
		return hlc;
	}

	public void setOverlapListener(OverlapListener overlapListener) {
		this.overlapListener = overlapListener;
	}

	public double getOverlap() {
		return this.calculateOverlap();
	}
	
}
