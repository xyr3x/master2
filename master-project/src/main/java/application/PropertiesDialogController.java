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
	
	/**
	 * shows the values of the properties
	 */
	public void showProperties() {
		populationsizeField.setText(Integer.toString(Main.PopulationSize));
		crewsizeField.setText(Integer.toString(Main.CrewSize));
		gridlengthField.setText(Integer.toString(Main.GridLength));
		timeintervalField.setText(Integer.toString(Main.TimeInterval));
		mutationprobabilityField.setText(Integer.toString(Main.MutationProbability));
	}

	/**
	 * sets the properties in Main when the okay button is pressed
	 */
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

	/**
	 * cancels the properties update
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * checks if field value is integer value
	 * @param field
	 * @return returns the fieldsvalue if it is an integer and larger than 0, returns -1 if it's not an integer or smaller than 0
	 */
	private int isValid(TextField field) {
		int textValue = 0;
		if (crewsizeField.getText() != null || crewsizeField.getText().length() > 0) {
			try {
				textValue = Integer.parseInt(crewsizeField.getText());
				if (textValue < 0) {
					return -1;
				}
				
			} catch (NumberFormatException e) {
				// handle error
				return -1;
			}
			return textValue;

		}
		return -1;
	}
}
