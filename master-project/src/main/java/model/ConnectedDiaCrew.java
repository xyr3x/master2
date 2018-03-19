package model;

import application.Main;

class ConnectedDiaCrew extends ConnectedFireFighterCrew{
	private final int size = 16;
	private int start;
	
	//constructs the crew which builds a diamond with size many fighters
	private void initialize() {
		Fitness = Main.CrewSize;
		generation = 0;
		
		//perfect diamond possible
		if(size % 4 == 0) {
			//initialize size many fighters
			for(int i = 0; i < size; i++) {
				ConnectedFireFighter fighter = new ConnectedFireFighter();
				int chain[] = new int[Main.TimeInterval];
				fighter.setChain(chain);
				crew.add(fighter);
			}
			
			//set left neighbour
			for(int i = 1; i < crew.size(); i++) {
				crew.get(i).setLeftNeighbour(crew.get(i-1));
			}
			
			//set start and current vertices
			//first half
			for(int i = 0; i < ((crew.size() / 2) + 1); i++) {
				crew.get(i).setStartVertice(start + i);
				crew.get(i).setCurrentVertice(crew.get(i).getStartVertice());
			}
			//second half
			for(int i = ((crew.size() / 2) + 1); i < crew.size(); i++) {
				crew.get(i).setStartVertice(start + 1 + i - (crew.size() / 2) + 1);
				crew.get(i).setCurrentVertice(crew.get(i).getStartVertice());	
			}
			
			//set chains, fighter 0,8,9,15 remain in their positions
			for(int i = 0; i < Main.TimeInterval; i++) {
				crew.get(0).setChainIndex(i, 0);
				crew.get(8).setChainIndex(i, 0);
				crew.get(9).setChainIndex(i, 0);
				crew.get(15).setChainIndex(i, 0);
			}
			//fighter 1,7,10,14 1 movement, then remain
			crew.get(1).setChainIndex(0, 1);
			crew.get(7).setChainIndex(0, 1);
			crew.get(10).setChainIndex(0, 3);
			crew.get(14).setChainIndex(0, 3);
			
			for(int i = 1; i < Main.TimeInterval; i++) {
				crew.get(1).setChainIndex(i, 0);
				crew.get(7).setChainIndex(i, 0);
				crew.get(10).setChainIndex(i, 0);
				crew.get(14).setChainIndex(i, 0);
			}
			
			//fighter 2,6,11,13 2 movements, then remain			
			for(int i = 0; i < 2; i++) {
				crew.get(2).setChainIndex(i, 1);
				crew.get(6).setChainIndex(i, 1);
				crew.get(11).setChainIndex(i, 3);
				crew.get(13).setChainIndex(i, 3);
			}
			
			for(int i = 2; i < Main.TimeInterval; i++) {
				crew.get(2).setChainIndex(i, 0);
				crew.get(6).setChainIndex(i, 0);
				crew.get(11).setChainIndex(i, 0);
				crew.get(13).setChainIndex(i, 0);
			}
			
			//fighter 3,5,12 3 movements, then remain
			for(int i = 0; i < 3; i++) {
				crew.get(3).setChainIndex(i, 1);
				crew.get(5).setChainIndex(i, 1);
				crew.get(12).setChainIndex(i, 3);
			}
			
			for(int i = 3; i < Main.TimeInterval; i++) {
				crew.get(3).setChainIndex(i, 0);
				crew.get(5).setChainIndex(i, 0);
				crew.get(12).setChainIndex(i, 0);
			}
			
			//fighter 4 4 movements then remain			
			for(int i = 0; i < 4; i++) {
				crew.get(4).setChainIndex(i, 1);
			}
			
			for(int i = 4; i < Main.TimeInterval; i++) {
				crew.get(4).setChainIndex(i, 0);
			}
			
		}
	}
	
}
