package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.ImageHelper;
import com.sencha.gxt.theme.base.client.tree.TreeBaseAppearance;
import com.sencha.gxt.theme.base.client.tree.TreeBaseAppearance.TreeBaseStyle;
import com.sencha.gxt.theme.base.client.tree.TreeBaseAppearance.TreeResources;
import com.sencha.gxt.theme.blue.client.tree.BlueTreeAppearance.BlueTreeResources;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader.ColumnHeaderAppearance;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader.ColumnHeaderStyles;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;
import com.sencha.gxt.widget.core.client.tree.Tree.Joint;
import com.sencha.gxt.widget.core.client.tree.TreeView.TreeViewRenderMode;


public class TaxonTreeAppearance extends TreeBaseAppearance {

	public interface TaxonTreeResources extends TreeResources, ClientBundle {

		@Source({ "com/sencha/gxt/theme/base/client/tree/Tree.css",
				"TaxonTree.css" })
		TreeBaseStyle style();

	}

	public TaxonTreeAppearance() {
		super((TreeResources) GWT.create(BlueTreeResources.class));
	}

	@Override
	public void renderNode(SafeHtmlBuilder sb, String id, SafeHtml text,
			TreeStyle ts, ImageResource icon, boolean checkable,
			CheckState checked, Joint joint, int level,
			TreeViewRenderMode renderMode) {

		if (renderMode == TreeViewRenderMode.ALL
				|| renderMode == TreeViewRenderMode.BUFFER_WRAP) {
			sb.appendHtmlConstant("<div id=\"" + SafeHtmlUtils.htmlEscape(id)
					+ "\" class=\"" + style.node() + "\">");

			sb.appendHtmlConstant("<div class=\"" + style.element() + "\">");
		}

		if (renderMode == TreeViewRenderMode.ALL
				|| renderMode == TreeViewRenderMode.BUFFER_BODY) {

			sb.appendHtmlConstant(getIndentMarkup(level));

			Element jointElement = null;
			switch (joint) {
			case COLLAPSED:
				jointElement = getImage(ts.getJointCloseIcon() == null ? resources
						.jointCollapsedIcon() : ts.getJointCloseIcon());
				break;
			case EXPANDED:
				jointElement = getImage(ts.getJointOpenIcon() == null ? resources
						.jointExpandedIcon() : ts.getJointOpenIcon());
				break;
			default:
				// empty
			}

			if (jointElement != null) {
				jointElement.addClassName(style.joint());
			}

			sb.appendHtmlConstant(jointElement == null ? "<img src=\""
					+ GXT.getBlankImageUrl()
					+ "\" style=\"width: 16px\" class=\"" + style.joint()
					+ "\" />" : jointElement.getString());
			
			
			// checkable
			if (checkable) {
				Element e = null;
				switch (checked) {
				case CHECKED:
					e = getImage(resources.checked());
					break;
				case UNCHECKED:
					e = getImage(resources.unchecked());
					break;
				case PARTIAL:
					e = getImage(resources.partialChecked());
					break;
				}

				e.addClassName(style.check());
				sb.appendHtmlConstant(e.getString());
			} else {
				sb.appendHtmlConstant("<span class='" + style.check()
						+ "'></span>");
			}
			
			
			if (icon != null) {
				Element e = getImage(icon);
				e.addClassName(style.icon());
				sb.appendHtmlConstant(e.getString());
			} else {
				sb.appendHtmlConstant("<span class=\"" + style.icon()
						+ "\"></span>");
			}
			
			sb.appendHtmlConstant("<span class=\"" + style.text() + "\">"
					+ text.asString() + "</span>"); 
		}

		if (renderMode == TreeViewRenderMode.ALL
				|| renderMode == TreeViewRenderMode.BUFFER_WRAP) {
			sb.appendHtmlConstant("</div>");
			sb.appendHtmlConstant("</div>");
		}

	}

}
