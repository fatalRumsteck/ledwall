package controller;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Animation;
import model.WallPanel;

public class StructController {
	
	private GridPane gpWall;
	private Animation animation;
	private LinkedList<WallPanel> panelList;
	private EventHandler<ActionEvent> btnSuppHandler;
	ArrayList<Color> colorList;
	
	
	@FXML
	private BorderPane bpMainPane;
	
	@FXML
	private VBox vbListPanel;
	
	public  void initialization(Animation animation){
		colorList = new ArrayList<Color>();
		colorList.add(new Color(1, 0, 0, 0.25));
		colorList.add(new Color(1, 1, 0, 0.25));
		
		this.animation = animation;
		
		panelList = new LinkedList<WallPanel>();
		
		gpWall = new GridPane();
		gpWall.setPrefWidth(animation.getWidth() * 20);
		gpWall.setPrefHeight(animation.getHeigh() * 20);
		
		for (int i = 0; i < animation.getWidth(); i++) {
			for (int j = 0; j < animation.getHeigh(); j++) {
				Rectangle myRect = new Rectangle(20, 20);
				myRect.setFill(Color.WHITE);
				gpWall.add(myRect, i, j);
			}
		}
		
		// Add the GridPane in the center of the borderPane
		gpWall.setGridLinesVisible(true);
		gpWall.setAlignment(Pos.CENTER);
		bpMainPane.setCenter(gpWall);
		
		
		btnSuppHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button btn = (Button) event.getSource();
				Node nodeToRemove = null;
				
				for(Node child: vbListPanel.getChildren()){
				    if(child.getId().equals(btn.getParent().getId())){
				    	nodeToRemove = child;
				    }
				}
				vbListPanel.getChildren().remove(nodeToRemove);
				panelList.remove(Integer.valueOf(btn.getParent().getId())-1);
			}
		};
	}
	
	@FXML
	private void addPanel(ActionEvent event) throws IOException{
		WallPanel newPanel = new WallPanel();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/addPanelView.fxml"));
		VBox root = (VBox) loader.load();
		AddPanelController controller = loader.<AddPanelController> getController();
		
		controller.initialization(animation);
		controller.setNewPanel(newPanel);
		
		Scene scene = new Scene(root);
		Stage addPanelStage = new Stage();
		
		addPanelStage.setScene(scene);
		addPanelStage.initModality(Modality.APPLICATION_MODAL);
		addPanelStage.showAndWait();
		
		if(newPanel != null){
			
			LinkedList<WallPanel> listSuperposition = WallPanel.checkPanel(newPanel, panelList);
			
			if(listSuperposition.size() == 0){
				newPanel.setId(panelList.size()+1);
				addListPanel(newPanel);
				updateWall(newPanel);
				panelList.add(newPanel);
			}
			else{
				Alert alert = new Alert(AlertType.ERROR);
		    	alert.setTitle("Error");
		    	alert.setHeaderText("Error");
		    	StringBuffer message = new StringBuffer("The new panel conflict with :\n");
		    	listSuperposition.forEach(panel -> message.append(panel.toString() + "\n"));
		    	alert.setContentText(message.toString());
		    	alert.showAndWait();
			}
		}
	}
	
	private void addListPanel(WallPanel panel){
		HBox mainBox = new HBox();
		mainBox.setId(String.valueOf(panelList.size()+1));
		mainBox.setAlignment(Pos.CENTER_LEFT);
		mainBox.setPadding(new Insets(5,5,5,5));
		
		String info;
		//Label lbNum = new Label("n°");
		info = "n°" + String.valueOf(panelList.size()+1) + " : " + (int)panel.getCorner1().getX() + "x" + (int)panel.getCorner1().getY() + " - " + (int)panel.getCorner2().getX() + "x" + (int)panel.getCorner2().getY() + "     ";
		Label lbInfo = new Label(info);
		
		Button btnSupp = new Button("X");
		btnSupp.setOnAction(btnSuppHandler);
		
		mainBox.getChildren().add(lbInfo);
		mainBox.getChildren().add(btnSupp);
		
		vbListPanel.getChildren().add(mainBox);		
	}

	private void updateWall(WallPanel panel){
		gpWall.getChildren().stream()
							.filter(node -> (GridPane.getColumnIndex(node) >= panel.getCorner1().getX() && GridPane.getColumnIndex(node) <= panel.getCorner2().getX() 
											 && GridPane.getRowIndex(node) >= panel.getCorner1().getY() && GridPane.getRowIndex(node) <= panel.getCorner2().getY()))
							.forEach(node -> ((Rectangle)node).setFill(colorList.get((panel.getId()-1)%2)));
	}
	
}
