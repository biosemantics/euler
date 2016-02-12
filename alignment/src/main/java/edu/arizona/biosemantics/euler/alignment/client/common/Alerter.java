package edu.arizona.biosemantics.euler.alignment.client.common;

import java.util.List;

import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;

public class Alerter {
		
	public static MessageBox startLoading() {
		AutoProgressMessageBox box = new AutoProgressMessageBox("Loading", "Loading your data, please wait...");
        box.setProgressText("Loading...");
        box.auto();
        box.show();
        return box;
	}
	
	public static void stopLoading(MessageBox box) {
		box.hide();
	}

	public static MessageBox articulationAlreadyExists(List<Articulation> articulations) {
		String result = "";
		for(Articulation articulation : articulations)
			result += articulation.getRelation().getDisplayName() + ", ";
		return showAlert("Articulation exists", "Some of these articulations already existed: " + result.substring(0, result.length() - 2));
	}
	
	public static MessageBox missingItemToCreateArticulation() {
		return showAlert("Can't create articulation", "You have to select a taxon from each taxonomy "
				+ "and select the relationship that holds between them to create an articulation");
	}
	
	public static MessageBox failedToImportArticulations(Throwable e) {
		return showAlert("Import Failed", "Failed to import articulations.</br>" + e.getMessage(), e);
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

	public static void failedToHighlight() {
		// TODO Auto-generated method stub
		
	}




	
}