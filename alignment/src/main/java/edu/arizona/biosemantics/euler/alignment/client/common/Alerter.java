package edu.arizona.biosemantics.euler.alignment.client.common;

import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;

public class Alerter {
	
	private static AutoProgressMessageBox box;
	
	public static MessageBox startLoading() {
		box = new AutoProgressMessageBox("Loading", "Loading your data, please wait...");
        box.setProgressText("Loading...");
        box.auto();
        box.show();
        return box;
	}
	
	public static void stopLoading() {
		box.hide();
		box = null;
	}

	public static MessageBox articulationAlreadyExists() {
		return showAlert("Articulation exists", "Articulation for these taxon concepts already exists.");
	}
	
	public static MessageBox missingItemToCreateArticulation() {
		return showAlert("Can't create articulation", "You have to select a taxon from each taxonomy "
				+ "and select the relationship that holds between them to create an articulation");
	}
	
	public static MessageBox failedToImportArticulations(Throwable e) {
		return showAlert("Import Failed", "Failed to import articulations", e);
	}
	
	private static MessageBox showAlert(String title, String message, Throwable caught) {
		if(caught != null)
			caught.printStackTrace();
		return showAlert(title, message);
	}
	
	private static MessageBox showAlert(String title, String message) {
		AlertMessageBox alert = new AlertMessageBox(title, message);
		alert.show();
		return alert;
	}
	
	private static MessageBox showConfirm(String title, String message) {
		 ConfirmMessageBox confirm = new ConfirmMessageBox(title, message);
		 confirm.show();
         return confirm;
	}

	private static MessageBox showYesNoCancelConfirm(String title, String message) {
		MessageBox box = new MessageBox(title, message);
        box.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
        box.setIcon(MessageBox.ICONS.question());
        box.show();
        return box;
	}




	
}