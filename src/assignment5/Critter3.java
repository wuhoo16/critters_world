/*
 * CRITTERS Critter3.java (SENTIENT TUMOR)
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
 * Critter3 is a sentient tumor that moves erratically and becomes increasingly malignant as a population.
 * 
 * Tumors can be benign or malignant:
 * - Benign tumors reproduce and have a chance to turn malignant with every time step. 
 * They never fight upon encounter, although they have a chance to turn malignant (100% if opponent is a tumor)
 * and will always reproduce as much as possible before losing.
 * - Malignant tumors do not reproduce as rapidly, only able to spread by chance or upon encountering 
 * another tumor. They have a high chance to engage humans (Critter2) and will always engage all other critter types
 * 
 * OVERALL BEHAVIOUR: Benign tumors facilitate the rapid spread of tumors in the world but is overtaken by more 
 * aggressive critters. Eventually, all tumors become malignant at which point the population has the chance to stablize
 * in the presence of other critters
 * 
 */

public class Critter3 extends Critter {
	
	/* START - NEW FOR PROJECT 5 */
	public CritterShape viewShape() {
        return CritterShape.STAR;
    }
	
	@Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return javafx.scene.paint.Color.BLACK;
    }
	
	@Override
    public javafx.scene.paint.Color viewFillColor() {
        return javafx.scene.paint.Color.GOLD;
    }
	/* END - NEW PROJECT 5 */
	
	private int direction;
	private boolean malignant;
	
	/**
	 * Overridden toString() method that prints Critter3 ("3") to console
	 */
    @Override
    public String toString() {
        return "3";
    }
    
    /**
     * Default constructor for Critter3 that assigns it a random direction
     * and its diet
     */
	public Critter3() {
		direction = Critter.getRandomInt(8);
		malignant = (Critter.getRandomInt(13) % 2 == 1);
	}
	
	/**
	 * Private constructor used in reproduction
	 * @param nature indicates whether critter is malignant or not
	 */
	private Critter3(boolean nature) {
		direction = Critter.getRandomInt(8);
		malignant = nature;
	}

   /**
    * Each timestep, the tumor randomly walks or runs in a direction and if
    * benign, has the chance to mutate into a malignant tumor
    */
    @Override
    public void doTimeStep() {
    	// move
    	int chaos = Critter.getRandomInt(3);
    	if(chaos > 0) {
    		if(chaos == 1) walk(direction);
    		if(chaos == 2) run(direction);
    		direction = (chaos * chaos) % 8;
    	}
    	// if benign, reproduce and potentially turn malignant
    	if(!malignant) {
    		if(getEnergy() >= Params.MIN_REPRODUCE_ENERGY) {
    			Critter3 child = new Critter3(false);
    			reproduce(child, (direction * chaos) % 8);
    		}
    		if(chaos == 0) malignant = true;
    	} else {// malignant tumor
    		if(chaos == 0) {
    			Critter3 child = new Critter3(true);
    			reproduce(child, (direction * chaos) % 8);
    		}
    	}
    }
    /**
     * Implements Critter3's encounter response:
     * - if opponent is a human, a malignant tumor has high chance of engaging
     * - if tumor is benign, will never engage, although potentially turn malignant and reproduce
     * - if opponent is a tumor, a malignant tumor will reproduce and engage
     * - if opponent is a tumor, a benign tumor will turn malignant and reproduce
     * - in all other cases, malignant tumors will always engage
     * @return boolean indicating willingness to fight
     */
    @Override
    public boolean fight(String opponent) {
    	if(malignant) {
    		if(opponent.equals("2")) {// if human
        		int misfortune = Critter.getRandomInt(13);
        		if(misfortune >= 9) return true;
        		else return false;
        	} else if(opponent.equals("3")) {// if tumor
        		if(getEnergy() >= Params.MIN_REPRODUCE_ENERGY) {
        			Critter3 child = new Critter3(true);
        			reproduce(child, (direction * direction) % 8);
        		}
        		return true;
        	} else return true;
    	} else {// tumor is benign
    		if(opponent.equals("3")) {
    			malignant = true;
    			int spawnPos = 0;
    			while(getEnergy() >= Params.MIN_REPRODUCE_ENERGY) {
    				Critter3 child = new Critter3(true);
    				reproduce(child, (direction + spawnPos) % 8);
    				spawnPos += 3;
    			}
    		} else {
    			int chaos = Critter.getRandomInt(3);
    			if(chaos == 0) malignant = true;
    			int spawnPos = 0;
    			while(getEnergy() >= Params.MIN_REPRODUCE_ENERGY) {
    				Critter3 child = new Critter3(true);
    				reproduce(child, (direction + spawnPos) % 8);
    				spawnPos += 3;
    			}
    		}
    		return false;
    	}
    	
    }
    
    /**
	 * Prints to console the total number of Critter3 and the proportion of benign/ malignant tumors
	 * 
	 * @param List<Critter> of all the existing Critter3 objects in the population
	 */
    public static String runStats(List<Critter> tumors) {
    	int totalBenign = 0;
    	int totalMalignant = 0;
    	
    	for (Critter crit: tumors) {
    		Critter3 tumor = (Critter3) crit;
    		if(tumor.malignant) totalMalignant++;
    		else totalBenign++;	
    	}
    	System.out.print(tumors.size() + " total tumors:   ");
    	if(tumors.size() > 0) {
    		System.out.print(totalBenign / (0.01 * tumors.size()) + "% benign   ");
    		System.out.print(totalMalignant / (0.01 * tumors.size()) + "% malignant   ");
    	}
    	
        System.out.println();
        
        // TODO: write actual return
        return "";
    }
}
