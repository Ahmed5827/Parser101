package org.example;

import com.imsweb.x12.Element;
import com.imsweb.x12.Loop;
import com.imsweb.x12.Segment;
import com.imsweb.x12.reader.X12Reader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OpenSC {

    public static void main(String[] args) {
        try {
            File ediFile = new File("src/main/resources/X223A2.txt");

            X12Reader reader = new X12Reader(X12Reader.FileType.ANSI837_5010_X223, ediFile);

            List<String> errors = reader.getErrors();
            List<String> fatalErrors = reader.getFatalErrors();

            if (!errors.isEmpty()) {
                System.out.println("Errors:");
                errors.forEach(System.out::println);
            }

            if (!fatalErrors.isEmpty()) {
                System.out.println("Fatal Errors:");
                fatalErrors.forEach(System.out::println);
            }

            List<Loop> loops = reader.getLoops();
            System.out.println("== All Loops, Segments, and Elements ==");

            for (Loop loop : loops) {
                printLoopRecursive(loop, 0);  // start recursive print
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printLoopRecursive(Loop loop, int indent) {
        String indentStr = "  ".repeat(indent);
        System.out.println(indentStr + "Loop ID: " + loop.getId());

        for (Segment segment : loop.getSegments()) {
            System.out.println(indentStr + "  Segment: " + segment.getId());
            List<Element> elements = segment.getElements();
            for (int i = 0; i < elements.size(); i++) {
                Element el = elements.get(i);
                System.out.println(indentStr + "     " + el.getId()  +" : " + el.getValue());
            }
        }

        for (Loop child : loop.getLoops()) {
            printLoopRecursive(child, indent + 1);
        }
    }
}
