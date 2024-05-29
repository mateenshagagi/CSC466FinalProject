import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class DBScan {
    final ArrayList<Point> points;
    final double epsilon;
    final int minPoints;
    HashSet<Point> visited;
    ArrayList<Cluster> clusters;

    DBScan(ArrayList<Point> points, double epsilon, int minPoints) {
        this.points = points;
        this.epsilon = epsilon;
        this.minPoints = minPoints;
        this.visited = new HashSet<>();
        this.clusters = new ArrayList<>();
    }

    public ArrayList<Cluster> findClusters() {
        for (Point point : points) {
            if (!isVisited(point)) {
                explore(point, true);
            }
        }

        return clusters;
    }

    boolean isVisited(Point point) {
        return visited.contains(point);
    }

    void explore(Point point, boolean addCluster) {
        visited.add(point);
        ArrayList<Point> neighbors = findNeighbors(point);
        if (isCorePoint(point)) {

            if (addCluster) {
                Cluster cluster = new Cluster(neighbors);
                cluster.add(point);
                clusters.add(cluster);
            }

            for (Point neighbor : neighbors) {
                if (isCorePoint(neighbor) && !isVisited(neighbor)) {
                    explore(neighbor, false);
                } else {
                    visited.add(neighbor);
                }
            }
        }
    }

    boolean isCorePoint(Point point) {
        ArrayList<Point> neighbors = findNeighbors(point);
        return neighbors.size() >= minPoints - 1;
    }

    ArrayList<Point> findNeighbors(Point point) {
        ArrayList<Point> neighbors = new ArrayList<>();
        for (Point potentialNeighbor : points) {
            double dist = getL2Norm(point, potentialNeighbor);
            if (dist <= epsilon && !point.equals(potentialNeighbor)) {
                neighbors.add(potentialNeighbor);
            }
        }

        return neighbors;
    }

    double getL2Norm(Point point1, Point point2) {
        int length = point1.size();
        double L2Norm = 0;
        for (int i = 0; i < length; i++) {
            double val1 = point1.get(i);
            double val2 = point2.get(i);
            L2Norm += Math.pow((val1 - val2), 2);
        }

        return Math.sqrt(L2Norm);
    }

    void saveClustersToCSV(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            int numFeatures = clusters.get(0).get(0).size();

            StringBuilder header = new StringBuilder();
            for (int featureNum = 1; featureNum <= numFeatures; featureNum++) {
                header.append("feature").append(featureNum);
                if (featureNum < numFeatures) {
                    header.append(",");
                }
            }
            header.append(",cluster");
            bw.write(header.toString());
            bw.newLine();

            for (int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex++) {
                Cluster cluster = clusters.get(clusterIndex);

                for (Point point : cluster.getCluster()) {
                    StringBuilder row = new StringBuilder();
                    for (int i = 0; i < point.size(); i++) {
                        row.append(point.get(i));
                        if (i < point.size() - 1) {
                            row.append(",");
                        }
                    }
                    row.append(",").append(clusterIndex);
                    bw.write(row.toString());
                    bw.newLine();
                }
            }

            System.out.println("Wrote " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
