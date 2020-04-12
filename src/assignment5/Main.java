package assignment5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.io.File;
import java.io.FilenameFilter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import sun.launcher.resources.launcher;
import sun.misc.Launcher;

public class Main extends Application {
	static boolean isLargeGrid = false;
	static ScrollPane worldScrollPane = null;
	static DecimalFormat df = new DecimalFormat("#.##"); 
	static boolean isRunning = false;
	
    private static String myPackage; // package of Critter file
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
    
    /**
     * Finds all critter class files that are in the assignment5 package at runtime and adds valid, concrete subclasses of Critter to an arrayList.
     * 
     * @param None
     * @return ArrayList of Strings
     */
    private static ArrayList<String> getValidClassNames() {
    	String MyPackageEdited = "";
    	if (!myPackage.startsWith("/")) {
           MyPackageEdited = "/" + myPackage;
        } 
    	
    	URL url = Launcher.class.getResource(MyPackageEdited);
    	File directory = new File(url.getFile().replace("%20", " ")); // replace space unicode with actual spaces (if any folder names have spaces)
    	String[] fileNames = directory.list();
    	ArrayList<String> classNamesList = new ArrayList<String>();
    	
    	for (String s: fileNames) {
    		if (s.contains(".class")) { // only process class files
    			String qualifiedClassName = myPackage + "." + s.replace(".class", "");
				try {
					boolean isValidSubclass = Critter.class.isAssignableFrom(Class.forName(qualifiedClassName));
					boolean isInstantiable = !Modifier.isAbstract(Class.forName(qualifiedClassName).getModifiers());
		        	if (isValidSubclass && isInstantiable) {
		        		classNamesList.add(s.replace(".class", ""));
		        		// DEBUG System.out.println(s.replace(".class", ""));
		        	}
				} catch (ClassNotFoundException e) {
					System.out.println("Reflection error - class not found.");
				}
    		}
    	}
    	
    	return classNamesList;
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
    	
    	// initialize list of critter class names that are in the assignment5 package at runtime
    	ArrayList<String> critterClassNames = getValidClassNames();
    	
    	// Root node containing the world and controlPanel
    	GridPane root = new GridPane();
    	
    	// 2D grid of the Critter world, initialized to empty 
    	GridPane world = new GridPane();
    	Critter.displayWorld(world); // create empty grid
    	
    	// Label node for string results from runStats()
    	Label statsMessage = new Label("");
    	
    	// Pie chart node for string results from runStats()
    	PieChart statsPieChart = new PieChart();
    	
    	
    	// All Control Panel nodes created below---------------------------------------------------------------------------
    	
    	// createCritter interface nodes
    	Label createCritterLabel = new Label("Create Critter");
    	ChoiceBox<String> createCritterMenu = new ChoiceBox<String> ();
    	createCritterMenu.getItems().addAll(critterClassNames);
    	TextField createCritter_ct = new TextField();
    	createCritter_ct.setPrefColumnCount(5);
    	createCritter_ct.setPromptText("Count");
    	Button createCritter_bt = new Button("Create");
    	createCritter_bt.setPrefWidth(55);
    	Text createCritter_err = new Text("");
    	VBox createCritter = new VBox(1, createCritterLabel, new HBox(5, createCritterMenu, createCritter_ct, createCritter_bt, createCritter_err));
    	
    	// doTimeStep interface nodes
    	Label timeStep_l = new Label("Do Time Step");
    	TextField timeStep_ct = new TextField();
    	timeStep_ct.setPrefColumnCount(5);
    	timeStep_ct.setPromptText("Steps");
    	Button timeStep_bt = new Button("Step");
    	timeStep_bt.setPrefWidth(55);
    	Text timeStep_err = new Text("");
    	VBox doTimeStep = new VBox(1, timeStep_l, new HBox(5, timeStep_ct, timeStep_bt, timeStep_err));
    	
    	// Animation slider nodes
    	Label sliderLabel = new Label("Animation Slider (timesteps/sec)");
    	Slider animationSlider = new Slider(0.0, 100.0, 1.0);
    	animationSlider.setPrefWidth(400);
    	animationSlider.setShowTickMarks(true);
    	animationSlider.setShowTickLabels(true);
    	animationSlider.setMinorTickCount(3);
    	animationSlider.setMajorTickUnit(20.0);
    	Button startStopBtn = new Button("Start/Stop");
    	startStopBtn.setPrefWidth(75);
    	VBox slider = new VBox(1, sliderLabel, new HBox(5, animationSlider, startStopBtn));
    	
    	
    	// runStats interface nodes
    	Label runStats_l = new Label("Display Critter Stats");
    	// will add functionality to show multiple Critter stats at once
    	ChoiceBox<String> runStatsMenu = new ChoiceBox<String> ();
    	runStatsMenu.getItems().addAll(critterClassNames);
    	Button runStats_bt = new Button("Display");
    	Text runStats_err = new Text("");
    	VBox runStats = new VBox(1,
    			runStats_l,
    			new HBox(5, runStatsMenu, runStats_bt, runStats_err));
    	
    	// setSeed interface nodes
    	Label setSeed_l = new Label("Set seed");
    	TextField setSeed_num = new TextField();
    	setSeed_num.setPrefColumnCount(5);
    	setSeed_num.setPromptText("Seed");
    	Button setSeed_bt = new Button("Set");
    	setSeed_bt.setPrefWidth(55);
    	Text setSeed_err = new Text("");
    	VBox setSeed = new VBox(1, setSeed_l, new HBox(5, setSeed_num, setSeed_bt, setSeed_err));
    	
    	// clear interface nodes
    	Button clear = new Button("Clear");
    	clear.setPrefWidth(55);
    	
    	// quit interface nodes (and handler)
    	Button quit = new Button("Quit");
    	quit.setPrefWidth(55);
    	quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {System.exit(0);}
    	});
    	
    	// Add clear and quit buttons to an Hbox node
    	HBox clearAndQuit = new HBox(5, clear, quit);
    	
    	
    	// All button handlers are below-------------------------------------------------------------------
    
    	
    	// createCritter button handler
    	createCritter_bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearErrorMessages(timeStep_err, createCritter_err, runStats_err, setSeed_err); // clear old error messages
				
				try {
					int count = 1;
					String critter_class_name = createCritterMenu.getValue();
					String count_String = createCritter_ct.getText();
					if(!count_String.equals("")) count = Integer.parseInt(count_String);
					for(int i = 0; i < count; i++) {
						Critter.createCritter(critter_class_name);
					}
					Critter.displayWorld(world);
					updateStats(statsMessage, statsPieChart, runStatsMenu, runStats_err); // update stats if new critters are created
				} catch (InvalidCritterException e) {
					createCritter_err.setText("Invalid Critter");
				} catch (NumberFormatException e) {
					createCritter_err.setText("Invalid Critter count");
				}
			}
    	});
    	
    	// timeStep button handler
    	timeStep_bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearErrorMessages(timeStep_err, createCritter_err, runStats_err, setSeed_err); // clear old error messages
				
				try {
					int steps = 1;
					String step_String = timeStep_ct.getText();
					if(!step_String.equals("") && step_String != null) steps = Integer.parseInt(step_String);		
					for(int i = 0; i < steps; i++) {
						Critter.worldTimeStep();
					}
					Critter.displayWorld(world);
					updateStats(statsMessage, statsPieChart, runStatsMenu, runStats_err); // update stats for chosen critter
				} catch (NumberFormatException e) {
					timeStep_err.setText("Invalid step count");
				}
			}
    	});
    	
    	// Animation slider start/stop button handler
    	startStopBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// toggle flag... however might cause concurrent modification exception so pherhaps we need to add a stop button and use a different handler
				if (isRunning) { isRunning = false;}
				else { isRunning = true;}
				
				int timeStepsValue = (int) animationSlider.getValue();
				while (isRunning) { // while startStop button has been pressed and hasn't been pressed again...
					// TODO (disable all other buttons besides startStopBtn, and do timeStepsValue # of timeSteps every 1 second
				}
				
				// TODO (if isRunning == false enable other buttons again before leaving handler)
			}
    			
    	});
    	
    	
    	// runStats button handler
    	runStats_bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearErrorMessages(timeStep_err, createCritter_err, runStats_err, setSeed_err);
				updateStats(statsMessage, statsPieChart, runStatsMenu, runStats_err);
			}
    	});
    	
    	
    	// setSeed button handler
    	setSeed_bt.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event) {
    			clearErrorMessages(timeStep_err, createCritter_err, runStats_err, setSeed_err); // clear old error messages
				
    			try {
        			String seed_String = setSeed_num.getText();
        			int seed = Integer.parseInt(seed_String);
        			Critter.setSeed(seed);
        			setSeed_err.setText("Program successfully seeded."); // user feedback for successfully seeding
    			} catch(NumberFormatException e) {
    				setSeed_err.setText("Invalid seed");
    			}			
    		}
    	});
    	
    	// clear button handler
    	clear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearErrorMessages(timeStep_err, createCritter_err, runStats_err, setSeed_err); // clear old error messages
				Critter.clearWorld();
				Critter.displayWorld(world);
				updateStats(statsMessage, statsPieChart, runStatsMenu, runStats_err); // update stats for cleared world
			}
    	});
    
    	
    	// Controller Node containing all command interface nodes
    	VBox controller = new VBox(5, 
    			createCritter,
    			doTimeStep,
    			slider,
    			runStats,
    			setSeed,
    			clearAndQuit);
    	VBox.setMargin(clearAndQuit, new Insets(15, 0, 50, 0)); // add small margin above the clearAndQuit row of buttons and large margin below
    	controller.getChildren().add(statsMessage);
    	controller.getChildren().add(statsPieChart);
    	
    	// Add left-side world grid to scrollpane if needed, and add to root gridpane
    	if (Main.isLargeGrid) {
    		worldScrollPane.setContent(world);
    		root.add(worldScrollPane, 0, 0);
    		GridPane.setMargin(worldScrollPane, new Insets(20));
    	}
    	else {
    		root.add(world, 0, 0);
    		GridPane.setMargin(world, new Insets(20));
    	}
    	
    	// Add right-side control panel to root gridpane
    	root.add(controller, 1, 0);
    	GridPane.setMargin(controller, new Insets(20, 20, 20, 0)); // set margins around the controller
    	
    	// Set the stage and display
    	primaryStage.setScene(new Scene(root, 2000, 1200)); // resized scene to be size of full screen
    	primaryStage.setTitle("Critters II: Jin Lee and Andy Wu");
    	primaryStage.show();
    } //end of start() method

    
    /**
     * Update both the stats message and the pie chart. All critter classes can update the stats message, but only Goblin and Critter 1-4 can display to the pie chart.
     * Usage - call this function when the Run button is pressed ORD anytime the world is updated (and the runStats menu has a selection)
     * 
     * @param Label statsMessage: runStats() output for the current chosen Critter class
     * @param statsPieChart: pie chart graphic (does not display if no critters of chosen type are alive)
     * @param ChoiceBox runStatsMenu: drop down menu of valid critter classes
     * @param Text runStats_err: error message text 
     */
	private static void updateStats(Label statsMessage, PieChart statsPieChart, ChoiceBox<String> runStatsMenu, Text runStats_err) {
		// clear previous data before updating stats
		statsMessage.setText("");
		statsPieChart.getData().clear();
		statsPieChart.setTitle("");
		
		String className = runStatsMenu.getValue();
		if (className == null) { // if no choice selected from runStats menu -> don't update anything
			return;
		}
		
		String fullClassName = myPackage + "." + className;
		try {
			List<Critter> critters = Critter.getInstances(className);
			Method method = Class.forName(fullClassName).getMethod("runStats", List.class); // this only gets the class of the first list element, not sure if it works for lists with subclasses as well
			String statsString = (String) method.invoke(null, critters);
			String[] dataLine = statsString.split("\n");
			// DEBUG System.out.println(statsString);
			statsMessage.setText(statsString);
			
			// special statistics pie chart data updated below
			switch(className) {
				case "Goblin":
					double totalGoblins = Double.parseDouble(dataLine[0].split(" ")[0]);
					double percentageStraight = Double.parseDouble(dataLine[1].split(" ")[0].replace("%", ""));
					double percentageBack = Double.parseDouble(dataLine[2].split(" ")[0].replace("%", ""));
					double percentageRight = Double.parseDouble(dataLine[3].split(" ")[0].replace("%", ""));
					double percentageLeft = Double.parseDouble(dataLine[4].split(" ")[0].replace("%", ""));
					if (totalGoblins > 0) {
						PieChart.Data slice1 = new PieChart.Data((df.format(percentageStraight) + "% " + dataLine[1].split(" ")[1]), (percentageStraight / 100) * 360);
						PieChart.Data slice2 = new PieChart.Data((df.format(percentageBack) + "% " + dataLine[2].split(" ")[1]), (percentageBack / 100) * 360);
						PieChart.Data slice3 = new PieChart.Data((df.format(percentageRight) + "% " + dataLine[3].split(" ")[1]), (percentageRight / 100) * 360);
						PieChart.Data slice4 = new PieChart.Data((df.format(percentageLeft) + "% " + dataLine[4].split(" ")[1]), (percentageLeft / 100) * 360);
		
						statsPieChart.setTitle("GOBLIN STATISTICS");
						statsPieChart.setLabelLineLength(20);
						statsPieChart.setStartAngle(180); 
						statsPieChart.getData().add(slice1);
						statsPieChart.getData().add(slice2);
						statsPieChart.getData().add(slice3);
						statsPieChart.getData().add(slice4);
					}
					break;
						
				case "Critter1":
					double totalVirus = Integer.parseInt(dataLine[0].split(" ")[0]);
					double numStrainA = Integer.parseInt(dataLine[1].split(" ")[0]);
					double numStrainB = Integer.parseInt(dataLine[2].split(" ")[0]);
					if (totalVirus > 0) {
						PieChart.Data sliceA = new PieChart.Data((df.format(100 * numStrainA / totalVirus) + "% " + dataLine[1].split(" ")[1]), (numStrainA / totalVirus) * 360);
						PieChart.Data sliceB = new PieChart.Data((df.format(100 * numStrainB / totalVirus) + "% " + dataLine[2].split(" ")[1]), (numStrainB / totalVirus) * 360);
						
						statsPieChart.setTitle("CORONAVIRUS STATISTICS");
						statsPieChart.setLabelLineLength(20);
						statsPieChart.setStartAngle(180); 
						statsPieChart.getData().add(sliceA);
						statsPieChart.getData().add(sliceB);
					}
					break;
					
				case "Critter2":
					double totalHumans = Integer.parseInt(dataLine[0].split(" ")[0]);
					double numMales = Integer.parseInt(dataLine[1].split(" ")[0]);
					double numFemales = Integer.parseInt(dataLine[2].split(" ")[0]);
					if (totalHumans > 0) {
						PieChart.Data maleSlice = new PieChart.Data((df.format(100 * numMales / totalHumans) + "% " + dataLine[1].split(" ")[1]), (numMales / totalHumans) * 360);
						PieChart.Data femaleSlice = new PieChart.Data((df.format(100 * numFemales / totalHumans) + "% " + dataLine[2].split(" ")[1]), (numFemales / totalHumans) * 360);
						
						statsPieChart.setTitle("HUMANS STATISTICS");
						statsPieChart.setLabelLineLength(20);
						statsPieChart.setStartAngle(180); 
						statsPieChart.getData().add(maleSlice);
						statsPieChart.getData().add(femaleSlice);
					}
					break;
					
				case "Critter3":
					// TODO
					break;
					
				case "Critter4":
					// TODO
					break;
					
				default: // other critter classes may have unknown return string formatting from their runStats() -> we cannot safely parse and add data to piechart
			} // end of switch	
		} catch (InvalidCritterException e) {
			runStats_err.setText("Invalid Input.");
		} catch (IllegalArgumentException e) {
			runStats_err.setText("Parsing Error.");
		} catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) { // run if no method exists
			// If class does not have runStats method()... call the static version
			try {
				String statsString = Critter.runStats(Critter.getInstances(className)); // static call to runStats
				statsMessage.setText(statsString);
				
			} catch (InvalidCritterException e1) {
				runStats_err.setText("Invalid Input.");
			}
		} 
	} // end of updateStats() method

	
	
	/**
	 * Clears all error output messages next to each button.
	 * Usage: should call this function at the beginning of every button handler.
	 * 
	 * @param timeStep_err
	 * @param createCritter_err
	 * @param runStats_err
	 * @param setSeed_err
	 */
	private static void clearErrorMessages(Text timeStep_err, Text createCritter_err, Text runStats_err, Text setSeed_err) {
		timeStep_err.setText("");
		createCritter_err.setText("");
		runStats_err.setText("");
		setSeed_err.setText("");
	}
	
	
} //end of Main class
