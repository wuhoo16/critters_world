/* CRITTERS Critter.java
 * EE422C Project 5 submission by
 * Jin Lee
 * jl67888
 * 16295
 * Andy Wu
 * amw5468
 * 16295
 * Slip days used: <0>
 * Spring 2020
 */

/*
   Describe here known bugs or issues in this file. If your issue spans multiple
   files, or you are not sure about details, add comments to the README.txt file.
 */
package assignment5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.scene.paint.*;

/*
 * See the PDF for descriptions of the methods and fields in this
 * class.
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {

    /* START --- NEW FOR PROJECT 5 */
    public enum CritterShape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        DIAMOND,
        STAR
    }


    private static double cellSize = 20;
    /* the default color is white, which I hope makes critters invisible by default
     * If you change the background color of your View component, then update the default
     * color to be the same as you background
     *
     * critters must override at least one of the following three methods, it is not
     * proper for critters to remain invisible in the view
     *
     * If a critter only overrides the outline color, then it will look like a non-filled
     * shape, at least, that's the intent. You can edit these default methods however you
     * need to, but please preserve that intent as you implement them.
     */
    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.WHITE;
    }

    public javafx.scene.paint.Color viewOutlineColor() {
        return viewColor();
    }

    public javafx.scene.paint.Color viewFillColor() {
        return viewColor();
    }

    public abstract CritterShape viewShape();

    protected final String look(int direction, boolean steps) {
    	this.energy -= Params.LOOK_ENERGY_COST;
    	
    	int num;
    	if(steps) num = 2;
    	else num = 1;
    	
    	if (this.inEncounter) { // must compare with all other critters' old positions
    		if (direction == 0) {// E
				for (Critter crit: previousPopulation) {
					if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == this.y_coord && crit.energy > 0) {
						return crit.toString();
					}
				}
			} 
			else if (direction == 1) {// NE
				for (Critter crit: previousPopulation) {
					if (crit.energy > 0) { // skip critters that died from encounters
						if (this.y_coord - num >= 0) {
							if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == this.y_coord - num) {
								return crit.toString();
							}
						}
						else { // if movement causes critter to exit top of world
							if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
								return crit.toString();
							}
						}
					}
				}
			} 
			else if (direction == 2) {// N
				for (Critter crit: previousPopulation) {
					if (crit.energy > 0) { // skip dead critters
						if (this.y_coord - num >= 0) {
							if (crit.x_coord == this.x_coord && crit.y_coord == this.y_coord - num) {
								return crit.toString();
							}
						}
						else { // if movement causes critter to exit top of the world
							if (crit.x_coord == this.x_coord && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
								return crit.toString();
							}
						}
					}
				}
			} 
			else if (direction == 3) {// NW
				for (Critter crit: previousPopulation) {
					if (crit.energy > 0) {
						if (this.x_coord - num >= 0 && this.y_coord - num >= 0 ) { // normal check
							if (crit.x_coord == this.x_coord - num && crit.y_coord == this.y_coord) {
								return crit.toString();
							}
						}
						else if (this.x_coord - num < 0 && this.y_coord - num >= 0) { // if movement will cause critter to exit left of world 
							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == this.y_coord) {
								return crit.toString();
							}
						}
						else if (this.x_coord - num >= 0 && this.y_coord - num < 0) { // if movement will cause critter to exit top of the world
							if (crit.x_coord == this.x_coord - num && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
								return crit.toString();
							}
						}
						else { // if movement will cause loop around on both x and y axes
							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
								return crit.toString();
							}
						}
					}
				}
			} 
			else if (direction == 4) {// W
				for (Critter crit: previousPopulation) {
					if (crit.energy > 0) {
						if (crit.x_coord - num >= 0) {
							if (crit.x_coord == this.x_coord - num && crit.y_coord == this.y_coord) {
								return crit.toString();
							}
						}
						else {
							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == this.y_coord) {
								return crit.toString();
							}
						}
					}
				}
			} 
			else if (direction == 5) {// SW
				for (Critter crit: previousPopulation) {
					if (crit.energy > 0) {
						if (crit.x_coord - num >= 0) {
							if (crit.x_coord == this.x_coord - num && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT) {
								return crit.toString();
							}
						}
						else {
							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT) {
								return crit.toString();
							}
						}
					}
				}
			} 
			else if (direction == 6) {// S
				for (Critter crit: previousPopulation) {
					if (crit.x_coord == this.x_coord && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT && crit.energy > 0) {
						return crit.toString();
					}
				}
			} 
			else if (direction == 7) {// SE
				for (Critter crit: previousPopulation) {
					if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT && crit.energy > 0) {
						return crit.toString();
					}
				}
			}
    		
    		
    		
    	}
    	else { // current Critter is in an encounter and has called look() from within a fight (need to compare with other critter's current positions)
	    	if (direction == 0) {// E
				for (Critter crit: population) {
					if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == this.y_coord && crit.energy > 0) {
						return crit.toString();
					}
				}
			} 
			else if (direction == 1) {// NE
				for (Critter crit: population) {
					if (crit.energy > 0) { // skip critters that died from encounters
						if (this.y_coord - num >= 0) {
							if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == this.y_coord - num) {
								return crit.toString();
							}
						}
						else { // if movement causes critter to exit top of world
							if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
								return crit.toString();
							}
						}
					}
				}
			} 
			else if (direction == 2) {// N
				for (Critter crit: population) {
					if (crit.energy > 0) { // skip dead critters
						if (this.y_coord - num >= 0) {
							if (crit.x_coord == this.x_coord && crit.y_coord == this.y_coord - num) {
								return crit.toString();
							}
						}
						else { // if movement causes critter to exit top of the world
							if (crit.x_coord == this.x_coord && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
								return crit.toString();
							}
						}
					}
				}
			} 
			else if (direction == 3) {// NW
				for (Critter crit: population) {
					if (crit.energy > 0) {
						if (this.x_coord - num >= 0 && this.y_coord - num >= 0 ) { // normal check
							if (crit.x_coord == this.x_coord - num && crit.y_coord == this.y_coord) {
								return crit.toString();
							}
						}
						else if (this.x_coord - num < 0 && this.y_coord - num >= 0) { // if movement will cause critter to exit left of world 
							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == this.y_coord) {
								return crit.toString();
							}
						}
						else if (this.x_coord - num >= 0 && this.y_coord - num < 0) { // if movement will cause critter to exit top of the world
							if (crit.x_coord == this.x_coord - num && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
								return crit.toString();
							}
						}
						else { // if movement will cause loop around on both x and y axes
							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
								return crit.toString();
							}
						}
					}
				}
			} 
			else if (direction == 4) {// W
				for (Critter crit: population) {
					if (crit.energy > 0) {
						if (crit.x_coord - num >= 0) {
							if (crit.x_coord == this.x_coord - num && crit.y_coord == this.y_coord) {
								return crit.toString();
							}
						}
						else {
							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == this.y_coord) {
								return crit.toString();
							}
						}
					}
				}
			} 
			else if (direction == 5) {// SW
				for (Critter crit: population) {
					if (crit.energy > 0) {
						if (crit.x_coord - num >= 0) {
							if (crit.x_coord == this.x_coord - num && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT) {
								return crit.toString();
							}
						}
						else {
							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT) {
								return crit.toString();
							}
						}
					}
				}
			} 
			else if (direction == 6) {// S
				for (Critter crit: population) {
					if (crit.x_coord == this.x_coord && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT && crit.energy > 0) {
						return crit.toString();
					}
				}
			} 
			else if (direction == 7) {// SE
				for (Critter crit: population) {
					if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT && crit.energy > 0) {
						return crit.toString();
					}
				}
			}
    	}
    	return null; // location is unoccupied
    }

    /**
     * Prints out how many Critters of each type there are on the board.
     * This static method is run if the user specifies a class that does not have an overridden runStats method.
     *
     * @param critters List of Critters
     * @return String
     */	
    public static String runStats(List<Critter> critters) {
    	String statsString = "";
    	
		statsString += (critters.size() + " critters as follows ---");
		statsString += "\n";
		
        Map<String, Integer> critter_count = new HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.getClass().getName();
            critter_count.put(crit_string,
                    critter_count.getOrDefault(crit_string, 0) + 1);
        }
        
        for (String critClass: critter_count.keySet()) {
            statsString += (critClass.replace("assignment5.", "") + ": " + critter_count.get(critClass));
            statsString += ("\n");
        }
        return statsString;
    }

    /**
     * updates the world (GridPane) to show the current critter population
     * @param pane - Object (GridPane) on which to paint the world
     */
    public static void displayWorld(Object pane) {
        GridPane world = (GridPane) pane;
        world.setHgap(0.0);
        world.setVgap(0.0);
        //Scene scene = new Scene(world, cellSize * (Params.WORLD_WIDTH + 1), cellSize * (Params.WORLD_HEIGHT + 1));
        paintCritters(world);  
    }
    
    /**
     * displayWorld helper function that adds cells and critters to world
     * @param grid - GridPane on which to paint the world
     */
    private static void paintCritters(GridPane world) {
    	world.getChildren().clear();
    	paintGridLines(world);
    	for(int i = 0; i < Params.WORLD_WIDTH; i++) {
    		for(int j = 0; j < Params.WORLD_HEIGHT; j++) {
    			boolean empty = true;
    			for(Critter crit : population) {
    				if(crit.x_coord == i && crit.y_coord == j && empty) {
    					Shape s = getCritterShape(crit);
    					world.add(s, i, j);
    					empty = false;
    				}
    			}
    		}
    	}
    }
    
    /**
     * paintCritters helper function that adds cells to world
     * @param grid - GridPane on which to paint the world
     */
    private static void paintGridLines(GridPane grid) {
    	for(int i = 0; i < Params.WORLD_WIDTH; i++) {
    		for(int j = 0; j < Params.WORLD_HEIGHT; j++) {
    			Shape cell = new Rectangle(cellSize, cellSize);
    			cell.setFill(Color.WHITE); // default
    			cell.setStroke(Color.DIMGREY);
    			grid.add(cell,  i, j);
    		}
    	}
    }
    
    /**
     * paintCritters helper function
     * @param crit - Critter to return Shape of
     * @return - Shape of crit, with its fill and outline
     */
    private static Shape getCritterShape(Critter crit) {
    	// set Shape
    	CritterShape s = crit.viewShape();
    	Shape critShape;
    	switch(s) {
	    	case CIRCLE: critShape = new Circle(cellSize/2); break;
	    	case SQUARE: critShape = new Rectangle(cellSize, cellSize); break;
	    	case TRIANGLE: Polygon triangle = new Polygon();
	    					triangle.getPoints().addAll(new Double[] {
	    							cellSize / 2, 1.0,
	    							1.0,  cellSize - 1,
	    							cellSize - 1,  cellSize - 1});
	    					critShape = triangle; break;   					
	    	case DIAMOND: Polygon diamond = new Polygon();
	    					diamond.getPoints().addAll(new Double[] {
	    							cellSize/2, 1.0,
	    							cellSize - 1, cellSize / 2,
	    							cellSize / 2, cellSize - 1,
	    							1.0, cellSize / 2});
	    					critShape = diamond; break;
	    	case STAR: Polygon star = new Polygon();
	    					star.getPoints().addAll(new Double[] {
	    							1.0, 1.0,
	    							cellSize / 2, cellSize / 4,
	    							cellSize - 1, 1.0,
	    							3 * cellSize / 4, cellSize / 2,
	    							cellSize - 1, cellSize - 1,
	    							cellSize / 2, 3 * cellSize / 4,
	    							1.0, cellSize - 1,
	    							cellSize / 4, cellSize / 2}); 
	    					critShape = star; break;
	    	default: critShape = new Circle(cellSize/2, Color.WHITESMOKE); break;
    	}
    	critShape.setFill(crit.viewFillColor()); // set fill color
    	critShape.setStroke(crit.viewOutlineColor()); // set outline color
    	return critShape;
    }

	/* END --- NEW FOR PROJECT 5
			rest is unchanged from Project 4 */
	private static int timeStep = 0;
	
    private int energy = 0;

    private int x_coord;
    private int y_coord;
    
    private boolean moved = false;
    private boolean inEncounter = false;

    private static List<Critter> previousPopulation = new ArrayList<Critter>();
    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the qualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name)
            throws InvalidCritterException {
    	String className = myPackage + "." + critter_class_name;
		try {
			Critter newCritter = (Critter) Class.forName(className).newInstance();
			newCritter.energy = Params.START_ENERGY;
			newCritter.x_coord = getRandomInt(Params.WORLD_WIDTH);
			newCritter.y_coord = getRandomInt(Params.WORLD_HEIGHT);
			newCritter.moved = false;
			newCritter.inEncounter = false;
			
			population.add(newCritter);
			previousPopulation.add(newCritter); // in case look() is called right after a create commmand we need to positions of these critters
		} catch (ReflectiveOperationException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (NoClassDefFoundError e) {
			throw new InvalidCritterException(critter_class_name);
		}
    }

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *                           Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name)
    		throws InvalidCritterException {
    	String className = myPackage + "." + critter_class_name; // may not need
    	List<Critter> instances = new ArrayList<>();
    	for(Critter crit : population) {
    		// Class critClass = crit.getClass();
    		try {
    			if(Class.forName(className).isInstance(crit)) { // CHANGED .equals TO reflection version of instanceOf (since we should also add any subclasses)
    				instances.add(crit);
    			}
    		} catch (ClassNotFoundException e) {
    			throw new InvalidCritterException(critter_class_name);
    		}
    	}

    	return instances;
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
    	population.clear();
    }

    public static void worldTimeStep() {
    	// 1.) increment time step
    	timeStep++; 

    	// 2.) reset movement flags, doTimeStep(), and update rest energy for every critter in the world
    	for(Critter crit : population) {
    		crit.moved = false;
    		crit.doTimeStep();
    	}

    	// 3.) Handle fights/encounters (only do this after all critters have moved or else they can move out of conflict)
    	Critter.doEncounters();
    	Critter.cull();

    	// 4.) Update rest energy for all critters
    	for (Critter crit: population) {
    		crit.energy -= Params.REST_ENERGY_COST; // update rest energy after all encounters
    	}

    	// 5.) Generate new clovers (according to piazza clovers can be generated on existing tiles and conflicts resolved in next timestep)
    	int cloverCount = Params.REFRESH_CLOVER_COUNT;
    	for(int i = 0; i < cloverCount; i++) {
    		try {
    			Critter.createCritter("Clover");
    		} catch (InvalidCritterException e) {
    			System.out.print(e);
    		}
    	}
    	// 6.) Spawn all babies produced from last timestep into the current population (can be generated on existing tiles since conflicts resolved in next timestep)
    	population.addAll(babies);
    	babies.clear();

    	// 7.) Cull dead critters
    	Critter.cull();  
    	
    	// save the state of the population before any movement in the next timestep (for look() functionality)
    	previousPopulation.clear();
    	previousPopulation.addAll(population);
    }

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }

    protected int getEnergy() {
        return energy;
    }

    protected final void walk(int direction) {
    	 this.move(direction, 1);
         this.energy -= Params.WALK_ENERGY_COST; // charge walk cost (even on failed attempts)
    }

    protected final void run(int direction) {
    	this.move(direction, 2);
        this.energy -= Params.RUN_ENERGY_COST; // charge run cost (even on failed attempts)
    }

    protected final void reproduce(Critter offspring, int direction) {
    	if (this.energy < Params.MIN_REPRODUCE_ENERGY) { // if parent critter does not have enough energy to reproduce, return immediately
    		return;
    	}

    	// update offspring and parent energies
    	offspring.energy = this.energy/2; // always rounded down
    	this.energy = this.energy/2 + (this.energy % 2); // decimal values will be rounded up

    	// Assign child's position based on direction parameter and parent's position
    	switch(direction) {
    	case 0: // E
    		offspring.x_coord = (this.x_coord + 1) % Params.WORLD_WIDTH;
    		offspring.y_coord = this.y_coord;
    		break;

    	case 1: // NE
    		offspring.x_coord = (this.x_coord + 1) % Params.WORLD_WIDTH;
    		offspring.y_coord = this.y_coord - 1;
    		if (offspring.y_coord < 0) {
    			offspring.y_coord += Params.WORLD_HEIGHT;
    		}
    		break;

    	case 2: // N
    		offspring.x_coord = this.x_coord;
    		offspring.y_coord = this.y_coord - 1;
    		if (offspring.y_coord < 0) {
    			offspring.y_coord += Params.WORLD_HEIGHT;
    		}
    		break;

    	case 3: // NW
    		offspring.x_coord = this.x_coord - 1;
    		if (offspring.x_coord < 0) {
    			offspring.x_coord += Params.WORLD_WIDTH;
    		}
    		offspring.y_coord = this.y_coord - 1;
    		if (offspring.y_coord < 0) {
    			offspring.y_coord += Params.WORLD_HEIGHT;
    		}
    		break;

    	case 4: // W
    		offspring.x_coord = this.x_coord - 1;
    		offspring.y_coord = this.y_coord;
    		if (offspring.x_coord < 0) {
    			offspring.x_coord += Params.WORLD_HEIGHT;
    		}
    		break;

    	case 5: // SW
    		offspring.x_coord = this.x_coord - 1;
    		if (offspring.x_coord < 0) {
    			offspring.x_coord += Params.WORLD_WIDTH;
    		}
    		offspring.y_coord = (this.y_coord + 1) % Params.WORLD_HEIGHT;
    		break;

    	case 6: // S
    		offspring.x_coord = this.x_coord;
    		offspring.y_coord = (this.y_coord + 1) % Params.WORLD_HEIGHT;
    		break;

    	case 7: // SE
    		offspring.x_coord = (this.x_coord + 1) % Params.WORLD_WIDTH;
    		offspring.y_coord = (this.y_coord + 1) % Params.WORLD_HEIGHT;
    		break;

    	default:
    		System.out.println("INVALID DIRECTION. Must be an integer between 0 - 7.");
    	}

    	// add the offspring to the babies list to be born at the end of the worldTimeStep
    	babies.add(offspring);
    }

    /**
  	 * Handles all encounters between critters occupying the same position. Specifies the winner of encounters that result in a fight.
  	 * Note that for fights that are draws, winner is picked arbitrarily.
     *
     * @param None
     * @return void
     */
    private static void doEncounters() {
    	for(Critter critA: population) {
    		int currentCritIndex = population.indexOf(critA);
    		for(int i = currentCritIndex + 1; i < population.size(); i++) { // only check conflicts with other critters and no need to check twice
    			Critter critB = population.get(i);
    			if (critA.x_coord == critB.x_coord && critA.y_coord == critB.y_coord) { // if crit A & B at same position
    				critA.inEncounter = true;
    				critB.inEncounter = true;
    				boolean responseA = critA.fight(critB.toString());
    				boolean responseB = critB.fight(critA.toString());
    				int fightPowerA = 0;
    				int fightPowerB = 0;

    				// Check if two critters are still alive and both have not fled
    				if (critA.energy > 0 && critB.energy > 0) { // if both critters are still alive
    					if (critA.inEncounter && critB.inEncounter) { // if both critters still in same position (did not flee successfully)
    						// if either critter is willing to fight, assign their attack power randomly based on their energy
    						if (responseA) {
    							fightPowerA = Critter.getRandomInt(critA.energy + 1);
    						}
    						if (responseB) {
    							fightPowerB = Critter.getRandomInt(critB.energy + 1);
    						}

    						if (fightPowerA >= fightPowerB) { // if critter A won the fight
    							critA.energy += critB.energy/2;
    							critA.inEncounter = false;
    							critB.energy = 0;
    						}
    						else { // else, critter B won the fight
    							critB.energy += critA.energy/2;
    							critB.inEncounter = false;
    							critA.energy = 0;
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
    /**
     * Helper function for walk() and run().
     * Moves a critter object <num> units in the the specified direction out of 8 cardinal directions.
     * Note that when a critter in an encounter tries to flee, it can't move if it has already moved before in the current timestep 
     * and cannot move to an occupied position.
     * A successful flee attempt resets the "this.inEncounter" boolean flag back to false
     *
     * @param int direction (0 is east, 1 is northeast, 2 is north ... etc)
     */
    protected final void move(int direction, int num) {
    	if (this.moved == false) { // only physically move critter object if it has not moved before in current timestep

    		if (this.inEncounter) {
    			boolean isOccupied = false;

    			if (direction == 0) {// E
    				for (Critter crit: population) {
    					if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == this.y_coord && crit.energy > 0) {
    						isOccupied = true;
    					}
    				}
    				if (isOccupied == false) {
    					this.x_coord = (this.x_coord + num) % Params.WORLD_WIDTH;
    					this.moved = true;
    					this.inEncounter = false;
    				}
    			} 
    			else if (direction == 1) {// NE
    				for (Critter crit: population) {
    					if (crit.energy > 0) { // skip critters that died from encounters
    						if (this.y_coord - num >= 0) {
    							if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == this.y_coord - num) {
    								isOccupied = true;
    							}
    						}
    						else { // if movement causes critter to exit top of world
    							if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
    								isOccupied = true;
    							}
    						}
    					}
    				}
    				if (isOccupied == false) {
    					this.x_coord = (this.x_coord + num) % Params.WORLD_WIDTH;
    					this.y_coord = this.y_coord - num;
    					if(this.y_coord < 0) {
    						this.y_coord = Params.WORLD_HEIGHT + this.y_coord;
    					}
    					this.moved = true;
    					this.inEncounter = false;
    				}
    			} 
    			else if (direction == 2) {// N
    				for (Critter crit: population) {
    					if (crit.energy > 0) { // skip dead critters
    						if (this.y_coord - num >= 0) {
    							if (crit.x_coord == this.x_coord && crit.y_coord == this.y_coord - num) {
    								isOccupied = true;
    							}
    						}
    						else { // if movement causes critter to exit top of the world
    							if (crit.x_coord == this.x_coord && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
    								isOccupied = true;
    							}
    						}
    					}
    				}
    				if (isOccupied == false) {
    					this.y_coord = this.y_coord - num;
    					if (this.y_coord < 0) {
    						this.y_coord = Params.WORLD_HEIGHT + this.y_coord;
    					}
    					this.moved = true;
    					this.inEncounter = false;
    				}
    			} 
    			else if (direction == 3) {// NW
    				for (Critter crit: population) {
    					if (crit.energy > 0) {
    						if (this.x_coord - num >= 0 && this.y_coord - num >= 0 ) { // normal check
    							if (crit.x_coord == this.x_coord - num && crit.y_coord == this.y_coord) {
    								isOccupied = true;
    							}
    						}
    						else if (this.x_coord - num < 0 && this.y_coord - num >= 0) { // if movement will cause critter to exit left of world 
    							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == this.y_coord) {
    								isOccupied = true;
    							}
    						}
    						else if (this.x_coord - num >= 0 && this.y_coord - num < 0) { // if movement will cause critter to exit top of the world
    							if (crit.x_coord == this.x_coord - num && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
    								isOccupied = true;
    							}
    						}
    						else { // if movement will cause loop around on both x and y axes
    							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == Params.WORLD_HEIGHT + this.y_coord - num) {
    								isOccupied = true;
    							}
    						}
    					}
    				}
    				if (isOccupied == false) {
    					this.x_coord = this.x_coord - num;
    					if (this.x_coord < 0) {
    						this.x_coord = Params.WORLD_WIDTH + this.x_coord;
    					}
    					this.y_coord = this.y_coord - num;
    					if (this.y_coord < 0) {
    						this.y_coord = Params.WORLD_HEIGHT + this.y_coord;
    					}
    					this.moved = true;
    					this.inEncounter = false;
    				}
    			} 
    			else if (direction == 4) {// W
    				for (Critter crit: population) {
    					if (crit.energy > 0) {
    						if (crit.x_coord - num >= 0) {
    							if (crit.x_coord == this.x_coord - num && crit.y_coord == this.y_coord) {
    								isOccupied = true;
    							}
    						}
    						else {
    							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == this.y_coord) {
    								isOccupied = true;
    							}
    						}
    					}
    				}
    				if (isOccupied == false) {
    					this.x_coord = this.x_coord - num;
    					if (this.x_coord < 0) {
    						this.x_coord = Params.WORLD_WIDTH + this.x_coord;
    					}
    					this.moved = true;
    					this.inEncounter = false;
    				}
    			} 
    			else if (direction == 5) {// SW
    				for (Critter crit: population) {
    					if (crit.energy > 0) {
    						if (crit.x_coord - num >= 0) {
    							if (crit.x_coord == this.x_coord - num && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT) {
    								isOccupied = true;
    							}
    						}
    						else {
    							if (crit.x_coord == Params.WORLD_WIDTH + this.x_coord - num && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT) {
    								isOccupied = true;
    							}
    						}
    					}
    				}
    				if (isOccupied == false) {
    					this.x_coord = this.x_coord - num;
    					if (this.x_coord < 0) {
    						this.x_coord = Params.WORLD_WIDTH + this.x_coord;
    					}
    					this.y_coord = (this.y_coord + num) % Params.WORLD_HEIGHT;
    					this.moved = true;
    					this.inEncounter = false;
    				}
    			} 
    			else if (direction == 6) {// S
    				for (Critter crit: population) {
    					if (crit.x_coord == this.x_coord && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT && crit.energy > 0) {
    						isOccupied = true;
    					}
    				}
    				if (isOccupied == false) {
    					this.y_coord = (this.y_coord + num) % Params.WORLD_HEIGHT;
    					this.moved = true;
    					this.inEncounter = false;
    				}
    			} 
    			else if (direction == 7) {// SE
    				for (Critter crit: population) {
    					if (crit.x_coord == (this.x_coord + num) % Params.WORLD_WIDTH && crit.y_coord == (this.y_coord + num) % Params.WORLD_HEIGHT && crit.energy > 0) {
    						isOccupied = true;
    					}
    				}
    				if (isOccupied == false) {
    					this.x_coord = (this.x_coord + num) % Params.WORLD_WIDTH;
    					this.y_coord = (this.y_coord + num) % Params.WORLD_HEIGHT;
    					this.moved = true;
    					this.inEncounter = false;
    				}
    			}
    		}
    		else { // critter is NOT currently in an encounter
    			if (direction == 0) {// E
    				this.x_coord = (this.x_coord + num) % Params.WORLD_WIDTH;
    			} 
    			else if (direction == 1) {// NE
    				this.x_coord = (this.x_coord + num) % Params.WORLD_WIDTH;
    				this.y_coord = this.y_coord - num;
    				if (this.y_coord < 0) {
    					this.y_coord = Params.WORLD_HEIGHT + this.y_coord;
    				}
    			} 
    			else if (direction == 2) {// N
    				this.y_coord = this.y_coord - num;
    				if (this.y_coord < 0) {
    					this.y_coord = Params.WORLD_HEIGHT + this.y_coord;
    				}
    			} 
    			else if (direction == 3) {// NW
    				this.x_coord = this.x_coord - num;
    				if (this.x_coord < 0) {
    					this.x_coord = Params.WORLD_WIDTH + this.x_coord;
    				}
    				this.y_coord = this.y_coord - num;
    				if (this.y_coord < 0) {
    					this.y_coord = Params.WORLD_HEIGHT + this.y_coord;
    				}
    			} 
    			else if (direction == 4) {// W
    				this.x_coord = this.x_coord - num;
    				if (this.x_coord < 0) {
    					this.x_coord = Params.WORLD_WIDTH + this.x_coord;
    				}
    			} 
    			else if (direction == 5) {// SW
    				this.x_coord = this.x_coord - num;
    				if (this.x_coord < 0) {
    					this.x_coord = Params.WORLD_WIDTH + this.x_coord;
    				}
    				this.y_coord = (this.y_coord + num) % Params.WORLD_HEIGHT;
    			} 
    			else if (direction == 6) {// S
    				this.y_coord = (this.y_coord + num) % Params.WORLD_HEIGHT;
    			} 
    			else if (direction == 7) {// SE
    				this.x_coord = (this.x_coord + num) % Params.WORLD_WIDTH;
    				this.y_coord = (this.y_coord + num) % Params.WORLD_HEIGHT;
    			}

    			this.moved = true;
    		}
    	}
    }

    /**
     * Helper function to remove all dead critters from the population list at the end of a worldTimeStep. 
     * Critters are dead if their energy falls to 0 or below
     * @param None
     * @return void
     */
    public static void cull() {
    	for(int i = 0; i < population.size(); i++) {
    		Critter crit = population.get(i);
    		if(crit.energy <= 0) {
    			population.remove(crit);
    			i--;
    		}
    	}    
    }
    
    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
}
