package tws.keeper.solution;

import java.util.EnumMap;
import java.util.EnumSet;
import tws.keeper.model.Position;
import tws.keeper.model.Action;

/**
 * 
 * @author Ismael
 * 
 * <p>Performs a maze exploration, keeping a map of the maze. Assuming the keeper
 * is at certain position in the maze, the exploration algorithm return the action
 * the keeper should do as it is described bellow.</p>
 * <ul>
 *	<li>Map is represented by an array of cell states.</li>
 * 	<li>In the beginning the whole map is set to UNEXPLORED.</li>
 *  <li>For every step in the algorithm if an adjacent cell is UNEXPLORED that is 
 *  marked as the next cell to explore.</li>
 *  <li>When a move is done, the origin cell state is set to EXPLORING_[direction].
 *  E.g. EXPLORING_LEFT when moving towards a left cell.</li>
 *  <li>If there is no UNEXPLORED cell, an EXPLORING cell is chosen only if it is a 
 *  backward movement.</li>
 *  </ul>
 * 
 * <p>This algorithm assures the whole maze is explored and branches are explored just
 * once.
 * MazeExplorer objects keeps information of the explored cells, but does not observe
 * the maze. Walls, keys and doors found should be passed using the proper methods.
 */
public class MazeExplorer {
	
	public enum CellState{
		UNEXPLORED,
		EXPLORING_UP,
		EXPLORING_DOWN,
		EXPLORING_RIGHT,
		EXPLORING_LEFT,
		EXPLORED,
		WALL
	}
	
	private EnumSet<CellState>[][] mazeMap;
	private Position doorPosition = null;
	private EnumMap<Action, CellState> straightMove = new EnumMap<Action, CellState>(Action.class);
	private EnumMap<Action, CellState> reverseMove = new EnumMap<Action, CellState>(Action.class);
	
	/**
	 * 
	 */
	public MazeExplorer() {
		mazeMap = new EnumSet[40][40];
		for(EnumSet<CellState>[] row:mazeMap)
			for(int i=0;i<row.length;i++)
				row[i] = EnumSet.of(CellState.UNEXPLORED);
		
		straightMove.put(Action.GO_UP, CellState.EXPLORING_UP);
		straightMove.put(Action.GO_DOWN, CellState.EXPLORING_DOWN);
		straightMove.put(Action.GO_RIGHT, CellState.EXPLORING_RIGHT);
		straightMove.put(Action.GO_LEFT, CellState.EXPLORING_LEFT);
		
		reverseMove.put(Action.GO_UP, CellState.EXPLORING_DOWN);
		reverseMove.put(Action.GO_DOWN, CellState.EXPLORING_UP);
		reverseMove.put(Action.GO_RIGHT, CellState.EXPLORING_LEFT);
		reverseMove.put(Action.GO_LEFT, CellState.EXPLORING_RIGHT);
	}
	
	/**
	 * Mark cell defined by position as a wall.
	 * @param pos Position of the cell.
	 */
	public void markWall(Position pos) {
		mazeMap[pos.getVertical()][pos.getHorizontal()]= EnumSet.of(CellState.WALL);
	}
	
	/**
	 * Set the position of the door.
	 * @param position Door position.
	 */
	public void setDoorPosition(Position position) {
		doorPosition = position;
	}
	
	/**
	 * Perform a step of the exploring algorithm.
	 * @param pos Current position of the keeper.
	 * @return Next Action in the exploration algorithm.
	 */
	public Action explore(Position pos) {
		Action nextMove = Action.DO_NOTHING;
		int x = pos.getHorizontal();
		int y = pos.getVertical();
		
		
		EnumMap<Action, EnumSet<CellState>> moveOptions = new EnumMap<Action, EnumSet<CellState>>(Action.class);
		moveOptions.put(Action.GO_UP, mazeMap[y-1][x]);
		moveOptions.put(Action.GO_DOWN, mazeMap[y+1][x]);
		moveOptions.put(Action.GO_RIGHT, mazeMap[y][x+1]);
		moveOptions.put(Action.GO_LEFT, mazeMap[y][x-1]);
		
		mazeMap[y][x].remove(CellState.UNEXPLORED);
		
		for(Action move:moveOptions.keySet()) {
			EnumSet<CellState> moveResult = moveOptions.get(move);
			
			if(moveResult.contains(CellState.UNEXPLORED)) {
				nextMove = move;
				mazeMap[y][x].add(straightMove.get(nextMove));
				mazeMap[y][x].remove(CellState.EXPLORED);
				break;
			}
			
			if(moveResult.contains(reverseMove.get(move))) {
				nextMove = move;
				mazeMap[y][x].add(CellState.EXPLORED);
			}
			
		}
		
		return nextMove;
	}
	
	/**
	 * 
	 * @return Door position.
	 */
	public Position getDoorPosition() {
		return doorPosition;
	}
	
	/**
	 * 
	 * @return True if door has already been found. 
	 */
	public boolean doorFound() {
		return doorPosition != null;
	}
	
	/**
	 * 
	 * @param pos Position to evaluate.
	 * @return True if position can be reached.
	 */
	public boolean canVisit(Position pos) {
		int x = pos.getHorizontal();
		int y = pos.getVertical();
		
		if(x<0 || y<0 || x>=mazeMap[0].length || y>=mazeMap.length)
			return false;
		
		return !mazeMap[pos.getVertical()][pos.getHorizontal()].contains(CellState.WALL);
	}
	
	/**
	 * If a cell has been visited, it does not need to be
	 * explored again.
	 * @param pos Position to evaluate.
	 * @return True if it needs to be explored.
	 */
	public boolean needObservation(Position pos) {
		return mazeMap[pos.getVertical()][pos.getHorizontal()].contains(CellState.UNEXPLORED);
	}
	
	/**
	 * 
	 * @param mazeMap
	 */
	public void setMazeMap(EnumSet<CellState>[][] mazeMap) {
		this.mazeMap = mazeMap;
	}
}
