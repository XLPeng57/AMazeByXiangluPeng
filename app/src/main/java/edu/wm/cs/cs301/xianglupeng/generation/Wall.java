package edu.wm.cs.cs301.xianglupeng.generation;


import android.graphics.Color;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.wm.cs.cs301.xianglupeng.gui.MazeFileWriter;
import edu.wm.cs.cs301.xianglupeng.gui.MazePanel;

/**
 * A wall is a continuous sequence of wallboards in the maze.
 *
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com,
 * Copyright (C) 1998, all rights reserved Paul Falstad granted permission to
 * modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
public class Wall {

    // The following fields are all read-only and set by constructor
    // considering updatePartitionIfBorderCase() values can be one off
    // for width and height limit
    // also: Floorplan.addWall suggests that x,y,dx,dy values
    // are scaled (multiplied) with a factor of map_unit
    /**
     * x coordinate of starting position of wall.
     * Range: 0 <= x <= width * Constants.MAP_UNIT
     */
    final private int x;
    /**
     * y coordinate of starting position of wall.
     * Range: 0 <= y <= height  * Constants.MAP_UNIT
     */
    final private int y;
    /**
     * direction (sign) and length (absolute value)
     * of wall in x coordinate.
     * Range: 0 <= x+dx <= width * Constants.MAP_UNIT
     */
    final private int dx;
    /**
     *  direction (sign) and length  (absolute value)
     *  of wall in y coordinate.
     *  Range: 0 <= y+dy <= height * Constants.MAP_UNIT
     */
    final private int dy;
    // Side condition: either dx != 0 and dy == 0 or vice versa
    // the coordinates of the end position are calculated as (x+dx, y+dy)
    

    /**
     * distance of starting position of this wall to exit
     * position of maze.
     */
    final private int dist;

    // Fields with read/write access
    /**
     * color of wall, only set by constructor and file reader.
     */
    private Color col;
    /**
     * partition flag, initially set to false. 
     * BSPBuilder code sets it to true for border case
     * and nodes that have been considered.
     * Hypothesis: flag is used to ensure that each wall
     * that is considered for the BSP tree is only handled
     * once. It may be a to-do vs done flag. 
     */
    private boolean partition;
    /**
     * seen flag tells if the wall has been seen
     * already by the user on its path through the maze.
     */
    private boolean seen;

    /**
     * Constructor assigns parameter values to instance variables.
     *
     * @param startX
     *            x coordinate of starting position of wall
     * @param startY
     *            y coordinate of starting position of wall
     * @param extensionX
     *            direction and length of wall in x coordinate
     * @param extensionY
     *            direction and length of wall in y coordinate
     * @param distance
     *            of starting position of this wall to exit position of maze
     * @param cc
     *            used to decide which color is assigned to wall, apparently
     *            it asks for a color change when a wall is split into two
     */
    public Wall(MazePanel panel, final int startX, final int startY, final int extensionX, final int extensionY,
                final int distance, final int cc) {

        System.out.println("yellowwwww");
        //System.out.println(psx + ", " + psy + ", " +pdx + ", " +pdy + ", " + distance);
        // set position
        x = startX;
        y = startY;
        // set extension
        dx = extensionX;
        dy = extensionY;

        // check conditions
        // width and height are not known, so can't check upper bounds
        assert (0 <= x) : "Starting position for x can't be negative";
        assert (0 <= y) : "Starting position for y can't be negative";
        assert (0 <= x + dx) : "Ending position for x+dx can't be negative";
        assert (0 <= y + dy) : "Ending position for y+dy can't be negative";
        assert (dx != 0 && dy == 0) || (dx == 0 && dy != 0);
//            : "Wall needs to extend into exactly one direction";

        // set distance
        dist = distance;
        // initialize boolean flags as false
        partition = false;
        seen = false;
        // determine color
//        initColor(distance, cc);
        //panel.getWallColor(distance, cc, extensionX);
//        setColor(Color.valueOf(Color.YELLOW));
        panel.setColor(Color.YELLOW);
        System.out.println("yellowwwww");

        // all fields initialized
    }

    /**
     * @param pdx
     *            direction and length of wall in x coordinate
     * @param pdy
     *            direction and length of wall in y coordinate
     * @return the matching cardinal direction
     */
    private static CardinalDirection getCD(final int pdx, final int pdy) {
        return CardinalDirection.getDirection((int) Math.signum(pdx),
                (int) Math.signum(pdy));
    }
    /**
     * Default minimum value for RGB values.
     */
    private static final int RGB_DEF = 20;
    private static final int RGB_DEF_GREEN = 60;
    /**
     * Determine and set the color for this wall.
     *
     * @param distance
     *            to exit
     * @param cc
     *            obscure
     */
