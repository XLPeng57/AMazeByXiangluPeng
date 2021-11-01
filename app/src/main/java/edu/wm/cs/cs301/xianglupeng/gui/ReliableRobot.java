package edu.wm.cs.cs301.xianglupeng.gui;


import edu.wm.cs.cs301.xianglupeng.generation.CardinalDirection;
import edu.wm.cs.cs301.xianglupeng.generation.Maze;

/**
*
* Responsibility: This interface specifies methods to operate a robot that is inside 
* a maze at a particular location and looking in a particular direction.
* An implementing class will support a robot with certain sensors. 
* A robot needs to be given an existing maze (a controller) to be operational.
* It provides an operating platform for a robot driver that experiences a maze (the real world) 
* through the sensors and actuators of this robot interface.
* @author Xianglu Peng
*
*/

public class ReliableRobot implements Robot{

	private StatePlaying maze;
	
	private float battery;
	private int odometer;

	
	private float sense_cost = 1;
	private float rotate_cost = 3;
	private float move_cost = 6;
	private float jump = 40;
	
	
	private ReliableSensor backward;
	private ReliableSensor left;
	private ReliableSensor right;
	private ReliableSensor forward;
	
	private boolean move;
	
	public ReliableRobot() {
		
		backward = new ReliableSensor();
		forward = new ReliableSensor();
		left = new ReliableSensor();
		right = new ReliableSensor();

		setBatteryLevel(3500);
		resetOdometer();
		move = true;

		
		
	}

	public void setMaze(StatePlaying play){
		this.maze = play;
	}

	public void setSensorMaze(StatePlaying play){
		backward.setmaze(play);
		forward.setmaze(play);
		left.setmaze(play);
		right.setmaze(play);
	}
//	public ReliableRobot(Controller controller) {
//		setController(controller);
//		setBatteryLevel(3500);
//		backward = new ReliableSensor();
//		forward = new ReliableSensor();
//		left = new ReliableSensor();
//		right = new ReliableSensor();
//		backward.setController(controller);
//		forward.setController(controller);
//		left.setController(controller);
//		right.setController(controller);
//		resetOdometer();
//		move = true;
//
//	}
	
	/**
	 * 
	 * Set controller to the robot.
	 */
	
//	@Override
//	public void setController(Controller controller) {
//		// TODO Auto-generated method stub
//
//		// throw !!!
//		this.controller = controller;
//	}

	
	/**
	 * Get current Position of the robot.
	 * @return array of length 2, x = array[0], y = array[1]
	 * and ({@code 0 <= x < width, 0 <= y < height}) of the maze
	 * @throws Exception if position is outside of the maze
	 */
	
	@Override
	public int[] getCurrentPosition() throws Exception {
	
		int x = maze.getCurrentPosition()[0];
		int y = maze.getCurrentPosition()[1];
		if (!maze.getMazeConfiguration().isValidPosition(x, y)) {
			throw new Exception("Current position is outside of maze.");
		}else {
			return maze.getCurrentPosition();
		}
			
		
	}
	
	/**
	 * Provides the robot's current direction.
	 * @return cardinal direction is the robot's current direction in absolute terms
	 */	

	@Override
	public CardinalDirection getCurrentDirection() {
		// TODO Auto-generated method stub
	
		return maze.getCurrentDirection();
	}

	
	/**
	 * Returns the current battery level.
	 * @return current battery level. 
	 */
	@Override
	public float getBatteryLevel() {
		// TODO Auto-generated method stub
		return battery;
	}

	
	/**
	 * Sets the current battery level.
//	 * @param A float of the current battery level.
	 */
	@Override
	public void setBatteryLevel(float level) {
		// TODO Auto-generated method stub
		battery = level;
		
	}
	
	/**
	 * Gives the energy consumption for a full 360 degree rotation.
	 * @return energy for a full rotation
	 */

	@Override
	public float getEnergyForFullRotation() {
		// TODO Auto-generated method stub
		return 4*rotate_cost;
	}

	
	/**
	 * Gives the energy consumption for moving forward for a distance of 1 step.
	 * @return energy for a single step forward
	 */
	@Override
	public float getEnergyForStepForward() {
		// TODO Auto-generated method stub
		return move_cost;
	}

	/** 
	 * Gets the distance traveled by the robot.
	 * @return the distance traveled measured in single-cell steps forward
	 */
	@Override
	public int getOdometerReading() {
		// TODO Auto-generated method stub
		return odometer;
	}

	/** 
     * Resets the odometer counter to zero.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     */
	@Override
	public void resetOdometer() {
		// TODO Auto-generated method stub
		odometer = 1;
	}

	
	/**
	 * Turn robot on the spot for amount of degrees. 
	 * If robot runs out of energy, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * @param turn is the direction to turn and relative to current forward direction. 
	 * @throws Exception 
	 */
	@Override
	public void rotate(Turn turn){
		// TODO Auto-generated method stub
		
		switch(turn) {
		//turn according to different directions 
		case LEFT:
			if (getBatteryLevel() > rotate_cost) {
				maze.keyDown(Constants.UserInput.Right, 1);
				setBatteryLevel(getBatteryLevel()-rotate_cost);
			}
			else {
				move = false;
			}
			break;
		
		case RIGHT:
			if (getBatteryLevel() > rotate_cost) {
				maze.keyDown(Constants.UserInput.Left, 1);
				setBatteryLevel(getBatteryLevel()-rotate_cost);
			}
			else {
				move = false;
			}
			break;
		
		case AROUND:
			if (getBatteryLevel() > getEnergyForFullRotation()/2) {
				maze.keyDown(Constants.UserInput.Right, 1);
				maze.keyDown(Constants.UserInput.Right, 1);
				setBatteryLevel(getBatteryLevel()-getEnergyForFullRotation()/2);
			}
			else {
				move = false;
			}
			break;
		}
		
	}

