/** Author: Moritz Wiemker
 * 	Masterthesis
 *
 *
 *
 */

package model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import application.Main;

public class ConnectedFireFighterCrew implements Comparable<ConnectedFireFighterCrew> {
	protected int ID;
	// number of non burning vertices
	protected int Fitness = Main.CrewSize;
	protected int maxNonBurningVertices;
	protected int averageNonBurningVertices;
	private int numOfGoodSteps;
	protected List<ConnectedFireFighter> crew = new ArrayList<ConnectedFireFighter>();
	protected int bestTimeStep;
	protected int generation;
	protected boolean changed = false;
	protected boolean newCrew = false;
	// save the defended vertices of all timesteps
	protected int[][] defendedVertices = new int[Main.TimeInterval][Main.CrewSize];
	// save the non burning vertices of all timesteps
	protected int[][] nonBurningVertices = new int[Main.TimeInterval][Main.GridLength * Main.GridLength + 100];

	// constructor
	public ConnectedFireFighterCrew() {
		this.ID = Main.CrewID;
		Main.CrewID++;
	}

	// getter and setter
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	
	public int getFitness() {
		return Fitness;
	}

	public void setFitness(int fitness) {
		Fitness = fitness;
	}
	
	
	public List<ConnectedFireFighter> getCrew() {
		return crew;
	}

	public void setCrew(List<ConnectedFireFighter> crew) {
		this.crew = crew;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean isNewCrew() {
		return newCrew;
	}

	public void setNewCrew(boolean newCrew) {
		this.newCrew = newCrew;
	}

	public int getBestTimeStep() {
		return bestTimeStep;
	}

	public void setBestTimeStep(int bestTimeStep) {
		this.bestTimeStep = bestTimeStep;
	}

	public int[][] getDefendedVertices() {
		return defendedVertices;
	}

	public int getDefendedVerticesIndex(int index1, int index2) {
		return defendedVertices[index1][index2];
	}

	public void setDefendedVertices(int[][] defendedVertices) {
		this.defendedVertices = defendedVertices;
	}

	public void setDefendedVerticesIndex(int value, int index1, int index2) {
		defendedVertices[index1][index2] = value;
	}

	public int[][] getNonBurningVertices() {
		return nonBurningVertices;
	}

	public int getNonBurningVerticesIndex(int index1, int index2) {
		return nonBurningVertices[index1][index2];
	}

	public void setNonBurningVertices(int[][] nonBurningVertices) {
		this.nonBurningVertices = nonBurningVertices;
	}

	public void setNonBurningVerticesIndex(int value, int index1, int index2) {
		nonBurningVertices[index1][index2] = value;
	}

	public int getMaxNonBurningVertices() {
		return maxNonBurningVertices;
	}

	public void setMaxNonBurningVertices(int maxNonBurningVertices) {
		this.maxNonBurningVertices = maxNonBurningVertices;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}
		
	public int getAverageNonBurningVertices() {
		return averageNonBurningVertices;
	}

	public void setAverageNonBurningVertices(int averageNonBurningVertices) {
		this.averageNonBurningVertices = averageNonBurningVertices;
	}

	public int getNumOfGoodSteps() {
		return numOfGoodSteps;
	}

	public void setNumOfGoodSteps(int numOfGoodSteps) {
		this.numOfGoodSteps = numOfGoodSteps;
	}

	// shifts for the grid
	// shift in x direction -- half of the grid
	public void shiftXPositive() {
		for (int i = 0; i < crew.size(); i++) {
			crew.get(i).setCurrentVertice(crew.get(i).getCurrentVertice() + (Main.GridLength / 2));
		}
	}

	// shift in x direction -- half of the grid
	public void shiftXNegative() {
		for (int i = 0; i < crew.size(); i++) {
			crew.get(i).setCurrentVertice(crew.get(i).getCurrentVertice() - (Main.GridLength / 2));
		}
	}

	// shift in y direction -- half of the grid
	public void shiftYPositive() {
		for (int i = 0; i < crew.size(); i++) {
			crew.get(i).setCurrentVertice(crew.get(i).getCurrentVertice() + ((Main.GridLength / 2) * Main.GridLength));
		}
	}

	// shift in y direction -- half of the grid
	public void shiftYNegative() {
		for (int i = 0; i < crew.size(); i++) {
			crew.get(i).setCurrentVertice(crew.get(i).getCurrentVertice() - ((Main.GridLength / 2) * Main.GridLength));
		}
	}

	public int compareTo(ConnectedFireFighterCrew arg0) {
		if (Fitness < arg0.getFitness()) {
			return 1;
		}

		if (Fitness > arg0.getFitness()) {
			return -1;
		}

		return 0;
	}

}