//    private void initColor(final int distance, final int cc) {
//        final int d = distance / 4;
//        // mod used to limit the number of colors to 6
//        final int rgbValue = calculateRGBValue(d);
//        //System.out.println("Initcolor rgb: " + rgbValue);
//        switch (((d >> 3) ^ cc) % 6) {
//        case 0:
//            setColor(Color.valueOf(rgbValue, RGB_DEF, RGB_DEF));
//            break;
//        case 1:
//            setColor(Color.valueOf(RGB_DEF, rgbValue, RGB_DEF));
//            break;
//        case 2:
//            setColor(Color.valueOf(RGB_DEF, RGB_DEF, rgbValue));
//            break;
//        case 3:
//            setColor(Color.valueOf(rgbValue, rgbValue, RGB_DEF));
//            break;
//        case 4:
//            setColor(Color.valueOf(RGB_DEF, rgbValue, rgbValue));
//
//
//            break;
//        case 5:
//            setColor(Color.valueOf(rgbValue, RGB_DEF, rgbValue));
//            break;
//        default:
//            setColor(Color.valueOf(RGB_DEF, RGB_DEF, RGB_DEF));
//            break;
//        }
//    }

    /**
     * Computes an RGB value based on the given numerical value.
     *
     * @param distance
     *            value to select color
     * @return the calculated RGB value
     */
