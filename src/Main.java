import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        getBestEvaluation();
    }

    public static void getBestEvaluation() {
        String filename = "src/datasets/moons.csv";
        boolean header = false;
        ArrayList<Point> points = parseDataset(filename, header, false);

        double bestEvaluation = 0;
        DBScan bestDBScan = null;

        for (double epsilon = 0.01; epsilon <= 1.0; epsilon += 0.01) {
            for (int minPoints = 3; minPoints <= 10; minPoints += 1) {
                DBScan dbScan = new DBScan(points, epsilon, minPoints);
                dbScan.findClusters();
                double evaluation = dbScan.evaluate();
                if (evaluation > bestEvaluation) {
                    bestEvaluation = evaluation;
                    bestDBScan = dbScan;
                }
            }
        }

        if (bestDBScan != null) {
            System.out.println("Epsilon: " + bestDBScan.epsilon);
            System.out.println("Min points: " + bestDBScan.minPoints);
            System.out.println("Evaluation: " + bestEvaluation);
            bestDBScan.printClustersInfo();

            String correctClustersFilename = "src/datasets_correct/moons.csv";
            ArrayList<Cluster> correctClusters = parseCorrectDataset(correctClustersFilename, false);
            bestDBScan.printEntropyAndPurity(correctClusters);

            bestDBScan.saveToCSV("moons.csv");
        }
    }

    public static ArrayList<String> getHeader(String filename) {
        ArrayList<String> header = new ArrayList<>();

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            String[] headerList = line.split(",");
            header.addAll(Arrays.asList(headerList));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return header;
    }

    public static ArrayList<Point> parseDataset(String filename, boolean header, boolean index) {
        ArrayList<Point> points = new ArrayList<>();

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            if (header) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                Point point = new Point();
                for (String value : values) {
                    try {
                        point.add(Double.parseDouble(value.trim()));
                    } catch (NumberFormatException e){
                        continue;
                    }
                }
                points.add(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (index) {
            for (Point p : points) {
                p.remove(0);
            }
        }

        return points;
    }

    /* Assumes clusters is the last column */
    public static ArrayList<Cluster> parseCorrectDataset(String filename, boolean header) {
        HashMap<Integer, Cluster> clustersMap = new HashMap<>();

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            if (header) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                Point point = new Point();

                int clusterNum = 0;
                for (int i = 0; i < values.length; i++) {
                    String stringValue = values[i];
                    if (i == values.length - 1) {
                        clusterNum = Integer.parseInt(stringValue.trim());
                    } else {
                        point.add(Double.parseDouble(stringValue.trim()));
                    }
                }

                if (!clustersMap.containsKey(clusterNum)) {
                    clustersMap.put(clusterNum, new Cluster());
                }

                clustersMap.get(clusterNum).add(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(clustersMap.values());
    }
}