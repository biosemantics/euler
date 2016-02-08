package edu.arizona.biosemantics.euler.alignment.client.settings;

import org.sgx.raphael4gwt.raphael.Circle;
import org.sgx.raphael4gwt.raphael.Paper;
import org.sgx.raphael4gwt.raphael.Raphael;
import org.sgx.raphael4gwt.raphael.Shape;
import org.sgx.raphael4gwt.raphael.event.DDListener;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import edu.arizona.biosemantics.euler.alignment.client.settings.ResizeCircleOverlap.OverlapListener;

public class ResizeCircleOverlap extends SimpleContainer {

	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int minR = 2;
	private int maxR = 200;
	private int defaultR = 100;
	private String color1;
	private String color2;
	private Slider slider1;
	private Slider slider2;
	private Shape circle1;
	private Shape circle2;

	public ResizeCircleOverlap(int x1, int y1, int x2, int y2, String color1,
			String color2, final OverlapListener overlapListener) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color1 = color1;
		this.color2 = color2;
		
		SimpleContainer paperContainer = new SimpleContainer();
	    createPaper(paperContainer);
		
		slider1 = new Slider();
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
	    
		slider2 = new Slider();
	    slider2.setMinValue(minR);
	    slider2.setMaxValue(maxR);
	    slider2.setValue(defaultR);
	    slider2.setIncrement(2);
	    slider2.setMessage("{0} inches tall");
	    slider2.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				circle2.setAttribute("r", event.getValue());
			}
	    });
	    
	    circle1.drag(new DDListener() {				
			@Override
			public void onStart(int x, int y, NativeEvent e) {	}				
			@Override
			public void onMove(int dx, int dy, int x, int y, NativeEvent e) {				
				int paperAbsX = ResizeCircleOverlap.this.getAbsoluteLeft();
				int paperAbsY = ResizeCircleOverlap.this.getAbsoluteTop();
				int newX = x - paperAbsX;
				int newY = y - paperAbsY;
				circle1.setAttribute("cx", newX);
				circle1.setAttribute("cy", newY);
				
				double x1 = Double.valueOf(circle1.getAttribute("cx"));
				double y1 = Double.valueOf(circle1.getAttribute("cy"));
				double r1 = Double.valueOf(circle1.getAttribute("r"));
				double x2 = Double.valueOf(circle2.getAttribute("cx"));
				double y2 = Double.valueOf(circle2.getAttribute("cy"));
				double r2 = Double.valueOf(circle2.getAttribute("r"));
				overlapListener.onOverlap(calculateOverlap(x1, y1, r1, x2, y2, r2));
			}
			@Override
			public void onEnd(NativeEvent e) {	}
		});
	    
	    circle2.drag(new DDListener() {
			@Override
			public void onMove(int dx, int dy, int x, int y, NativeEvent e) {
				int paperAbsX = ResizeCircleOverlap.this.getAbsoluteLeft();
				int paperAbsY = ResizeCircleOverlap.this.getAbsoluteTop();
				int newX = x - paperAbsX;
				int newY = y - paperAbsY;
				circle2.setAttribute("cx", newX);
				circle2.setAttribute("cy", newY);
				
				double x1 = Double.valueOf(circle1.getAttribute("cx"));
				double y1 = Double.valueOf(circle1.getAttribute("cy"));
				double r1 = Double.valueOf(circle1.getAttribute("r"));
				double x2 = Double.valueOf(circle2.getAttribute("cx"));
				double y2 = Double.valueOf(circle2.getAttribute("cy"));
				double r2 = Double.valueOf(circle2.getAttribute("r"));
				overlapListener.onOverlap(calculateOverlap(x1, y1, r1, x2, y2, r2));
			}
			@Override
			public void onStart(int x, int y, NativeEvent e) {	}
			@Override
			public void onEnd(NativeEvent e) {	}
		});
	    
	    VerticalLayoutContainer vlc = new VerticalLayoutContainer();
	    vlc.add(paperContainer, new VerticalLayoutData(1, -1));
	    vlc.add(slider1, new VerticalLayoutData(1, -1));
	    vlc.add(slider2, new VerticalLayoutData(1, -1));
	    this.setWidget(vlc);
	}

	protected double calculateOverlap(double x1, double y1, double r1, double x2, double y2, double r2) {
		Double circleArea1 = Math.PI * Math.pow(r1, 2);
		Double circleArea2 = Math.PI * Math.pow(r2, 2);
		double d = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y1 - y2, 2));
		if(r1+r2 <= d) 
			return 0;
		if(d < Math.abs(r1-r2))
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

	private Paper createPaper(SimpleContainer paperContainer) {
		Paper paper = Raphael.paper(paperContainer.getElement());
		circle1 = paper.circle(x1, y1, defaultR);
		circle1.setAttribute("fill", color1);
		circle1.setAttribute("fill-opacity", "0.5");
		circle2 = paper.circle(x2, y2, defaultR);
		circle2.setAttribute("fill", color2);
		circle2.setAttribute("fill-opacity", "0.5");
		return paper;
	}

	public static interface OverlapListener {

		public void onOverlap(double overlap);

	}

}
