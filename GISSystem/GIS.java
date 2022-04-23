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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class GIS {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        RandomAccessFile rac = new RandomAccessFile(args[0], "rw");
        File script = new File(args[1]);
        FileWriter output = new FileWriter(args[2]);
        Scanner scan = new Scanner(script);
        bufferPool bp = new bufferPool();
        prQuadTree pr = null;
        hashTable ht = null;
        int xMin = 0;
        int xMax = 0;
        int yMin = 0;
        int yMax = 0;
        int iter = 0;
        long offset = 0;
        while (scan.hasNextLine()) {
            String val = scan.next();
            if (val.equals("world")) {
                String xMins = scan.next();
                String xMaxs = scan.next();
                String yMins = scan.next();
                String yMaxs = scan.next();
                xMin = DMS_seconds(xMins);
                xMax = DMS_seconds(xMaxs);
                yMin = DMS_seconds(yMins);
                yMax = DMS_seconds(yMaxs);
                pr = new prQuadTree(xMin, xMax, yMin, yMax);
                output.write("world ");
                output.write(xMins);
                output.write(" ");
                output.write(xMaxs);
                output.write(" ");
                output.write(yMins);
                output.write(" ");
                output.write(yMaxs);
                output.write("\n");
                output.write("\n");
                output.write("GIS Program");
                output.write("\n");
                output.write("\n");
                output.write("dbFile:   ");
                output.write(args[0]);
                output.write("\n");
                output.write("script:   ");
                output.write(args[1]);
                output.write("\n");
                output.write("log:   ");
                output.write(args[2]);
                output.write("\n");
                output.write("Start time:    ");
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd HH:mm:ss 'EDT' yyyy");
                String formattedDate = myDateObj.format(myFormatObj);
                output.write(formattedDate);
                output.write("\n");
                output.write("QuadTree children are printed in the order SW SE NE NW\n");
                output.write("--------------------------------------------------------------------------------\n\n");
                output.write("Latitude/longitude values in index entries are shown as signed integers, in total seconds.\n\n");
                output.write("World boundaries are set to:\n");
                output.write("          ");
                output.write(Integer.toString(yMax));
                output.write("\n");
                output.write("  ");
                output.write(Integer.toString(xMin));
                output.write("          ");
                output.write(Integer.toString(xMax));
                output.write("\n");
                output.write("          ");
                output.write(Integer.toString(yMin));
                output.write("\n");
                output.write("--------------------------------------------------------------------------------\n");
                iter++;
            }
            if (val.equals("import")) {
                String Grecord = scan.next();
                File records = new File(Grecord);
                Scanner scanner = new Scanner(records).useDelimiter("\\|");
                String line = scanner.nextLine();
                if (rac.getFilePointer() == 0) {
                    rac.writeChars(line);
                    rac.writeChars("\n");
                }
                int hashval = 0;
                int prval = 0;
                int len = 0;
                int total = 0;
                
                output.write("Command ");
                output.write(Integer.toString(iter));
                iter++;
                output.write(": import  ");
                output.write(Grecord);
                output.write("\n\n");
                ht = new hashTable(null, null);
                printTree cos = new printTree();
                Scanner sc = null;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    offset = rac.getFilePointer();
                    sc = new Scanner(line).useDelimiter("\\|");
                    sc.next();
                    String name = sc.next();
                    sc.next();
                    String state = sc.next();
                    for (int i = 0; i < 3; i++) {
                        sc.next();
                    }
                    String comb = name + ":" + state;
                    if (!ht.insert(new nameEntry(comb, offset/2))) {
                        nameEntry here = (nameEntry) ht.find(new nameEntry(comb, offset/2));
                        here.addLocation(offset/2);
                    }
                    String wordy = sc.next();
                    String wordx = sc.next();
                    if (!(wordy.equals("Unknown") || wordx.equals("Unknown"))) {
                        int xval = DMS_seconds(wordx);
                        int yval = DMS_seconds(wordy);
                        Point value = new Point(xval, yval, offset/2);
                        if (!pr.insert(value)) {
                            pr.find(value).getLocations().add(offset/2);
                        }
                        
                        hashval++;
                    }
                    rac.writeChars(line);
                    rac.writeChars("\n");
                    len += name.length();
                    total++;
                    
                }
                output.write("Imported Features by name:    ");
                output.write(Integer.toString(total));
                output.write("\n");
                output.write("Imported Locations:   ");
                output.write(Integer.toString(hashval));
                output.write("\n");
                output.write("Average name length:  ");
                output.write(Integer.toString(len/total));
                output.write("\n");
                output.write("--------------------------------------------------------------------------------\n");
                sc.close();
            }
            if (val.equals("show")) {
                String type = scan.next();
                if (type.equals("quad")) {
                    output.write("Command ");
                    output.write(Integer.toString(iter));
                    iter++;
                    output.write(": show quad\n\n");
                    printTree prt = new printTree();
                    prt.printTree(output, pr);
                    output.write("--------------------------------------------------------------------------------\n");
                }
                if (type.equals("hash")) {
                    output.write("Command ");
                    output.write(Integer.toString(iter));
                    iter++;
                    output.write(": show hash\n\n");
                    ht.display(output);
                    output.write("--------------------------------------------------------------------------------\n");
                }
                if (type.equals("pool")) {
                    output.write("Command ");
                    output.write(Integer.toString(iter));
                    output.write(": show pool\n\n");
                    iter++;
                    bp.printBufferPool(output);
                    output.write("--------------------------------------------------------------------------------\n");
                }
            }
            if (val.equals("what_is")) {
                output.write("Command ");
                output.write(Integer.toString(iter));
                output.write(": what_is ");
                String find = scan.nextLine();
                output.write(find);
                output.write("\n\n");
                String[] list = find.split(" ");
                Scanner sc1 = new Scanner(find);
                String temp = "";
                for (int r = 0; r < list.length; r++) {
                    temp += sc1.next();
                    if (r < list.length - 1) {
                        temp += " ";
                    }
                }
                temp += ":" + sc1.next();
                nameEntry temp1 = new nameEntry(temp, (long) 0);
                nameEntry temp2 = (nameEntry) ht.find(temp1);
                if (temp2 != null) {
                    for(int s = 0; s < temp2.locations().size(); s++) {
                        nameEntry tmp = bp.find(temp2.locations().get(s));
                        Scanner sc3 = null;
                        if (tmp != null) {
                            sc3 = new Scanner(tmp.key()).useDelimiter("\\|");
                            bp.insert(tmp);
                        }
                        else {
                            rac.seek(temp2.locations().get(s)*2);
                            String vals = reduce(rac.readLine());
                            bp.insert(new nameEntry(vals, temp2.locations().get(s)));
                            sc3 = new Scanner(vals).useDelimiter("\\|");
                        }
                        output.write("      ");
                        output.write(Long.toString(temp2.locations().get(s)));
                        output.write(": ");

                        sc3.next();
                        sc3.next();
                        sc3.next();
                        sc3.next();
                        sc3.next();
                        output.write(sc3.next());
                        sc3.next();
                        String longitude = sc3.next();
                        output.write("  (");
                        String latitude = sc3.next();
                        output.write(DMS(latitude));
                        output.write(", ");
                        output.write(DMS(longitude));
                        output.write(")\n");
                        sc3.close();
                    }
                    
                }
                else {
                    sc1.close();
                    sc1 = new Scanner(find);
                    output.write("  No records match ");
                    String tempn1 = "";
                    for (int r = 0; r < list.length; r++) {
                        tempn1 += sc1.next();
                        if (r < list.length) {
                            tempn1 += " ";
                        }
                    }
                    tempn1 += "and " + sc1.next();
                    output.write(tempn1);
                    output.write("\n");
                }
                output.write("--------------------------------------------------------------------------------\n");
                iter++;
            }
            if (val.equals("what_is_at")) {
                String latitude = scan.next();
                String longitude = scan.next();
                int lat = DMS_seconds(latitude);
                int lon = DMS_seconds(longitude);
                Point fin = new Point(lon, lat, 0);
                output.write("Command ");
                output.write(Integer.toString(iter));
                output.write(": what_is_at  ");
                output.write(latitude);
                output.write("  ");
                output.write(longitude);
                int s1;
                if (pr.find(fin) == null) {
                    s1 = 0;
                }
                else {
                    s1 = pr.find(fin).getLocations().size();
                }
                if (s1 > 0) {
                    output.write("\n\n  The following features were found at (");
                    output.write(DMS(longitude));
                    output.write(", ");
                    output.write(DMS(latitude));
                    output.write("):\n");
                    for (int p = 0; p < pr.find(fin).getLocations().size(); p++) {
                        nameEntry tmp2 = bp.find((Long) pr.find(fin).getLocations().get(p));
                        Scanner sc5;
                        if (tmp2 != null) {
                            sc5 = new Scanner(tmp2.key()).useDelimiter("\\|");
                        }
                        else {
                            rac.seek((Long) pr.find(fin).getLocations().get(p)*2);
                            String vals2 = reduce(rac.readLine());
                            bp.insert(new nameEntry(vals2, (Long) pr.find(fin).getLocations().get(p)));
                            sc5 = new Scanner(vals2).useDelimiter("\\|");
                        }
                    
                        output.write("  ");
                        output.write(Long.toString((Long) pr.find(fin).getLocations().get(p)));
                        output.write(": ");
                        sc5.next();
                        output.write(sc5.next());
                        output.write("  ");
                        sc5.next();
                        String State = sc5.next();
                        sc5.next();
                        output.write(sc5.next());
                        output.write("  ");
                        output.write(State);
                        output.write("\n");
                    }
                }
                else {
                    output.write("\n\n Nothing was found at (");
                    output.write(DMS(longitude));
                    output.write(", ");
                    output.write(DMS(latitude));
                    output.write(")\n");
                }
                
                
                output.write("--------------------------------------------------------------------------------\n");
                iter++;
            }
            if (val.equals("what_is_in")) {
                String longitude = scan.next();
                String latitude = scan.next();
                int val1 = Integer.parseInt(scan.next());
                int val2 = Integer.parseInt(scan.next());
                ArrayList<Point> ap = pr.find(DMS_seconds(latitude) - val2, DMS_seconds(latitude) + val2, DMS_seconds(longitude) - val1, DMS_seconds(longitude) + val1);
                output.write("Command ");
                output.write(Integer.toString(iter));
                output.write(":  what_is_in  ");
                output.write(longitude);
                output.write("  ");
                output.write(latitude);
                output.write("  ");
                output.write(Integer.toString(val1));
                output.write("  ");
                output.write(Integer.toString(val2));
                output.write("\n\n  ");
                if (ap.size() > 0) {
                    output.write("The following ");
                    int nums = 0;
                    for (int g = 0; g < ap.size(); g++) {
                        for (int h = 0; h < ap.get(g).getLocations().size(); h++) {
                            nums++;
                        }
                    }
                    output.write(Integer.toString(nums));
                    output.write(" features were found in (");
                }
                else {
                    output.write("Nothing was found in (");
                }
                
                output.write(DMS(latitude));
                output.write(" +/- ");
                output.write(Integer.toString(val2));
                output.write(", ");
                output.write(DMS(longitude));
                output.write(" +/- ");
                output.write(Integer.toString(val1));
                output.write(")\n");
                for (int l = 0; l < ap.size(); l++) {
                    for (int y = 0; y < ap.get(l).getLocations().size(); y++) {
                        output.write("  ");
                        output.write(Long.toString(ap.get(l).getLocations().get(y)));
                        nameEntry tmp3 = bp.find(ap.get(l).getLocations().get(y));
                        Scanner sc8;
                        if (tmp3 != null) {
                            sc8 = new Scanner(tmp3.key()).useDelimiter("\\|");
                        }
                        else {
                            rac.seek(ap.get(l).getLocations().get(y)*2);
                            String vals3 = reduce(rac.readLine());
                            bp.insert(new nameEntry(vals3, (Long) ap.get(l).getLocations().get(y)));
                            sc8 = new Scanner(vals3).useDelimiter("\\|");
                        }
                        sc8.next();
                        output.write(": ");
                        output.write(sc8.next());
                        output.write("   ");
                        sc8.next();
                        output.write(sc8.next());
                        output.write("  (");
                        sc8.next();
                        sc8.next();
                        sc8.next();
                        String lat = sc8.next();
                        output.write(DMS(sc8.next()));
                        output.write(", ");
                        output.write(DMS(lat));
                        output.write(")\n");
                        sc8.close();
                    }
                    
                }
                output.write("--------------------------------------------------------------------------------\n");
                iter++;
            }
            if (val.equals("quit")) {
                rac.close();
                output.write("Command ");
                output.write(Integer.toString(iter));
                output.write(": quit\n\n");
                output.write("Terminating execution of commands.\n");
                output.write("End time: ");
                LocalDateTime myDateObj2 = LocalDateTime.now();
                DateTimeFormatter myFormatObj2 = DateTimeFormatter.ofPattern("E, MMM dd HH:mm:ss 'EDT' yyyy");
                String formattedDate2 = myDateObj2.format(myFormatObj2);
                output.write(formattedDate2);
                output.write("\n--------------------------------------------------------------------------------\n");
                scan.close();
                output.close();
                break;
            }
            if (val.equals(";")) {
                output.write("; ");
                output.write(scan.nextLine());
                output.write("\n");
            }
            if (val.equals(";show")) {
                output.write(";show ");
                output.write(scan.nextLine());
                output.write("\n");
            }
        }
    }

    public static String DMS(String val){
        String value = "";
        int days;
        int min;
        int sec;
        String direc;
        
        if (val.length() == 7){
            days = Integer.parseInt(val.substring(0, 2));
            min = Integer.parseInt(val.substring(2, 4));
            sec = Integer.parseInt(val.substring(4, 6));
            if (val.charAt(6) == 'N') {
                direc = "North";
            }
            else {
                direc = "South";
            }
        }
        else {
            days = Integer.parseInt(val.substring(0, 3));
            min = Integer.valueOf(val.substring(3, 5));
            sec = Integer.parseInt(val.substring(5, 7));
            if (val.charAt(7) == 'W') {
                direc = "West";
            }
            else {
                direc = "East";
            }
        }
        String result = days + "d " + min + "m " + sec + "s " + direc;
        return result;
    }

    public static int DMS_seconds(String val) {
        int degree;
        int minute;
        int second;
        if (val.length() == 8) {
            degree = Integer.parseInt(val.substring(0, 3));
            minute = Integer.parseInt(val.substring(3, 5));
            second = Integer.parseInt(val.substring(5, 7));
        }
        else {
            degree = Integer.parseInt(val.substring(0, 2));
            minute = Integer.parseInt(val.substring(2, 4));
            second = Integer.parseInt(val.substring(4, 6));
        }
        int fsecond = degree*3600 + minute*60 + second;
        if ((val.charAt(val.length() - 1) == 'W') || (val.charAt(val.length() - 1) == 'S')) {
            fsecond = -fsecond;
        }
        return fsecond;
    }

    public static String reduce(String test) {
        String gom = "";
        for (int b = 0; b < test.length(); b++) {
            if (b%2 != 0) {
                gom += test.charAt(b);
            }
        }
        return gom;
    }
}