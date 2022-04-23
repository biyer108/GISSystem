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

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

// To support testing, we will make certain elements of the generic.
//
// You may safely add data members and function members as needed, but
// you must not modify any of the public members that are shown.
//
public class prQuadTree< T extends Compare2D<? super T> > {
   
   // Inner classes for nodes (public so test harness has access)  
//   public abstract class prQuadNode { . . . }
   public abstract class prQuadNode{
//      T data;
//      prQuadLeaf leaf;
//      prQuadInternal quad;
   }

   public class prQuadLeaf extends prQuadNode {
      // Use an ArrayList to support a bucketed implementation later.
      ArrayList<T> Elements;
   }

   public class prQuadInternal extends prQuadNode {
      // Use base-type pointers since children can be either leaf nodes
      // or internal nodes.
      prQuadNode NW, NE, SE, SW;
   }
   
   // prQuadTree elements (public so test harness has access)
   public prQuadNode root;
   public long xMin, xMax, yMin, yMax;
   
   // Add private data members as needed...

   // Initialize quadtree to empty state, representing the specified region.
   // Pre:   xMin < xMax and yMin < yMax
   public prQuadTree(long xMin, long xMax, long yMin, long yMax) {
      this.xMin = xMin;
      this.xMax = xMax;
      this.yMin = yMin;
      this.yMax = yMax;
      this.root = null;
   }
    
   // Pre:   elem != null
   // Post:  If elem lies within the tree's region, and elem is not already 
   //        present in the tree, elem has been inserted into the tree.
   // Return true iff elem is inserted into the tree. 
   public boolean insert(T elem) {
	   
	  // Implement this method...
     this.root = insertHelper(this.root, elem, this.xMin, this.xMax, this.yMin, this.yMax);
     if (this.root != null) {
        return true;
     }
     return false;
   }

   // Pre:  elem != null
   // Returns reference to an element x within the tree such that 
   // elem.equals(x)is true, provided such a matching element occurs within
   // the tree; returns null otherwise.
   public T find(T Elem) {
	   
	   // Implement this method...
      prQuadNode node = this.root;
      long xLo = this.xMin;
      long xHigh = this.xMax;
      long yLo = this.yMin;
      long yHigh = this.yMax; 
      while (node.getClass().equals(prQuadInternal.class)) {
         long xcenter = (xLo + xHigh)/2;
         long ycenter = (yLo + yHigh)/2;
         prQuadInternal quad = (prQuadInternal) node;
         Direction temp = Elem.directionFrom(xcenter, ycenter);
         if (temp == Direction.NE) {
            node = quad.NE;
            xLo = xcenter;
            yLo = ycenter;
         }
         else if (temp == Direction.NW) {
            node = quad.NW;
            xHigh = xcenter;
            yLo = ycenter;
         }
         else if (temp == Direction.SE) {
            node = quad.SE;
            xLo = xcenter;
            yHigh = ycenter;
         }
         else if (temp == Direction.SW) {
            node = quad.SW;
            xHigh = xcenter;
            yHigh = ycenter;
         }
      }
      if (node.getClass().equals(prQuadLeaf.class)) {
         prQuadLeaf leaf = (prQuadLeaf) node;
         for (T el : leaf.Elements) {
            if (el.equals(Elem)) {
               return el;
            }
         }
      }
      return null;
   }

   // Pre:  xLo < xHi and yLo < yHi
   // Returns a collection of (references to) all elements x such that x is 
   // in the tree and x lies at coordinates within the defined rectangular 
   // region, including the boundary of the region.
   public ArrayList<T> find(long xLo, long xHi, long yLo, long yHi) {
      ArrayList<T> results = findHelper(this.root, xLo, xHi, yLo, yHi);
      return results;
   }

   // Add private helpers as needed...
   boolean inRegion(long xLo, long xHi, long yLo, long yHi) {
      return (xLo <= this.xMax && xHi >= this.xMin && yLo <= this.yMax && yHi >= this.yMin);
   }

