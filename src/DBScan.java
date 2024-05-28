import java.io.*;
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
            if (!isVisited(point)) {
                explore(point, true);
            }
        }

        return clusters;
    }

    boolean isVisited(ArrayList<Double> point) {
        return visited.contains(point);
    }

    void explore(ArrayList<Double> point, boolean addCluster) {
        visited.add(point);
        ArrayList<ArrayList<Double>> neighbors = findNeighbors(point);
        if (isCorePoint(point)) {

            if (addCluster) {
                ArrayList<ArrayList<Double>> cluster = new ArrayList<>(neighbors);
                cluster.add(point);
                clusters.add(cluster);
            }

            for (ArrayList<Double> neighbor : neighbors) {
                if (isCorePoint(neighbor) && !isVisited(neighbor)) {
                    explore(neighbor, false);
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
                ArrayList<ArrayList<Double>> cluster = clusters.get(clusterIndex);

                for (ArrayList<Double> point : cluster) {
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