//    private int calculateRGBValue(final int distance) {
//        // compute rgb value, depends on distance and x direction
//        // 7 in binary is 0...0111
//        // use AND to get last 3 digits of distance
//        final int part1 = distance & 7;
//        final int add = (getExtensionX() != 0) ? 1 : 0;
//        final int rgbValue = ((part1 + 2 + add) * 70) / 8 + 80;
//        return rgbValue;
//    }

    /**
     * Computes specific integer values for the X,Y directions.
     * If x direction matters, it returns the inverse direction,
     * either -1 or 1.
     * If y direction matters, it returns the inverse direction,
     * either -2 or 2.
     * Possible return values limited to {-2,-1,1,2}.
     *
     * @return calculated direction as one of {-2,-1,1,2}
     */
    private int getDir() {
        if (getExtensionX() != 0) {
            return (getExtensionX() < 0) ? 1 : -1;
        }
        return (getExtensionY() < 0) ? 2 : -2;
    }

    /**
     * Tells if the given wall has essentially same direction but for its
     * sign, so it is the opposite (or reversed).
     *
     * @param s
     *            given wall to compare
     * @return true if given wall has same direction but reversed
     */
    public boolean hasOppositeDirection(final Wall s) {
        return this.getDir() == -s.getDir();
    }
    /**
     * Tells if the given wall has the exact same direction.
     *
     * @param s
     *            given wall to compare
     * @return true if given wall has exact same direction
     */
    public boolean hasSameDirection(final Wall s) {
        return this.getDir() == s.getDir();
    }
    /**
     * @return the distance
     */
    public int getDistance() {
        return dist;
    }

    /**
     * stores fields into the given document with the help of MazeFileWriter.
     *
     * @param doc
     *            document to add data to
     * @param mazeXML
     *            element to add data to
     * @param number
     *            number for this element
     * @param i
     *            id for this element
     */
    public void storeWall(MazePanel panel, final Document doc, final Element mazeXML,
            final int number, final int i) {
        MazeFileWriter.appendChild(doc, mazeXML, "distSeg_" + number + "_" + i,
                dist);
        MazeFileWriter.appendChild(doc, mazeXML, "dxSeg_" + number + "_" + i,
                getExtensionX());
        MazeFileWriter.appendChild(doc, mazeXML, "dySeg_" + number + "_" + i,
                getExtensionY());
        MazeFileWriter.appendChild(doc, mazeXML,
                "partitionSeg_" + number + "_" + i, isPartition());
        MazeFileWriter.appendChild(doc, mazeXML, "seenSeg_" + number + "_" + i,
                isSeen());
        MazeFileWriter.appendChild(doc, mazeXML, "xSeg_" + number + "_" + i,
                getStartPositionX());
        MazeFileWriter.appendChild(doc, mazeXML, "ySeg_" + number + "_" + i,
                getStartPositionY());
        MazeFileWriter.appendChild(doc, mazeXML, "colSeg_" + number + "_" + i,
                panel.getColor());
    }

    /**
     * Equals method that checks if the other object matches in dimensions and
     * content.
     *
     * @param other
     *            provides fully functional cells object to compare its content
     */
    public boolean equals(final Object other) {
        // trivial special cases
        if (this == other) {
            return true;
        }
        if (null == other) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        // general case
        final Wall o = (Wall) other; // type cast safe after checking class
                                   // objects
        // compare all fields
        if ((x != o.x) || (dx != o.dx) || (y != o.y) || (dy != o.dy)) {
            return false;
        }
        if ((dist != o.dist) || (partition != o.partition) || (seen != o.seen)
                || getColor()!= o.col) {
            return false;
        }
        // all fields are equal, so both objects are equal
        return true;
    }

    /**
     * Inefficient default implementation of hashCode method.
     * We override the equals method, so it is good practice to do this for the
     * hashCode method as well.
     * @return constant value so hashing works but all entities go to same bin
     */
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42; // any arbitrary constant will do
    }

    /**
     * @return the partition
     */
    public boolean isPartition() {
        return partition;
    }

    /**
     * @param partition
     *            the partition to set
     */
    public void setPartition(final boolean partition) {
        this.partition = partition;
    }

    /**
     * Sets partition bit to true for cases where the wall touches the border
     * of the maze and has an extension of 0. Method is used in BSPBuilder.
     *
     * @param width
     *            width of maze * Constants.MAP_UNIT
     * @param height
     *            height of maze * Constants.MAP_UNIT
     */
    public void updatePartitionIfBorderCase(final int width, final int height) {
        //System.out.println("update" + width + "," + height);
        // case 1: left or right most column and vertical wall
        // case 2: top or bottom row and horizontal wall
        // TODO: check if width and height are adjusted by map_unit scaling factor
        if (((x == 0 || x == width) && dx == 0)
                || ((y == 0 || y == height) && dy == 0)) {
            partition = true;
        }
    }

    /**
     * @return if the wall has been seen by the user before
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     * @param seen tells if the wall has been seen by the user before
     */
    public void setSeen(final boolean seen) {
        this.seen = seen;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return col;
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(final Color color) {
        /*
         * for debugging: use random color settings such that all walls look
         * different
         * int r = SingleRandom.getRandom().nextIntWithinInterval(20,240) ;
         * int g = SingleRandom.getRandom().nextIntWithinInterval(20,240) ;
         * int b = SingleRandom.getRandom().nextIntWithinInterval(20,240) ;
         * this.col = new Color(r,g,b); return ;
         */
        col = color;
    }

    /**
     * @return the x
     */
    public int getStartPositionX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getStartPositionY() {
        return y;
    }

    /**
     * @return the value for length and direction (sign), horizontal
     */
    public int getExtensionX() {
        return dx;
    }

    /**
     * @return the value for length and direction (sign), vertical
     */
    public int getExtensionY() {
        return dy;
    }
    /**
     * A wall has to two ends and this method gives
     * the y coordinate of a position next to one end.
     * Use getY() to get the y coordinate of the starting
     * position.
     * @return the end position +1 in y direction
     */
    public int getEndPositionY() {
        return getStartPositionY() + getExtensionY();
    }
    /**
     * A wall has to two ends and this method gives
     * the x coordinate of a position next to one end.
     * Use getX() to get the y coordinate of the starting
     * position.
     * @return the end position in x direction
     */
    public int getEndPositionX() {
        return getStartPositionX() + getExtensionX();
    }
    /**
     * @return length of wall, returned value is >= 0
     */
    public int getLength() {
        return Math.abs(getExtensionX() + getExtensionY());
    }

    /**
     * Method called in BSPBuilder.genNodes to determine the minimum of all such grades. 
     * The method does not update internal attributes and just calculates the returned value.
     * @param sl vector of walls
     * @return undocumented
     */
    public int calculateGrade(ArrayList<Wall> sl) {
        // System.out.println("Calling Seg.calculateGrade for " + x +", " + y +", " + dx +", " + dy +", ");
        // code relocated code from BSPBuilder
        // copy attributes of parameter pe
        //final int x  = getStartPositionX(); // WARNING: local variable hide instance variables
        //final int y  = getStartPositionY();
        //final int dx = getExtensionX();
        //final int dy = getExtensionY();
        final int inc = (sl.size() >= 100) ? sl.size() / 50 : 1 ; // increment for iteration below
        // define some local counter
        int lcount = 0, rcount = 0, splits = 0;
        // check all walls, loop calculates lcount, rcount and splits
        for (int i = 0; i < sl.size(); i += inc) {
            Wall se = (Wall) sl.get(i);
            // extract information from wall
             // difference between beginning of wall and x
             // difference between beginning of wall and y
             // difference between end of wall and x
             // difference between end of wall and y
            int dotStart = calculateDot(se.getStartPositionX() - x, se.getStartPositionY() - y);
            int dotEnd = calculateDot(se.getEndPositionX() - x, se.getEndPositionY() - y);
            // update splits if necessary
            if (BSPBuilder.getSign(dotStart) != BSPBuilder.getSign(dotEnd)) {
                if (dotStart == 0)
                    dotStart = dotEnd;
                else if (dotEnd != 0) {
                    splits++;
                    continue;
                }
            }
            // update lcount, rcount values
            if (dotStart > 0 ||
                    (dotStart == 0 && hasSameDirection(se))) {
                rcount++;
            } else if (dotStart < 0 ||
                    (dotStart == 0 && hasOppositeDirection(se))) {
                lcount++;
            } else {
                BSPBuilder.dbg("grade_partition problem: dot1 = "+dotStart+", dot2 = "+dotEnd);
            }
        }
        return Math.abs(lcount-rcount) + splits * 3;
    }

    /**
     * Helper method for calculateGrade, resulted from refactoring
     * @param df1x
     * @param df1y
     * @return
     */
    private int calculateDot(int df1x, int df1y) {
        return df1x * dy + df1y * (-dx);
    }
    /**
     * Calculate a partitioning of this wall into 2.
     * The starting position of the first wall matches with this wall.
     * The end position of the second wall matches with this wall.
     * The splitting coordinates are derived from the given splitter.
     * The length of this wall matches with the sum of the lengths
     * of the returned walls. 
     * Used for the BSBbuilder.
     * @param splitter is the wall that crosses this one
     * @param colchange is a color attribute
     * @return array with 2 walls that together describe this one
     */
    public Wall[] calculatePartitioning(MazePanel panel, Wall splitter, int colchange) {
    	// calculate coordinates for the splitting point (spx, spy)
    	// if the splitter is vertical, we need to cut the current wall on the x-axis
    	// if the splitter is horizontal, cut on the y-axis
    	int spx = getStartPositionX();
    	int spy = getStartPositionY();
    	if (splitter.getExtensionX() == 0) // case of a vertical splitting wall
    		spx = splitter.getStartPositionX();
    	else // case of a horizontal splitting wall
    		spy = splitter.getStartPositionY();
    	
    	Wall[] result = new Wall[2];
    	// the left wall starts at the original position, extends to (spx, spy)
    	// its extension in one direction is 0
    	// its extension in the other direction is the difference between the splitter position and its own
    	// so it goes from the original starting position to the starting position of the splitter
    	result[0] = new Wall(panel, getStartPositionX(), getStartPositionY(), 
    			spx-getStartPositionX(), spy-getStartPositionY(), 
    			getDistance(), colchange);
    	// the right wall starts where the left wall ended
    	// e.g. for x: getStartPositionX() + spx-getStartPositionX() = spx
    	// and it extends to the original end position
    	// e.g. for x: spx + getEndPositionX()-spx = getEndPositionX()
    	result[1] = new Wall(panel, spx, spy, 
    			getEndPositionX()-spx, getEndPositionY()-spy, 
    			getDistance(), colchange);
    	result[0].setPartition(isPartition());
    	result[1].setPartition(isPartition());
    	// trace output to check the result
    	/*
    	System.out.println("Wall.calculatePartitioning: (x1,y1,x2,y2) to (x3,y3,x4,y4) and (x5,y5,x6,y6)");
    	System.out.print("("+getStartPositionX()+","+getStartPositionY()+","+getEndPositionX()+","+getEndPositionY()+")");
    	System.out.print(" to ");
    	System.out.print("("+result[0].getStartPositionX()+","+result[0].getStartPositionY()+","+result[0].getEndPositionX()+","+result[0].getEndPositionY()+")");
    	System.out.print(" and ");
    	System.out.println("("+result[1].getStartPositionX()+","+result[1].getStartPositionY()+","+result[1].getEndPositionX()+","+result[1].getEndPositionY()+")");
    	 */
    	return result;
    }
}
