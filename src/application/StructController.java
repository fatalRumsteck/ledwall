package application;


import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StructController {
	
	private GridPane gpWall;
	private Animation animation;
	private ArrayList<WallPanel> panelList;
	private EventHandler<ActionEvent> btnSuppHandler;
	
	@FXML
	private BorderPane bpMainPane;
	
	@FXML
	private VBox vbListPanel;
	
	public  void initialization(Animation animation){
		this.animation = animation;
		
		panelList = new ArrayList<WallPanel>();
		
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
		newPanel.setCorner1(0, 0);
		newPanel.setCorner2(1, 1);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("addPanelController.fxml"));
		BorderPane root = (BorderPane) loader.load();

		addPanelController controller = loader.<addPanelController> getController();
		controller.initialization(newPanel);

		Scene scene = new Scene(root);

		Stage addPanelStage = new Stage();
		addPanelStage.setScene(scene);
		addPanelStage.initModality(Modality.APPLICATION_MODAL);
		addPanelStage.showAndWait();
		
		if(newPanel != null){
			newPanel.setId(panelList.size()+1);
			addListPanel(newPanel);
			panelList.add(newPanel);
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

	
}
