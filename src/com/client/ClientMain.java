package com.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application{


	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientGUI UI = new ClientGUI(10, 10, 10, 10);
		
		Scene scene = new Scene(UI);
		UI.connect();
		UI.showList();
		UI.download();
		UI.upload(new Stage());
		UI.displayImage();
		
		
		primaryStage.setTitle("CLIENT");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		System.out.println("Hello");
		launch(args);
		
	}
}
