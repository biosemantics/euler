package edu.arizona.biosemantics.euler.alignment.client;

import org.sgx.raphael4gwt.raphael.Circle;
import org.sgx.raphael4gwt.raphael.Paper;
import org.sgx.raphael4gwt.raphael.Raphael;
import org.sgx.raphael4gwt.raphael.base.Attrs;
import org.sgx.raphael4gwt.raphael.event.DDListener;
import org.sgx.raphael4gwt.raphael.util.Util;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawComponent;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.Scaling;
import com.sencha.gxt.chart.client.draw.sprite.CircleSprite;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentServiceAsync;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class EulerAlignment implements EntryPoint {

	private String test = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><!-- Generated by graphviz version 2.36.0 (20140111.2315) --><!-- Title: %3 Pages: 1 --><svg width=\"350pt\" height=\"186pt\" viewBox=\"0.00 0.00 350.00 186.00\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"><g id=\"graph0\" class=\"graph\" transform=\"scale(1 1) rotate(0) translate(4 182)\"><title>%3</title><polygon fill=\"white\" stroke=\"none\" points=\"-4,4 -4,-182 346,-182 346,4 -4,4\"/><!-- 1.e --><g id=\"node1\" class=\"node\"><title>1.e</title><polygon fill=\"#ccffcc\" stroke=\"black\" points=\"144,-107 90,-107 90,-71 144,-71 144,-107\"/><text text-anchor=\"middle\" x=\"117\" y=\"-84.8\" font-family=\"Times,serif\" font-size=\"14.00\">1.e</text></g><!-- 1.a\n2.c --><g id=\"node5\" class=\"node\"><title>1.a\n2.c</title><path fill=\"#eeeeee\" stroke=\"black\" d=\"M42,-134C42,-134 12,-134 12,-134 6,-134 0,-128 0,-122 0,-122 0,-110 0,-110 0,-104 6,-98 12,-98 12,-98 42,-98 42,-98 48,-98 54,-104 54,-110 54,-110 54,-122 54,-122 54,-128 48,-134 42,-134\"/><text text-anchor=\"middle\" x=\"27\" y=\"-118.8\" font-family=\"Times,serif\" font-size=\"14.00\">1.a</text><text text-anchor=\"middle\" x=\"27\" y=\"-104.8\" font-family=\"Times,serif\" font-size=\"14.00\">2.c</text></g><!-- 1.e&#45;&gt;1.a\n2.c --><g id=\"edge2\" class=\"edge\"><title>1.e&#45;&gt;1.a\n2.c</title><path fill=\"none\" stroke=\"#000000\" d=\"M89.5971,-97.1009C81.519,-99.5794 72.4933,-102.349 63.8954,-104.987\"/><polygon fill=\"#000000\" stroke=\"#000000\" points=\"62.6145,-101.719 54.081,-107.998 64.6678,-108.411 62.6145,-101.719\"/></g><!-- 1.b --><g id=\"node2\" class=\"node\"><title>1.b</title><polygon fill=\"#ccffcc\" stroke=\"black\" points=\"234,-161 180,-161 180,-125 234,-125 234,-161\"/><text text-anchor=\"middle\" x=\"207\" y=\"-138.8\" font-family=\"Times,serif\" font-size=\"14.00\">1.b</text></g><!-- 2.d --><g id=\"node4\" class=\"node\"><title>2.d</title><polygon fill=\"#ffffcc\" stroke=\"black\" points=\"144,-135.544 144,-150.456 128.184,-161 105.816,-161 90,-150.456 90,-135.544 105.816,-125 128.184,-125 144,-135.544\"/><text text-anchor=\"middle\" x=\"117\" y=\"-138.8\" font-family=\"Times,serif\" font-size=\"14.00\">2.d</text></g><!-- 1.b&#45;&gt;2.d --><g id=\"edge3\" class=\"edge\"><title>1.b&#45;&gt;2.d</title><path fill=\"none\" stroke=\"#ff0000\" d=\"M179.597,-143C171.607,-143 162.689,-143 154.176,-143\"/><polygon fill=\"#ff0000\" stroke=\"#ff0000\" points=\"154.081,-139.5 144.081,-143 154.081,-146.5 154.081,-139.5\"/></g><!-- 2.f --><g id=\"node3\" class=\"node\"><title>2.f</title><polygon fill=\"#ffffcc\" stroke=\"black\" points=\"234,-81.5442 234,-96.4558 218.184,-107 195.816,-107 180,-96.4558 180,-81.5442 195.816,-71 218.184,-71 234,-81.5442\"/><text text-anchor=\"middle\" x=\"207\" y=\"-84.8\" font-family=\"Times,serif\" font-size=\"14.00\">2.f</text></g><!-- 2.f&#45;&gt;1.e --><g id=\"edge4\" class=\"edge\"><title>2.f&#45;&gt;1.e</title><path fill=\"none\" stroke=\"#ff0000\" d=\"M179.597,-89C171.607,-89 162.689,-89 154.176,-89\"/><polygon fill=\"#ff0000\" stroke=\"#ff0000\" points=\"154.081,-85.5001 144.081,-89 154.081,-92.5001 154.081,-85.5001\"/></g><!-- 2.d&#45;&gt;1.a\n2.c --><g id=\"edge1\" class=\"edge\"><title>2.d&#45;&gt;1.a\n2.c</title><path fill=\"none\" stroke=\"#000000\" d=\"M90.0731,-135.045C81.8218,-132.514 72.5442,-129.667 63.7266,-126.962\"/><polygon fill=\"#000000\" stroke=\"#000000\" points=\"64.6325,-123.579 54.0457,-123.991 62.5792,-130.271 64.6325,-123.579\"/></g><!-- Legend --><g id=\"node6\" class=\"node\"><title>Legend</title><path fill=\"white\" stroke=\"black\" d=\"M330,-178.5C330,-178.5 282,-178.5 282,-178.5 276,-178.5 270,-172.5 270,-166.5 270,-166.5 270,-11.5 270,-11.5 270,-5.5 276,0.5 282,0.5 282,0.5 330,0.5 330,0.5 336,0.5 342,-5.5 342,-11.5 342,-11.5 342,-166.5 342,-166.5 342,-172.5 336,-178.5 330,-178.5\"/><polygon fill=\"none\" stroke=\"black\" points=\"270,-148 270,-178 342,-178 342,-148 270,-148\"/><text text-anchor=\"start\" x=\"279.552\" y=\"-161.671\" font-family=\"Arial Black\" font-size=\"14.00\"> Nodes</text><polygon fill=\"#ccffcc\" stroke=\"none\" points=\"270,-124 270,-148 325,-148 325,-124 270,-124\"/><polygon fill=\"none\" stroke=\"black\" points=\"270,-124 270,-148 325,-148 325,-124 270,-124\"/><text text-anchor=\"start\" x=\"294\" y=\"-131.8\" font-family=\"Times,serif\" font-size=\"14.00\">1</text><polygon fill=\"none\" stroke=\"black\" points=\"325,-124 325,-148 342,-148 342,-124 325,-124\"/><text text-anchor=\"start\" x=\"330\" y=\"-131.8\" font-family=\"Times,serif\" font-size=\"14.00\">2</text><polygon fill=\"#ffffcc\" stroke=\"none\" points=\"270,-100 270,-124 325,-124 325,-100 270,-100\"/><polygon fill=\"none\" stroke=\"black\" points=\"270,-100 270,-124 325,-124 325,-100 270,-100\"/><text text-anchor=\"start\" x=\"294\" y=\"-107.8\" font-family=\"Times,serif\" font-size=\"14.00\">2</text><polygon fill=\"none\" stroke=\"black\" points=\"325,-100 325,-124 342,-124 342,-100 325,-100\"/><text text-anchor=\"start\" x=\"330\" y=\"-107.8\" font-family=\"Times,serif\" font-size=\"14.00\">2</text><polygon fill=\"#eeeeee\" stroke=\"none\" points=\"270,-76 270,-100 325,-100 325,-76 270,-76\"/><polygon fill=\"none\" stroke=\"black\" points=\"270,-76 270,-100 325,-100 325,-76 270,-76\"/><text text-anchor=\"start\" x=\"281.948\" y=\"-83.8\" font-family=\"Times,serif\" font-size=\"14.00\">comb</text><polygon fill=\"none\" stroke=\"black\" points=\"325,-76 325,-100 342,-100 342,-76 325,-76\"/><text text-anchor=\"start\" x=\"330\" y=\"-83.8\" font-family=\"Times,serif\" font-size=\"14.00\">1</text><polygon fill=\"none\" stroke=\"black\" points=\"270,-47 270,-76 342,-76 342,-47 270,-47\"/><text text-anchor=\"start\" x=\"277.993\" y=\"-59.8\" font-family=\"Arial Black\" font-size=\"14.00\"> Edges </text><polygon fill=\"none\" stroke=\"black\" points=\"270,-23 270,-47 325,-47 325,-23 270,-23\"/><text text-anchor=\"start\" x=\"283.11\" y=\"-30.8\" font-family=\"Times,serif\" font-size=\"14.00\" fill=\"#000000\">input</text><polygon fill=\"none\" stroke=\"black\" points=\"325,-23 325,-47 342,-47 342,-23 325,-23\"/><text text-anchor=\"start\" x=\"330\" y=\"-30.8\" font-family=\"Times,serif\" font-size=\"14.00\">2</text><polygon fill=\"none\" stroke=\"black\" points=\"270,1 270,-23 325,-23 325,1 270,1\"/><text text-anchor=\"start\" x=\"275.348\" y=\"-6.8\" font-family=\"Times,serif\" font-size=\"14.00\" fill=\"#ff0000\">inferred</text><polygon fill=\"none\" stroke=\"black\" points=\"325,1 325,-23 342,-23 342,1 325,1\"/><text text-anchor=\"start\" x=\"330\" y=\"-6.8\" font-family=\"Times,serif\" font-size=\"14.00\">2</text></g></g></svg>";
		
	@Override
	public void onModuleLoad() {	
		//System.out.println(calculateOverlap(0, 0, 100, 0, 0, 100));
				
		//OMSVGElement elem = org.vectomatic.dom.svg.utils.OMSVGParser.parse(test); 
		
		/*OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		svg.setWidth(OMSVGLength.SVG_LENGTHTYPE_PX, 300);
		svg.setHeight(OMSVGLength.SVG_LENGTHTYPE_PX, 300);
		OMSVGCircleElement circle = doc.createSVGCircleElement(150, 150, 100);
		circle.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "blue");
		svg.appendChild(circle);*/
		
		//RootPanel.get().getElement().appendChild(elem.getElement());	      
		
		final EulerAlignmentView view = new EulerAlignmentView();

		// simulate etc site 
		DockLayoutPanel dock = new DockLayoutPanel(Unit.EM);
		dock.addNorth(new HTML("header"), 2);
		HTML footer = new HTML("footer");
		dock.addSouth(footer, 2);
		dock.add(view.asWidget());
		RootLayoutPanel.get().add(dock);
		
		int id = 22;
		String secret = "95";
		IEulerAlignmentServiceAsync eulerAlignmentService = GWT.create(IEulerAlignmentService.class);
		// TaxonMatrix taxonMatrix = createSampleMatrix();
		eulerAlignmentService.getCollection(id, secret, new AsyncCallback<Collection>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(final Collection result) {
				view.setCollection(result);
			}
		}); 
		
		
		
		
		
		//RootLayoutPanel.get().add(new CirclesOverlapView());
	}


}
