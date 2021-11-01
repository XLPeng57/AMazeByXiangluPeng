package edu.wm.cs.cs301.xianglupeng.gui;

import edu.wm.cs.cs301.xianglupeng.generation.CardinalDirection;

public class UnreliableSensor implements DistanceSensor, Runnable {
	
	
//	private Controller controller;
	private StatePlaying maze;
	public CardinalDirection cd;
	public boolean operational;
	private Thread threadS;
	private int RepairTime;
	private int WorkingTime;
	
	public UnreliableSensor() {
		this.operational = true;
		
	}
	
	
	public void setWorking(boolean operate) {
		this.operational = operate;
	}

	public void setmaze(StatePlaying play){
		this.maze = play;
	}
	
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		int distance = 0; //count the distance 
		
		// if the robot does not have sensor at the given direction, throw exception
		// get the relative direction of the robot

		int x = currentPosition[0];
		int y = currentPosition[1];
			
		
		while (!maze.getMazeConfiguration().hasWall(x, y, currentDirection)) {
			distance ++;

			if (currentDirection == CardinalDirection.East || currentDirection == CardinalDirection.West) {
				x = x + currentDirection.getDirection()[0];
			}else {
				y = y + currentDirection.getDirection()[1];
			}
			
			if (x < 0 || x > maze.getMazeConfiguration().getWidth() - 1 ||
					y < 0 || y > maze.getMazeConfiguration().getHeight() - 1) {
				return Integer.MAX_VALUE;
			}
			
		}
		
						
		return distance;
	
	}


	public void setMaze(StatePlaying maze) {
		this.maze = maze;
		
	}

	@Override
	public void setSensorDirection(Robot.Direction mountedDirection) {
		switch(mountedDirection) {
		
		case LEFT:
			cd = maze.getCurrentDirection().oppositeDirection().rotateClockwise();
			break;
			
		case BACKWARD:
			cd = maze.getCurrentDirection().oppositeDirection();
			break;
			
		case FORWARD:
			cd = maze.getCurrentDirection();
			break;
			
		case RIGHT:
			cd = maze.getCurrentDirection().rotateClockwise();
			break;
			
		default:
			break;
		}
	}

	@Override
	public float getEnergyConsumptionForSensing() {
		return 1;
	}

	
	/**
	 * Method starts a concurrent, independent failure and repair
	 * process that makes the sensor fail and repair itself.
	 * This creates alternating time periods of up time and down time.
	 * Up time: The duration of a time period when the sensor is in 
	 * operational is characterized by a distribution
	 * whose mean value is given by parameter meanTimeBetweenFailures.
	 * Down time: The duration of a time period when the sensor is in repair
	 * and not operational is characterized by a distribution
	 * whose mean value is given by parameter meanTimeToRepair.
	 * 
	 * This an optional operation. If not implemented, the method
	 * throws an UnsupportedOperationException.
	 * 
	 * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
	 * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
	 * @throws UnsupportedOperationException if method not supported
	 */
	
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		this.RepairTime = meanTimeToRepair;
		this.WorkingTime = meanTimeBetweenFailures;
		threadS = new Thread(this); 
		threadS.start();
		
		
	}
	
	/**
	 * This method stops a failure and repair process and
	 * leaves the sensor in an operational state.
	 * 
	 * It is complementary to starting a 
	 * failure and repair process. 
	 * 
	 * Intended use: If called after starting a process, this method
	 * will stop the process as soon as the sensor is operational.
	 * 
	 * If called with no running failure and repair process, 
	 * the method will return an UnsupportedOperationException.
	 * 
	 * This an optional operation. If not implemented, the method
	 * throws an UnsupportedOperationException.
	 * 
	 * @throws UnsupportedOperationException if method not supported
	 */

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		if (operational == true) {
			throw new UnsupportedOperationException("No running failure!");
		}

		operational = true;
		
	}


	@Override
	public void run() {
			while (true) {
			operational = false;
			try {
				Thread.sleep(RepairTime);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			operational = true;		
			try {
				Thread.sleep(WorkingTime);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			}

	}
}
