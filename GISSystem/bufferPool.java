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

public class bufferPool {
    ArrayList<nameEntry> mru;
    String v1;
    public bufferPool() {
        mru = new ArrayList<nameEntry>(15);
    }
    public ArrayList<nameEntry> list() {
        return mru;
    }
    public void insert(nameEntry name) {
        if (mru.indexOf(name) == -1) {
            if (mru.size() < 15) {
                mru.add(0, name);
            }
            else {
                mru.remove(14);
                mru.add(0, name);
            }
        }
        else {
            mru.remove(mru.indexOf(name));
            mru.add(0, name);
        }
    }

    public nameEntry find(long offset) {
        for (int i = 0; i < mru.size(); i++) {
            for (int r = 0; r < mru.get(i).locations().size(); r++) {
                if (mru.get(i).locations().get(r) == offset) {
                    return mru.get(i);
                }
            }
        }
        return null;
    }

    public void printBufferPool(FileWriter fw) throws Exception {
        fw.write("MRU\n");
        for (int s = 0; s < mru.size(); s++) {
            for (int q = 0; q < mru.get(s).locations().size(); q++) {
                fw.write("  ");
                fw.write(Long.toString(mru.get(s).locations().get(q)));
                fw.write(": ");
                fw.write(mru.get(s).key());
                fw.write("\n");
            }
        }
        fw.write("LRU\n");
    }
}