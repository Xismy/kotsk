package tws.keeper.solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

import tws.keeper.model.Action;
import tws.keeper.model.Position;
import tws.keeper.solution.MazeExplorer.CellState;

/**
 * 
 * @author Ismael Yeste Espin
 * 
 * <p>A* algorithm implementation to find the best path form one Cell 
 * of the Maze to another target Cell.
 */
public class A_Star {
	private MazeExplorer map;
	private Position target;
	private ArrayList<Position> pathPositions = null;
	
	/**
	 * 
	 * @param explorer
	 */
	public A_Star(MazeExplorer explorer){
		this.map = explorer;
	}
	
	public boolean needUpdate() {
		if(pathPositions==null)
			return true;
		
		for(Position pos:pathPositions) {
			if(!map.canVisit(pos))
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * Runs the A* algorithm, finding the best path to reach the target.
	 * @param start Start position.
	 * @return Actions the Keeper should take to reach the target.
	 */
	public ArrayList<Action> solve(Position start, Position target) throws UnsolvableProblemException{
		this.target = target;
		
		ArrayList<Node> closedSet = new ArrayList<Node>();
		ArrayList<Node> openSet = new ArrayList<Node>();
		
		openSet.add(new Node(start));
		
		int i=0;
		
		while (!openSet.isEmpty()) {
			Node current = openSet.get(0);
			if(current.position.equals(target)) {
				pathPositions = new ArrayList<Position>();
				return current.getPath(pathPositions);
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			ArrayList<Node> neighbors = current.getNeighbors();
			
			for(Node neighbor:neighbors) {
				if(!closedSet.contains(neighbor)) {
					if(!openSet.contains(neighbor)) {
						openSet.add(neighbor);
					}
					else {
						Node nodeInOpen = openSet.get(openSet.indexOf(neighbor));
						if(nodeInOpen.compareTo(neighbor)>0) {
							openSet.remove(nodeInOpen);
							openSet.add(neighbor);
						}
					}
				}
			}
			
			Collections.sort(openSet);
		}
		
		throw new UnsolvableProblemException();
	}
	
	/**
	 * Nodes for the A* algorithm. Contains the evaluations of the heuristic function(h), 
	 * cost function(g) and total cost(f). The h function is defined using the Manhattan
	 * distance between the Node and the target. The g function is defined by the total
	 * steps necessaries to reach the Node from the start. 
	 * Nodes natural order is defined by their f function. Equal method is defined in a
	 * way that two Nodes are equals if they have the same position.
	 * Note that this natural order in inconsistent with equals. 
	 * @author Ismael Yeste Espin
	 *
	 */
	private class Node implements Comparable<Node>{
		Position position;
		int h; //Heuristic
		int g; //Cost
		int f; //h+g
		Node parent = null;
		
		
		/**
		 * Create a Node without parent.
		 * @param pos Position of the Node.
		 */
		Node(Position pos){
			position = pos;
			h = Math.abs(target.getHorizontal()-pos.getHorizontal())+ //Manhattan distance
				Math.abs(target.getVertical()-pos.getVertical());
			g = 0;
			f = h; 
			
		}
		
		/**
		 * Create a child Node from a given parent. 
		 * @param pos Position of the child Node.
		 * @param parent Parent Node.
		 */
		Node(Position pos, Node parent){
			this(pos);
			g = parent.g+1;
			f += g;
			this.parent = parent;
		}
		
		/**
		 * Instantiate every neighbors nodes (top, bottom, left and right) of the Node. 
		 * @return Array containing neighbor nodes.
		 */
		ArrayList<Node> getNeighbors() {
			ArrayList<Node> neighbors = new ArrayList<Node>(); 
			int x = position.getHorizontal();
			int y = position.getVertical();
			Position positions[] = {
					new Position(y-1, x),
					new Position(y+1, x),
					new Position(y, x-1),
					new Position(y, x+1)
			};
			
			for(Position position:positions) {
				if(map.canVisit(position))
					neighbors.add(new Node(position, this));
			}
			
			return neighbors;
		}
		
		/**
		 * Find the necessary steps to reach the Node 
		 * by exploring parent nodes recursively.
		 * 
		 * @return Array containing every Action to reach the Node in the right order.
		 */
		ArrayList<Action> getPath(ArrayList<Position> positions) {
			ArrayList<Action> path = new ArrayList<Action>();
			Node current = this;
			
			while(current.parent != null) {
				int dx = current.position.getHorizontal() - current.parent.position.getHorizontal();
				int dy = current.position.getVertical() - current.parent.position.getVertical();
				
				path.add(0,
						 dx>0? Action.GO_RIGHT
						:dx<0? Action.GO_LEFT
						:dy>0? Action.GO_DOWN
						:dy<0? Action.GO_UP
						:Action.DO_NOTHING
						);
				
				positions.add(0, current.position);
				
				current = current.parent;
			}
			
			return path;
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj instanceof Node && ((Node)obj).position.equals(position);
		}
		
		@Override
		public int hashCode() {
			return position.getHorizontal()<<16 | position.getVertical();
		}

		@Override
		public int compareTo(Node node) {
			return f - node.f;
		}
	}
	
	public static class UnsolvableProblemException extends Exception{
		private static final long serialVersionUID = 1L;
		
	}
}