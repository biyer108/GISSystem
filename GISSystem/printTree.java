import java.io.*;
import java.util.*;
public class printTree {
	String pad = new String("   ");
    public void printTree(FileWriter Out, prQuadTree<Point> Tree) {
    	try {
           if ( Tree.root == null )
              Out.write("  Empty tree.\n");
           else
              printTreeHelper(Out,  Tree.root, "");
    	}
    	catch ( IOException e ) {
    		return;
    	}
     }

	@SuppressWarnings("rawtypes")
	public void printTreeHelper(FileWriter Out, prQuadTree.prQuadNode sRoot, String Padding) {

		try {
			// Check for empty leaf
			if ( sRoot == null ) {
				Out.write(Padding + "*\n");
				return;
			}
			// Check for and process SW and SE subtrees
			if ( sRoot.getClass().equals(prQuadTree.prQuadInternal.class) ) {
				prQuadTree.prQuadInternal p = (prQuadTree.prQuadInternal) sRoot;
				printTreeHelper(Out, p.SW, Padding + pad);
				printTreeHelper(Out, p.SE, Padding + pad);
			}
			// Display indentation padding for current node
			//Out.write(Padding);

			// Determine if at leaf or internal and display accordingly
			if ( sRoot.getClass().equals(prQuadTree.prQuadLeaf.class) ) {
				prQuadTree.prQuadLeaf p = (prQuadTree.prQuadLeaf) sRoot;
				Out.write(Padding);
				for (int pos = 0; pos < p.Elements.size(); pos++) {
					Out.write(p.Elements.get(pos) + " ");
				}
				Out.write("\n");
//				Out.write(" " + Padding + p.Elements + "\n");
			}
			else if ( sRoot.getClass().equals(prQuadTree.prQuadInternal.class) )
				Out.write(Padding + "@\n" );
			else
				Out.write(sRoot.getClass().getName() + "#\n");

			// Check for and process NE and NW subtrees
			if ( sRoot.getClass().equals(prQuadTree.prQuadInternal.class) ) {
				prQuadTree.prQuadInternal p = (prQuadTree.prQuadInternal) sRoot;
				printTreeHelper(Out, p.NE, Padding + pad);
				printTreeHelper(Out, p.NW, Padding + pad);
			}
		}
		catch ( IOException e ) {
			return;
		}
	}
}