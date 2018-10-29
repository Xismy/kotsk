package tws.keeper.solution;

import tws.keeper.model.*;
import tws.keeper.model.Observable;
import tws.keeper.solution.A_Star.UnsolvableProblemException;
import java.util.*;

/**
 * 
 * @author Ismael Yeste Espín
 *
 *	<p>Implementation of the keeper IA logic.
 *	For unknown key positions it performs the algorithm described in the MazeExplorer
 *	class. When all keys have been found, if door has been discovered it uses the A* 
 *	algorithm to reach it. Otherwise, it keeps running the MazeExplorer algorithm.
 *	<p>For known key positions it performs the A* algorithm described implemented in the
 *	A_Star class, starting by the closest key in Manhattan distance terms.
 *	
 *	<p>If it is allowed to know key position, that should be indicated by setting
 *	the environment variable "CAST_TO_MAZE_ALLOWED" to "true".
 */
public class KeeperAI implements Keeper {
	private MazeExplorer map = new MazeExplorer(); 
	private Position currentPos;
	private A_Star pathFinder = new A_Star(map);
	private ArrayList<Action> path = null;
	private boolean needPathUpdate = true;
	private Position target = null;
	private final boolean CAST_TO_MAZE_ALLOWED;
	
	public KeeperAI() {
		String env = System.getenv("CAST_TO_MAZE_ALLOWED");
		CAST_TO_MAZE_ALLOWED = env!=null && env.toLowerCase().equals("true"); 
	}

	/**
	 * Perform a step in the maze.
	 */
	public Action act(Observable maze) {
		currentPos = maze.getKeeperPosition();
		
		if(needPathUpdate) {
			if(maze.getKeysFound()!=maze.getTotalNumberOfKeys()) {
				if(CAST_TO_MAZE_ALLOWED && maze instanceof Maze)
					target = getNearestKey((Maze)maze);
				else
					target = null;
			}
			else {
				if(CAST_TO_MAZE_ALLOWED && maze instanceof Maze)
					target = ((Maze)maze).getDoorPosition();
				else if(map.doorFound())
					target = map.getDoorPosition();
				else
					target = null;
			}
		}
		
		if(target != null) {
		
			if(needPathUpdate) {
				try {
					path = pathFinder.solve(currentPos, target);
					needPathUpdate = false;
				}
				catch(UnsolvableProblemException upe) {
					System.err.println(upe);
					return Action.DO_NOTHING;
				}
			}
		}
		
		explore(maze);
		
		if(path!=null) {
			needPathUpdate = pathFinder.needUpdate();
			if(path.size()==0)
				needPathUpdate = true;
			
			return needPathUpdate? act(maze):path.remove(0);
		}
		else
			return map.explore(currentPos);
	}
	
	
	/**
	 * Observe contiguous cells and update map.
	 * @param maze
	 */
    public void explore(Observable maze) {
    	
    	if(!map.needObservation(currentPos))
    		return;
    	
    	HashMap<Position, Cell> observations = new HashMap<>(4, 1);
    	
    	Position observationPos = new Position(currentPos.getVertical()-1, currentPos.getHorizontal());
    	observations.put(observationPos, maze.lookUp());
    	
    	observationPos = new Position(currentPos.getVertical()+1, currentPos.getHorizontal());
    	observations.put(observationPos, maze.lookDown());
    	
    	observationPos = new Position(currentPos.getVertical(), currentPos.getHorizontal()+1);
    	observations.put(observationPos, maze.lookRight());
    	
    	observationPos = new Position(currentPos.getVertical(), currentPos.getHorizontal()-1);
    	observations.put(observationPos, maze.lookLeft());
    	
    	for(Position pos:observations.keySet()) {
    		switch(observations.get(pos)) {
				case DOOR:
					map.setDoorPosition(pos);
					break;
				case KEY:
					break;
				case PATH:
					break;
				case WALL:
					map.markWall(pos);
					break;
				default:
					break;
    		
    		}
    	}
    }
    
    /**
     * Get the nearest key in terms of Manhattan distance.
     * @param maze
     * @return Nearest key position.
     */
    private Position getNearestKey(Maze maze) {
    	List<Position> keys = maze.getKeysPositions();
    	List<Position> found = maze.getKeysFoundPositions();
    	Position pos = maze.getKeeperPosition();
    	int minManhattan = Integer.MAX_VALUE;
    	Position best = null;
    	
    	for(Position key:keys) {
    		if(!found.contains(key)) {
    			int manhattanDist = Math.abs(key.getHorizontal()-pos.getHorizontal())+
    								Math.abs(key.getVertical()-pos.getVertical());
    			if(manhattanDist<minManhattan) {
    				minManhattan = manhattanDist;
    				best = key;
    			}
    		}
    	}
    	
    	return best;
    }
    
    

}