package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance.ContentPanelResources;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;
import com.sencha.gxt.theme.blue.client.panel.BlueContentPanelAppearance.BlueContentPanelResources;
import com.sencha.gxt.theme.blue.client.panel.BlueHeaderAppearance;

public class RedContentPanelAppearance extends ContentPanelBaseAppearance {

  public interface RedContentPanelResources extends ContentPanelResources {

    @Source({"com/sencha/gxt/theme/base/client/panel/ContentPanel.css", "RedContentPanel.css"})
    @Override
    RedContentPanelStyle style();

  }

  public interface RedContentPanelStyle extends ContentPanelStyle {

  }

  public RedContentPanelAppearance() {
    super(GWT.<RedContentPanelResources> create(RedContentPanelResources.class),
        GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  public RedContentPanelAppearance(RedContentPanelResources resources) {
    super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new RedHeaderAppearance();
  }

}