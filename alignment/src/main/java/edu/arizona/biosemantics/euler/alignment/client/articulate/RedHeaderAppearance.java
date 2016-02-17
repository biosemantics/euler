package edu.arizona.biosemantics.euler.alignment.client.articulate;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;

public class RedHeaderAppearance extends HeaderDefaultAppearance {

  public interface RedHeaderStyle extends HeaderStyle {
    String header();

    String headerIcon();

    String headerHasIcon();

    String headerText();

    String headerBar();
  }

  public interface RedHeaderResources extends HeaderResources {

    @Source({"com/sencha/gxt/theme/base/client/widget/Header.css", "RedHeader.css"})
    RedHeaderStyle style();
    
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource redbackground();
  }
  

  public RedHeaderAppearance() {
    this(GWT.<RedHeaderResources> create(RedHeaderResources.class),
        GWT.<Template> create(Template.class));
  }

  public RedHeaderAppearance(HeaderResources resources, Template template) {
    super(resources, template);
  }

}