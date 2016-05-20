package model;

import java.net.URL;

import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			
			//FXMLLoader loader = new FXMLLoader(new URL("file:/C:/Users/Antoine/git/AnimationEditor/bin/view/mainView.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/mainView.fxml"));
			BorderPane root = (BorderPane) loader.load();

			Scene scene = new Scene(root);

			Controller controller = loader.<Controller> getController();
			controller.initialization();

			primaryStage.setScene(scene);
			primaryStage.setTitle("Animation Editor");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
