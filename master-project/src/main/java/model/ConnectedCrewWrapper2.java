package model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import application.Main;


@XmlRootElement(name = "connectedCrews")
public class ConnectedCrewWrapper2 {
	private List<ConnectedFireFighter> fighter;
	private int fitness;
	// save the defended vertices of all timesteps
	protected int[][] defendedVertices = new int[Main.TimeInterval][Main.CrewSize];
	// save the non burning vertices of all timesteps
	protected int[][] nonBurningVertices = new int[Main.TimeInterval][Main.CrewSize * Main.CrewSize];
	
	
	
	
	
	
	
	
	
	
	@XmlElement(name = "fitness")
	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	@XmlElement(name = "defendedVertices")
	public int[][] getDefendedVertices() {
		return defendedVertices;
	}

	public void setDefendedVertices(int[][] defendedVertices) {
		this.defendedVertices = defendedVertices;
	}
	
	@XmlElement(name = "nonBurningVertices")
	public int[][] getNonBurningVertices() {
		return nonBurningVertices;
	}

	public void setNonBurningVertices(int[][] nonBurningVertices) {
		this.nonBurningVertices = nonBurningVertices;
	}

	@XmlElement(name = "fighters")
	public List<ConnectedFireFighter> getFighter(){
		return fighter;
	}
	
	public void setFighter(List <ConnectedFireFighter> fighter) {
		this.fighter = fighter;
	}
}
