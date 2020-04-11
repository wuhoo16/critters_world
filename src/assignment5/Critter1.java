/*
 * CRITTERS Critter1.java (CORONAVIRUS)
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

import java.util.List;

/**
 * Critter1/CORONAVIRUS: does not move during doTimeStep() but increases contagiousLevel by 1 per timestep (and can evolve).
 * Only reproduces when encountering other Critters (that are not of Critter1 type).
 * 
 * Randomly generated contagiousLevel determines what type of strain the coronavirus is:
 * Strain type 16A = contagiousLevel from 0 to 100, and spreads 2 16A babies to the left and right upon touching any other Critter
 * Strain type 16B = contagiousLevel from 101 to (WORLD_WIDTH * WORLD_HEIGHT), and spreads 4 16B babies on all 4 sides upon touching any other Critter
 * 16B strains are dominant and always win fights against 16A strains.
 * 
 * OVERALL BEHAVIOUR: The larger the world, the higher the chance of more infectious 16B compared to 16A strains. Since reproduction is so costly to energy,
 * Critter1/CORONAVIRUS spreads extremely quickly to adjacent squares but also dies out easily after long periods of time since all babies have a fraction of the parent's energy; therefore, they often lose fights
 * with healthier opponents and have much shorter lifespans. 
 * With other critter types: After large amounts of coronavirus are created, they spread rapidly in long connected strips, especially in populated areas of the world. After long periods of time, strain A tends to get eradicated much easier than strain B.
 * Without other critters: The coronavirus population remains stable until they passively die of rest energy cost
 * 
 */

public class Critter1 extends Critter {
	
	/* START - NEW FOR PROJECT 5 */
	public CritterShape viewShape() {
        return CritterShape.CIRCLE;
    }
	
	@Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return javafx.scene.paint.Color.BLACK;
    }
	
	@Override
    public javafx.scene.paint.Color viewFillColor() {
		return javafx.scene.paint.Color.PINK;
    }
	/* END - NEW PROJECT 5 */
	
	private int contagiousLevel;
	private String strainType;
	
	/**
	 * Default constructor for Critter1. Initializes the strainType based on randomly generated int in range [0, (WORLD_WIDTH * WORLD_HEIGHT)].
	 * Probability of spawning strainType 16B scales with world size.
	 */
	public Critter1 () {
		contagiousLevel = Critter.getRandomInt(Params.WORLD_WIDTH * Params.WORLD_HEIGHT + 1);
		if (contagiousLevel <= 100) {
			strainType = "16A";
		}
		else {
			strainType = "16B";
		}
	}
	
	/**
	 * Parameterized constructor for Critter1 that is only called for babies. 
	 * ALL babies are the same strainType as their parents and have the same contagiousLevel as their parent at time of birth.
	 * 
	 * @param String representing parent's strainType, int representing parent's contagiousLevel
	 */
	public Critter1 (String parentStrainType, int parentContagiousLevel) {
		this.strainType = parentStrainType;
		this.contagiousLevel = parentContagiousLevel;
	}
	
	/**
	 * Overriden toString to print Critter1 to console. Must return "1" for grading script to work
	 */
    @Override
    public String toString() {
        return "1";
    }
	
	/**
	 * Determines coronavirus response to encounters. Will reproduce rapidly before fighting other critters. 16B strains will always win fights against 16A.
	 * 16A strains produce 2 children on the left and right (if possible)
	 * 16B strains produce 4 children in all four directions (if possible)
	 * Note that the parent can only reproduce if it has Params.MIN_REPRODUCE_ENERGY amount of energy left after the previous reproduce.
	 * 
	 * @param String of opponent's toString
	 * @return boolean indicating if this object wishes to fight opponent at the same position
	 */
    public boolean fight(String opponent) {
    	if (!opponent.equals("1")) {
    		if (this.strainType.equals("16A")) {
    			Critter1 rightVirus = new Critter1(this.strainType, this.contagiousLevel);
    			Critter1 leftVirus = new Critter1(this.strainType, this.contagiousLevel);
    			this.reproduce(rightVirus, 0);
    			this.reproduce(leftVirus, 4);
    		}
    		else { // 16B strains reproduce in all 4 directions
    			Critter1 rightVirus = new Critter1(this.strainType, this.contagiousLevel);
    			Critter1 upVirus = new Critter1(this.strainType, this.contagiousLevel);
    			Critter1 leftVirus = new Critter1(this.strainType, this.contagiousLevel);
    			Critter1 downVirus = new Critter1(this.strainType, this.contagiousLevel);
    			
    			this.reproduce(rightVirus, 0);
    			this.reproduce(upVirus, 2);
    			this.reproduce(leftVirus, 4);
    			this.reproduce(downVirus, 6);
    		}
    		return true;
    	}
    	else { // coronavirus cannot spread/reproduce when encountering other coronavirus
    		if (this.strainType.equals("16B")) { // 16B always wins over 16A strain
    			return true;
    		}
    		else {
    			return false;
    		}
    	}
    }
    
	/**
	 * Each timeStep, coronavirus increases contagiousLevel by 1 and evolves into 16B strain if it passes the 100 threshold.
	 * However, coronavirus cannot move and waits until other Critters move onto the same space to spread
	 */
    @Override
    public void doTimeStep() {
    	this.contagiousLevel += 1;
		if (this.contagiousLevel > 100) {
			this.strainType = "16B"; // evolves into strainB
		}
		
    	// coronavirus does not move, so no call to walk() or run()
    }
    
	/**
	 * Prints to console the total number of Critter1/coronavirus, number of each strain, min contagiousLevel, and max contagiousLevel
	 * 
	 * @param List<Critter> of all the existing Critter1 objects in the population
	 */
    public static String runStats(List<Critter> viruses) {
    	int totalA = 0;
    	int totalB = 0;
    	int currentMin = Integer.MAX_VALUE;
    	int currentMax = -1;
    	
    	for (Critter crit: viruses) {
    		Critter1 coronavirus = (Critter1) crit;
    		
    		if (coronavirus.contagiousLevel > currentMax) { // update max
    			currentMax = coronavirus.contagiousLevel;
    		}
    		if (coronavirus.contagiousLevel < currentMin) { // update min
    			currentMin = coronavirus.contagiousLevel;
    		}
    		
    		if (coronavirus.strainType.equals("16A")) { // count # of each strain
    			totalA++;
    		}
    		else {
    			totalB++;
    		}
    	}
    	
    	System.out.print(viruses.size() + " total Coronavirus:   ");
    	System.out.print(totalA + " \"16A\" strain(s)    ");
    	System.out.print(totalB + " \"16B\" strain(s)    ");
    	if (viruses.size() == 0) {
    		System.out.print("N/A = weakest contagion level    ");
        	System.out.print("N/A = strongest contagion level");
    	}
    	else {
	    	System.out.print(currentMin + " = weakest contagion level    ");
	    	System.out.print(currentMax + " = strongest contagion level");
    	}
        System.out.println();
        
        // TODO: write actual return
        return "";
    }   
}
