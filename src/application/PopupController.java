package application;

import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class PopupController {
	
	private HashMap<String,String> hmInformation;
	
	@FXML
	private Button btnFinish, btnCancel;

	@FXML
	private TextField tfName, tfRow, tfColumn;
	
	@FXML
	public void cancel(ActionEvent e){
		Stage stage = (Stage)btnFinish.getScene().getWindow();
		hmInformation.put("Name", "-1");
		stage.close();
	}
	@FXML
	public void getAnimationInformation(ActionEvent e){
		if(tfName.getText() != "" && tfRow.getText() != "" && tfColumn.getText() != ""){
			hmInformation.put("Name", tfName.getText());
			hmInformation.put("Rows", tfRow.getText());
			hmInformation.put("Columns", tfColumn.getText());
			Stage stage = (Stage)btnFinish.getScene().getWindow();
			stage.close();
		}
		else{
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Error");
	    	alert.setHeaderText("Error");
	    	alert.setContentText("Please fill all fields");
	    	alert.showAndWait();
		}
	}
	
	public void sethmInformation(HashMap<String,String> hmInformation){
		this.hmInformation = hmInformation;
	}
	
	
	public void initialization() {

		tfRow.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) >= 1 && Integer.valueOf(newValue) <= 50) {
					tfRow.setText(newValue);
				} else {
					tfRow.setText(oldValue);
				}
			}
		});

		tfColumn.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) >= 1 && Integer.valueOf(newValue) <= 50) {
					tfColumn.setText(newValue);
				} else {
					tfColumn.setText(oldValue);
				}
			}
		});

	}
	
}
