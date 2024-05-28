import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ArrayList<ArrayList<Double>> points = new ArrayList<>();
        points.add(new ArrayList<>(Arrays.asList(5.0, 5.0)));
        points.add(new ArrayList<>(Arrays.asList(8.0, 5.0)));
        points.add(new ArrayList<>(Arrays.asList(10.0, 3.0)));
        points.add(new ArrayList<>(Arrays.asList(-20.0, -20.0)));
        points.add(new ArrayList<>(Arrays.asList(-21.0, -22.0)));
        points.add(new ArrayList<>(Arrays.asList(-23.0, -18.0)));


        DBScan dbScan = new DBScan(points, 7, 3);
        ArrayList<ArrayList<ArrayList<Double>>> clusters = dbScan.findClusters();
        for (ArrayList<ArrayList<Double>> cluster : clusters) {
            System.out.println(cluster);
        }
    }
}