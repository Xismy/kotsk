import java.util.ArrayList;
import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;

import tws.keeper.model.Position;
import tws.keeper.solution.MazeExplorer.CellState;

public class TestUtils {
	public static Position randomPosition(int width, int height) {
		return new Position(
				ThreadLocalRandom.current().nextInt(1, height),
				ThreadLocalRandom.current().nextInt(1, width)
		);
	}
	
	public static  ArrayList<Position> generateRandomMap(EnumSet<CellState>[][] mazeMap){
		int height = mazeMap.length;
		int width = mazeMap[0].length;
		
		for(EnumSet<CellState>[] row:mazeMap) {
			for(int i=0;i<row.length;i++) {
				row[i] = EnumSet.of(CellState.WALL);
			}
		}
		
		ArrayList<Position> path = new ArrayList<Position>();
		path.add(randomPosition(width, height));
		
		do{
			Position origin = path.get(ThreadLocalRandom.current().nextInt(0, path.size()));
			Position destiny = randomPosition(mazeMap[0].length, mazeMap.length);
			int x0 = origin.getHorizontal();
			int x = destiny.getHorizontal();
			int y0 = origin.getVertical();
			int y = destiny.getVertical();
			int stepX = x>x0?1:-1;
			int stepY = y>y0?1:-1;
			
			for(int i=x0; i!=x; i=i+stepX) {
				Position next = new Position(y0, i); 
				mazeMap[next.getVertical()][next.getHorizontal()].remove(CellState.WALL);
				if(!path.contains(next))
					path.add(next);
			}
			
			for(int j=y0; j!=y; j=j+stepY) {
					Position next = new Position(j, x); 
					mazeMap[next.getVertical()][next.getHorizontal()].remove(CellState.WALL);
					mazeMap[next.getVertical()][next.getHorizontal()].add(CellState.UNEXPLORED);
					
					if(!path.contains(next))
						path.add(next);
			}
			
		}while(path.size()<mazeMap.length*mazeMap[0].length/5);
		return path;
	}
	
	public static  ArrayList<Position> generateUnsolvableMap(EnumSet<CellState>[][] mazeMap){
		ArrayList<Position> path = generateRandomMap(mazeMap);
		Position unreachablePos = randomPosition(mazeMap[0].length, mazeMap.length);
		path.add(unreachablePos);
		return path;
	}

}
