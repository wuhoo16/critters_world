package assignment5;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	// Root node containing the world and controlPanel
    	GridPane root = new GridPane();
    	
    	// 2D grid of the Critter world, initialized to empty 
    	GridPane world = new GridPane();
    	Critter.displayWorld(world); // create empty grid
    	
    	// Controller containing all command inputs
    	GridPane controller = new GridPane();
    	// doTimeStep
    	Label timeStep_l = new Label("Do Time Step");
    	Button timeStep_bt = new Button("Time step(s)");
    	
    	controller.add(timeStep_l, 0, 0);
    	controller.add(timeStep_bt, 0, 1);
    	timeStep_bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Critter.worldTimeStep();
				Critter.displayWorld(world);		
			}
    	});
    	// createCritter (Eventually change to TextField + drop down menu)
    	Label createCritter_l = new Label("Create Critter");
    	Button createCritter_bt = new Button("Create");
    	TextField createCritter_name = new TextField();
    	createCritter_name.setPromptText("Enter a Critter class");
    	Text createCritter_err = new Text("");
    	
    	controller.add(createCritter_l, 0, 2);
    	controller.add(createCritter_name, 0, 3);
    	controller.add(createCritter_bt, 1, 3);
    	controller.add(createCritter_err, 2, 3);
    	createCritter_bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					createCritter_err.setText("");
					String critter_class_name = createCritter_name.getText();
					Critter.createCritter(critter_class_name);
					Critter.displayWorld(world);
				} catch (InvalidCritterException e) {
					createCritter_err.setText("Invalid Critter");
				}
			}
    	});
    	
    	root.add(world, 0, 0);
    	root.add(controller, 1, 0);
    	primaryStage.setScene(new Scene(root, 500, 250));
    	primaryStage.show();
    }
}
