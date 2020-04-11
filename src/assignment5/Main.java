package assignment5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;

public class Main extends Application {
	static boolean isLargeGrid = false;
	static ScrollPane worldScrollPane = null;
	// static GridPane statsTable = null;
	// static Label statsMessage = null;
	
    private static String myPackage; // package of Critter file
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	if (Params.WORLD_WIDTH >= 65 || Params.WORLD_HEIGHT >= 49) {
    		isLargeGrid = true;
    		worldScrollPane = new ScrollPane();
    		worldScrollPane.setPannable(true);
    		worldScrollPane.setPrefViewportHeight(1000);
    		worldScrollPane.setPrefViewportWidth(1400);
    	}
    	
    	// Root node containing the world and controlPanel
    	GridPane root = new GridPane();
    	
    	// 2D grid of the Critter world, initialized to empty 
    	GridPane world = new GridPane();
    	Critter.displayWorld(world); // create empty grid
    	
    	// Label node for string results from runStats()
    	Label statsMessage = new Label("");
    	
    	// Pie chart node for string results from runStats()
    	PieChart statsPieChart = new PieChart();
    	
    	DecimalFormat df = new DecimalFormat("#.##"); 
    	
    	// doTimeStep interface
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
				// reset data from previous iteration
				statsMessage.setText("");
				statsPieChart.getData().clear();
				statsPieChart.setTitle("");
				
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
    	
    	// createCritter interface
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
				// reset data from previous iteration
				statsMessage.setText("");
				statsPieChart.getData().clear();
				statsPieChart.setTitle("");
				
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
    	
    	// runStats interface
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
				// reset data from previous iteration
				statsMessage.setText("");
				statsPieChart.getData().clear();
				statsPieChart.setTitle("");
				
				String className = runStats_name.getText();
				String fullClassName = myPackage + "." + className;
				try {
    	    		List<Critter> critters = Critter.getInstances(className);
    				Method method = Class.forName(fullClassName).getMethod("runStats", List.class); // this only gets the class of the first list element, not sure if it works for lists with subclasses as well
    				String statsString = (String) method.invoke(null, critters);
    				String[] dataLine = statsString.split("\n");
    				System.out.println(statsString);
    				statsMessage.setText(statsString);
    				
    				// special statistics graphics updated below
    				switch(className) {
    					case "Goblin":
    						double totalGoblins = Integer.parseInt(dataLine[0].split(" ")[0]);
    						double numStraight = Integer.parseInt(dataLine[1].split(" ")[0].replace("%", ""));
    						double numBack = Integer.parseInt(dataLine[2].split(" ")[0].replace("%", ""));
    						double numRight = Integer.parseInt(dataLine[3].split(" ")[0].replace("%", ""));
    						double numLeft = Integer.parseInt(dataLine[4].split(" ")[0].replace("%", ""));
    						PieChart.Data slice1 = new PieChart.Data((df.format(100 * numStraight / totalGoblins) + "% " + dataLine[1].split(" ")[1]), (numStraight / totalGoblins) * 360);
    						PieChart.Data slice2 = new PieChart.Data((df.format(100 * numBack / totalGoblins) + "% " + dataLine[2].split(" ")[1]), (numBack / totalGoblins) * 360);
    						PieChart.Data slice3 = new PieChart.Data((df.format(100 * numRight / totalGoblins) + "% " + dataLine[3].split(" ")[1]), (numRight / totalGoblins) * 360);
    						PieChart.Data slice4 = new PieChart.Data((df.format(100 * numLeft / totalGoblins) + "% " + dataLine[4].split(" ")[1]), (numLeft / totalGoblins) * 360);

    						statsPieChart.getData().add(slice1);
    						statsPieChart.getData().add(slice2);
    						statsPieChart.getData().add(slice3);
    						statsPieChart.getData().add(slice4);
    						break;
    							
    					case "Critter1":
    						double totalVirus = Integer.parseInt(dataLine[0].split(" ")[0]);
    						double numStrainA = Integer.parseInt(dataLine[1].split(" ")[0]);
    						double numStrainB = Integer.parseInt(dataLine[2].split(" ")[0]);
    						PieChart.Data sliceA = new PieChart.Data((df.format(100 * numStrainA / totalVirus) + "% " + dataLine[1].split(" ")[1]), (numStrainA / totalVirus) * 360);
    						PieChart.Data sliceB = new PieChart.Data((df.format(100 * numStrainB / totalVirus) + "% " + dataLine[2].split(" ")[1]), (numStrainB / totalVirus) * 360);
    						
    						statsPieChart.setTitle("CORONAVIRUS STATISTICS");
    						statsPieChart.setLabelLineLength(20);
    						statsPieChart.setStartAngle(180); 
    						statsPieChart.getData().add(sliceA);
    						statsPieChart.getData().add(sliceB);
    						break;
    						
    					case "Critter2":
    						double totalHumans = Integer.parseInt(dataLine[0].split(" ")[0]);
    						double numMales = Integer.parseInt(dataLine[1].split(" ")[0]);
    						double numFemales = Integer.parseInt(dataLine[2].split(" ")[0]);
    						PieChart.Data maleSlice = new PieChart.Data((df.format(100 * numMales / totalHumans) + "% " + dataLine[1].split(" ")[1]), (numMales / totalHumans) * 360);
    						PieChart.Data femaleSlice = new PieChart.Data((df.format(100 * numFemales / totalHumans) + "% " + dataLine[2].split(" ")[1]), (numFemales / totalHumans) * 360);
    						
    						statsPieChart.setTitle("HUMANS STATISTICS");
    						statsPieChart.setLabelLineLength(20);
    						statsPieChart.setStartAngle(180); 
    						statsPieChart.getData().add(maleSlice);
    						statsPieChart.getData().add(femaleSlice);
    						break;
    						
    					case "Critter3":
    						break;
    						
    					case "Critter4":
    						break;
    						
    					default:
    				}
    				
    			} catch (InvalidCritterException e) {
					runStats_err.setText("Invalid Input.");
				} catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException e) { // run if no method exists
					System.out.println("method does not exist to invoke...");
					try {
						String statsString = Critter.runStats(Critter.getInstances(className));
						// DEBUG System.out.println(statsString);
						statsMessage.setText(statsString);
						
					} catch (InvalidCritterException e1) {
						runStats_err.setText("Invalid Input.");
					}
					
    			} 
				
			}
    	});
    	
    	// setSeed interface/button
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
				// reset data from previous iteration
				statsMessage.setText("");
				statsPieChart.getData().clear();
				statsPieChart.setTitle("");
				
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
    	
    	// quit interface/button
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
    	
    	controller.getChildren().add(statsMessage);
    	controller.getChildren().add(statsPieChart);
    		
    	if (Main.isLargeGrid) {
    		worldScrollPane.setContent(world);
    		root.add(worldScrollPane, 0, 0);
    		GridPane.setMargin(root.getChildren().get(0), new Insets(20));
    	}
    	else {
    		root.add(world, 0, 0);
    		GridPane.setMargin(root.getChildren().get(0), new Insets(20));
    	}
    	root.add(controller, 1, 0);
    	
    	
    	GridPane.setMargin(root.getChildren().get(1), new Insets(20));
    	primaryStage.setScene(new Scene(root, 2000, 1200)); // resized scene to be size of full screen
    	primaryStage.setTitle("Critters II: Jin Lee and Andy Wu");
    	primaryStage.show();
    }
}
