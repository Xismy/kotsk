# Keeper of the Seven Keys - Java coding challenge

Solution to the challenge [Keeper of the seven keys](https://github.com/The-Workshop-Inventors-of-play/keeperchallenge).

* **Author:** Ismael Yeste Espín
* **E-mail:** mailto:iye1994@gmail.com

# Solution

Solution has been implemented using three classes:
* **KeeperAI:** Implements the logic for every step in the maze.
* **MazeExplorer:** Keeps a map of the maze and implements an algorithm to explore it.
* **A_Star:** A* algorithm implementation applied to MazeExplorer objects.

#### Note: The challenge provides with an Observable object that is instance of the Maze class. A casting could be used to obtain the postion of the keys and the door. Since it is not clear this is allowed, two solutions have been implemented, one of them using this casting and other assuming this is not allowed. Default solution is the second one described. In order to use the first, the environment variable "CAST_TO_MAZE_ALLOWED" must be defined and set to "true".

### Solution 1: With allowed casting
1. We instantiate a MazeExplorer object with an empty map.
2. Set the closest key as the target position.
3. A* algorithm is run to get the best path to the target.
4. The KeeperAI object starts following the obtained path, updating the MazeExplorer.
5. If a wall is reached, go to 3.
6. If target is reached update the target to the next key or door if all keys has been obtained. If challenge is not completed, go to 3.

## Solution 2: Cast not allowed
1. The KeeperAI object observes the current cell neighbors.
2. A MazeExplorer object is updated with walls, keys or dor found by the KeeperAI.
3. The MazeExplorer checks if there is unexplored neighbor cells. If one is found that is the next step, go to 5.
4. The MazeExplorer find the reverse path, i.e. when a "branch" is completely explored, it return to the previous intersection.
5. If there are keys not found go to 3.
6. If the door has been previously found run A* algorithm and follow path. If walls are found in the path (unexplored are considered valid) re-run A*.
7. If door has not been found keep exploring, go to 3.

# Starting the server

You can start the web server with the **mvn spring-boot:run** command. 

Navigate to [http://localhost:8080](http://localhost:8080) to display the website.
