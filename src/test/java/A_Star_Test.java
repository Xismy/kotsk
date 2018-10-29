import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.*;
import tws.keeper.model.Action;
import tws.keeper.model.Position;
import tws.keeper.solution.A_Star;
import tws.keeper.solution.A_Star.UnsolvableProblemException;
import tws.keeper.solution.MazeExplorer;
import tws.keeper.solution.MazeExplorer.CellState;


public class A_Star_Test {
		
	@Test
	public void pathShoulBeValid() {
		@SuppressWarnings("unchecked")
		EnumSet<CellState>[][] map = new EnumSet[40][40];
		ArrayList<Position> path = TestUtils.generateRandomMap(map);
		MazeExplorer explorer = new MazeExplorer();
		explorer.setMazeMap(map);
		A_Star a_star = new A_Star(explorer);
		ArrayList<Action> actions;
		
		Position position = path.get(0);
		Position target = path.get(path.size()-1);
		
		try {
			actions = a_star.solve(position, target);
			
			for(Action action:actions) {
				int x = position.getHorizontal();
				int y = position.getVertical();
				
				position = new Position(
						action==Action.GO_UP? y-1:
						action==Action.GO_DOWN? y+1:y,
								
						action==Action.GO_LEFT? x-1:
						action==Action.GO_RIGHT? x+1:x);
				
				if(!path.contains(position)) {
					break;
				}
			}
			
		}
		catch(UnsolvableProblemException upe) {
			System.err.println(upe);
		}
		
		assertEquals(position, target);
		
	}
	
	@Test(expected = UnsolvableProblemException.class)
	public void unsolvableProblemExceptionMustBeThrown() throws UnsolvableProblemException {
		@SuppressWarnings("unchecked")
		EnumSet<CellState>[][] map = new EnumSet[40][40];
		ArrayList<Position> path = TestUtils.generateUnsolvableMap(map);
		MazeExplorer explorer = new MazeExplorer();
		explorer.setMazeMap(map);
		A_Star a_star = new A_Star(explorer);
		ArrayList<Action> actions;
		
		Position position = path.get(0);
		Position target = path.get(path.size()-1);
		
		actions = a_star.solve(position, target);
	}
}
