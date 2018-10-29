import static org.junit.Assert.assertEquals;

import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import tws.keeper.model.Action;
import tws.keeper.model.Position;
import tws.keeper.solution.MazeExplorer;
import tws.keeper.solution.MazeExplorer.CellState;

public class MazeExplorerTest {
	
	@Test
	public void exploreShouldGoUnexploredFirst() {
		EnumSet<CellState>[][] map = new EnumSet[3][3];
		map[1][1] = EnumSet.of(CellState.UNEXPLORED);
		Action actions[] = {Action.GO_UP, Action.GO_LEFT, Action.GO_RIGHT, Action.GO_DOWN};
		
		Position[] adjacent = {
				new Position(0,1),
				new Position(1,0),
				new Position(1,2),
				new Position(2,1)
		};
		
		int unexplored = ThreadLocalRandom.current().nextInt(0,4);
		
		for(int i=0;i<4;i++) {
			map[adjacent[i].getVertical()][adjacent[i].getHorizontal()] = EnumSet.of(i==unexplored?CellState.UNEXPLORED:CellState.EXPLORING_LEFT);
		}
		
		
		MazeExplorer explorer = new MazeExplorer();
		explorer.setMazeMap(map);
		Action action = explorer.explore(new Position(1,1));
		assertEquals(action, actions[unexplored]);
	}
	
	@Test
	public void exploreShouldGoBackWhenNoUnexplored() {
		EnumSet<CellState>[][] map = new EnumSet[3][3];
		map[1][1] = EnumSet.of(CellState.UNEXPLORED);
		Action actions[] = {Action.GO_UP, Action.GO_LEFT, Action.GO_RIGHT, Action.GO_DOWN};
		CellState states[] = {CellState.EXPLORING_DOWN, CellState.EXPLORING_RIGHT, CellState.EXPLORING_LEFT, CellState.EXPLORING_UP};
 		
		Position[] adjacent = {
				new Position(0,1),
				new Position(1,0),
				new Position(1,2),
				new Position(2,1)
		};
		
		int state = ThreadLocalRandom.current().nextInt(0,4);
		
		for(int i=0;i<4;i++) {
			map[adjacent[i].getVertical()][adjacent[i].getHorizontal()] = EnumSet.of(states[state]);
		}
		
		
		MazeExplorer explorer = new MazeExplorer();
		explorer.setMazeMap(map);
		Action action = explorer.explore(new Position(1,1));
		assertEquals(action, actions[state]);
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void exploreShouldFailWhenCellIsEdge() {
		EnumSet<CellState>[][] map = new EnumSet[3][3];
		map[1][1] = EnumSet.of(CellState.UNEXPLORED);
		CellState states[] = {CellState.EXPLORING_DOWN, CellState.EXPLORING_RIGHT, CellState.EXPLORING_LEFT, CellState.EXPLORING_UP};
 		
		Position[] adjacent = {
				new Position(0,1),
				new Position(1,0),
				new Position(1,2),
				new Position(2,1)
		};
		
		int state = ThreadLocalRandom.current().nextInt(0,4);
		
		for(int i=0;i<4;i++) {
			map[adjacent[i].getVertical()][adjacent[i].getHorizontal()] = EnumSet.of(states[state]);
		}
		
		
		MazeExplorer explorer = new MazeExplorer();
		explorer.setMazeMap(map);
		Action action = explorer.explore(new Position(1,2));
	}
	
	@Test
	public void exploreShouldDoNothingWhenNoTurningBack() {
		EnumSet<CellState>[][] map = new EnumSet[3][3];
		map[1][1] = EnumSet.of(CellState.UNEXPLORED);
		CellState states[] = {CellState.EXPLORING_UP, CellState.EXPLORING_LEFT, CellState.EXPLORING_RIGHT, CellState.EXPLORING_DOWN};
 		
		Position[] adjacent = {
				new Position(0,1),
				new Position(1,0),
				new Position(1,2),
				new Position(2,1)
		};
		
		
		for(int i=0;i<4;i++) {
			map[adjacent[i].getVertical()][adjacent[i].getHorizontal()] = EnumSet.of(states[i]);
		}
		
		
		MazeExplorer explorer = new MazeExplorer();
		explorer.setMazeMap(map);
		Action action = explorer.explore(new Position(1,1));
		assertEquals(action, Action.DO_NOTHING);
	}
}
