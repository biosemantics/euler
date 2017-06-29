package edu.arizona.biosemantics.euler.alignment.client.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import edu.arizona.biosemantics.euler.alignment.client.event.model.SetCommentEvent;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Run;
import edu.arizona.biosemantics.euler.alignment.shared.model.RunProperties;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;

public class CommentsDialog extends CommonDialog {

	public enum CommentType {
		taxonType("Taxon"),
		articulation("Articulation"),
		run("Run");
		
		private String readable;

		private CommentType(String readable) {
			this.readable = readable;
		}
		
		public String getReadable() {
			return readable;
		}
		
		@Override
		public String toString() {
			return getReadable();
		}
	}
	
	public class Comment {

		private String id;
		private Object object;
		private String source;
		private String text;
		private CommentType type;

		public Comment(String id, Object object, String source, String text) {
			this.id = id;
			this.object = object;
			if (object instanceof Taxon)
				type = CommentType.taxonType;
			if (object instanceof Articulation)
				type = CommentType.articulation;
			if (object instanceof Run)
				type = CommentType.run;
			this.source = source;
			this.text = text;
		}

		public String getSource() {
			return source;
		}

		public String getText() {
			return text;
		}

		public Object getObject() {
			return object;
		}

		public CommentType getType() {
			return type;
		}

		public void setText(String text) {
			this.text = text;
		}

		public void setObject(Object object) {
			this.object = object;
		}
		
		public String getId() {
			return id;
		}
	}

	public interface CommentProperties extends PropertyAccess<Comment> {

		@Path("id")
		ModelKeyProvider<Comment> key();

		@Path("object")
		ValueProvider<Comment, Object> object();

		@Path("source")
		ValueProvider<Comment, String> source();

		@Path("text")
		ValueProvider<Comment, String> text();

		@Path("type")
		ValueProvider<Comment, CommentType> type();

	}

	private EventBus eventBus;
	private Collection collection;
	private ListStore<Comment> commentStore;
	private Grid<Comment> grid;

	public CommentsDialog(final EventBus eventBus, final Collection collection) {
		this.eventBus = eventBus;
		this.collection = collection;
		CommentProperties commentProperties = GWT
				.create(CommentProperties.class);

		IdentityValueProvider<Comment> identity = new IdentityValueProvider<Comment>();
		final CheckBoxSelectionModel<Comment> checkBoxSelectionModel = new CheckBoxSelectionModel<Comment>(
				identity);

		checkBoxSelectionModel.setSelectionMode(SelectionMode.MULTI);

		ColumnConfig<Comment, CommentType> typeCol = new ColumnConfig<Comment, CommentType>(
				commentProperties.type(), 0, "Type");
		ColumnConfig<Comment, String> sourceCol = new ColumnConfig<Comment, String>(
				commentProperties.source(), 190, "Source");
		final ColumnConfig<Comment, String> textCol = new ColumnConfig<Comment, String>(
				commentProperties.text(), 400, "Comment");

		List<ColumnConfig<Comment, ?>> columns = new ArrayList<ColumnConfig<Comment, ?>>();
		columns.add(checkBoxSelectionModel.getColumn());
		columns.add(typeCol);
		columns.add(sourceCol);
		columns.add(textCol);
		ColumnModel<Comment> cm = new ColumnModel<Comment>(columns);

		commentStore = new ListStore<Comment>(commentProperties.key());
		commentStore.setAutoCommit(true);
		
		List<Comment> comments = createComments();
		for (Comment comment : comments)
			commentStore.add(comment);

		final GroupingView<Comment> groupingView = new GroupingView<Comment>();
		groupingView.setShowGroupedColumn(false);
		groupingView.setForceFit(true);
		groupingView.groupBy(typeCol);

		grid = new Grid<Comment>(commentStore, cm);
		grid.setView(groupingView);
		grid.setContextMenu(createContextMenu());
		grid.setSelectionModel(checkBoxSelectionModel);
		grid.getView().setAutoExpandColumn(textCol);
		grid.setBorders(false);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);

		StringFilter<Comment> textFilter = new StringFilter<Comment>(
				commentProperties.text());
		StringFilter<Comment> sourceFilter = new StringFilter<Comment>(
				commentProperties.source());
		
		ListStore<CommentType> typeFilterStore = new ListStore<CommentType>(
				new ModelKeyProvider<CommentType>() {
					@Override
					public String getKey(CommentType item) {
						return item.toString();
					}
				});
		for(CommentType type : CommentType.values())
			typeFilterStore.add(type);
		ListFilter<Comment, CommentType> typeFilter = new ListFilter<Comment, CommentType>(
				commentProperties.type(), typeFilterStore);

		GridFilters<Comment> filters = new GridFilters<Comment>();
		filters.initPlugin(grid);
		filters.setLocal(true);

		filters.addFilter(textFilter);
		filters.addFilter(sourceFilter);
		filters.addFilter(typeFilter);

		GridInlineEditing<Comment> editing = new GridInlineEditing<Comment>(grid);
		editing.addEditor(textCol, new TextField());
		editing.addCompleteEditHandler(new CompleteEditHandler<Comment>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Comment> event) {			
				GridCell cell = event.getEditCell();
				Comment comment = grid.getStore().get(cell.getRow());
				ColumnConfig<Comment, String> config = grid.getColumnModel().getColumn(cell.getCol());
				if(config.equals(textCol)) 
					eventBus.fireEvent(new SetCommentEvent(comment.getObject(), comment.getText()));
			}
		});

		setBodyBorder(false);
		setHeading("Comments");
		setWidth(800);
		setHeight(600);
		setHideOnButtonClick(true);
		setModal(true);

		ContentPanel panel = new ContentPanel();
		panel.add(grid);
		this.add(panel);
	}

	private Menu createContextMenu() {
		Menu menu = new Menu();
		MenuItem removeItem = new MenuItem("Remove");
		menu.add(removeItem);
		removeItem.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				for (Comment comment : grid.getSelectionModel()
						.getSelectedItems()) {
					commentStore.remove(comment);
					eventBus.fireEvent(new SetCommentEvent(comment.getObject(), ""));
				}
			}
		});
		return menu;
	}

	private List<Comment> createComments() {
		List<Comment> comments = new LinkedList<Comment>();
		
		for(Taxonomy taxonomy : collection.getModel().getTaxonomies()) {
			for (Taxon taxon : taxonomy.getTaxaDFS()) {
				if (collection.getModel().hasComment(taxon))
					comments.add(new Comment("taxon-" + taxon.getId(), taxon, taxon.getBiologicalName(), collection.getModel()
							.getComment(taxon)));
			}
		}
		for(Articulation articulation : collection.getModel().getArticulations()) {
			if(collection.getModel().hasComment(articulation)) {
				comments.add(new Comment("articulation-" + articulation.getId(), articulation, articulation.getText(), collection.getModel().getComment(articulation)));
			}
		}
		for(Run run : collection.getModel().getRunHistory()) {
			RunProperties.DisplayNameValueProvider nameProvider = new RunProperties.DisplayNameValueProvider();
			String runName = nameProvider.getValue(run);
			if(collection.getModel().hasComment(run))
				comments.add(new Comment("run-" + run.getId(), run, runName, collection.getModel().getComment(run)));
			
			for(Articulation articulation : run.getArticulations()) {
				if(collection.getModel().hasComment(articulation)) {
					comments.add(new Comment("articulation-" + articulation.getId(), articulation, "(Run: " + runName + ") " + articulation.getText(), collection.getModel().getComment(articulation)));
				}
			}
		}
		return comments;
	}

}
