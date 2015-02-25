package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Params;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.MultiLinePromptMessageBox;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.HeaderMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

import edu.arizona.biosemantics.euler.alignment.client.event.ShowDescriptionEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.AddArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadModelEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.RemoveArticulationsEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetColorEvent;
import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Color;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.TaxonProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;

public class TaxonomyView extends ContentPanel {
	
	private TaxonProperties taxonProperties = GWT.create(TaxonProperties.class);
	private HTML infoHtml = new HTML();
	private Model model;
	private EventBus eventBus;
	private Tree<Taxon, Taxon> tree;
	private TreeStore<Taxon> store = new TreeStore<Taxon>(taxonProperties.key());
	private Set<SelectionChangedHandler<Taxon>> selectionChangeHandlers = 
			new HashSet<SelectionChangedHandler<Taxon>>();
	private boolean taxonomyA;
	
	public TaxonomyView(EventBus eventBus, boolean taxonomyA) {
		this.eventBus = eventBus;
		this.taxonomyA = taxonomyA;
		tree = createTree();
		
		//this.setTitle("Right-clicks on taxa to bring up more taxa management functions");
		//this.setHeadingText("Select Taxa for the Matrix");
		
		FieldSet taxaFieldSet = new FieldSet();
		//taxonFieldSet.setCollapsible(true);
		taxaFieldSet.setHeadingText("Taxa");
		taxaFieldSet.setWidget(tree);
		
		FieldSet infoFieldSet = new FieldSet();
		//taxonFieldSet.setCollapsible(true);
		infoFieldSet.setHeadingText("Taxon Details");
		FlowLayoutContainer flowInfoHtml = new FlowLayoutContainer();
		flowInfoHtml.add(infoHtml);
		flowInfoHtml.getScrollSupport().setScrollMode(ScrollMode.AUTO);
		infoFieldSet.setWidget(flowInfoHtml);
		
		HorizontalLayoutContainer horizontalLayoutContainer = new HorizontalLayoutContainer();
		horizontalLayoutContainer.add(taxaFieldSet, new HorizontalLayoutData(0.5, 1.0));
		horizontalLayoutContainer.add(infoFieldSet, new HorizontalLayoutData(0.5, 1.0));

		VerticalLayoutContainer vertical = new VerticalLayoutContainer();
		vertical.add(horizontalLayoutContainer, new VerticalLayoutData(1.0, 1.0));
		//vertical.add(createTaxaButtonBar(), new VerticalLayoutData());
				
		this.setWidget(vertical);
		
		bindEvents();
	}

