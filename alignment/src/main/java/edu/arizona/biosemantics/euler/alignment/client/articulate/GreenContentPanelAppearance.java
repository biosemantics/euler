package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance.ContentPanelResources;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;
import com.sencha.gxt.theme.blue.client.panel.BlueContentPanelAppearance.BlueContentPanelResources;
import com.sencha.gxt.theme.blue.client.panel.BlueHeaderAppearance;

public class GreenContentPanelAppearance extends ContentPanelBaseAppearance {

  public interface GreenContentPanelResources extends ContentPanelResources {

    @Source({"com/sencha/gxt/theme/base/client/panel/ContentPanel.css", "GreenContentPanel.css"})
    @Override
    GreenContentPanelStyle style();

  }

  public interface GreenContentPanelStyle extends ContentPanelStyle {

  }

  public GreenContentPanelAppearance() {
    super(GWT.<GreenContentPanelResources> create(GreenContentPanelResources.class),
        GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  public GreenContentPanelAppearance(GreenContentPanelResources resources) {
    super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new GreenHeaderAppearance();
  }

}