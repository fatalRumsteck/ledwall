package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Animation;
import model.WallPanel;

public class AddPanelController {
	
	private WallPanel newPanel;
	
	@FXML
	private TextField tfCorner1x, tfCorner1y, tfCorner2x, tfCorner2y; 
	
	
	@FXML
	private void cancelPressed (ActionEvent event){
		Stage stage = (Stage)tfCorner1x.getScene().getWindow();
		newPanel = null;
		stage.close();
	}
	
	@FXML
	private void finishPressed (ActionEvent event){
		newPanel.setCorner1(Integer.valueOf(tfCorner1x.getText()), Integer.valueOf(tfCorner1y.getText()));
		newPanel.setCorner2(Integer.valueOf(tfCorner2x.getText()), Integer.valueOf(tfCorner2y.getText()));
		Stage stage = (Stage)tfCorner1x.getScene().getWindow();
		stage.close();
	}
	
	public void setNewPanel(WallPanel newPanel){
		this.newPanel = newPanel;
	}
	
	public void initialization(Animation animation){
		
		tfCorner1x.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) >= 1 && Integer.valueOf(newValue) <= animation.getWidth()-1) {
					tfCorner1x.setText(newValue);
				} else {
					tfCorner1x.setText(oldValue);
				}
			}
		});

		tfCorner1y.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) >= 1 && Integer.valueOf(newValue) <= animation.getHeigh()-1) {
					tfCorner1y.setText(newValue);
				} else {
					tfCorner1y.setText(oldValue);
				}
			}
		});
		
		tfCorner2x.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) >= 1 && Integer.valueOf(newValue) <= animation.getWidth()-1) {
					tfCorner2x.setText(newValue);
				} else {
					tfCorner2x.setText(oldValue);
				}
			}
		});

		tfCorner2y.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) >= 0 && Integer.valueOf(newValue) <= animation.getHeigh()-1) {
					tfCorner2y.setText(newValue);
				} else {
					tfCorner2y.setText(oldValue);
				}
			}
		});
	}
}
