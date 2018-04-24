package controller;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import application.Main;
import model.ConnectedFireFighter;
import model.ConnectedFireFighterCrew;
import model.FireFighterCrew;

/*
 * this class describes my self-written saving stuff because xml doesnt work
 */
public class SaveFunctions {
	
	/**
	 * this function converts a given crew into a string
	 * 
	 * @param crew expects ConnectedFireFighterCrew to convert
	 * @return returns the crew as a string to save somewhere
	 */
	public String connectedCrewToString(ConnectedFireFighterCrew crew) {
		String result = new String();
		StringBuilder sb = new StringBuilder();
		StringBuilder dummy = new StringBuilder();
		//String separator = System.lineSeparator();
		
		LocalDateTime now = LocalDateTime.now();
		
		//setup string
		sb.append("Save: ConnectedFireFighterCrew ____ " + now + "\r\n");
		sb.append("<ConnectedFireFighterCrew>\r\n");
		//id
		sb.append("\tid:" + crew.getID() + "\r\n");
		//fitness
		sb.append("\tfitness:" + crew.getFitness() + "\r\n");
		//generation
		sb.append("\tgeneration:" + crew.getGeneration() + "\r\n");
		//maxNonBurningVertices
		sb.append("\tmaxNonBurningVertices:" + crew.getMaxNonBurningVertices() + "\r\n");
		//defendedVertices
		dummy.append("\tdefendedVertices:");
		for(int i = 0; i < Main.TimeInterval; i++) {
			for(int j = 0; j < Main.CrewSize; j++) {
				dummy.append(crew.getDefendedVerticesIndex(i, j) + "|");
			}
		}
		sb.append(dummy + "\r\n");
		//nonBurningVertices
		dummy.delete(0, dummy.length());
		dummy.append("\tnonBurningVertices:");
		for(int i = 0; i < Main.TimeInterval; i++) {
			for(int j = 0; j < (Main.GridLength * Main.GridLength + 100); j++) {
				dummy.append(crew.getNonBurningVerticesIndex(i, j) + "|");
			}
		}
		sb.append(dummy + "\r\n");
		
		//fighter
		for(ConnectedFireFighter k : crew.getCrew()) {
			sb.append("\t<ConnectedFireFighter>\r\n");
			//id
			sb.append("\t\tid:" + k.getID() + "\r\n");
			//startvertice
			sb.append("\t\tstartVertice:" + k.getStartVertice() + "\r\n");
			//chain
			dummy.delete(0, dummy.length());
			dummy.append("\t\tchain:");	
			for (int i = 0; i < Main.TimeInterval; i++) {
				dummy.append(k.getChainIndex(i) + "|");
			}
			sb.append(dummy + "\r\n");
			//position
			dummy.delete(0, dummy.length());
			dummy.append("\t\tposition:");	
			for (int i = 0; i < Main.TimeInterval; i++) {
				dummy.append(k.getPositionIndex(i) + "|");
			}
			sb.append(dummy + "\r\n");
			sb.append("\t</ConnectedFireFighter>\r\n");
		}
		sb.append("</ConnectedFireFighterCrew>\r\n");	
		
		
		result = sb.toString();
		//result.replace("\\r\n", separator);
		
		return result;
		
		
	}
	
	
	/**
	 * this function converts a given crew into a string
	 * 
	 * @param crew expects FireFighterCrew to convert
	 * @return returns the crew as a string to save somewhere
	 */
	public String crewToString(FireFighterCrew crew) {
		String result = new String();
		
		
		
		
		return result;
		
		
	}
	
	
	/**
	 * this function converts a loaded String into a firefighterCrew
	 * @param crewString expects a crew as a string
	 * @return returns a ConnectedFireFighterCrew
	 */
	public ConnectedFireFighterCrew stringToConnectedCrew (String crewString) {
		//remove all tabs - they were just for readability
		crewString = crewString.replace("\t", "");
		
		String [] lines = crewString.split("\\r\n");
		String currentLine;
		String [] currentLineSplit;
		int beginCrew = 0;
		int[] beginFighter = new int[Main.CrewSize];
		int index = 0;
		
		//search for begin of crew and fighters
		for (int i = 0; i < lines.length; i++) {
			currentLine = lines[i];		
			//begin crew
			if (currentLine.equals("<ConnectedFireFighterCrew>")) {
				beginCrew = i;
			}
			//begin fighter
			if (currentLine.equals("<ConnectedFireFighter>")) {
				beginFighter[index] = i;
				index++;
			}
		}
		
		

		ConnectedFireFighterCrew crew = new ConnectedFireFighterCrew();
		//set crewsettings
		for (int k = beginCrew; k < beginFighter[0]; k++) {
			currentLine = lines[k];
			currentLineSplit = currentLine.split(":");
			
			//id
			if(currentLineSplit[0].equals("id")) {
				crew.setID(Integer.parseInt(currentLineSplit[1]));
			}
			
			//fitness
			if(currentLineSplit[0].equals("fitness")) {
				crew.setFitness(Integer.parseInt(currentLineSplit[1]));
			}
			
			//generation
			if(currentLineSplit[0].equals("generation")) {
				crew.setGeneration(Integer.parseInt(currentLineSplit[1]));
			}
			
			//maxNonBurningVertices
			if(currentLineSplit[0].equals("maxNonBurningVertices")) {
				crew.setMaxNonBurningVertices(Integer.parseInt(currentLineSplit[1]));
			}
			
			//defendedVertices
			if(currentLineSplit[0].equals("defendedVertices")) {
				String [] dummy = currentLineSplit[1].split(Pattern.quote("|"));
				int indexDummy = 0;
				for(int i = 0; i < Main.TimeInterval; i++) {
					for(int j = 0; j < Main.CrewSize; j++) {
						crew.setDefendedVerticesIndex(Integer.parseInt(dummy[indexDummy]), i, j);
						indexDummy++;
					}
				}
			}			
			//nonBunringVertices
			if(currentLineSplit[0].equals("nonBurningVertices")) {
				String [] dummy = currentLineSplit[1].split(Pattern.quote("|"));
				int indexDummy = 0;
				for(int i = 0; i < Main.TimeInterval; i++) {
					for(int j = 0; j < (Main.GridLength * Main.GridLength + 100); j++) {
						crew.setDefendedVerticesIndex(Integer.parseInt(dummy[indexDummy]), i, j);
						indexDummy++;
					}
				}
			}
						
		}
		
		//all fighter except last one
		for(int k = 0; k < Main.CrewSize - 1; k++) {
			for (int i = beginFighter[k]; i < beginFighter[k + 1]; i++) {
				ConnectedFireFighter fighter = new ConnectedFireFighter();
				currentLine = lines[i];
				currentLineSplit = currentLine.split(":");
				
				//id
				if(currentLineSplit[0].equals("id")) {
					fighter.setID(Integer.parseInt(currentLineSplit[1]));
				}
				//startVertice
				if(currentLineSplit[0].equals("startVertice")) {
					fighter.setStartVertice(Integer.parseInt(currentLineSplit[1]));
				}
				
				//chain
				if(currentLineSplit[0].equals("chain")) {
					String [] dummy = currentLineSplit[1].split(Pattern.quote("|"));
					int indexDummy = 0;
					for(int j = 0; j < Main.TimeInterval; j++) {
						fighter.setChainIndex(j, Integer.parseInt(dummy[indexDummy]));
						indexDummy++;
					}
				}
				
				//position
				if(currentLineSplit[0].equals("position")) {
					String [] dummy = currentLineSplit[1].split(Pattern.quote("|"));
					int indexDummy = 0;
					for(int j = 0; j < Main.TimeInterval; j++) {
						fighter.setPositionIndex(j, Integer.parseInt(dummy[indexDummy]));
						indexDummy++;
					}
				}
				crew.getCrew().add(fighter);
			}
			
		}
		
		//last fighter
		for (int i = beginFighter[beginFighter.length - 1]; i < lines.length; i++) {
			ConnectedFireFighter fighter = new ConnectedFireFighter();
			currentLine = lines[i];
			currentLineSplit = currentLine.split(":");
			
			//id
			if(currentLineSplit[0].equals("id")) {
				fighter.setID(Integer.parseInt(currentLineSplit[1]));
			}
			//startVertice
			if(currentLineSplit[0].equals("startVertice")) {
				fighter.setStartVertice(Integer.parseInt(currentLineSplit[1]));
			}
			
			//chain
			if(currentLineSplit[0].equals("chain")) {
				String [] dummy = currentLineSplit[1].split(Pattern.quote("|"));
				int indexDummy = 0;
				for(int j = 0; j < Main.TimeInterval; j++) {
					fighter.setChainIndex(j, Integer.parseInt(dummy[indexDummy]));
					indexDummy++;
				}
			}
			
			//position
			if(currentLineSplit[0].equals("position")) {
				String [] dummy = currentLineSplit[1].split(Pattern.quote("|"));
				int indexDummy = 0;
				for(int j = 0; j < Main.TimeInterval; j++) {
					fighter.setPositionIndex(j, Integer.parseInt(dummy[indexDummy]));
					indexDummy++;
				}
			}
			crew.getCrew().add(fighter);
		}
	
		
		
		return crew;
	}
	
	/**
	 * this function converts a loaded String into a firefighterCrew
	 * @param crewString expects a crew as a string
	 * @return returns a FireFighterCrew
	 */
	public FireFighterCrew stringToCrew (String crewString) {
		FireFighterCrew crew = new FireFighterCrew();
		
		return crew;
	}
	
	
	
	
	/**
	 * Saves the given String into a File
	 * 
	 * @param crew expects a crew as a string
	 * @throws IOException 
	 */
	public void stringToFile(String crew) throws IOException {
		String text = "Hello World.";
	    Path target = Paths.get("C:/dev/temp/test.txt");

	    InputStream is = new ByteArrayInputStream(text.getBytes());
	    Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
	}
	
	
}
