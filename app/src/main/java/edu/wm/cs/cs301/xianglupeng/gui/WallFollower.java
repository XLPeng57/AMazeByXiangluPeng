package edu.wm.cs.cs301.xianglupeng.gui;

import edu.wm.cs.cs301.xianglupeng.generation.CardinalDirection;

public class WallFollower implements RobotDriver{

	private Robot robot;
//	private Controller maze;
	private float initial_battery;
	private float consumption;

	
	/**
	 * Assigns a robot platform to the driver. 
	 * @param r robot to operate
	 */
	@Override
	public void setRobot(Robot r) {
		this.robot = r;
		this.initial_battery = robot.getBatteryLevel();
		
	}

	/**
	 * Provides the robot driver with the maze information.
	 * Only some drivers such as the wizard rely on this information to find the exit.
	 *
	 * @param maze represents the maze, must be non-null and a fully functional maze object.
	 */
	@Override
	public void setMaze(StatePlaying maze) {

	}

	/**
	 * Provides the robot driver with the maze information.
	 * @param maze represents the maze, must be non-null and a fully functional maze object.
	 */

//	@Override
//	public void setMaze(Controller maze) {
//		this.maze = maze;
//	}

	/**
	  * Drives the robot towards the exit.
	  * If the robot failed due to lack of energy or crashed, the method
	  * throws an exception.
	  * @return true if driver successfully reaches the exit, false otherwise
	  * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	  */	
	@Override
	public boolean drive2Exit() throws Exception {
		
		robot.startFailureAndRepairProcess(Robot.Direction.LEFT, 4000, 2000);
		
		while (!robot.isAtExit()) {
			if (CheckLeftWall()) {
				continue;
			}else if (CheckFrontWall()) {
				continue;
			}else {
				robot.rotate(Robot.Turn.RIGHT);
			}
		}
			
		
		if(robot.isAtExit()) {
			if(robot.canSeeThroughTheExitIntoEternity(Robot.Direction.LEFT)) {
				robot.rotate(Robot.Turn.LEFT);
			}
			
			else if(robot.canSeeThroughTheExitIntoEternity(Robot.Direction.RIGHT)) {
				robot.rotate(Robot.Turn.RIGHT);
			}
					
		}
		
		//found exit yeah 
		//return true
		return true;
	
		
	}

	
	/**
	  * This method checks the left side of the robot to see 
	  * if it has a left wall.
	  * @return true if the robot doesn't have a left wall and move one step,
	  * false otherwise.
	  */
	
	
	public boolean CheckLeftWall() throws Exception {
		
		if (robot.distanceToObstacle(Robot.Direction.LEFT) == -1) {
			if (robot.distanceToObstacle(Robot.Direction.FORWARD) == -1) {
				if (robot.distanceToObstacle(Robot.Direction.BACKWARD) == -1) {
					if (robot.distanceToObstacle(Robot.Direction.RIGHT) == -1) {
						System.out.print("No working sensor!!!!!!\n");
						
						while (robot.distanceToObstacle(Robot.Direction.LEFT) == -1
								&& robot.distanceToObstacle(Robot.Direction.FORWARD) == -1
								&& robot.distanceToObstacle(Robot.Direction.BACKWARD) == -1
								&& robot.distanceToObstacle(Robot.Direction.RIGHT) == -1) {
							System.out.print("Waiting!!!!!!\n");
							
						}
					}else {
						System.out.print("Right sensor is used.\n");
						robot.rotate(Robot.Turn.AROUND);
						if (robot.distanceToObstacle(Robot.Direction.RIGHT)>0) {
							robot.rotate(Robot.Turn.AROUND);
							robot.rotate(Robot.Turn.LEFT);
							robot.move(1);
							return true;
							
						}else {
							robot.rotate(Robot.Turn.AROUND);
							return false;
						}					
						
					}
				}else {
					System.out.print("Backward sensor is used.\n");
					robot.rotate(Robot.Turn.RIGHT);
					if (robot.distanceToObstacle(Robot.Direction.BACKWARD)>0) {
						robot.rotate(Robot.Turn.LEFT);
						robot.rotate(Robot.Turn.LEFT);
						robot.move(1);
						return true;
					}else {
						robot.rotate(Robot.Turn.LEFT);
						return false;
					}				
				}
			}else {
				System.out.print("Forward sensor is used.\n");
				robot.rotate(Robot.Turn.LEFT);
				if (robot.distanceToObstacle(Robot.Direction.FORWARD)>0) {
					robot.rotate(Robot.Turn.RIGHT);
					robot.rotate(Robot.Turn.LEFT);
					robot.move(1);
					return true;
				}else {
					robot.rotate(Robot.Turn.RIGHT);
					return false;
				}				
			}
			
	
		}else if(robot.distanceToObstacle(Robot.Direction.LEFT) > 0) {
			robot.rotate(Robot.Turn.LEFT);
			robot.move(1);
			return true;
		}
		return false;		
	}
	
