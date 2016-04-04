package application;

import java.io.IOException;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {

	private Animation currentAnimation;
	private Motif currentMotif;
	private GridPane gpWall;
	private Color currentColor;
	private EventHandler<MouseEvent> mouseHandler ;

	@FXML
	private Rectangle rectColor;

	@FXML
	private Button btnStart;

	@FXML
	private Label lbMotifNumber;

	@FXML
	private BorderPane bpWall;

	@FXML
	private Slider sliderMotif, sliderRed, sliderGreen, sliderBlue;

	@FXML
	private TextField tfRed, tfMotifNumber, tfMotifTime, tfGreen, tfBlue;

	@FXML
	private void addMotif(ActionEvent event) {

		if (currentAnimation != null) {
			Motif newMotif = new Motif(currentAnimation.getWidth(), currentAnimation.getHeigh());
			currentAnimation.add(newMotif);
			currentMotif = newMotif;

			updateIHM();
			updateWallFromMotif();
		}
	}
	
	@FXML
	private void onColorExamplePressed (MouseEvent event){
		Rectangle rect = (Rectangle)event.getSource();
		Color rectColor = (Color)rect.getFill();
		sliderRed.setValue((rectColor.getRed()*255)/16);
		sliderGreen.setValue((rectColor.getGreen()*255)/16);
		sliderBlue.setValue((rectColor.getBlue()*255)/16);
	}

	@FXML
	private void timeOnChange() {

	}

	@FXML
	private void createNewAnimation(ActionEvent event) throws IOException {

		HashMap<String, String> hmInformation = new HashMap<String, String>();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("popupCreateAnim.fxml"));
		VBox root = (VBox) loader.load();

		PopupController popupController = loader.<PopupController> getController();
		popupController.sethmInformation(hmInformation);

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

				gpWall.setPrefWidth(nbColumns * 20);
				gpWall.setPrefHeight(nbRows * 20);

				for (int i = 0; i < nbColumns; i++) {
					for (int j = 0; j < nbRows; j++) {
						Rectangle myRect = new Rectangle(20, 20);

						/*
						 * Définition du comportement du rectangle suite à un
						 * clic
						 */
						myRect.setOnMouseClicked(mouseHandler);
						myRect.setFill(Color.WHITE);
						gpWall.add(myRect, i, j);
					}
				}

				// Add the GridPane in the center of the borderPane
				gpWall.setGridLinesVisible(true);
				gpWall.setAlignment(Pos.CENTER);
				bpWall.setCenter(gpWall);

				Motif newMotif = new Motif(nbColumns, nbRows);

				currentAnimation = new Animation(hmInformation.get("Name"), nbColumns, nbRows);
				currentAnimation.add(newMotif);
				currentMotif = newMotif;

				/*
				 * Pas Marché
				 * 
				 * Stage stage = (Stage)btnStart.getScene().getWindow();
				 * stage.setTitle("Animation Editor - " +
				 * hmInformation.get("Name"));
				 * 
				 */
				updateIHM();
			} else {
				// throw something
			}
		}
	}

	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
				return node;
			}
		}
		return null;
	}

	private void updateCurrentColor() {
		currentColor = Color.rgb(Integer.valueOf(tfRed.getText()) * 16, Integer.valueOf(tfGreen.getText()) * 16,
				Integer.valueOf(tfBlue.getText()) * 16);
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

	private void updateIHM() {
		sliderMotif.setMin(1);
		sliderMotif.setMax(currentAnimation.size());
		sliderMotif.setValue(currentMotif.getId());
		tfMotifNumber.setText(String.valueOf(currentMotif.getId()));
		lbMotifNumber.setText("Motif n°" + String.valueOf(currentMotif.getId()));
		tfMotifTime.setText(String.valueOf(currentMotif.getTime()));
	}

	public void initialization() {

		
		mouseHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				Rectangle rect = (Rectangle)mouseEvent.getSource();
				rect.setFill(currentColor);
				updateMotifFromWall();	
			}
		};
		
		gpWall = new GridPane();
		/*
		gpWall.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				Rectangle rect = (Rectangle)mouseEvent.getTarget();
				rect.setFill(currentColor);
				updateMotifFromWall();	
			}
		});
		*/
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
}