	private void bindEvents() {
		eventBus.addHandler(LoadModelEvent.TYPE, new LoadModelEvent.LoadModelEventHandler() {
			@Override
			public void onLoad(LoadModelEvent event) {
				model = event.getModel();
			}
		});
		eventBus.addHandler(SetColorEvent.TYPE, new SetColorEvent.SetColorEventHandler() {
			@Override
			public void onSet(SetColorEvent event) {
				if(event.getObject() instanceof Taxon) {
					updateStore((Taxon)event.getObject());
				}
			}
		});
		eventBus.addHandler(SetCommentEvent.TYPE, new SetCommentEvent.SetCommentEventHandler() {
			@Override
			public void onSet(SetCommentEvent event) {
				if(event.getObject() instanceof Taxon) {
					updateStore((Taxon)event.getObject());
				}
			}
		});
		eventBus.addHandler(AddArticulationsEvent.TYPE, new AddArticulationsEvent.AddArticulationEventHandler() {
			@Override
			public void onAdd(AddArticulationsEvent event) {
				for(Articulation articulation : event.getArticulations()) {
					if(taxonomyA)
						updateStore(articulation.getTaxonA());
					else
						updateStore(articulation.getTaxonB());
				}
			}
		});
		eventBus.addHandler(RemoveArticulationsEvent.TYPE, new RemoveArticulationsEvent.RemoveArticulationsEventHandler() {
			@Override
			public void onRemove(RemoveArticulationsEvent event) {
				for(Articulation articulation : event.getArticulations()) {
					if(taxonomyA)
						updateStore(articulation.getTaxonA());
					else 
						updateStore(articulation.getTaxonB());
				}
			}
		});
		
		tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Taxon>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<Taxon> event) {
				for(SelectionChangedHandler<Taxon> handler :  selectionChangeHandlers) {
					handler.onSelectionChanged(new SelectionChangedEvent<Taxon>(event.getSelection()));
				}
			}
		});
		
	}

	protected void updateStore(Taxon taxon) {
		if(store.hasRecord(taxon)) 
			store.update(taxon);
	}

	protected void markNotArticulated(List<Articulation> articulations) {
		
	}

	protected void markArticulated(List<Articulation> articulations) {
		// TODO Auto-generated method stub
		
	}

	private Tree<Taxon, Taxon> createTree() {
		final Tree<Taxon, Taxon> tree = new Tree<Taxon, Taxon>(store, new IdentityValueProvider<Taxon>());	
		tree.getSelectionModel().addSelectionHandler(new SelectionHandler<Taxon>() {
			@Override
			public void onSelection(SelectionEvent<Taxon> event) {
				Taxon selection = event.getSelectedItem();
				//tree.getSelectionModel().select(store.getChildren(selection), true);	
				updateTextArea(selection);
			}
		});
		
		tree.setContextMenu(createTreeContextMenu(tree));
		tree.setCell(new AbstractCell<Taxon>() {
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,	Taxon taxon, SafeHtmlBuilder sb) {
					String colorHex = "";
					if(model.hasColor(taxon))
						colorHex = model.getColor(taxon).getHex();
					String style = "background-color:#" + colorHex + ";";
					if(model.hasArticulationFor(taxon)) 
						style += " font-weight: bold;";
					sb.append(SafeHtmlUtils.fromTrustedString("<div style='" + style + "'>" + 
							taxon.getFullName() + "</div>"));
			}
		});
		return tree;
	}	

	private Menu createTreeContextMenu(Tree<Taxon, Taxon> tree2) {
		final Menu menu = new Menu();
		menu.add(new HeaderMenuItem("View"));
		MenuItem item = new MenuItem();
		item.setText("Expand All");
		// item.setIcon(header.getAppearance().sortAscendingIcon());
		item.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				tree.expandAll();
			}
		});
		menu.add(item);
		
		item = new MenuItem();
		item.setText("Collapse All");
		// item.setIcon(header.getAppearance().sortAscendingIcon());
		item.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				tree.collapseAll();
			}
		});
		menu.add(item);
		
		menu.add(new HeaderMenuItem("Annotation"));
		final MenuItem commentItem = new MenuItem();
		commentItem.setText("Comment");
		// commentItem.setIcon(header.getAppearance().sortAscendingIcon());
		commentItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				final List<Taxon> taxa = getSelectedTaxa();
				final MultiLinePromptMessageBox box = new MultiLinePromptMessageBox("Comment", "");

				if(taxa.size() == 1)
					box.getTextArea().setValue(model.hasComment(taxa.get(0)) ? model.getComment(taxa.get(0)) : "");
				else 
					box.getTextArea().setValue("");
				
				box.addHideHandler(new HideHandler() {
					@Override
					public void onHide(HideEvent event) {
						for(Taxon taxon : taxa) { 
							eventBus.fireEvent(new SetCommentEvent(taxon, box.getValue()));
							updateStore(taxon);
						}
						String comment = Format.ellipse(box.getValue(), 80);
						String message = Format.substitute("'{0}' saved", new Params(comment));
						Info.display("Comment", message);
					}
				});
				box.show();
			}
		});
		menu.add(commentItem);
		
		final MenuItem colorizeItem = new MenuItem("Colorize");
		menu.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				if(!model.getColors().isEmpty()) {
					menu.insert(colorizeItem, menu.getWidgetIndex(commentItem));
					//colors can change, refresh
					colorizeItem.setSubMenu(createColorizeMenu());
				} else {
					menu.remove(colorizeItem);
				}
			}
		});
		
		menu.add(new HeaderMenuItem("Analysis"));
		item = new MenuItem("Show Description");
		item.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				Taxon selected = tree.getSelectionModel().getSelectedItem();
				eventBus.fireEvent(new ShowDescriptionEvent(selected));
			}
		});
		menu.add(item); 
		item = new MenuItem("Search Images");
		item.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				Taxon selected = tree.getSelectionModel().getSelectedItem();
				String fullName = selected.getFullName();
				Window.open("http://www.google.com/search?tbm=isch&q=" + fullName, "_blank", "");
			}
		});
		menu.add(item);
		
		return menu;
	}
	
	protected Menu createColorizeMenu() {
		Menu colorMenu = new Menu();
		MenuItem offItem = new MenuItem("None");
		offItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				final List<Taxon> taxa = getSelectedTaxa();
				for(Taxon taxon : taxa) {
					eventBus.fireEvent(new SetColorEvent(taxon, null));
					updateStore(taxon);
				}
			}
		});
		colorMenu.add(offItem);
		for(final Color color : model.getColors()) {
			MenuItem colorItem = new MenuItem(color.getUse());
			colorItem.getElement().getStyle().setProperty("backgroundColor", "#" + color.getHex());
			colorItem.addSelectionHandler(new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					final List<Taxon> taxa = getSelectedTaxa();
					for(Taxon taxon : taxa) {
						eventBus.fireEvent(new SetColorEvent(taxon, color));
						updateStore(taxon);
					}
				}
			});
			colorMenu.add(colorItem);
		}
		return colorMenu;
	}

	public void loadModel(Taxonomy taxonomy) {
		this.setHeadingText(taxonomy.getName());
		store.clear();
		for(Taxon rootTaxon : taxonomy.getRootTaxa()) {
			store.add(rootTaxon);
			addToStoreRecursively(store, rootTaxon);
		}
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				tree.expandAll();
			}
		});
	}

	protected void updateTextArea(Taxon taxon) {
		List<Taxon> ancestors = new LinkedList<Taxon>();
		Taxon parent = taxon.getParent();
		while(parent != null) {
			ancestors.add(parent);
			parent = parent.getParent();
		}

		String taxonomy = "";
		String prefix = "";
		for(int i=ancestors.size() - 1; i >= 0; i--) {
			Taxon ancestor = ancestors.get(i);
			prefix += "-";
			taxonomy += "<p>" + prefix + " " + 
					(ancestor.getRank() == null ? "" : ancestor.getRank().name()) + " " + 
					ancestor.getName() + " " + 
					ancestor.getAuthor() + " " + 
					ancestor.getYear() + 
					"</p>";
		}
					
		String infoText = "<p><b>Rank:&nbsp;</b>" + (taxon.getRank() == null ? "" : taxon.getRank().name()) + "</p>" +
				"<p><b>Name:&nbsp;</b>" + taxon.getName() + "</p>" +
				"<p><b>Author:&nbsp;</b>" + taxon.getAuthor() + "</p>" +
				"<p><b>Year:&nbsp;</b>" + taxon.getYear() + "</p>" +
				"<p><b>Taxonomy:&nbsp;</b>" + taxonomy + "</p>" +
				"<p><b>Description:&nbsp;</b>" + taxon.getDescription().replaceAll("\n", "</br>") + "</p>";
		if(model.hasComment(taxon))
			infoText +=	"<p><b>Comment:&nbsp;</b>" + model.getComment(taxon) + "</p>";
		if(model.hasColor(taxon))
			infoText += "<p><b>Color:&nbsp;</b>" + model.getColor(taxon).getUse() + "</p>";
		
		infoHtml.setHTML(SafeHtmlUtils.fromSafeConstant(infoText));
	}

	private void addToStoreRecursively(TreeStore<Taxon> store, Taxon taxon) {
		for(Taxon child : taxon.getChildren()) {
			store.add(taxon, child);
			this.addToStoreRecursively(store, child);
		}
	}

	public List<Taxon> getSelectedTaxa() {
		return tree.getSelectionModel().getSelectedItems();
	}
	
	public void addSelectionChangeHandler(SelectionChangedHandler<Taxon> handler) {
		this.selectionChangeHandlers.add(handler);
	}
	
	public void removeSelectionChangeHandler(SelectionChangedHandler<Taxon> handler) {
		this.selectionChangeHandlers.remove(handler);
	}

	public void setSelected(Taxon selected) {
		List<Taxon> selection = new LinkedList<Taxon>();
		selection.add(selected);
		tree.getSelectionModel().setSelection(selection);
	}
}
