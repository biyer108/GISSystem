// On my honor:
//
// - I have not discussed the Java language code in my program with
// anyone other than my instructor or the teaching assistants
// assigned to this course.
//
// - I have not used Java language code obtained from another student,
// or any other unauthorized source, including the Internet, either
// modified or unmodified.
//
// - If any Java language code or documentation used in my program
// was obtained from another source, such as a text book or course
// notes, that has been clearly noted with a proper citation in
// the comments of my program.
//
// - I have not designed this program in such a way as to defeat or
// interfere with the normal operation of the supplied grading code.
//
// Bhargav Iyer
// biyer108@vt.edu

// Represents a point in the xy-plane with integer-valued coordinates.
// Supplies comparison functions specified in the Compare2D interface.
//

import java.io.*;
import java.util.*;

public class Point implements Compare2D<Point> {

    public long xcoord;
    public long ycoord;
    ArrayList<Long> locations;
    
    public Point() {
       xcoord = 0;
       ycoord = 0;
    }
    public Point(long x, long y, long offset) {
       xcoord = x;
       ycoord = y;
       locations = new ArrayList<Long>();
       locations.add(offset);
    }
    public long getX() {
       return xcoord;
    }
    public long getY() {
       return ycoord;
    }
    public ArrayList<Long> getLocations() {
       return locations;
    }
    public Direction directionFrom(long X, long Y) {

       // Implement this method...
       long angle1 = this.getX() - X;
       long angle2 = this.getY() - Y;
       if (angle1 > 0 && angle2 >= 0 ) {
          return Direction.NE;
       }
       else if (angle1 == 0 && angle2 == 0) {
          return Direction.NE;
       }
       else if (angle1 >= 0 && angle2 < 0) {
          return Direction.SE;
       }
       else if (angle1 <= 0 && angle2 > 0) {
          return Direction.NW;
       }
       else if (angle1 < 0 && angle2 <= 0) {
          return Direction.SW;
       }
       return Direction.NOQUADRANT;
    }
    
    public Direction inQuadrant(double xLo, double xHi, double yLo, double yHi) {
       
       // Implement this method...
       if (this.inBox(xLo, xHi, yLo, yHi)) {
          long x_center = (long)(xLo + xHi)/2;
          long y_center = (long)(yLo + yHi)/2;
          return this.directionFrom(x_center, y_center);
       }
       return Direction.NOQUADRANT;
    }
    
    public boolean   inBox(double xLo, double xHi, double yLo, double yHi) {
       
      // Implement this method...
      if ((this.getX() >= xLo) && (this.getX() <= xHi) && (this.getY() >= yLo) && (this.getY() <= yHi)){
         return true;
      }
      return false;
    }
    
    public String toString() {
        
       // Do not change...
       String there = new String("[(" + xcoord + ", " + ycoord + "), ");
       for (int i = 0; i < locations.size(); i++) {
          there = there + locations.get(i);
          if (i < locations.size() - 1) {
             there = there + ", ";
          }
       }
       return there + "]";
//       return new String("[" + "(" + xcoord + ", " + ycoord + ")," + locations + "]");
    }
    
    public boolean equals(Object o) {
       
      // Implement this method...
      if (o.getClass().equals(Point.class)) {
         Point obj = (Point) o;
         if ((obj.getX() == this.getX()) && (obj.getY() == this.getY())) {
            return true;
         }
      }
      return false;
    }
}
