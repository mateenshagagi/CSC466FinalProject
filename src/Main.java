import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String filename = "src/Mall_Customers.csv";
        ArrayList<Point> points = parseDataset(filename, true);

        if(filename.equals("src/Mall_Customers.csv")) { //remove index attribute
            for (Point p : points) {
                p.remove(0);
            }
        }

        System.out.println(points);


        DBScan dbScan = new DBScan(points, 15, 3);
        dbScan.findClusters();
        dbScan.printClustersInfo();
        dbScan.saveClustersToCSV("src/customers_clusters.csv");

        double bestEvaluation = 99999999;
        DBScan bestDBScan = null;

        for (double epsilon = 0.1; epsilon < 1; epsilon += 0.1) {
            for (int minPoints = 3; minPoints < 20; minPoints += 1) {
                DBScan curdbScan = new DBScan(points, epsilon, minPoints);
                curdbScan.findClusters();
                double evaluation = curdbScan.evaluate();
                if (evaluation < bestEvaluation) {
                    bestEvaluation = evaluation;
                    bestDBScan = curdbScan;
                }
            }
        }



        if (bestDBScan != null) {
            System.out.println("Epsilon: " + bestDBScan.epsilon);
            System.out.println("Min points: " + bestDBScan.minPoints);
            bestDBScan.printClustersInfo();
            //bestDBScan.saveClustersToCSV("src/clusters.csv");
        }
    }

    public static ArrayList<Point> parseDataset(String filename, boolean header) {
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

        return points;
    }
}