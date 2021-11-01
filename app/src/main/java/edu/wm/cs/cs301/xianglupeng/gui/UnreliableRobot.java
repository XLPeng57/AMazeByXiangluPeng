package edu.wm.cs.cs301.xianglupeng.gui;

import java.util.ArrayList;

import edu.wm.cs.cs301.xianglupeng.generation.CardinalDirection;

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

public class UnreliableRobot implements Robot{
	
	private StatePlaying maze;

	private float battery;
	private int odometer;
//	private Controller controller;
	
	private float sense_cost = 1;
	private float rotate_cost = 3;
	private float move_cost = 6;
	private float jump = 40;
	
	public UnreliableSensor backward;
	public UnreliableSensor left;
	public UnreliableSensor right;
	public UnreliableSensor forward;
	
	public boolean move;
	
	public char Forward;
	public char Left;
	public char Right;
	public char Backward;
	
	public UnreliableRobot() {
		setBatteryLevel(3500);
		resetOdometer();
		move = true;

		
		
	}

	public UnreliableRobot(char Forward, char Left, char Right, char Backward) {
		
//		setController(controller);
		setBatteryLevel(3500);
		resetOdometer();
		this.move = true;
		
		backward = new UnreliableSensor();
		forward = new UnreliableSensor();
		left = new UnreliableSensor();
		right = new UnreliableSensor();

		this.Forward = Forward;
		this.Left = Left;
		this.Right = Right;
		this.Backward = Backward;
	
	}


	/**
	 * Provides the robot with a reference to the controller to cooperate with.
	 * The robot memorizes the controller such that this method is most likely called only once
	 * and for initialization purposes. The controller serves as the main source of information
	 * for the robot about the current position, the presence of walls, the reaching of an exit.
	 * The controller is assumed to be in the playing state.
	 * //	 * @param controller is the communication partner for robot
	 *
	 * @param maze
	 * @throws IllegalArgumentException if controller is null,
	 *                                  or if controller is not in playing state,
	 *                                  or if controller does not have a maze
	 */
	@Override
	public void setMaze(StatePlaying maze) {
		this.maze = maze;
	}

	public void setSensorMaze(StatePlaying play){
		backward.setmaze(play);
		forward.setmaze(play);
		left.setmaze(play);
		right.setmaze(play);
	}
	
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
		return maze.getCurrentDirection();
	}

	
	/**
	 * Returns the current battery level.
	 * @return current battery level. 
	 */
	@Override
	public float getBatteryLevel() {
		return battery;
	}

	
	/**
	 * Sets the current battery level.
//	 * @param A float of the current battery level.
	 */
	@Override
	public void setBatteryLevel(float level) {
		battery = level;
		
	}
	
	/**
	 * Gives the energy consumption for a full 360 degree rotation.
	 * @return energy for a full rotation
	 */

	@Override
	public float getEnergyForFullRotation() {
		return 4*rotate_cost;
	}

	
	/**
	 * Gives the energy consumption for moving forward for a distance of 1 step.
	 * @return energy for a single step forward
	 */
	@Override
	public float getEnergyForStepForward() {
		return move_cost;
	}

	/** 
	 * Gets the distance traveled by the robot.
	 * @return the distance traveled measured in single-cell steps forward
	 */
	@Override
	public int getOdometerReading() {
		return odometer;
	}

	/** 
     * Resets the odometer counter to zero.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     */
	@Override
	public void resetOdometer() {
		odometer = 1;
	}

	
	/**
	 * Turn robot on the spot for amount of degrees. 
	 * If robot runs out of energy, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * @param turn is the direction to turn and relative to current forward direction. 
	 */
	@Override
	public void rotate(Turn turn) {
		switch(turn) {
		//turn according to different directions 
		case LEFT:
			if (getBatteryLevel() > rotate_cost) {
				maze.keyDown(Constants.UserInput.Right, 1);
				setBatteryLevel(getBatteryLevel()-rotate_cost);
			}
			else {
				move = false;
				System.out.print("chucuole");
				//throw new Exception ("meidianle");
			}
			break;
		
		case RIGHT:
			if (getBatteryLevel() > rotate_cost) {
				maze.keyDown(Constants.UserInput.Left, 1);
				setBatteryLevel(getBatteryLevel()-rotate_cost);
			}
			else {
				move = false;
				System.out.print("chucuole");
				//throw new Exception ("meidianle");
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
				System.out.print("chucuole");
				//throw new Exception ("meidianle");
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
		if (distance < 0) {
			throw new IllegalArgumentException ("Distance is negative!");
		}
		
	    //while distance > 0, keep moving the robot 
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
	
		int distance = 0; //count the distance 
		CardinalDirection cd = null;
		float[] power = new float[1];
		power[0] = sense_cost;
		
		
		//check if sensor exists for each direction then get the relative direction 
		switch (direction) {
		case FORWARD:
			if (!forward.operational) {
				System.out.print("No sensor in forward direction or sensor is currently not operational!\n");
				return -1;
			}
			else {
				System.out.print("Forward sensor is working.\n");
				updateSenseBattery(sense_cost);
				forward.setSensorDirection(direction);
				cd = forward.cd;
				try {
					distance = forward.distanceToObstacle(getCurrentPosition(), cd, power);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
			
			
			break;
			
		case BACKWARD:
			 
			if (!backward.operational) {
				System.out.print("No sensor in backward direction or sensor is currently not operational!\n");
				return -1;
			}
			else {
				System.out.print("Backward sensor is working.\n");
				updateSenseBattery(sense_cost);
				backward.setSensorDirection(direction);
				cd = backward.cd;
				try {
					distance = backward.distanceToObstacle(getCurrentPosition(), cd, power);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}		
			break;
			
		case LEFT:
			
			if (!left.operational) {
				System.out.print("No sensor in left direction or sensor is currently not operational!\n");
				return -1;
			}
							
			else {
				System.out.print("Left sensor is working.\n");
				updateSenseBattery(sense_cost);
				left.setSensorDirection(direction);
				cd = left.cd;
				try {
					distance = left.distanceToObstacle(getCurrentPosition(), cd, power);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			break;
			
		case RIGHT:
			
			if (!right.operational) {
				System.out.print("No sensor in right direction or sensor is currently not operational!\n");
				return -1;
			}
			else {
				System.out.print("Right sensor is working.\n");
				updateSenseBattery(sense_cost);
				right.setSensorDirection(direction);
				cd = right.cd;
				try {
					distance = right.distanceToObstacle(getCurrentPosition(), cd, power);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
		
			break;
		}
			
		return distance;
	}
	
	public void updateSenseBattery(float cost) {
		if (getBatteryLevel()<cost) {
			move = false;
		}else {
			setBatteryLevel(getBatteryLevel()-cost);
			
		}
	}


	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
	
		//if the distance to the wall is eternity, the robot can see the exit
		if(getBatteryLevel() < sense_cost) {
			move = false;
		}
		setBatteryLevel(getBatteryLevel() - sense_cost);

		
	    return (distanceToObstacle(direction) == Integer.MAX_VALUE);
	}
	

	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		
		ArrayList<UnreliableSensor> array = new ArrayList<UnreliableSensor> () ;
		
		if  (this.Forward == '0')
			array.add(forward);
		if  (this.Left == '0')
			array.add(left);
		if  (this.Right == '0')
			array.add(right);
		if  (this.Backward == '0')
			array.add(backward);
		
		if (array.size()>0) {
			for(int i=0;i< array.size(); i++) {
				array.get(i).startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
				try {
					Thread.sleep(1300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	
	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Method not supported.");
	}
	
	
	
	

}
