package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance.ContentPanelResources;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;
import com.sencha.gxt.theme.blue.client.panel.BlueContentPanelAppearance.BlueContentPanelResources;
import com.sencha.gxt.theme.blue.client.panel.BlueHeaderAppearance;

public class MixedContentPanelAppearance extends ContentPanelBaseAppearance {

  public interface MixedContentPanelResources extends ContentPanelResources {

    @Source({"com/sencha/gxt/theme/base/client/panel/ContentPanel.css", "MixedContentPanel.css"})
    @Override
    MixedContentPanelStyle style();

  }

  public interface MixedContentPanelStyle extends ContentPanelStyle {

  }

  public MixedContentPanelAppearance() {
    super(GWT.<MixedContentPanelResources> create(MixedContentPanelResources.class),
        GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  public MixedContentPanelAppearance(MixedContentPanelResources resources) {
    super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new MixedHeaderAppearance();
  }

}