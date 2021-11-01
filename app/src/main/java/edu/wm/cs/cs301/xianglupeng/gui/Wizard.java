package edu.wm.cs.cs301.xianglupeng.gui;


import edu.wm.cs.cs301.xianglupeng.generation.CardinalDirection;

/**
 *
 * 
 * @author Xianglu Peng
 * This interface specifies a robot driver that operates a robot to escape from a given maze. 
 *
 */

public class Wizard implements RobotDriver {
	
	private Robot robot;
	private StatePlaying maze;
	private float initial_battery;
	private float consumption;

	
	/**
	 * Assigns a robot platform to the driver. 
	 * @param r robot to operate
	 */
	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		this.robot = r;
		this.initial_battery = robot.getBatteryLevel();
		
	}
	
	/**
	 * Provides the robot driver with the maze information.
	 * @param maze represents the maze, must be non-null and a fully functional maze object.
	 */

	@Override
	public void setMaze(StatePlaying maze) {
		// TODO Auto-generated method stub
		this.maze = maze;
	}

	/**
	  * Drives the robot towards the exit.
	  * If the robot failed due to lack of energy or crashed, the method
	  * throws an exception.
	  * @return true if driver successfully reaches the exit, false otherwise
	  * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	  */	
	@Override
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		int cur_x, cur_y;
		int closer_x, closer_y;
		
		while (!robot.isAtExit()) {	
			cur_x = robot.getCurrentPosition()[0];
			cur_y = robot.getCurrentPosition()[1];
			closer_x = maze.getMazeConfiguration().getNeighborCloserToExit(cur_x, cur_y)[0];
			closer_y = maze.getMazeConfiguration().getNeighborCloserToExit(cur_x, cur_y)[1];			
			// robot stopped due to some problem, throw exception
			if (robot.hasStopped()) {
				System.out.println("Robot stopped due to some problem!");
				throw new Exception();
			}
					
			if (cur_x == closer_x) {
				//find relative direction
				//rotate and move				
				if (cur_y - closer_y == 1) {
					Rotate(CardinalDirection.North);
					drive1Step2Exit();
				}	
				if (cur_y - closer_y == -1) {
					Rotate(CardinalDirection.South);
					drive1Step2Exit();
				}	
			}
			
			if (cur_y == closer_y) {
				//find relative direction
				//rotate and move
				if (cur_x - closer_x == 1) {
					Rotate(CardinalDirection.West);
					drive1Step2Exit(); 
				}		
				if (cur_x - closer_x == -1) {
					Rotate(CardinalDirection.East);
					drive1Step2Exit();
				}	
			}
	}
		//found exit yeah 
		//return true		
		if (maze.getMazeConfiguration().getFloorplan().isExitPosition
				(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1])) {
			return true;
		}else {
			return false;
		}
		
	}

	
	/**
	 * Given the Cardinal Direction to turn and the robot's current direction
	 * Calculate the relatice direction and perform rotations. 
	 * 
	 * @param cd The direction to turn
	 * @throws Exception 
	 */
	public void Rotate(CardinalDirection cd) throws Exception {
		
		switch (cd) {
		
		case East:
			
			if (robot.getCurrentDirection() == CardinalDirection.North)
				robot.rotate(Robot.Turn.RIGHT);
			if (robot.getCurrentDirection() == CardinalDirection.South)
				robot.rotate(Robot.Turn.LEFT);
			if (robot.getCurrentDirection() == CardinalDirection.West)
				robot.rotate(Robot.Turn.AROUND);
			break;
			
		case North:			
			if (robot.getCurrentDirection() == CardinalDirection.East)
				robot.rotate(Robot.Turn.LEFT);
			if (robot.getCurrentDirection() == CardinalDirection.South)
				robot.rotate(Robot.Turn.AROUND);
			if (robot.getCurrentDirection() == CardinalDirection.West)
				robot.rotate(Robot.Turn.RIGHT);
			break;
			
		case South:			
			if (robot.getCurrentDirection() == CardinalDirection.East)
				robot.rotate(Robot.Turn.RIGHT);
			if (robot.getCurrentDirection() == CardinalDirection.North)
				robot.rotate(Robot.Turn.AROUND);
			if (robot.getCurrentDirection() == CardinalDirection.West)
				robot.rotate(Robot.Turn.LEFT);
			break;
			
		case West:			
			if (robot.getCurrentDirection() == CardinalDirection.East)
				robot.rotate(Robot.Turn.AROUND);
			if (robot.getCurrentDirection() == CardinalDirection.North)
				robot.rotate(Robot.Turn.LEFT);
			if (robot.getCurrentDirection() == CardinalDirection.South)
				robot.rotate(Robot.Turn.RIGHT);
			break;	
		}
	
	}
	
	/**
	  * Move the robot one step to the exit.
	  * @return true if it moved the robot to an adjacent cell, false otherwise
	  * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	  */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		
		
//		if (robot.getCurrentPosition() == maze.getMazeConfiguration().getMazedists().getExitPosition()) {
//			return false;
//		}
//		else 
		if (robot.getBatteryLevel() < robot.getEnergyForStepForward()) {
			throw new Exception ("No enough energy");
			
		}else {
			robot.move(1);
			return true;
		}
			
		 
		
	} 

	
	/**
	 * Returns the the difference between the robot's initial energy level at
	 * the starting position and its energy level at the exit position. 
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total energy consumption of the journey
	 */
	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		consumption = initial_battery - robot.getBatteryLevel();
		return consumption;
	}

	
	/**
	 * Returns the total length of the journey in number of cells traversed. 
	 * Being at the initial position counts as 0. 
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total length of the journey in number of cells traversed
	 */
	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		
		return robot.getOdometerReading();
	}

	@Override
	public boolean CheckLeftWall() throws Exception {
		return false;
	}

	@Override
	public boolean CheckFrontWall() throws Exception {
		return false;
	}


}
