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
	
	public  void initialization(Animation animation, LinkedList<WallPanel> listPanel){
		colorList = new ArrayList<Color>();
		colorList.add(new Color(1, 0, 0, 0.25));
		colorList.add(new Color(0, 1, 0, 0.25));
		colorList.add(new Color(1, 0.5, 0, 0.25));
		colorList.add(new Color(0, 0, 1, 0.25));
		colorList.add(new Color(1, 1, 0, 0.25));
		
		this.animation = animation;
		this.panelList = listPanel;
		
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
				
				vbListPanel.getChildren()
				   		   .stream()
				   		   .filter(child -> Integer.valueOf(child.getId()) >= Integer.valueOf(btn.getParent().getId()))
				   		   .collect(Collectors.toCollection(ArrayList::new))
				   		   .forEach(node -> vbListPanel.getChildren().remove(node));
				
				removeNodesAndUpdateWall(Integer.valueOf(btn.getParent().getId()));
			}
		};
		
		if(panelList.size() != 0 ){
			panelList.forEach(panel -> addPanelIHM(panel));
		}
	}
	
	private void removeNodesAndUpdateWall(Integer index){

		panelList.stream()
				 .filter(panel -> panel.getId() >= index)
				 .collect(Collectors.toCollection(ArrayList::new))
				 .forEach(panel -> {
					 updateWall(panel, Color.WHITE);
					 panelList.remove(panel);
				 });
	}
	
	@FXML
	private void addPanelPressed(ActionEvent event) throws IOException{
		WallPanel newPanel = new WallPanel();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/addPanelView.fxml"));
		VBox root = (VBox) loader.load();
		
		AddPanelController controller = loader.<AddPanelController> getController();
		controller.initialization(animation);
		controller.setNewPanel(newPanel);
		
		Scene scene = new Scene(root);
		Stage addPanelStage = new Stage();
		
		addPanelStage.setScene(scene);
		addPanelStage.setTitle("Add new panel");
		addPanelStage.initModality(Modality.APPLICATION_MODAL);
		addPanelStage.showAndWait();
		
		if(newPanel != null){
			
			
			boolean panelOk = newPanel.IsCorrect();
			LinkedList<WallPanel> listSuperposition = newPanel.checkPanel(panelList);
			
			if(listSuperposition.size() == 0 && panelOk){
				newPanel.setId(panelList.size()+1);
				addPanelIHM(newPanel);
				panelList.add(newPanel);
			}
			else{
				Alert alert = new Alert(AlertType.ERROR);
		    	alert.setTitle("Error");
		    	alert.setHeaderText("Error");
		    	StringBuffer message = new StringBuffer("");
		    	if(listSuperposition.size() != 0){
		    		message.append("The new panel conflict with :\n");
		    		listSuperposition.forEach(panel -> message.append(panel.toString() + "\n"));
		    	} else {
		    		message.append("Please set the corner1 as the top left led of the panel\nand the corner1 as the bottom right");
		    	}
		    	alert.setContentText(message.toString());
		    	alert.showAndWait();
			}
		}
	}
	
	private void addPanelIHM(WallPanel newPanel){
		addListPanel(newPanel);
		updateWall(newPanel, colorList.get((newPanel.getId()-1)%colorList.size()));
	}
	
	
	private void addListPanel(WallPanel panel){
		HBox mainBox = new HBox();
		mainBox.setId(String.valueOf(panel.getId()));
		mainBox.setAlignment(Pos.CENTER_LEFT);
		mainBox.setPadding(new Insets(5,5,5,5));
		
		String info;
		//Label lbNum = new Label("n°");
		info = "n°" + String.valueOf(panel.getId()) + " : " + (int)panel.getCorner1().getX() + "x" + (int)panel.getCorner1().getY() + " - " + (int)panel.getCorner2().getX() + "x" + (int)panel.getCorner2().getY() + "     ";
		Label lbInfo = new Label(info);
		lbInfo.setId("str");
		Button btnSupp = new Button("X");
		btnSupp.setOnAction(btnSuppHandler);
		
		mainBox.getChildren().add(lbInfo);
		mainBox.getChildren().add(btnSupp);
		
		vbListPanel.getChildren().add(mainBox);		
	}

	private void updateWall(WallPanel panel, Color color){
		gpWall.getChildren().stream()
							.filter(node -> nodeIsAffected(node, panel))
							.forEach(node -> ((Rectangle)node).setFill(color));
		
	}
	
	private boolean nodeIsAffected(Node node, WallPanel panel){
		
		if(GridPane.getColumnIndex(node) == null || GridPane.getRowIndex(node) == null){
			return false;
		} else {
			return (GridPane.getColumnIndex(node) >= (int)panel.getCorner1().x && GridPane.getColumnIndex(node) <= (int)panel.getCorner2().x
					&& GridPane.getRowIndex(node) >= (int)panel.getCorner1().y && GridPane.getRowIndex(node) <= (int)panel.getCorner2().y);	
		}
	}
	
}