	/**
	  * This method checks the forward direction of the robot to see 
	  * if it has a front wall.
	  * @return true if the robot doesn't have a front wall and move one step,
	  * false otherwise.
	  */
	
	public boolean CheckFrontWall() throws Exception {
			
		if (robot.distanceToObstacle(Robot.Direction.FORWARD) == -1) {
			if (robot.distanceToObstacle(Robot.Direction.LEFT) == -1) {
				if (robot.distanceToObstacle(Robot.Direction.RIGHT) == -1) {
					if (robot.distanceToObstacle(Robot.Direction.BACKWARD) == -1) {
						
						while (robot.distanceToObstacle(Robot.Direction.LEFT) == -1
								&& robot.distanceToObstacle(Robot.Direction.FORWARD) == -1
								&& robot.distanceToObstacle(Robot.Direction.BACKWARD) == -1
								&& robot.distanceToObstacle(Robot.Direction.RIGHT) == -1) {
							System.out.print("Waiting!!!!!!\n");
						}
						
					}else {
						System.out.print("Backward sensor is used. FW\n");
						robot.rotate(Robot.Turn.AROUND);
						if (robot.distanceToObstacle(Robot.Direction.BACKWARD)>0) {
							robot.rotate(Robot.Turn.AROUND);
							robot.move(1);
							return true;
						}else {
							robot.rotate(Robot.Turn.AROUND);
							return false;
						}				
					}
					
				}else {
					System.out.print("Right sensor is used.FW\n");
					robot.rotate(Robot.Turn.LEFT);
					if (robot.distanceToObstacle(Robot.Direction.RIGHT)>0) {
						robot.rotate(Robot.Turn.RIGHT);
						robot.move(1);
						return true;
					}else {
						robot.rotate(Robot.Turn.RIGHT);
						return false;
					}				
				}
				
			}else {
				System.out.print("Left sensor is used. FW\n");
				robot.rotate(Robot.Turn.RIGHT);
				if (robot.distanceToObstacle(Robot.Direction.LEFT)>0) {
					robot.rotate(Robot.Turn.LEFT);
					robot.move(1);
					return true;
				}else {
					robot.rotate(Robot.Turn.LEFT);
					return false;
				}			
			}		
		}else if (robot.distanceToObstacle(Robot.Direction.FORWARD) > 0) {
			robot.move(1);
			return true;
		}
		
		return false;	
				
	}

	/**
	  * Move the robot one step to the exit.
	  * @return true if it moved the robot to an adjacent cell, false otherwise
	  * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	  */
	
	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		
		if (robot.getBatteryLevel() < robot.getEnergyForStepForward()) {
			throw new Exception ("No enough energy");
			
		}else {
			robot.move(1);
			return true;
		} 
		
	}

	@Override
	public void Rotate(CardinalDirection cd) throws Exception {

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
}
