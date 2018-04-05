package application;

import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.control.*;

public class PropertiesDialogController {
	@FXML
	private TextField gridlengthField;
	@FXML
	private TextField populationsizeField;
	@FXML
	private TextField crewsizeField;
	@FXML
	private TextField timeintervalField;
	@FXML
	private TextField mutationprobabilityField;

	private Stage dialogStage;

	@FXML
	private void initialize() {
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@FXML
	private void handleOkay() {
		int textValue = 0;

		// populationsize
		textValue = isValid(populationsizeField);
		if (textValue > 0) {
			Main.PopulationSize = textValue;
		}

		// Crewsize
		textValue = isValid(crewsizeField);
		if (textValue > 0) {
			Main.CrewSize = textValue;
		}

		// gridlength
		textValue = isValid(gridlengthField);
		if (textValue > 0) {
			Main.GridLength = textValue;
		}

		// timeinterval
		textValue = isValid(timeintervalField);
		if (textValue > 0) {
			Main.TimeInterval = textValue;
		}

		// MutationProbability
		textValue = isValid(mutationprobabilityField);
		if (textValue > 0 && textValue < 100) {
			Main.MutationProbability = textValue;
		}
		
		//close dialog
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private int isValid(TextField field) {
		int textValue = 0;
		if (crewsizeField.getText() != null || crewsizeField.getText().length() > 0) {
			try {
				textValue = Integer.parseInt(crewsizeField.getText());
			} catch (NumberFormatException e) {
				// handle error
				return -1;
			}
			return textValue;

		}
		return -1;
	}
}
