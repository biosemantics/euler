package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.AdapterField;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DualListField.DualListFieldAppearance;
import com.sencha.gxt.widget.core.client.form.DualListField.DualListFieldMessages;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;

import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;

/**
 * Combines two list view fields and allows selections to be moved between
 * fields either using buttons or by dragging and dropping selections
 * 
 * @param <M> the model type
 * @param <T> the type displayed in the list view
 */
public class MyDualListField extends AdapterField<List<Relation>> {

	protected class DualListFieldDefaultMessages implements DualListFieldMessages {

	    @Override
	    public String addAll() {
	      return DefaultMessages.getMessages().listField_addAll();
	    }

	    @Override
	    public String addSelected() {
	      return DefaultMessages.getMessages().listField_addSelected();
	    }

	    @Override
	    public String moveDown() {
	      return DefaultMessages.getMessages().listField_moveSelectedDown();
	    }

	    @Override
	    public String moveUp() {
	      return DefaultMessages.getMessages().listField_moveSelectedUp();
	    }

	    @Override
	    public String removeAll() {
	      return DefaultMessages.getMessages().listField_removeAll();
	    }

	    @Override
	    public String removeSelected() {
	      return DefaultMessages.getMessages().listField_removeSelected();
	    }

	  }
  
  protected Mode mode = Mode.APPEND;
  protected ListViewDragSource<Relation> sourceFromField;
  protected ListViewDragSource<Relation> sourceToField;

  protected ListViewDropTarget<Relation> targetFromField;
  protected ListViewDropTarget<Relation> targetToField;

  private DualListFieldMessages messages;
  private VerticalPanel buttonBar;
  private ListView<Relation, String> fromView, toView;
  private ListStore<Relation> fromStore, toStore;
  private IconButton up, allRight, right, left, allLeft, down;
  private final DualListFieldAppearance appearance;
  private String dndGroup;

  private boolean enableDnd = true;
	private EventBus eventBus;
	private Collection collection;
	private ComboBox<Taxon> taxonomyACombo;
	private ComboBox<Taxon> taxonomyBCombo;

  /**
   * Creates a dual list field that allows selections to be moved between two
   * list views using buttons or by dragging and dropping selections.
   * 
   * @param fromStore the store containing the base set of items
   * @param toStore the store containing the items selected by the user
   * @param valueProvider the interface to {@code <M>}
   * @param cell displays the data in the list view (e.g. {@link TextCell})
   */
  @UiConstructor
  public MyDualListField(ListStore<Relation> fromStore, ListStore<Relation> toStore, ValueProvider<? super Relation, String> valueProvider,
      Cell<String> cell, EventBus eventBus, Collection collection, ComboBox<Taxon> taxonomyACombo, ComboBox<Taxon> taxonomyBCombo) {
    this(fromStore, toStore, valueProvider, cell, eventBus, collection, taxonomyACombo, taxonomyBCombo, GWT.<DualListFieldAppearance>create(DualListFieldAppearance.class));
  }

