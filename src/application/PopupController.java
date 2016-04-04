package application;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
		hmInformation.put("Name", tfName.getText());
		hmInformation.put("Rows", tfRow.getText());
		hmInformation.put("Columns", tfColumn.getText());
		Stage stage = (Stage)btnFinish.getScene().getWindow();
		stage.close();
	}
	
	public void sethmInformation(HashMap<String,String> hmInformation){
		this.hmInformation = hmInformation;
	}
}
