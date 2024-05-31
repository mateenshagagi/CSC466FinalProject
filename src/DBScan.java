import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class DBScan {
    final ArrayList<Point> points;
    final ArrayList<Point> normalizedPoints;
    final double epsilon;
    final int minPoints;
    HashSet<Point> visited;
    ArrayList<Cluster> clusters;
    HashSet<Point> normalizedNoisePoints;

    DBScan(ArrayList<Point> points, double epsilon, int minPoints) {
        this.points = points;
        this.normalizedPoints = new ArrayList<>();
        normalize();
        this.epsilon = epsilon;
        this.minPoints = minPoints;
        this.visited = new HashSet<>();
        this.clusters = new ArrayList<>();
        this.normalizedNoisePoints = new HashSet<>();
    }

    public void findClusters() {
        for (Point normalizedPoint : normalizedPoints) {
            if (isVisited(normalizedPoint)) {
                continue;
            }
            if (isCorePoint(normalizedPoint)) {
                Cluster cluster = new Cluster();
                explore(normalizedPoint, cluster);
                clusters.add(cluster);
            }
        }

        for (Point normalizedPoint : normalizedPoints) {
            boolean inCluster = false;
            for (Cluster cluster : clusters) {
                if (cluster.getCluster().contains(normalizedPoint)) {
                    inCluster = true;
                    break;
                }
            }
            if (!inCluster) {
                normalizedNoisePoints.add(normalizedPoint);
            }
        }
    }

    boolean isVisited(Point point) {
        return visited.contains(point);
    }

    void normalize() {
        for (Point point : points) {
            Point normalizedPoint = new Point();
            for (int featureIdx = 0; featureIdx < point.size(); featureIdx++) {
                int finalFeatureIdx = featureIdx;
                double mean = points.stream().mapToDouble(p -> p.get(finalFeatureIdx)).sum() / points.size();
                double standard_dev = 0;
                for (Point pt : points) {
                    standard_dev += Math.pow((pt.get(featureIdx) - mean), 2);
                }
                standard_dev /= points.size();
                standard_dev = Math.sqrt(standard_dev);
                normalizedPoint.add((point.get(featureIdx) - mean) / standard_dev);
            }

            normalizedPoints.add(normalizedPoint);
        }
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
        for (Point potentialNeighbor : normalizedPoints) {
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

                for (Point normalizedPoint : cluster.getCluster()) {
                    int pointIndex = normalizedPoints.indexOf(normalizedPoint);
                    Point point = points.get(pointIndex);
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

    void saveToCSV(String filename) {
        saveClustersToCSV(filename);
        String baseName = filename.substring(0, filename.lastIndexOf('.'));
        String noiseFilename = baseName + "_noise.csv";
        saveNoisePointsToCSV(noiseFilename);
    }

    void saveNoisePointsToCSV(String filename) {
        if (normalizedNoisePoints.isEmpty()) {
            System.out.println("No noise points, not writing to file " + filename);
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
            bw.write(header.toString());
            bw.newLine();

            for (Point normalizedNoisePoint : normalizedNoisePoints) {
                StringBuilder row = new StringBuilder();
                int pointIndex = normalizedPoints.indexOf(normalizedNoisePoint);
                Point point = points.get(pointIndex);
                for (int i = 0; i < point.size(); i++) {
                    row.append(point.get(i));
                    if (i < point.size() - 1) {
                        row.append(",");
                    }
                }
                row.append(",").append(clusters.size());
                bw.write(row.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double evaluate() {
        if (clusters.size() <= 1) {
            return 0;
        }

        double totalSilhouetteScore = 0;

        for (Cluster cluster : clusters) {
            for (Point point : cluster.getCluster()) {
                double sumDistToOtherPointsInCluster = 0;
                for (Point otherPoint : cluster.getCluster()) {
                    if (point.equals(otherPoint)) continue;

                    sumDistToOtherPointsInCluster += getL2Norm(point, otherPoint);
                }
                double avgDistToOtherPointsInCluster = sumDistToOtherPointsInCluster / cluster.getCluster().size();

                double minAvgDistToOtherClusters = Double.MAX_VALUE;
                for (Cluster otherCluster : clusters) {
                    if (otherCluster.equals(cluster)) continue;

                    double sumDistToOtherClusterPoints = 0;
                    for (Point otherClusterPoint : otherCluster.getCluster()) {
                        sumDistToOtherClusterPoints += getL2Norm(point, otherClusterPoint);
                    }
                    double avgDistToOtherClusterPoints = sumDistToOtherClusterPoints / otherCluster.getCluster().size();
                    if (avgDistToOtherClusterPoints < minAvgDistToOtherClusters) {
                        minAvgDistToOtherClusters = avgDistToOtherClusterPoints;
                    }
                }

                double silhouetteScore = (minAvgDistToOtherClusters - avgDistToOtherPointsInCluster) / Math.max(avgDistToOtherPointsInCluster, minAvgDistToOtherClusters);
                totalSilhouetteScore += silhouetteScore;
            }
        }

        int totalPoints = clusters.stream().mapToInt(c -> c.getCluster().size()).sum();
        int numNoisePoints = normalizedNoisePoints.size();

        return (totalSilhouetteScore / totalPoints) - 0 * ((double) numNoisePoints / points.size());
    }

    void printClustersInfo() {
        System.out.println("Number of clusters: " + clusters.size());

        int numPoints = 0;
        for (Cluster cluster : clusters) {
            numPoints += cluster.getCluster().size();
        }
        System.out.println("Total number of points: " + points.size());
        System.out.println("Total number of points in clusters: " + numPoints);

        System.out.println("Number of noise points: " + normalizedNoisePoints.size());
    }
}
