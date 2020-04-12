/*
 * CRITTERS Critter4.java (WILDFIRE)
 * EE422C Project 4 submission by
 * Jin Lee
 * jl67888
 * 16295
 * Andy Wu
 * amw5468
 * 16295
 * Slip days used: 0
 * Spring 2020
 */
package assignment5;

import assignment5.Critter;
import assignment5.Critter.CritterShape;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Critter4 is a wildfire critter that spreads sporadically in a single direction as it 
 * consumes clovers. When a clover is consumed during a time step, the fire is marked as 
 * fueled, indicating that it now has a chance to combust in subsequent time steps, draining
 * its energy to reproduce many children. After combustion, the fire is no longer fueled and 
 * will likely perish from energy depletion or defeat.
 * 
 * OVERALL BEHAVIOUR: All Critter4 move in a linear path and likely have short lifespans as 
 * after consuming a clover, they have a 50% chance to combust. This weakness is compensated
 * by their rapid spread, and given an approximately equal distribution of Critter1,...4s,
 * fire will eventually fill the world before burning out
 */

public class Critter4 extends Critter {
	
	static DecimalFormat df = new DecimalFormat("#.##");
	
	/* START - NEW FOR PROJECT 5 */
	public CritterShape viewShape() {
        return CritterShape.TRIANGLE;
    }
	
	@Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return javafx.scene.paint.Color.ORANGE;
    }
	
	@Override
    public javafx.scene.paint.Color viewFillColor() {
		return javafx.scene.paint.Color.ORANGERED;
    }
	/* END - NEW PROJECT 5 */
	
	private int dir;
	private boolean fueled;
	
	/**
	 * Overridden toString() method that prints Critter3 ("4") to console
	 */
    @Override
    public String toString() {
        return "4";
    }
   
    /**
     * Default constructor for Critter4 that assigns it a random direction
     */
	public Critter4() {
		dir = Critter.getRandomInt(8);
		fueled = false;
	}

	/**
	 * Each time step, the fire either moves or reproduces. If it encountered a clover 
	 * in the last time step, drain the fire's energy to create as many children as possible
	 */
    @Override
    public void doTimeStep() {
    	int spark = Critter.getRandomInt(2);
    	if(spark == 0) walk(dir);
    	else {
    		if(fueled) {
    			combust();
    		} else {
        		Critter4 child = new Critter4();
            	reproduce(child, Critter.getRandomInt(8));
    		}
    	}
    }
    /**
     * Critter4 will always fight; if opponent is a clover, mark 
     * Critter4 as fueled (will combust next non-movement time step)
     * @return boolean indicating willingness to fight
     */
    @Override
    public boolean fight(String opponent) {
    	if(opponent == "@") {
    		fueled = true;
    	// if there is a clover within walk/run distance, move to it instead
    	
    	} else if(look(dir,false) == "@") {
    		walk(dir); 
    		return false;
    	} else if(look(dir, true) == "@") {
    		run(dir);
    		return false;
    	}
    	return true;
    }

    
    /**
     * Drains critter's energy to create as many children as possible 
     */
    private void combust() {
    	int deviance = Critter.getRandomInt(4);
    	while(this.getEnergy() >= Params.MIN_REPRODUCE_ENERGY ) {
    		Critter4 child = new Critter4();
    		reproduce(child, childPosition((dir + deviance) % 8));
    	}
    	fueled = false;
    }
    
    /**
     * Determines a child's spawning position based on the parent's direction
     * @param parentDir is direction of parent
     * @return key used to determine child's relative position
     */
    private int childPosition(int parentDir) {
    	int deviance = Critter.getRandomInt(4);
    	return (parentDir + deviance) % 8;
    }
    
    /**
	 * Prints to console the total number of Critter4 and the proportion of unfueled/fueled fires
	 * 
	 * @param List<Critter> of all the existing Critter4 objects in the population
	 */
    public static String runStats(List<Critter> fires) {
    	int totalUnfueled = 0;
    	int totalFueled = 0;
    	String statsString = "";
    	
    	for (Critter crit: fires) {
    		Critter4 fire = (Critter4) crit;
    		if(fire.fueled) totalFueled++;
    		else totalUnfueled++;	
    	}

    	statsString += (fires.size() + " total fires: \n");
    	statsString += (totalUnfueled + " unfueled \n");
    	statsString += (totalFueled + " fueled");

    	return statsString;
    }
}
