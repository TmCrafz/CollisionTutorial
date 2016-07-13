package org.tmcrafz.collisiontutorial;

import org.tmcrafz.collisiontutorial.Game;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class CollisionTutorial extends Application {	
	
	public static void main(String[] args) 
    {		
        launch(args);
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		int windowWidth = 800;
		int windowHeigth = 600;
				
		primaryStage.setTitle("Collision Tutorial");
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		Canvas canvas = new Canvas(windowWidth, windowHeigth);
		root.getChildren().add(canvas);
		
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		
		Game game = new Game(scene, graphicsContext, windowWidth, windowHeigth);
		game.startGame();
		
		primaryStage.show();	
			
	}
	
	

}
