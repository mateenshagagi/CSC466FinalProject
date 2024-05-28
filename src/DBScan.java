import java.util.ArrayList;
import java.util.HashSet;

public class DBScan {
    final ArrayList<ArrayList<Double>> points;
    final double epsilon;
    final int minPoints;
    HashSet<ArrayList<Double>> visited;
    ArrayList<ArrayList<ArrayList<Double>>> clusters;

    DBScan(ArrayList<ArrayList<Double>> points, double epsilon, int minPoints) {
        this.points = points;
        this.epsilon = epsilon;
        this.minPoints = minPoints;
        this.visited = new HashSet<>();
        this.clusters = new ArrayList<>();
    }

    public ArrayList<ArrayList<ArrayList<Double>>> findClusters() {
        for (ArrayList<Double> point : points) {
            if (!visited.contains(point)) {
                explore(point);
            }
        }

        return clusters;
    }

    void explore(ArrayList<Double> point) {
        visited.add(point);
        ArrayList<ArrayList<Double>> neighbors = findNeighbors(point);
        if (isCorePoint(point)) {
            ArrayList<ArrayList<Double>> cluster = new ArrayList<>(neighbors);
            cluster.add(point);
            clusters.add(cluster);

            for (ArrayList<Double> neighbor : neighbors) {
                if (isCorePoint(neighbor) && !visited.contains(neighbor)) {
                    explore(neighbor);
                } else {
                    visited.add(neighbor);
                }
            }
        }
    }

    boolean isCorePoint(ArrayList<Double> point) {
        ArrayList<ArrayList<Double>> neighbors = findNeighbors(point);
        return neighbors.size() >= minPoints - 1;
    }

    ArrayList<ArrayList<Double>> findNeighbors(ArrayList<Double> point) {
        ArrayList<ArrayList<Double>> neighbors = new ArrayList<>();
        for (ArrayList<Double> potentialNeighbor : points) {
            double dist = getL2Norm(point, potentialNeighbor);
            if (dist <= epsilon && !point.equals(potentialNeighbor)) {
                neighbors.add(potentialNeighbor);
            }
        }

        return neighbors;
    }

    double getL2Norm(ArrayList<Double> point1, ArrayList<Double> point2) {
        int length = point1.size();
        double L2Norm = 0;
        for (int i = 0; i < length; i++) {
            double val1 = point1.get(i);
            double val2 = point2.get(i);
            L2Norm += Math.pow((val1 - val2), 2);
        }

        return Math.sqrt(L2Norm);
    }
}
