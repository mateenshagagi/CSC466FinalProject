import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Point> points = parseDataset("src/Mall_Customers.csv");

        for(Point p : points){
            p.remove(0);
        }

        System.out.println(points);

        DBScan dbScan = new DBScan(points, 10, 10);
        dbScan.findClusters();
        dbScan.saveClustersToCSV("src/customers_clusters.csv");
    }

    public static ArrayList<Point> parseDataset(String filename) {
        ArrayList<Point> points = new ArrayList<>();
        
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                Point point = new Point();
                for (String value : values) {
                    try{
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