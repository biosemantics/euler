package edu.arizona.biosemantics.euler.alignment.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

public class InstructionsText extends SimpleContainer {

	public interface CodeSnippetHtml extends XTemplates {
		@XTemplate(source = "InstructionsText.html")
		SafeHtml getTemplate();
	}

	public InstructionsText() {
		CodeSnippetHtml html = GWT.create(CodeSnippetHtml.class);
		HtmlLayoutContainer c = new HtmlLayoutContainer(html.getTemplate());
		FlowLayoutContainer flowLayoutContainer = new FlowLayoutContainer();
		flowLayoutContainer.setScrollMode(ScrollMode.AUTO);
		flowLayoutContainer.add(c);
		this.add(flowLayoutContainer);
	}

}