	/**
	 * Moves robot forward a given number of steps. A step matches a single cell.
	 * @param distance is the number of cells to move in the robot's current forward direction 
	 * @throws IllegalArgumentException if distance not positive
	 */
	
	@Override
	public void move(int distance) {
		// TODO Auto-generated method stub
		
		if (distance < 0) {
			throw new IllegalArgumentException ("Distance is negative!");
		}
		
	// while distance > 0, keep moving the robot 
		while (distance>0) {
		
		if (maze.getMazeConfiguration().hasWall(maze.getCurrentPosition()[0], maze.getCurrentPosition()[1], getCurrentDirection())) {
			move = false;
			System.out.println("Hit the wall");
			break;
		}
		
		if (getBatteryLevel()>getEnergyForStepForward()) {
			setBatteryLevel(getBatteryLevel()-getEnergyForStepForward());
			maze.keyDown(Constants.UserInput.Up, 1);
			distance--;
			odometer++;
		} else {
			move = false;
			System.out.println("No enough energy");
			break;
			
		}
		
	}
}
	
	/**
	 * Makes robot move in a forward direction even if there is a wall
	 * in front of it. In this sense, the robot jumps over the wall
	 * if necessary. The distance is always 1 step and the direction
	 * is always forward.
	 */

	@Override
	public void jump() {
		// TODO Auto-generated method stub
		
		if (getBatteryLevel() > jump || !hasStopped()) {
			maze.keyDown(Constants.UserInput.Jump, 1);
			setBatteryLevel(getBatteryLevel()-jump);
		}
		else {
			move = false;
		}
		
	}

	
	/**
	 * Tells if the current position is right at the exit but still inside the maze. 
	 * The exit can be in any direction. It is not guaranteed that 
	 * the robot is facing the exit in a forward direction.
	 * @return true if robot is at the exit, false otherwise
	 */
	@Override
	public boolean isAtExit() {
		// TODO Auto-generated method stub
		int x = maze.getCurrentPosition()[0];
		int y = maze.getCurrentPosition()[1];
		return maze.getMazeConfiguration().getFloorplan().isExitPosition(x, y);
	}

	/**
	 * Tells if current position is inside a room. 
	 * @return true if robot is inside a room, false otherwise
	 */	
	@Override
	public boolean isInsideRoom() {
		// TODO Auto-generated method stub
		
		int x = maze.getCurrentPosition()[0];
		int y = maze.getCurrentPosition()[1];
		return maze.getMazeConfiguration().getFloorplan().isInRoom(x,y);
		
	}
	
	/**
	 * Tells if the robot has stopped for reasons like lack of energy, 
	 * hitting an obstacle, etc.
	 * @return true if the robot has stopped, false otherwise
	 */
	@Override
	public boolean hasStopped() {
		// TODO Auto-generated method stub
		return !move;
	}

	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		
		if (getBatteryLevel()<sense_cost) {
			move = false;
		}else {
			setBatteryLevel(getBatteryLevel()-sense_cost);
			
		}
		int distance = 0; //count the distance 
		
		// if the robot does not have sensor at the given direction, throw exception
		// get the relative direction of the robot
		
		CardinalDirection cd = null;
		float[] power = new float[1];
		power[0] = sense_cost;
		
		//check if sensor exists for each direction then get the relative direction 
		switch (direction) {
		case FORWARD:
			if (forward != null) {;}
			else {
				throw new UnsupportedOperationException("No sensor in this direction or sensor is currently not operational!");
			}	
			forward.setSensorDirection(direction);
			cd = forward.cd;
			try {
				distance = forward.distanceToObstacle(getCurrentPosition(), cd, power);	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
			
		case BACKWARD:
			
			if (backward != null) {;}
			else {
				throw new UnsupportedOperationException("No sensor in this direction or sensor is currently not operational!");
			}
			backward.setSensorDirection(direction);
			cd = backward.cd;
			try {
				distance = backward.distanceToObstacle(getCurrentPosition(), cd, power);
			} catch (Exception e) {
				e.printStackTrace();
			}		
			break;
			
		case LEFT:
			
			if (left != null) {;}
			else {
				throw new UnsupportedOperationException("No sensor in this direction or sensor is currently not operational!");
			}
			left.setSensorDirection(direction);
			cd = left.cd;
			try {
				distance = left.distanceToObstacle(getCurrentPosition(), cd, power);
			} catch (Exception e) {
				e.printStackTrace();
			}		
			break;
			
		case RIGHT:
			
			if (right != null) {;}
			else {
				throw new UnsupportedOperationException("No sensor in this direction or sensor is currently not operational!");
			}
			right.setSensorDirection(direction);
			cd = right.cd;
			try {
				distance = right.distanceToObstacle(getCurrentPosition(), cd, power);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
			
		return distance;
	}


	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
		//if the distance to the wall is eternity, the robot can see the exit
	    return (distanceToObstacle(direction) == Integer.MAX_VALUE);
	}
	

	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Method not supported.");
		
	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Method not supported.");
	}
	
	
	

}
