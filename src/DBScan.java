import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class DBScan {
    final ArrayList<Point> points;
    final double epsilon;
    final int minPoints;
    HashSet<Point> visited;
    ArrayList<Cluster> clusters;
    HashSet<Point> noisePoints;

    DBScan(ArrayList<Point> points, double epsilon, int minPoints) {
        this.points = points;
        this.epsilon = epsilon;
        this.minPoints = minPoints;
        this.visited = new HashSet<>();
        this.clusters = new ArrayList<>();
        this.noisePoints = new HashSet<>();
    }

    public void findClusters() {
        for (Point point : points) {
            if (isVisited(point)) {
                continue;
            }
            if (isCorePoint(point)) {
                Cluster cluster = new Cluster();
                explore(point, cluster);
                clusters.add(cluster);
            }
        }

        for (Point point : points) {
            boolean inCluster = false;
            for (Cluster cluster : clusters) {
                if (cluster.getCluster().contains(point)) {
                    inCluster = true;
                    break;
                }
            }
            if (!inCluster) {
                noisePoints.add(point);
            }
        }
    }

    boolean isVisited(Point point) {
        return visited.contains(point);
    }

    void explore(Point point, Cluster cluster) {
        visited.add(point);

        cluster.add(point);

        ArrayList<Point> neighbors = findNeighbors(point);
        for (Point neighbor : neighbors) {
            if (isVisited(neighbor)) {
                continue;
            }

            if (isCorePoint(neighbor)) {
                explore(neighbor, cluster);
            } else if (!cluster.getCluster().contains(neighbor)) {
                visited.add(neighbor);
                cluster.add(neighbor);
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
        if (clusters.isEmpty()) {
            System.out.println("Clusters is empty, not writing to file " + filename);
            return;
        }

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

            for (Point noisePoint : noisePoints) {
                StringBuilder row = new StringBuilder();
                for (int i = 0; i < noisePoint.size(); i++) {
                    row.append(noisePoint.get(i));
                    if (i < noisePoint.size() - 1) {
                        row.append(",");
                    }
                }
                row.append(",").append(clusters.size());
                bw.write(row.toString());
                bw.newLine();
            }

            System.out.println("Wrote " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double evaluate() {
        double evaluation = 0;

        for (Cluster cluster : clusters) {
            int numFeatures = cluster.get(0).size();

            for (int i = 0; i < numFeatures; i++) {
                ArrayList<Double> feature = new ArrayList<>();
                for (Point point : cluster.getCluster()) {
                    feature.add(point.get(i));
                }

                double featureSum = feature.stream().mapToDouble(f -> f).sum();
                feature.replaceAll(f -> f / featureSum);

                Collections.sort(feature);

                double maxDistance = 0;
                for (int j = 0; j < feature.size() - 1; j++) {
                    int next = j + 1;
                    double difference = Math.abs(feature.get(next) - feature.get(j));
                    if (difference > maxDistance) {
                        maxDistance = difference;
                    }
                }

                evaluation += maxDistance / cluster.getCluster().size();
            }
        }

        return evaluation;
    }

    void printClustersInfo() {
        System.out.println("Number of clusters: " + clusters.size());

        int numPoints = 0;
        for (Cluster cluster : clusters) {
            numPoints += cluster.getCluster().size();
        }
        System.out.println("Total number of points: " + points.size());
        System.out.println("Total number of points in clusters: " + numPoints);

        System.out.println("Number of noise points: " + noisePoints.size());
    }
}
