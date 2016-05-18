package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class addPanelController {
	
	private WallPanel newPanel;
	
	@FXML
	private TextField tfCorner1x, tfCorner1y, tfCorner2x, tfCorner2y; 
	
	public void initialization (WallPanel newPanel){
		this.newPanel = newPanel;
	}
	
	@FXML
	private void cancelPressed (ActionEvent event){
		
	}
	
	@FXML
	private void finishPressed (ActionEvent event){
		
	}
}
