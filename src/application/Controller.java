package application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.jdom2.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {
	
	private LinkedList<WallPanel> currentWallConfig;
	private Animation currentAnimation;
	private Motif currentMotif;
	private GridPane gpWall;
	private Color currentColor;
	private EventHandler<MouseEvent> mouseHandler ;
	private PlayThread thread;

	@FXML
	private TextField tfRed, tfMotifNumber, tfMotifTime, tfGreen, tfBlue;
	
	@FXML
	private Rectangle rectColor;

	@FXML
	private Button btnPlay, btnPlayAll, btnStop, btnAddEmptyMotif, btnDuplicateMotif, btnDeleteMotif, btnClear;

	@FXML
	private Label lbMotifNumber;

	@FXML
	private BorderPane bpWall;

	@FXML
	private Slider sliderMotif, sliderRed, sliderGreen, sliderBlue;

	@FXML
	private VBox vbRightPane;
	
	@FXML
	private HBox hbSelectMotifPane;
	
	@FXML
	private SplitPane spMainPane;
	
	@FXML
	private MenuItem miNew, miSave, miOpen, miPlayAll, miPlay, miStop, miAddEmptyMotif, miDuplicateMotif, miDeleteMotif, miClear, miDefWall;
	
	@FXML
	private void createNewAnimation(ActionEvent event) throws IOException {
		HashMap<String, String> hmInformation = new HashMap<String, String>();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("createAnimView.fxml"));
		VBox root = (VBox) loader.load();

		PopupController popupController = loader.<PopupController> getController();
		popupController.sethmInformation(hmInformation);
		popupController.initialization();

		Scene scene = new Scene(root);

		Stage createAnimStage = new Stage();
		createAnimStage.setScene(scene);
		createAnimStage.initModality(Modality.APPLICATION_MODAL);
		createAnimStage.showAndWait();

		// Check cancel pressed
		if (hmInformation.get("Name") != "-1") {
			if (hmInformation.get("Name").matches("[\\w]+") && hmInformation.get("Rows").matches("[\\d]+")
					&& hmInformation.get("Columns").matches("[\\d]+")) {

				// Create a GridPane
				int nbRows = Integer.parseUnsignedInt(hmInformation.get("Rows"));
				int nbColumns = Integer.parseUnsignedInt(hmInformation.get("Columns"));
				
				initializeGpWall(nbColumns, nbRows);
				
				Motif newMotif = new Motif(nbColumns, nbRows);

				currentAnimation = new Animation(hmInformation.get("Name"), nbColumns, nbRows);
				currentAnimation.add(0, newMotif);
				currentMotif = newMotif;
				clearAMotif(currentMotif);
				setDisable(false);
				btnDeleteMotif.setDisable(true);
				updateIHM();
			} else {
				// throw something
			}
		}
	}
	
	@FXML
	private void defineWallStructure(ActionEvent event) throws IOException {
		
		if(currentAnimation != null){
			FXMLLoader loader = new FXMLLoader(getClass().getResource("structView.fxml"));
			BorderPane root = (BorderPane) loader.load();
	
			StructController structController = loader.<StructController> getController();
			structController.initialization(currentAnimation);
	
			Scene scene = new Scene(root);
	
			Stage structStage = new Stage();
			structStage.setScene(scene);
			structStage.initModality(Modality.APPLICATION_MODAL);
			structStage.showAndWait();
		}
	}
	
	@FXML
	private void saveAnimation(ActionEvent event){
		Document document = XmlBuilder.buildXML(currentAnimation);
		
		final FileChooser dialog = new FileChooser(); 
	    final File file = dialog.showSaveDialog(tfRed.getScene().getWindow());
		
	    if(file != null){
	    	XmlBuilder.saveXML(file.getPath(), document);
	    }
	    else{
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Error");
	    	alert.setHeaderText("Error");
	    	alert.setContentText("Save error");
	    	alert.showAndWait();
	    }
	}
	
	@FXML
	private void openAnimation(ActionEvent event){
		final FileChooser dialog = new FileChooser(); 
		final File file = dialog.showOpenDialog(tfRed.getScene().getWindow());
		
	    if(file != null){
		    currentAnimation = XmlBuilder.openAnimation(file.getPath());//file.getPath(), currentAnimation);
		    initializeGpWall(currentAnimation.getWidth(), currentAnimation.getHeigh());
		    currentMotif = currentAnimation.getMotif(1);
		    updateIHM();
		    setDisable(false);
	    }
	    else{	    	
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Error");
	    	alert.setHeaderText("Error");
	    	alert.setContentText("Open error");
	    	alert.showAndWait();
	    }
	}

	private void initializeGpWall(int nbColumns, int nbRows){
		gpWall = new GridPane();
		gpWall.setPrefWidth(nbColumns * 20);
		gpWall.setPrefHeight(nbRows * 20);
		
		for (int i = 0; i < nbColumns; i++) {
			for (int j = 0; j < nbRows; j++) {
				Rectangle myRect = new Rectangle(20, 20);
				myRect.setOnMouseClicked(mouseHandler);
				myRect.setFill(Color.WHITE);
				gpWall.add(myRect, i, j);
			}
		}
		
		// Add the GridPane in the center of the borderPane
		gpWall.setGridLinesVisible(true);
		gpWall.setAlignment(Pos.CENTER);
		bpWall.setCenter(gpWall);
	}
	
	private void clearAMotif(Motif m){
		for (int i = 0; i < currentAnimation.getWidth(); i++) {
			for (int j = 0; j < currentAnimation.getHeigh(); j++) {
				m.setColor(Color.BLACK, i, j);
			}
		}
		updateWallFromMotif();
	}
	
	@FXML
	private void clearMotif(ActionEvent event){
		clearAMotif(currentMotif);
	}
	
	@FXML
	private void addEmptyMotif(ActionEvent event) {

		if (currentAnimation != null) {
			Motif newMotif = new Motif(currentAnimation.getWidth(), currentAnimation.getHeigh()); 
			clearAMotif(newMotif);
			currentAnimation.add(currentMotif.getId(), newMotif);
			currentMotif = newMotif;
			btnDeleteMotif.setDisable(false);
			
			updateIHM();
		}
	}
	
	@FXML
	private void duplicateMotif(ActionEvent event) {

		if (currentAnimation != null) {
			Motif newMotif = new Motif(currentAnimation.getWidth(), currentAnimation.getHeigh()); 
			updateMotifFromWall(newMotif);
			newMotif.setTime(currentMotif.getTime());
			currentAnimation.add(currentMotif.getId(), newMotif);
			currentMotif = newMotif;
			btnDeleteMotif.setDisable(false);
			updateIHM();
		}
	}
	
	
	@FXML
	private void colorExamplePressed (MouseEvent event){
		Rectangle rect = (Rectangle)event.getSource();
		Color rectColor = (Color)rect.getFill();
		sliderRed.setValue((rectColor.getRed()*255)/16);
		sliderGreen.setValue((rectColor.getGreen()*255)/16);
		sliderBlue.setValue((rectColor.getBlue()*255)/16);
	}

	@FXML 
	private void playAllAnimation (ActionEvent event){
		if(currentAnimation != null){
			setDisableIHMforPlay(true);
			thread = new PlayThread(this, 0);
			thread.start();
		}
	}
	
	@FXML
	private void deleteMotif(){
		if(currentAnimation != null && currentAnimation.size() != 1){
			int id = currentMotif.getId();
			
			if(id == 1){
				currentMotif = currentAnimation.getMotif(id+1);	
			}
			else{
				currentMotif = currentAnimation.getMotif(id-1);		
			}
			
			currentAnimation.remove(id);
			updateIHM();
			updateWallFromMotif();
		}
		if(currentAnimation.size() == 1){
			btnDeleteMotif.setDisable(true);
		}
		
	}	
	
	@FXML 
	private void playAnimation (ActionEvent event){
		if(currentAnimation != null){
			setDisableIHMforPlay(true);
			thread = new PlayThread(this, currentMotif.getId());
			thread.start();
		}
	}
	
	public void setDisableIHMforPlay(boolean bool){
		btnPlay.setDisable(bool);
		btnPlayAll.setDisable(bool);
		btnStop.setDisable(!bool);
		btnDuplicateMotif.setDisable(bool);
		btnAddEmptyMotif.setDisable(bool);
		btnDeleteMotif.setDisable(bool);
		vbRightPane.setDisable(bool);
		hbSelectMotifPane.setDisable(bool);
		miNew.setDisable(bool);
		miSave.setDisable(bool);
		miOpen.setDisable(bool);
		miPlayAll.setDisable(bool);
		miPlay.setDisable(bool);
		miStop.setDisable(!bool);
		miAddEmptyMotif.setDisable(bool);
		miDuplicateMotif.setDisable(bool);
		miDeleteMotif.setDisable(bool);
		miClear.setDisable(bool);
	}
	
	private void setDisable(boolean bool){
		spMainPane.setDisable(bool);
		miSave.setDisable(bool);
		miPlayAll.setDisable(bool);
		miPlay.setDisable(bool);
		miStop.setDisable(bool);
		miAddEmptyMotif.setDisable(bool);
		miDuplicateMotif.setDisable(bool);
		miDeleteMotif.setDisable(bool);
		miClear.setDisable(bool);
	}
	
	@FXML
	private void stopAnimation(){
		thread.stopThread();
		setDisableIHMforPlay(false);
		updateWallFromMotif();
	}
	
	static public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
				return node;
			}
		}
		return null;
	}

	private void updateCurrentColor() {
		currentColor = Color.rgb(Integer.valueOf(tfRed.getText()) * 16, Integer.valueOf(tfGreen.getText()) * 16,Integer.valueOf(tfBlue.getText()) * 16);
		rectColor.setFill(currentColor);
	}

	private void updateWallFromMotif() {

		for (int i = 0; i < currentAnimation.getWidth(); i++) {
			for (int j = 0; j < currentAnimation.getHeigh(); j++) {
				Rectangle currentRect = (Rectangle) getNodeFromGridPane(gpWall, i, j);
				currentRect.setFill(currentMotif.getColor(i, j));
			}
		}
	}

	private void updateMotifFromWall() {
		for (int i = 0; i < currentAnimation.getWidth(); i++) {
			for (int j = 0; j < currentAnimation.getHeigh(); j++) {
				Rectangle currentRect = (Rectangle) getNodeFromGridPane(gpWall, i, j);
				currentMotif.setColor((Color) currentRect.getFill(), i, j);
			}
		}
	}
	
	private void updateMotifFromWall(Motif m) {
		for (int i = 0; i < currentAnimation.getWidth(); i++) {
			for (int j = 0; j < currentAnimation.getHeigh(); j++) {
				Rectangle currentRect = (Rectangle) getNodeFromGridPane(gpWall, i, j);
				m.setColor((Color) currentRect.getFill(), i, j);
			}
		}
	}

	private void updateIHM() {
		sliderMotif.setMin(1);
		sliderMotif.setMax(currentAnimation.size());
		sliderMotif.setValue(currentMotif.getId());
		lbMotifNumber.setText("Motif n°" + String.valueOf(currentMotif.getId()));
		tfMotifTime.setText(String.valueOf(currentMotif.getTime()));
	}
	
	private void updateMotifTime(){
		currentMotif.setTime(Integer.valueOf(tfMotifTime.getText()));
	}
	
	public void initialization() {
		
		setDisable(true);
		
		miNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		//miNew.setGraphic();
		miSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		miOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		miPlayAll.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN)); 
		miPlay.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN)); 
		miStop.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN)); 
		miAddEmptyMotif.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN)); 
		miDuplicateMotif.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
		miDeleteMotif.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN)); 
		miClear.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
		
		currentColor = Color.BLACK;
		
		mouseHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				Rectangle rect = (Rectangle)mouseEvent.getSource();
				rect.setFill(currentColor);
				updateMotifFromWall();	
			}
		};

		tfMotifNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (currentAnimation != null) {
					if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) > 0
							&& Integer.valueOf(newValue) <= currentAnimation.size()) {
						currentMotif = currentAnimation.getMotif(Integer.valueOf(newValue));
						updateIHM();
						updateWallFromMotif();
					} else {
						tfMotifNumber.setText(oldValue);
					}
				}
			}
		});
		
		tfMotifTime.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) >= 1 && Integer.valueOf(newValue) <= 65535) {
					updateMotifTime();
				} else {
					tfMotifTime.setText(oldValue);
				}
			}
		});

		tfRed.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) >= 0 && Integer.valueOf(newValue) <= 15) {
					updateCurrentColor();
				} else {
					tfRed.setText(oldValue);
				}
			}
		});

		tfGreen.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) >= 0 && Integer.valueOf(newValue) <= 15) {
					updateCurrentColor();
				} else {
					tfGreen.setText(oldValue);
				}
			}
		});

		tfBlue.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("[\\d]+") && Integer.valueOf(newValue) >= 0 && Integer.valueOf(newValue) <= 15) {
					updateCurrentColor();
				} else {
					tfBlue.setText(oldValue);
				}
			}
		});

		sliderRed.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				tfRed.setText(String.valueOf(newValue.intValue()));
			}
		});

		sliderGreen.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				tfGreen.setText(String.valueOf(newValue.intValue()));
			}
		});

		sliderBlue.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				tfBlue.setText(String.valueOf(newValue.intValue()));
			}
		});

		sliderMotif.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				tfMotifNumber.setText(String.valueOf(newValue.intValue()));
			}
		});

	}
	
	public Animation getAnimation(){
		return currentAnimation;
	}
	
	public GridPane getWall(){
		return gpWall;
	}
}
