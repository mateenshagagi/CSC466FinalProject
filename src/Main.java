import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Point> points = parseDataset("src/dataset.csv");

        DBScan dbScan = new DBScan(points, 0.3, 10);
        dbScan.findClusters();
        dbScan.saveClustersToCSV("src/clusters.csv");
    }

    public static ArrayList<Point> parseDataset(String filename) {
        ArrayList<Point> points = new ArrayList<>();
        
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                Point point = new Point();
                for (String value : values) {
                    point.add(Double.parseDouble(value.trim()));
                }
                points.add(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return points;
    }
}