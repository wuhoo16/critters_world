/*
 * CRITTERS Critter2.java (Stubborn Human)
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
 * Critter2/Stubborn Human: This human critter is very stubborn and wants to explore as far as possible. He/she can only move in ONE direction for their entire existence.
 * During each timeStep()...
 * Humans under the age of 18: run in the specified direction
 * Humans ages 18 - 69 (inclusive): walk in the specified direction
 * Humans ages 70+: take no action and just rests
 * Only adults ages 18-69 are wise enough to look ahead before spending energy to walk forward
 * 
 * Upon encounters, this critter tries to run away from his problems first, then fights if he/she cannot flee
 * 
 * Randomly generated number from [0, 7] determines exploration direction, gender is also randomly generated
 * Only females with energy level 100 or above can reproduce and they must come into contact with another Critter2 to reproduce.
 * 
 * OVERALL BEHAVIOUR: All Critter2/Humans will only move in a linear path. They initially run, then walk, then eventually stop moving.
 * Eventually all humans will be 70+ years old and will only move when in danger. Males tend to live longer since they don't expend energy reproducing.
 * A population with only males will eventually go extinct.
 */

public class Critter2 extends Critter {
	
	/* START - NEW FOR PROJECT 5 */
	public CritterShape viewShape() {
        return CritterShape.DIAMOND;
    }
	
	@Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return javafx.scene.paint.Color.BLACK;
    }
	
	@Override
    public javafx.scene.paint.Color viewFillColor() {
		return javafx.scene.paint.Color.BLACK;	
    }
	/* END - NEW PROJECT 5 */
	
	private int age = 0;
	private char gender;
	private int direction;
	
	/**
	 * Default constructor for Critter2. Initializes the direction and gender randomly. 
	 * age always initialized to 0.
	 */
	public Critter2 () {
		age = 0;
		direction = Critter.getRandomInt(8);
		int isMale = Critter.getRandomInt(2);
		if (isMale == 1) {
			gender = 'M';
		}
		else {
			gender = 'F';
		}
	}
	
	
	/**
	 * Overriden toString to print Critter2 to console. Must return "2" for grading script to work
	 */
    @Override
    public String toString() {
        return "2";
    }
	
	/**
	 * Upon encounters, humans will always first try to run away from all opponents. However, will fight if fleeing fails.
	 * However, if a female lands on the same space as another human, she will create a baby if her energy is at least 100
	 * 
	 * @param String of opponent's toString
	 * @return boolean indicating if this object wishes to fight opponent at the same position
	 */
    public boolean fight(String opponent) {
    	if (opponent.equals("2")) { // only allow reproduction if female comes into contact with another human
    		if (this.gender == 'F' && this.getEnergy() >= 100) {
    			Critter2 baby = new Critter2();
    			this.reproduce(baby, direction);
    		}
    	}
    	
    	run(direction); // always attempts to run away first
    	return true; // humans will fight if forced to (if running away fails and their .inEncounter flag stays true)
    }
    
	/**
	 * Each timeStep, humans age by 1 year. 
	 * Humans under the age of 18: run in the specified direction
	 * Humans ages 18 - 69 (inclusive): walk in the specified direction ONLY if it is unoccupied
	 * Humans ages 70+: take no action and just rests
	 */
    @Override
    public void doTimeStep() {
    	this.age++;
    	
    	if (this.age < 18) { // children
    		run(direction);
    	}
    	else if (this.age < 70) { // adults will walk ONLY if the next space in that direction is unoccupied
    		if (look(direction, false) == null) {
    			walk(direction);
    		}
    	}
    	else { // seniors
    		// do nothing (old humans always rest)
    	}
    }
    
	/**
	 * Prints to console the total number of Critter2/humans, number of each gender, and youngest/oldest human
	 * 
	 * @param List<Critter> of all the existing Critter2 objects in the population
	 */
    public static String runStats(List<Critter> people) {
    	int totalM = 0;
    	int totalF = 0;
    	int numChildren = 0;
    	int numAdults = 0;
    	int numSeniors = 0;
    	int currentYoungest = Integer.MAX_VALUE;
    	int currentOldest = -1;
    	
    	for (Critter person: people) {
    		Critter2 human = (Critter2) person;
    		
    		if (human.age > currentOldest) { // update max
    			currentOldest = human.age;
    		}
    		if (human.age < currentYoungest) { // update min
    			currentYoungest = human.age;
    		}
    		
    		if (human.gender == 'M') { // count # of each gender
    			totalM++;
    		}
    		else {
    			totalF++;
    		}
    		
    		if (human.age < 18) { // count # of each classification
    			numChildren++;
    		}
    		else if (human.age < 70) {
    			numAdults++;
    		}
    		else {
    			numSeniors++;
    		}
    	}
    	
    	System.out.print(people.size() + " total human(s):   ");
    	System.out.print(totalM + " male(s)    ");
    	System.out.print(totalF + " female(s)    ");
    	System.out.print(numChildren + " children    ");
    	System.out.print(numAdults + " adult(s)    ");
    	System.out.print(numSeniors + " senior(s)    ");
    	if (people.size() == 0) {
    		System.out.print("N/A = youngest human    ");
    		System.out.print("N/A = oldest human");
    	}
    	else {
    		System.out.print(currentYoungest + " = youngest human's age    ");
    		System.out.print(currentOldest + " = oldest human's age");
    	}
        System.out.println();
        
        // TODO: write actual return
        return "";
    }

}