  /**
   * Creates a dual list field that allows selections to be moved between two
   * list views using buttons or by dragging and dropping selections.
   *
   * @param fromStore the store containing the base set of items
   * @param toStore the store containing the items selected by the user
   * @param valueProvider the interface to {@code <M>}
   * @param cell displays the data in the list view (e.g. {@link TextCell})
   * @param appearance the appearance instance to use when rendering this widget
   */
  public MyDualListField(ListStore<Relation> fromStore, ListStore<Relation> toStore, ValueProvider<? super Relation, String> valueProvider,
                       Cell<String> cell, EventBus eventBus, Collection collection, ComboBox<Taxon> taxonomyACombo, ComboBox<Taxon> taxonomyBCombo, DualListFieldAppearance appearance) {
    super(new HorizontalPanel());
    this.eventBus = eventBus;
    this.collection = collection;
    this.taxonomyACombo = taxonomyACombo;
    this.taxonomyBCombo = taxonomyBCombo;

    this.appearance = appearance;

    this.fromStore = fromStore;
    this.toStore = toStore;
    HorizontalPanel panel = (HorizontalPanel) getWidget();
    this.buttonBar = new VerticalPanel();

    fromView = new ListView<Relation, String>(this.fromStore, valueProvider);
    fromView.setCell(cell);
    fromView.setWidth(125);

    toView = new ListView<Relation, String>(this.toStore, valueProvider);
    toView.setCell(cell);
    toView.setWidth(125);

    buttonBar.setSpacing(3);
    buttonBar.getElement().getStyle().setProperty("margin", "7px");
    buttonBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

    up = new IconButton(appearance.up());
    up.setToolTip(getMessages().moveUp());
    up.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        onUp();
      }
    });

    allRight = new IconButton(appearance.allRight());
    allRight.setToolTip(getMessages().addAll());
    allRight.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        onAllRight();
      }
    });

    right = new IconButton(appearance.right());
    right.setToolTip(getMessages().addSelected());
    right.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        onRight();
      }
    });

    left = new IconButton(appearance.left());
    left.setToolTip(getMessages().removeSelected());
    left.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        onLeft();
      }
    });

    allLeft = new IconButton(appearance.allLeft());
    allLeft.setToolTip(getMessages().removeAll());
    allLeft.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        onAllLeft();
      }
    });

    down = new IconButton(appearance.down());
    down.setToolTip(getMessages().moveDown());
    down.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        onDown();
      }
    });

    buttonBar.add(up);
    buttonBar.add(allRight);
    buttonBar.add(right);
    buttonBar.add(left);
    buttonBar.add(allLeft);
    buttonBar.add(down);

    panel.add(fromView);
    panel.add(buttonBar);
    panel.add(toView);

    setMode(mode);
    setPixelSize(200, 125);

    bindEvents();
  }

  private void bindEvents() {
	eventBus.addHandler(LoadCollectionEvent.TYPE, new LoadCollectionEvent.LoadCollectionEventHandler() {
		@Override
		public void onLoad(LoadCollectionEvent event) {
			collection = event.getCollection();
		}
	});
  }

public DualListFieldAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the DND group name.
   * 
   * @return the group name
   */
  public String getDndGroup() {
    return dndGroup;
  }

  /**
   * Returns the from field's drag source instance.
   * 
   * @return the drag source
   */
  public ListViewDragSource<Relation> getDragSourceFromField() {
    return sourceFromField;
  }

  /**
   * Returns the to field's drag source instance.
   * 
   * @return the drag source
   */
  public ListViewDragSource<Relation> getDragSourceToField() {
    return sourceToField;
  }

  /**
   * Returns the from field's drop target instance.
   * 
   * @return the drag source
   */
  public ListViewDropTarget<Relation> getDropTargetFromField() {
    return targetFromField;
  }

  /**
   * Returns the to field's drop target instance.
   * 
   * @return the drag source
   */
  public ListViewDropTarget<Relation> getDropTargetToField() {
    return targetToField;
  }

  /**
   * Returns the list view that provides the source of selectable items.
   * 
   * @return the list view that provides the source of selectable items
   */
  public ListView<Relation, String> getFromView() {
    return fromView;
  }

  /**
   * Returns the ListStore that manages the source of selectable items.
   * @return the list store that manages the source of selectable items
   */
  public ListStore<Relation> getFromStore() {
    return fromStore;
  }

  /**
   * Returns the locale-sensitive messages used by this class.
   * 
   * @return the local-sensitive messages used by this class.
   */
  public DualListFieldMessages getMessages() {
    if (messages == null) {
      messages = new DualListFieldDefaultMessages();
    }
    return messages;
  }

  /**
   * Returns the list field's mode.
   * 
   * @return the mode
   */
  public Mode getMode() {
    return mode;
  }

  /**
   * Returns the list view that provides the destination for selectable items.
   * 
   * @return the list view that provides the destination for selectable items
   */
  public ListView<Relation, String> getToView() {
    return toView;
  }

  /**
   * Returns the ListStore that manages the destination for selectable items.
   *
   * @return the ListStore that manages the destination for selectable items
   */
  public ListStore<Relation> getToStore() {
    return toStore;
  }

  @Override
  public List<Relation> getValue() {
    return toStore.getAll();
  }

  /**
   * Returns true if drag and drop is enabled.
   * 
   * @return true if drag and drop is enabled
   */
  public boolean isEnableDnd() {
    return enableDnd;
  }

  /**
   * Sets the drag and drop group name. A group name will be generated if none
   * is specified.
   * 
   * @param group the group name
   */
  public void setDndGroup(String group) {
    if (group == null) {
      group = getId() + "-group";
    }
    this.dndGroup = group;
    if (sourceFromField != null) {
      sourceFromField.setGroup(dndGroup);
    }
    if (sourceToField != null) {
      sourceToField.setGroup(dndGroup);
    }
    if (targetFromField != null) {
      targetFromField.setGroup(dndGroup);
    }
    if (targetToField != null) {
      targetToField.setGroup(dndGroup);
    }
  }

  /**
   * True to allow selections to be dragged and dropped between lists (defaults
   * to true).
   * 
   * @param enableDnd true to enable drag and drop
   */
  public void setEnableDnd(boolean enableDnd) {
    if (enableDnd) {
      if (sourceFromField == null) {
        sourceFromField = new ListViewDragSource<Relation>(fromView);
        sourceToField = new ListViewDragSource<Relation>(toView);

        targetFromField = new ListViewDropTarget<Relation>(fromView) {
          @Override
       	  protected void onDragDrop(DndDropEvent event) {
         	    Object data = event.getData();
       	    List<Relation> models = (List) prepareDropData(data, true);
       	    
       		List<Articulation> articulations = new LinkedList<Articulation>();
       		for (Relation m : models) {
       			articulations.add(collection.getModel().getArticulation(taxonomyACombo.getValue(), taxonomyBCombo.getValue(), m));
       		}
       		eventBus.fireEvent(new RemoveArticulationsEvent(articulations));
       	    
       	    //super.onDragDrop(event);
       	  }
        };
        targetFromField.setAutoSelect(true);
        targetToField = new ListViewDropTarget<Relation>(toView) {
        	  @Override
        	  protected void onDragDrop(DndDropEvent event) {
          	    Object data = event.getData();
        	    List<Relation> models = (List) prepareDropData(data, true);
        	    
        		List<Articulation> articulations = new LinkedList<Articulation>();
        		for (Relation m : models) {
        			articulations.add(new Articulation(taxonomyACombo.getValue(), taxonomyBCombo.getValue(), m, 1.0, Type.USER));
        		}
        		eventBus.fireEvent(new AddArticulationsEvent(articulations));
        	    
        	    //super.onDragDrop(event);
        	  }
        };
        targetToField.setAutoSelect(true);

        if (mode == Mode.INSERT) {
          targetToField.setAllowSelfAsSource(true);
          targetFromField.setFeedback(Feedback.INSERT);
          targetToField.setFeedback(Feedback.INSERT);
        }

        setDndGroup(dndGroup);
      }

    } else {
      if (sourceFromField != null) {
        sourceFromField.release();
        sourceFromField = null;
      }
      if (sourceToField != null) {
        sourceToField.release();
        sourceToField = null;
      }
      if (targetFromField != null) {
        targetFromField.release();
        targetFromField = null;
      }
      if (targetToField != null) {
        targetToField.release();
        targetToField = null;
      }
    }

    this.enableDnd = enableDnd;
  }

  /**
   * Sets the local-sensitive messages used by this class.
   * 
   * @param messages the locale sensitive messages used by this class.
   */
  public void setMessages(DualListFieldMessages messages) {
    this.messages = messages;
  }

  /**
   * Specifies if selections are either inserted or appended when moving between
   * lists.
   * 
   * @param mode the mode
   */
  public void setMode(Mode mode) {
    this.mode = mode;
    switch (mode) {
      case APPEND:
        up.setVisible(false);
        down.setVisible(false);
        break;

      case INSERT:
        up.setVisible(true);
        down.setVisible(true);
        break;
    }
  }

  @Override
  public void setValue(List<Relation> value) {
    if (value == null || value.isEmpty()) {
      onAllLeft();
      return;
    }
    //copy value list so we can modify it
    value = new ArrayList<Relation>(value);

    //first we collect all items used in either list
    //at this point, 'nonSelectedItems' is actually all items
    List<Relation> nonSelectedItems = new ArrayList<Relation>(toStore.getAll());
    nonSelectedItems.addAll(fromStore.getAll());

    //then remove any item *not* in either from the set of items to select
    value.retainAll(nonSelectedItems);

    //now we remove the items that are not selected, making it really nonSelectedItems
    nonSelectedItems.removeAll(value);

    //assign the stores to these new items
    fromStore.replaceAll(nonSelectedItems);
    toStore.replaceAll(value);

  }

  @Override
  protected void onDisable() {
    super.onDisable();
    fromView.disable();
    toView.disable();
    allLeft.disable();
    allRight.disable();
    right.disable();
    left.disable();
    up.disable();
    down.disable();
  }

  protected void onDown() {
    toView.moveSelectedDown();
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    fromView.enable();
    toView.enable();
    allLeft.enable();
    allRight.enable();
    right.enable();
    left.enable();
    up.enable();
    down.enable();
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);

    int w = (width - (buttonBar.getOffsetWidth() + 14)) / 2;

    fromView.setPixelSize(w, height);
    toView.setPixelSize(w, height);
  }

  protected void onUp() {
    toView.moveSelectedUp();
  }

 
	public void onAllRight() {
		List<Articulation> articulations = new LinkedList<Articulation>();
		for(Relation relation : getFromStore().getAll()) {
			Articulation articulation = new Articulation(taxonomyACombo.getValue(), taxonomyBCombo.getValue(), relation, 1.0, Type.USER);
			articulations.add(articulation);
		}
		eventBus.fireEvent(new AddArticulationsEvent(articulations));
		
		List<Relation> sel = fromStore.getAll();
	    toStore.addAll(sel);
	    fromStore.clear();
	}
	
	public void onAllLeft() {
		java.util.Collection<Articulation> articulations = collection.getModel().getArticulations(taxonomyACombo.getValue(), taxonomyBCombo.getValue());
		eventBus.fireEvent(new RemoveArticulationsEvent(articulations));

		List<Relation> sel = toStore.getAll();
	    fromStore.addAll(sel);
	    toStore.clear();
	}
	
	public void onLeft() {
		List<Articulation> articulations = new LinkedList<Articulation>();

		List<Relation> sel = getToView().getSelectionModel()
				.getSelectedItems();
		for (Relation m : sel) {
			getToStore().remove(m);
			articulations.add(collection.getModel().getArticulation(taxonomyACombo.getValue(), taxonomyBCombo.getValue(), m));
		}
		getFromStore().addAll(sel);
		getFromView().getSelectionModel().select(sel, false);

		eventBus.fireEvent(new RemoveArticulationsEvent(articulations));
	}
	
	public void onRight() {
		List<Articulation> articulations = new LinkedList<Articulation>();
		
		List<Relation> sel = this.getFromView().getSelectionModel().getSelectedItems();
		for (Relation m : sel) {
			this.getFromStore().remove(m);
			articulations.add(new Articulation(taxonomyACombo.getValue(), taxonomyBCombo.getValue(), m, 1.0, Type.USER));
			
		}
		getToStore().addAll(sel);
		getToView().getSelectionModel().select(sel, false);
		
		eventBus.fireEvent(new AddArticulationsEvent(articulations));
	}
}
