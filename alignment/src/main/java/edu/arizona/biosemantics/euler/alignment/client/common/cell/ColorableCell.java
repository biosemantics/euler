package edu.arizona.biosemantics.euler.alignment.client.common.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader.ColumnHeaderAppearance;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader.ColumnHeaderStyles;
import com.sencha.gxt.widget.core.client.grid.GridView.GridAppearance;
import com.sencha.gxt.widget.core.client.grid.GridView.GridStyles;

import edu.arizona.biosemantics.euler.alignment.client.event.model.LoadCollectionEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Color;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class ColorableCell<T> extends AbstractCell<T> {

	public interface CommentColorizableObjectsProvider {
		
		public Object provide(Object source);
		
	}
	
	interface Templates extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<div class=\"{0}\" qtip=\"{4}\">" +
				"<div class=\"{1}\" " +
				"style=\"" +
				"width: calc(100% - 9px); " +
				"height:14px; " +
				"background: no-repeat 0 0;" +
				"background-image:{6};" +
				"background-color:{5};" +
				"\">{3}<a class=\"{2}\" style=\"height: 22px;\"></a>" +
				"</div>" +
				"</div>")
		SafeHtml cell(String grandParentStyleClass, String parentStyleClass,
				String aStyleClass, String value, String quickTipText, String colorHex, String backgroundImage);
	}

	protected ColumnHeaderAppearance columnHeaderAppearance;
	protected GridAppearance gridAppearance;
	protected ColumnHeaderStyles columnHeaderStyles;
	protected GridStyles gridStyles;
	
	protected static Templates templates = GWT.create(Templates.class);
	private EventBus eventBus;
	private Collection collection;
	private ListStore commentColorizableObjectsStore;
	private CommentColorizableObjectsProvider commentColorizableObjectsProvider;
	private QuickTipProvider<T> quickTipProvider;
	
	public ColorableCell(EventBus eventBus, Collection collection, QuickTipProvider<T> quickTipProvider) {
		this(GWT.<ColumnHeaderAppearance> create(ColumnHeaderAppearance.class), GWT.<GridAppearance> create(GridAppearance.class), eventBus, collection, quickTipProvider);
	}
	
	public ColorableCell(ColumnHeaderAppearance columnHeaderAppearance, GridAppearance gridAppearance, EventBus eventBus, Collection collection, 
			QuickTipProvider<T> quickTipProvider) {
		super(BrowserEvents.MOUSEOVER, BrowserEvents.MOUSEOUT, BrowserEvents.CLICK);
		
		this.eventBus = eventBus;
		this.collection = collection;
		this.quickTipProvider = quickTipProvider;
		this.columnHeaderAppearance = columnHeaderAppearance;
		this.gridAppearance = gridAppearance;
		columnHeaderStyles = columnHeaderAppearance.styles();
		gridStyles = gridAppearance.styles();
		
		/*
		System.out.println(styles.headOver());
		System.out.println(styles.columnMoveBottom());
		System.out.println(styles.columnMoveTop());
		System.out.println(styles.head());
		System.out.println(styles.headButton());
		System.out.println(styles.header());
		System.out.println(styles.headInner());
		System.out.println(styles.headMenuOpen());
		System.out.println(styles.headOver());
		System.out.println(styles.headRow());
		System.out.println(styles.sortAsc());
		System.out.println(styles.sortDesc());
		System.out.println(styles.sortIcon());
		System.out.println(styles.headerInner());
		*/
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

	@Override
	public void render(Context context,	T value, SafeHtmlBuilder sb) {
		int rowIndex = context.getIndex();
		Object source = commentColorizableObjectsStore.get(rowIndex);
		Object commentColorizableObject = commentColorizableObjectsProvider.provide(source);
		
		if (value == null)
			return;
		
		String quickTipText = "";
		if(quickTipProvider != null) 
			quickTipText = quickTipProvider.getQuickTip(value);
		String comment = null;
		if(commentColorizableObject != null) {
			comment = collection.getModel().getComment(commentColorizableObject);
			if(comment != null && !comment.isEmpty())
				quickTipText += "<br>Comment:" + comment;
		}
		Color color = null;
		if(commentColorizableObject != null) {
			color = collection.getModel().getColor(commentColorizableObject);
		}

		String colorHex = "";
		if(color != null) 
			colorHex = "#" + color.getHex();
		
		String backgroundImage = "";
		SafeHtml rendered = templates.cell("", columnHeaderStyles.headInner(),
				columnHeaderStyles.headButton(), value.toString(), quickTipText, colorHex, backgroundImage);
		sb.append(rendered);
	}
	
	public void setCommentColorizableObjectsStore(ListStore<Object> commentColorizableObjectsStore, CommentColorizableObjectsProvider commentColorizableObjectsProvider) {
		this.commentColorizableObjectsStore = commentColorizableObjectsStore;
		this.commentColorizableObjectsProvider = commentColorizableObjectsProvider;
	}
}
