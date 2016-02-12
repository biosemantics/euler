package edu.arizona.biosemantics.euler.alignment.client.articulate;

import org.sgx.raphael4gwt.raphael.Circle;
import org.sgx.raphael4gwt.raphael.Paper;
import org.sgx.raphael4gwt.raphael.Raphael;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;

import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterOverlap;

public class TaxonCirclesView extends SimpleContainer {
	
	private final int paperWidth = 500;
	private final int paperHeight = 200;
	private final int defaultR = 100;
	
	private EventBus eventBus;
	private Collection collection;
	private Taxon taxonA;
	private Taxon taxonB;
	
	private Circle taxonACircle;
	private Circle taxonBCircle;

	public TaxonCirclesView(EventBus eventBus, Collection collection, Taxon taxonA, Taxon taxonB) {
		this.eventBus = eventBus;
		this.collection = collection;
		this.taxonA = taxonA;
		this.taxonB = taxonB;
		
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		final SimpleContainer container = new SimpleContainer();
		Paper paper = Raphael.paper(container.getElement(), paperWidth, paperHeight);
		taxonACircle = paper.circle(140, 100, defaultR);
		taxonACircle.setAttribute("fill", "red");
		taxonACircle.setAttribute("fill-opacity", "0.5");
		taxonACircle.setAttribute("r", 100);
		taxonBCircle = paper.circle(230, 100, defaultR);
		taxonBCircle.setAttribute("fill", "green");
		taxonBCircle.setAttribute("fill-opacity", "0.5");
		taxonBCircle.setAttribute("r", 100);
		
		/*text1 = paper.text(90, 130, "0");
		text1.setAttribute("font", "20px Arial");
		text1.click(new MouseEventListener() {
			@Override
			public void notifyMouseEvent(NativeEvent e) {
				Dialog dialog = new Dialog();
				String text = "";
				for(CharacterState characterState : characterOverlap.getCharacterStatesA()) {
					text += characterState.toString() + "</br>";
				}
				HTML html = new HTML(text, true); 
				dialog.setWidget(html);
				dialog.setHideOnButtonClick(true);
				dialog.show();
			}	
		});
		text2 = paper.text(180, 130, "0");
		text2.setAttribute("font", "20px Arial");
		text2.click(new MouseEventListener() {
			@Override
			public void notifyMouseEvent(NativeEvent e) {
				Dialog dialog = new Dialog();
				String text = "";
				for(CharacterState characterStateA : characterOverlap.getEqualCharacters().keySet()) {
					for(CharacterState characterStateB : characterOverlap.getEqualCharacters().get(characterStateA).keySet()) {
						text += characterStateA.toString() + "==" + characterStateB.toString() + "</br>";
					}
				}
				HTML html = new HTML(text, true); 
				dialog.setWidget(html);
				dialog.setHideOnButtonClick(true);
				dialog.show();
			}	
		});
		text3 = paper.text(280, 130, "0");
		text3.setAttribute("font", "20px Arial");
		text3.click(new MouseEventListener() {
			@Override
			public void notifyMouseEvent(NativeEvent e) {
				Dialog dialog = new Dialog();
				String text = "";
				for(CharacterState characterState : characterOverlap.getCharacterStatesB()) {
					text += characterState.toString() + "</br>";
				}

				HTML html = new HTML(text, true); 
				dialog.setWidget(html);
				dialog.setHideOnButtonClick(true);
				dialog.show();
			}	
		});*/

		hlc.add(new Label(), new HorizontalLayoutData(0.5, -1));
		hlc.add(container, new HorizontalLayoutData(-1, -1));
		hlc.add(new Label(), new HorizontalLayoutData(0.5, -1));
			      
		this.setWidget(hlc);
	}
	
