import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String filename = "src/Mall_Customers.csv";
        ArrayList<Point> points = parseDataset(filename, true, true);

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
            bestDBScan.saveToCSV("src/customers_clusters.csv");
        }
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
}