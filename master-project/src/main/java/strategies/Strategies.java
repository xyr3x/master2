/**
 * 
 */
package strategies;

import application.Main;
import model.ConnectedFireFighter;
import model.ConnectedFireFighterCrew;

/**
 * @author Moritz
 *
 */
public class Strategies {

	// connected diaCrew
	public void diaCrew(ConnectedFireFighterCrew crew) {
		System.out.println("strat");
		int fitness = Main.CrewSize;
		int generation = 0;
		crew.setFitness(fitness);
		crew.setGeneration(generation);
		
		int start = Main.GridSize / 2 + Main.GridLength / 2;

		// set left neighbour -- TODO:falsch!!!
		for (int i = 1; i < crew.getCrew().size(); i++) {
			crew.getCrew().get(i).setLeftNeighbour(crew.getCrew().get(i - 1));
		}

		// set start and current vertices
		// first half
		for (int i = 0; i < ((crew.getCrew().size() / 2) + 1); i++) {
			crew.getCrew().get(i).setStartVertice(start + i);
			crew.getCrew().get(i).setCurrentVertice(crew.getCrew().get(i).getStartVertice());

		}
		// second half
		for (int i = ((crew.getCrew().size() / 2) + 1); i < crew.getCrew().size(); i++) {
			crew.getCrew().get(i).setStartVertice(start - Main.GridLength + crew.getCrew().size() - i);
			crew.getCrew().get(i).setCurrentVertice(crew.getCrew().get(i).getStartVertice());
		}

		// set chains, fighter 0,8,9,15 remain in their positions
		for (int i = 0; i < Main.TimeInterval; i++) {
			crew.getCrew().get(0).setChainIndex(i, 0);
			crew.getCrew().get(8).setChainIndex(i, 0);
			crew.getCrew().get(9).setChainIndex(i, 0);
			crew.getCrew().get(15).setChainIndex(i, 0);
		}
		// fighter 1,7,10,14 1 movement, then remain
		crew.getCrew().get(1).setChainIndex(0, 1);
		crew.getCrew().get(7).setChainIndex(0, 1);
		crew.getCrew().get(10).setChainIndex(0, 3);
		crew.getCrew().get(14).setChainIndex(0, 3);

		for (int i = 1; i < Main.TimeInterval; i++) {
			crew.getCrew().get(1).setChainIndex(i, 0);
			crew.getCrew().get(7).setChainIndex(i, 0);
			crew.getCrew().get(10).setChainIndex(i, 0);
			crew.getCrew().get(14).setChainIndex(i, 0);
		}

		// fighter 2,6,11,13 2 movements, then remain
		for (int i = 0; i < 2; i++) {
			crew.getCrew().get(2).setChainIndex(i, 1);
			crew.getCrew().get(6).setChainIndex(i, 1);
			crew.getCrew().get(11).setChainIndex(i, 3);
			crew.getCrew().get(13).setChainIndex(i, 3);
		}

		for (int i = 2; i < Main.TimeInterval; i++) {
			crew.getCrew().get(2).setChainIndex(i, 0);
			crew.getCrew().get(6).setChainIndex(i, 0);
			crew.getCrew().get(11).setChainIndex(i, 0);
			crew.getCrew().get(13).setChainIndex(i, 0);
		}

		// fighter 3,5,12 3 movements, then remain
		for (int i = 0; i < 3; i++) {
			crew.getCrew().get(3).setChainIndex(i, 1);
			crew.getCrew().get(5).setChainIndex(i, 1);
			crew.getCrew().get(12).setChainIndex(i, 3);
		}

		for (int i = 3; i < Main.TimeInterval; i++) {
			crew.getCrew().get(3).setChainIndex(i, 0);
			crew.getCrew().get(5).setChainIndex(i, 0);
			crew.getCrew().get(12).setChainIndex(i, 0);
		}

		// fighter 4 4 movements then remain
		for (int i = 0; i < 4; i++) {
			crew.getCrew().get(4).setChainIndex(i, 1);
		}

		for (int i = 4; i < Main.TimeInterval; i++) {
			crew.getCrew().get(4).setChainIndex(i, 0);
		}

	}

}
