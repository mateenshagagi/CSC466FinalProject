import java.util.ArrayList;

public class Cluster {

    private final ArrayList<Point> cluster;

    public Cluster(ArrayList<Point> cluster){
        this.cluster = cluster;
    }

    public Cluster() {
        this.cluster = new ArrayList<>();
    }

    public ArrayList<Point> getCluster() {
        return cluster;
    }

    public void add(Point p){
        this.cluster.add(p);
    }

    public Point get(int i){
        return this.cluster.get(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cluster: ");
        for (Point p : cluster) {
            sb.append(p.toString()).append(", ");
        }

        return sb.toString();
    }
}
