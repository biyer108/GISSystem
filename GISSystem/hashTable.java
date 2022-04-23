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

import java.io.*;
import java.util.*;
public class hashTable<T extends Hashable<T>> {
    private ArrayList<LinkedList<T>> table;
    private Integer numElements = 0;
    private Double loadLimit = 0.7;
    private Integer defaultTableSize = 256;
    private Integer maxSlotLength = 0;

    public hashTable(Integer size, Double ldLimit) {
        if (size == null) {
            table = new ArrayList<LinkedList<T>>(defaultTableSize);
        }
        else {
            table = new ArrayList<LinkedList<T>>(size);
            defaultTableSize = size;
        }
        if (ldLimit != null) {
            loadLimit = ldLimit;
        }
        for (int i = 0; i < defaultTableSize; i++) {
            table.add(new LinkedList<T>());
        }
    }

    public boolean insert (T elem) {
        int index = elem.Hash()%defaultTableSize;
        int val = table.get(index).indexOf(elem);
        if (val == -1) {
            table.get(index).add(elem);
            numElements++;
            double ldlim = (double) numElements/defaultTableSize;
            if (ldlim > loadLimit) {
//                maxSlotLength = 0;
                defaultTableSize = defaultTableSize*2;
                ArrayList<LinkedList<T>> ntable = new ArrayList<LinkedList<T>>(defaultTableSize);
                for (int i = 0; i < defaultTableSize; i++) {
                    ntable.add(new LinkedList<T>());
                }
                for (int idx = 0; idx < table.size(); idx++) {
                    LinkedList<T> curr = table.get(idx);
                    for (int r = 0; r < curr.size(); r++) {
                        T here = curr.get(r);
                        int ind = here.Hash()%defaultTableSize;
                        ntable.get(ind).add(here);
                    }
                    
                }
                table = ntable;
            }
            return true;
        }
        else {
            return false;
        }
    }

    public T find(T elem) {
        int index = elem.Hash()%defaultTableSize;
        int val = table.get(index).indexOf(elem);
        if (val != -1) {
            return table.get(index).get(val);
        }
        return null;
    }

    public T remove(T elem) {
        int index = elem.Hash()%defaultTableSize;
        int val = table.get(index).indexOf(elem);
        if (val != -1) {
            T here = table.get(index).remove(val);
            return here;
        }
        return null;
    }

    public void display(FileWriter fw) throws IOException {
        for (int j = 0; j < table.size(); j++) {
            if (maxSlotLength < table.get(j).size()) {
                maxSlotLength = table.get(j).size();
            }
        }
        fw.write("Number of elements: " + numElements + "\n");
        fw.write("Number of slots: " + table.size() + "\n");
        fw.write("Maximum elements in a slot: " + maxSlotLength + "\n");
        fw.write("Load limit: " + loadLimit + "\n");
        fw.write("\n");

        fw.write("Slot Contents\n");
        for (int idx = 0; idx < table.size(); idx++) {

            LinkedList<T> curr = table.get(idx);

            if ( curr != null && !curr.isEmpty() ) {

                fw.write(String.format("%5d: %s\n", idx, curr.toString()));
            }
        }
    }
    
}