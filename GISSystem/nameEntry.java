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

import java.util.*;
import java.io.*;
public class nameEntry implements Hashable<nameEntry> {

    String key; // GIS feature name
    ArrayList<Long> locations; // file offsets of matching records

    /** Initialize a new nameEntry object with the given feature name
    * and a single file offset.
    */
    public nameEntry(String name, Long offset) {
        key = name;
        locations = new ArrayList<Long>();
        locations.add(offset);
    }
    /** Return feature name.
    */
    public String key() {
        return key;
    }
    /** Return list of file offsets.
    */
    public ArrayList<Long> locations() {
        return locations;
    }
    /** Append a file offset to the existing list.
    */
    public boolean addLocation(Long offset) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i) == offset) {
                return false;
            }
        }
        locations.add(offset);
        return true;
    }
    /** Fowler/Noll/Vo hash function is mandatory for this assignment. **/
    public int Hash() {
        final int fnvPrime = 0x01000193; // Constant values for FNV
        final int fnvBasis = 0x811c9dc5; // hash algorithm
        int hashValue = fnvBasis;
        for (int i = 0; i < key.length(); i++) {
            hashValue ^= key.charAt(i);
            hashValue *= fnvPrime;
        }
        return Math.abs(hashValue);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        else if (other == null) {
            return false;
        }
        else if (this.getClass() != other.getClass()) {
            return false;
        }
        nameEntry test = (nameEntry) other;
        return Objects.equals(this.key(), test.key());
    }
    public String toString() {
        return ( "[" + this.key + ", " + this.locations.toString() + "]" );
    }


}
