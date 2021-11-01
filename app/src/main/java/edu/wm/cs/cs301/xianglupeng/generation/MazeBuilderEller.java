
package edu.wm.cs.cs301.xianglupeng.generation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * This class has the responsibility to create a maze of given dimensions (width, height) 
 * together with a solution based on a distance matrix.
 * The MazeBuilder implements Runnable such that it can be run a separate thread.
 * The MazeFactory has a MazeBuilder and handles the thread management.   

 * 
 * The maze is built with Eller's algorithm. 
 *   
 * @author Xianglu Peng
 */

public class MazeBuilderEller extends MazeBuilder implements Runnable {
	
	public MazeBuilderEller() {
		super();
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	
	
private int[][] eller;
	
	
	/**
	 * This method generates pathways into the maze by using Eller's algorithm. 
	 * First, set values for the first row, break the walls of the first row
	 * Then, break vertical walls and use for loop to go through remaining matrix
	 * Finally, break the walls of the last row. 
	 */

	@Override
	protected void generatePathways() {
		
	
		eller = new int[width][height];
		
		for (int x = 0; x < width; x++) {
			eller[x][0] = x + 1; 
		}
		
		
		horizontalRows(0);
		
		for (int y = 1; y < height-1; y++) { 
			vertical(y);
			horizontalRows(y);
		}
		
		vertical(height - 1);
		
		int y = height - 1;
		
		for(int x = 0; x < width-1; x++) {
			Wallboard wall = new Wallboard(x, y, CardinalDirection.East);
			if(eller[x][y] != eller[x+1][y] && floorplan.canTearDown(wall)) {
				floorplan.deleteWallboard(wall);
				eller[x+1][y] = eller[x][y];
			}
		}
		
		
		
		
	}
	
	/**
	 * This method generates random numbers to decide whether to break the wall.
	 * It also break walls that are not selected by the random number generator.
	 * @param row
	 */
	
	private void horizontalRows(int row) {
	
		for(int x = 0; x < width - 1; x++) {
		
			Wallboard w1 = new Wallboard(x, row, CardinalDirection.East);
			Wallboard w2 = new Wallboard(x, row, CardinalDirection.South);
		
			int rand = random.nextIntWithinInterval(1,10);
			
			if(rand <= 5 || (floorplan.canTearDown(w1) && floorplan.isInRoom(x+1, row))) {

				breakWall(x, row);
			}
		
			if( x!=0 && !floorplan.canTearDown(w2)) {
			
				breakWall(x, row);
			}
			
		}
		

}
	
	/**
	 * This method delete walls and merge sets.
	 * Check the nearest cells are in the same set, pass. 
	 * If not in the same set, merge the set, update other cell in the set and delete the wall 
	 * @param x
	 * @param y
	 */
	private void breakWall(int x, int y) {
		
		int Old, New;
		
		if (eller[x][y] == eller[x + 1][y]) return;
		
		else {
			Old  =  eller[x + 1][y];
			New = eller[x][y];
			eller[x + 1][y] = eller[x][y];
		}
		
		for (int a = 0; a < width; a++) {
			if (eller[a][y] == Old) {
				eller[a][y] = New;
			}
		}

		Wallboard wall = new Wallboard(x, y, CardinalDirection.East);
		floorplan.deleteWallboard(wall);
	}
	
	/**
	 * This method extends the maze vertically.
	 * First, use hashmap to store the set and its elements.
	 * Then, get unique value in this row, If the wall can tear down, save it into hash map (toRemove).
	 * Then, store all bottom walls in each set and randomly select walls that will be delete.
	 * Finally, find the maximum value in this row and update cells with new sets 
	 * @param y
	 */
	private void vertical(int y) {
		
		HashMap<Integer, List<Integer>> set = new HashMap<Integer, List<Integer>>();
		List<Integer> deletewalls = new ArrayList<Integer>();
		
		for (int x = 0; x < width; x++) {
			
			Wallboard wall = new Wallboard(x, y-1, CardinalDirection.South);
			ArrayList<Integer> temp = new ArrayList<Integer>();
			
			if (set.get(eller[x][y-1]) == null) {
				set.put(eller[x][y-1],temp);
			}
			
			if (floorplan.canTearDown(wall)) {
				set.get(eller[x][y-1]).add(x); 
			}
			
			if (floorplan.isInRoom(x, y)) deletewalls.add(x); 
		}

		
		Collection<Integer> keySet = set.keySet();
		for (Object key: keySet) {
			
			
			List<Integer> valueSet = set.get(key); 		
			Collections.shuffle(valueSet);
			Random remove = new Random();
			int temp = remove.nextInt(valueSet.size())+1;
			for (int p=0; p<temp;p++) {
				if (!deletewalls.contains(set.get(key).get(p))){
					deletewalls.add(set.get(key).get(p));
				}
			} 
		}		

		for (int i = 0; i < deletewalls.size(); i++) {
			Wallboard wall = new Wallboard(deletewalls.get(i), y-1, CardinalDirection.South);
			floorplan.deleteWallboard(wall);
			eller[deletewalls.get(i)][y] = eller[deletewalls.get(i)][y-1];
		}		
		
		int find_max = findMax(y);
			
		for (int x = 0; x < width-1; x++)
			if (eller[x][y] == 0) {
				
				if (!floorplan.hasWall(x, y, CardinalDirection.West) && eller[x-1][y] != 0) {
					eller[x][y] = eller[x - 1][y];
				}else{
					eller[x][y] = find_max+1;
					find_max = find_max +1;
				}
			}
		
	}


	/** This method finds the maximum value in this row
	 *
	 */
	public int findMax(int row) {
		
		int find_max = eller[0][row];
		for(int x = 0; x < width; x++) {
			if(eller[x][row] > find_max) {
				find_max = eller[x][row];
			}
		}
		return find_max;
	}
}

/* Pseudocode For Eller
 * 
 * 1. generatePathways()
 * 
 * 	- create a matrix with dimension width*height to represent our maze
 *  - create the first row and fill it with number 1 to width
 *  - delete walls and merge sets for the first row
 *  - use for loop to go over remaining matrix excepts last row 
 *  - merge sets for last row
 *   - use for loop to go through the last row, if the current cell and 
 *     its right cell are not in the same set, delete the walls 
 * 
 * 
 * 2. horizontalRows(int row)
 * 
 *  - use for loop to go through this row
 *  - use simple random method to generate number, decide whether to delete a wall
 *  - if the cell is in the room but was not selected by the random number generator, we delete this wall manually
 * 
 * 
 * 3. breakWall(int x, int y)
 * 
 *  - if the current cell and its next cell are already in the same set, pass
 *  - otherwise delete the wall (floorplan) and update the sets (matrix)
 * 
 * 
 * 4. vertical(int y)
 * 
 *  - create bottom walls for each cell in this row and use hashmap to store sets and indices
 *  - create a list (deletewalls) to store the indices for walls that would be deleted 
 *  - generate random numbers to select walls, add them into the list(deletewalls)
 * 	- add walls that are in rooms but was not selected into the list(deletewalls)
 *  - delete walls (floorplan) in the list and update sets (matrix)
 *  - update the set and assign new values to the matrix
 *  - then find the maximum value of the current row, update sets for the next row 
 *  
 */



		


