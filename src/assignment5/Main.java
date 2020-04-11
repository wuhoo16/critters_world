package assignment5;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;

public class Main extends Application {
	static boolean isLargeGrid = false;

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	if (Params.WORLD_HEIGHT > 50 || Params.WORLD_WIDTH > 50) {
    		isLargeGrid = true;
    	}
    	
    	// Root node containing the world and controlPanel
    	GridPane root = new GridPane();
    	
    	// 2D grid of the Critter world, initialized to empty 
    	GridPane world = new GridPane();
    	Critter.displayWorld(world); // create empty grid
    	
    	
    	// doTimeStep
    	Label timeStep_l = new Label("Do Time Step");
    	TextField timeStep_ct = new TextField();
    	timeStep_ct.setPrefColumnCount(5);
    	timeStep_ct.setPromptText("Steps");
    	Button timeStep_bt = new Button("Step");
    	Text timeStep_err = new Text("");
    	VBox doTimeStep = new VBox(1,
    			timeStep_l,
    			new HBox(timeStep_ct, timeStep_bt, timeStep_err));
    	
    	timeStep_bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					timeStep_err.setText("");
					int steps = 1;
					String step_String = timeStep_ct.getText();
					if(!step_String.equals("")) steps = Integer.parseInt(step_String);		
					for(int i = 0; i < steps; i++) {
						Critter.worldTimeStep();
					}
					Critter.displayWorld(world);		
				} catch (NumberFormatException e) {
					timeStep_err.setText("Invalid step count");
				}
			}
    	});
    	
    	// createCritter
    	Label createCritter_l = new Label("Create Critter");
    	TextField createCritter_name = new TextField();
    	createCritter_name.setPrefColumnCount(5);
    	createCritter_name.setPromptText("Class");
    	TextField createCritter_ct = new TextField();
    	createCritter_ct.setPrefColumnCount(5);
    	createCritter_ct.setPromptText("Count");
    	Button createCritter_bt = new Button("Create");
    	Text createCritter_err = new Text("");
    	VBox createCritter = new VBox(1, 
    			createCritter_l, 
    			new HBox(createCritter_name, createCritter_ct, createCritter_bt, createCritter_err));

    	createCritter_bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					createCritter_err.setText("");
					int count = 1;
					String critter_class_name = createCritter_name.getText();
					String count_String = createCritter_ct.getText();
					if(!count_String.equals("")) count = Integer.parseInt(count_String);
					for(int i = 0; i < count; i++) {
						Critter.createCritter(critter_class_name);
					}
					Critter.displayWorld(world);
				} catch (InvalidCritterException e) {
					createCritter_err.setText("Invalid Critter");
				} catch (NumberFormatException e) {
					createCritter_err.setText("Invalid Critter count");
				}
			}
    	});
    	
    	// runStats
    	Label runStats_l = new Label("Run Critter stats");
    	// will add functionality to show multiple Critter stats at once
    	TextField runStats_name = new TextField();
    	runStats_name.setPrefColumnCount(5);
    	runStats_name.setPromptText("Class");
    	Button runStats_bt = new Button("Run");
    	Text runStats_err = new Text("");
    	VBox runStats = new VBox(1,
    			runStats_l,
    			new HBox(runStats_name, runStats_bt, runStats_err));
    	runStats_bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO
			}
    	});
    	
    	// setSeed
    	Label setSeed_l = new Label("Set seed");
    	TextField setSeed_num = new TextField();
    	setSeed_num.setPrefColumnCount(5);
    	setSeed_num.setPromptText("Seed");
    	Button setSeed_bt = new Button("Set");
    	Text setSeed_err = new Text("");
    	VBox setSeed = new VBox(1,
    			setSeed_l,
    			new HBox(setSeed_num, setSeed_bt, setSeed_err));
    	
    	setSeed_bt.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event) {
    			try {
    				setSeed_err.setText("");
        			String seed_String = setSeed_num.getText();
        			int seed = Integer.parseInt(seed_String);
        			Critter.setSeed(seed);
    			} catch(NumberFormatException e) {
    				setSeed_err.setText("Invalid seed");
    			}			
    		}
    	});
    	
    	// quit
    	Button quit = new Button("Quit");
    	quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {System.exit(0);}
    	});
    	
    	// Controller containing all command inputs
    	VBox controller = new VBox(5, 
    			doTimeStep,
    			createCritter,
    			runStats,
    			setSeed,
    			quit);
    	if (Main.isLargeGrid) {
    		javafx.scene.control.ScrollPane worldStackPane = new javafx.scene.control.ScrollPane();
    		worldStackPane.setPannable(true);
    		worldStackPane.setContent(world);
    		// TO-DO : set the scrollpane's viewport size to be majority of left side of screen
    		worldStackPane.setPrefViewportHeight(1000);
    		worldStackPane.setPrefViewportWidth(1500);
    		root.add(worldStackPane, 0, 0);
    		GridPane.setMargin(root.getChildren().get(0), new Insets(20));
    	}
    	else {
    		root.add(world, 0, 0);
    		GridPane.setMargin(root.getChildren().get(0), new Insets(20));
    	}
    	root.add(controller, 1, 0);
    	GridPane.setMargin(root.getChildren().get(1), new Insets(20));
    	primaryStage.setScene(new Scene(root, 2000, 1200));
    	primaryStage.setTitle("Critters II: Jin Lee and Andy Wu");
    	primaryStage.show();
    }
}
