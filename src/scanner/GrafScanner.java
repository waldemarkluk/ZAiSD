package scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import graph.Graf;
import graph.GrafE;
import graph.GrafV;

public class GrafScanner {


    /* Private methods */

    private static void proceedNextLine(Graf graph, String line) {
        line = line.replaceAll("\\s+", "");
        String lines[] = line.split(";");
        GrafV firstNode = new GrafV(Integer.parseInt(lines[0]));
        GrafV secondNode = new GrafV(Integer.parseInt(lines[1]));
        graph.addV(firstNode);
        graph.addV(secondNode);
        graph.addE(new GrafE(firstNode, secondNode, Integer.parseInt(lines[2])));
    }


    /* Public methods */

    public static Graf importGraphFromConsole(Graf graph) {
        System.out.println("Write new edges in lines in format:    firstNodeId; secondNodeId; edgeWeight");
        System.out.println("To end creating graph put empty line.");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        while (line != null && !line.replaceAll("\\s+", "").equals("")) {
            proceedNextLine(graph, line);
            line = scanner.nextLine();
        }
        scanner.close();
        return graph;
    }

    public static Graf importGraphFromString(Graf graph, String graphString) {
        String lines[] = graphString.split("\\r?\\n");
        for (String line : lines)
            proceedNextLine(graph, line);
        return graph;
    }

    public static Graf importGraphFromFile(Graf graph, String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNextLine())
            proceedNextLine(graph, scanner.nextLine());
        scanner.close();
        return graph;
    }
}