	protected void update(CharacterOverlap characterOverlap) {
		int targetAbsoluteOverlapSize = characterOverlap.getOverlap().size();
		int targetAbsoluteCircleSize1 = characterOverlap.getCharacterStatesA().size() + targetAbsoluteOverlapSize;
		int targetAbsoluteCircleSize2 = characterOverlap.getCharacterStatesB().size() + targetAbsoluteOverlapSize;
		
		double resizedAbsoluteCircleSize1 = 
				((double)targetAbsoluteCircleSize1 / (targetAbsoluteCircleSize1 + targetAbsoluteCircleSize2) * getAreaForRadius(defaultR));
		double resizedAbsoluteCircleSize2 = 
				((double)targetAbsoluteCircleSize2 / (targetAbsoluteCircleSize1 + targetAbsoluteCircleSize2) * getAreaForRadius(defaultR));
		double resizedAbsoluteOverlapSize = 
				((double)targetAbsoluteOverlapSize / (targetAbsoluteCircleSize1 + targetAbsoluteCircleSize2) * getAreaForRadius(defaultR));
		int radiusCircle1 = (int)Math.ceil(this.getRadiusForArea(resizedAbsoluteCircleSize1));
		int radiusCircle2 = (int)Math.ceil(this.getRadiusForArea(resizedAbsoluteCircleSize2));
		
		int circleDistance = getDistance(resizedAbsoluteOverlapSize, resizedAbsoluteCircleSize1, resizedAbsoluteCircleSize2);

		int diagramWidth = 0;
		if(circleDistance == 0)
			diagramWidth = radiusCircle1 < radiusCircle2 ? 2 * radiusCircle2 : radiusCircle1 * 2;
		else if(circleDistance >= radiusCircle1 + radiusCircle2) {
			diagramWidth = radiusCircle1 + radiusCircle2 + circleDistance;
		} else {
			diagramWidth = radiusCircle1 < radiusCircle2 ? radiusCircle2 + circleDistance + radiusCircle1 - (radiusCircle2 - circleDistance) :
				radiusCircle1 + circleDistance + radiusCircle2 - (radiusCircle1 - circleDistance);
		}
		
		taxonACircle.setAttribute("cx", (paperWidth - diagramWidth) / 2);
		taxonACircle.setAttribute("cy", 100);
		taxonACircle.setAttribute("r", radiusCircle1);
		taxonBCircle.setAttribute("cx", ((paperWidth - diagramWidth) / 2) + circleDistance);
		taxonBCircle.setAttribute("cy", 100);
		taxonBCircle.setAttribute("r", radiusCircle2);
	}
	
	private double getRadiusForArea(double area) {
		return Math.sqrt(area / Math.PI); 
	}
	
	private double getAreaForRadius(double radius) {
		return Math.PI * Math.pow(radius, 2);
	}

	private int getDistance(double absoluteOverlap, double absoluteCircleSize1, double absoluteCircleSize2) {
		int radiusCircle1 = (int)Math.ceil(this.getRadiusForArea(absoluteCircleSize1));
		int radiusCircle2 = (int)Math.ceil(this.getRadiusForArea(absoluteCircleSize2));
		
		int closestDistance = -1;
		double closestOverlapDistance = Integer.MAX_VALUE;
		for(int d=0; d <= (radiusCircle1 + radiusCircle2 + 1); d++) {
			System.out.println(d);
			int x1 = 0;
			int y1 = 0;
			int r1 = radiusCircle1;
			int x2 = d;
			int y2 = 0;
			int r2 = radiusCircle2;
			double calculatedOverlap = calculateOverlap(x1, y1, r1, x2, y2, r2); //absolute

			System.out.println("overlap " + calculatedOverlap);
			System.out.println("asked overlap " + absoluteOverlap);
			
			if(Math.abs(calculatedOverlap - absoluteOverlap) <= closestOverlapDistance) {
				closestDistance = d;
				closestOverlapDistance = Math.abs(calculatedOverlap - absoluteOverlap);
			} else {
				break;
			}
		}
		return closestDistance;
	}
	
	private double calculateOverlap(int x1, int y1, int r1, int x2, int y2, int r2) {
		Double circleArea1 = Math.PI * Math.pow(r1, 2);
		Double circleArea2 = Math.PI * Math.pow(r2, 2);
		double d = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y1 - y2, 2));
		if(r1+r2 <= d) 
			return 0;
		if(d < Math.abs(r1-r2) || d == 0)
			return circleArea1 < circleArea2 ? circleArea1 : circleArea2;
		int r = r1;
		int R = r2;
		if(R < r){
		    // swap
		    r = r2;
		    R = r1;
		}
		Double part1 = r*r*Math.acos((d*d + r*r - R*R)/(2*d*r));
		Double part2 = R*R*Math.acos((d*d + R*R - r*r)/(2*d*R));
		Double part3 = 0.5*Math.sqrt((-d+r+R)*(d+r-R)*(d-r+R)*(d+r+R));

		Double intersectionArea = part1 + part2 - part3;
		return intersectionArea;
	}
}
