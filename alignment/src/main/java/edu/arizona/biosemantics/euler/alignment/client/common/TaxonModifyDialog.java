//package edu.arizona.biosemantics.euler.alignment.client.common;
//
//import com.google.gwt.event.shared.EventBus;
//import com.sencha.gxt.widget.core.client.Dialog;
//import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
//import com.sencha.gxt.widget.core.client.button.TextButton;
//import com.sencha.gxt.widget.core.client.event.SelectEvent;
//import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
//import com.sencha.gxt.widget.core.client.form.ComboBox;
//import com.sencha.gxt.widget.core.client.form.TextField;
//import com.sencha.gxt.widget.core.client.tree.Tree;
//
//import edu.arizona.biosemantics.common.taxonomy.Rank;
//import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
//import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
//
//public class TaxonModifyDialog extends CommonDialog {
//		
//		private TaxonInformationContainer taxonInformationContainer;
//
//		public TaxonModifyDialog(final EventBus eventBus, final Model model, final Taxon taxon) {
//			this.setHeadingText("Modify Taxon");
//			taxonInformationContainer = new TaxonInformationContainer(model, taxon.getParent(), taxon);
//		    this.add(taxonInformationContainer);
//			
//		    final Tree<Taxon, String> taxaTree = taxonInformationContainer.getTaxaTree();
//			final ComboBox<Rank> levelCombo = taxonInformationContainer.getLevelComboBox(); 
//		    final TextField nameField = taxonInformationContainer.getNameField();
//		    final TextField authorField = taxonInformationContainer.getAuthorField();
//		    final TextField yearField = taxonInformationContainer.getYearField();
//		 
//		    getButtonBar().clear();
//		    TextButton save = new TextButton("Save");
//		    save.addSelectHandler(new SelectHandler() {
//				@Override
//				public void onSelect(SelectEvent event) {
//					if(!nameField.validate()) {
//						AlertMessageBox alert = new AlertMessageBox("Name empty", "A name is required");
//						alert.show();
//						return;
//					}
//					if(!authorField.validate()) {
//						AlertMessageBox alert = new AlertMessageBox("Author empty", "An author is required");
//						alert.show();
//						return;
//					}
//					if(!yearField.validate()) {
//						AlertMessageBox alert = new AlertMessageBox("Year empty", "A year is required");
//						alert.show();
//						return;
//					}
//					eventBus.fireEvent(new ModifyTaxonEvent(taxon, taxaTree.getSelectionModel().getSelectedItem(), 
//							levelCombo.getValue(), nameField.getText(), authorField.getText(), yearField.getText()));
//					TaxonModifyDialog.this.hide();
//				}
//		    });
//		    TextButton cancel =  new TextButton("Cancel");
//		    cancel.addSelectHandler(new SelectHandler() {
//				@Override
//				public void onSelect(SelectEvent event) {
//					TaxonModifyDialog.this.hide();
//				}
//		    });
//		    addButton(save);
//		    addButton(cancel);
//		}
//		
//		public void selectParent(Taxon taxon) {
//			taxonInformationContainer.selectParent(taxon);
//		}
//	}
