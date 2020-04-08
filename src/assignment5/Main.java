package assignment5;

import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	Critter.createCritter("Critter1");
    	Critter.createCritter("Critter2");
    	Critter.createCritter("Critter3");
    	Critter.createCritter("Critter4");
    	Critter.worldTimeStep();
    	GridPane pane = new GridPane();
    	Critter.displayWorld(pane);
    }
}
