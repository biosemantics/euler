package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;

public class GreenHeaderAppearance extends HeaderDefaultAppearance {

  public interface GreenHeaderStyle extends HeaderStyle {
    String header();

    String headerIcon();

    String headerHasIcon();

    String headerText();

    String headerBar();
  }

  public interface GreenHeaderResources extends HeaderResources {

    @Source({"com/sencha/gxt/theme/base/client/widget/Header.css", "GreenHeader.css"})
    GreenHeaderStyle style();
    
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource greenbackground();
  }
  

  public GreenHeaderAppearance() {
    this(GWT.<GreenHeaderResources> create(GreenHeaderResources.class),
        GWT.<Template> create(Template.class));
  }

  public GreenHeaderAppearance(HeaderResources resources, Template template) {
    super(resources, template);
  }

}