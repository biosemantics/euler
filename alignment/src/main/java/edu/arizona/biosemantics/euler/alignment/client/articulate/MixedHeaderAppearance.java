package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;

public class MixedHeaderAppearance extends HeaderDefaultAppearance {

  public interface MixedHeaderStyle extends HeaderStyle {
    String header();

    String headerIcon();

    String headerHasIcon();

    String headerText();

    String headerBar();
  }

  public interface MixedHeaderResources extends HeaderResources {

    @Source({"com/sencha/gxt/theme/base/client/widget/Header.css", "MixedHeader.css"})
    MixedHeaderStyle style();
    
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource mixedbackground();
  }
  

  public MixedHeaderAppearance() {
    this(GWT.<MixedHeaderResources> create(MixedHeaderResources.class),
        GWT.<Template> create(Template.class));
  }

  public MixedHeaderAppearance(HeaderResources resources, Template template) {
    super(resources, template);
  }

}