   private ArrayList<T> findHelper(prQuadNode node, long xLo, long xHi, long yLo, long yHi) {
      ArrayList<T> vals = new ArrayList<T>();
      if (node != null) {
         if (inRegion(xLo, xHi, yLo, yHi)) {
            if (node.getClass().equals(prQuadLeaf.class)) {
               prQuadLeaf leaf = (prQuadLeaf) node;
               for (T el : leaf.Elements) {
                  if (el.inBox(xLo, xHi, yLo, yHi)) {
                     vals.add(el);
                  }
               }
            }
            else {
               prQuadInternal quad = (prQuadInternal) node;
               long xcenter = (xLo + xHi)/2;
               long ycenter = (yLo + yHi)/2;
               if (inRegion(xcenter, xHi, ycenter, yHi)) {
                  vals.addAll(findHelper(quad.NE, xLo, xHi, yLo, yHi));
               }
               if (inRegion(xLo, xcenter, ycenter, yHi)) {
                  vals.addAll(findHelper(quad.NW, xLo, xHi, yLo, yHi));
               }
               if (inRegion(xcenter, xHi, yLo, ycenter)) {
                  vals.addAll(findHelper(quad.SE, xLo, xHi, yLo, yHi));
               }
               if (inRegion(xLo, xcenter, yLo, ycenter)) {
                  vals.addAll(findHelper(quad.SW, xLo, xHi, yLo, yHi));
               }
            }
         }
      }
      return vals;
   }

   private prQuadNode insertHelper(prQuadNode node, T elem, long xLo, long xHigh, long yLo, long yHigh) {
      Direction temp2 = elem.inQuadrant(xLo, xHigh, yLo, yHigh);
      long xcenter = (xLo + xHigh)/2;
      long ycenter = (yLo + yHigh)/2;
      if (node == null) {
         prQuadLeaf leaf = new prQuadLeaf();
         leaf.Elements = new ArrayList<T>();
         leaf.Elements.add(elem);
         return leaf;
      }
      if (node.getClass().equals(prQuadLeaf.class)) {
         prQuadLeaf leaf1 = (prQuadLeaf) node;
         if (leaf1.Elements.size() < 4) {
            for (T el : leaf1.Elements) {
               if (el.equals(elem)) {
                  el.getLocations().addAll(elem.getLocations());
                  return leaf1;
               }
            }
            leaf1.Elements.add(elem);
            return leaf1;
         }
         prQuadInternal quad = new prQuadInternal();
         for (T el : leaf1.Elements) {
            Direction temp1 = el.inQuadrant(xLo, xHigh, yLo, yHigh);
            if (temp1 ==  Direction.NE) {
               quad.NE = insertHelper(quad.NE, el, xcenter, xHigh, ycenter, yHigh);
            }
            else if (temp1 == Direction.NW) {
               quad.NW = insertHelper(quad.NW, el, xLo, xcenter, ycenter, yHigh);
            }
            else if (temp1 == Direction.SE) {
               quad.SE = insertHelper(quad.SE, el, xcenter, xHigh, yLo, ycenter);
            }
            else if (temp1 == Direction.SW) {
               quad.SW = insertHelper(quad.SW, el, xLo, xcenter, yLo, ycenter);
            }
         }
         if (temp2 == Direction.NE) {
            quad.NE = insertHelper(quad.NE, elem, xcenter, xHigh, ycenter, yHigh);
         }
         else if (temp2 == Direction.NW) {
            quad.NW = insertHelper(quad.NW, elem, xLo, xcenter, ycenter, yHigh);
         }
         else if (temp2 == Direction.SE) {
            quad.SE = insertHelper(quad.SE, elem, xcenter, xHigh, yLo, ycenter);
         }
         else if (temp2 == Direction.SW) {
            quad.SW = insertHelper(quad.SW, elem, xLo, xcenter, yLo, ycenter);
         }
         return quad;
      }
      if (node.getClass().equals(prQuadInternal.class)) {
         prQuadInternal quad1 = (prQuadInternal) node;
         if (temp2 == Direction.NE) {
            quad1.NE = insertHelper(quad1.NE, elem, xcenter, xHigh, ycenter, yHigh);
         }
         else if (temp2 == Direction.NW) {
            quad1.NW = insertHelper(quad1.NW, elem, xLo, xcenter, ycenter, yHigh);
         }
         else if (temp2 == Direction.SE) {
            quad1.SE = insertHelper(quad1.SE, elem, xcenter, xHigh, yLo, ycenter);
         }
         else if (temp2 == Direction.SW) {
            quad1.SW = insertHelper(quad1.SW, elem, xLo, xcenter, yLo, ycenter);
         }
         return quad1;
      }
      return null;
   }

   

}