import java.util.ArrayList;

public class Cluster {

    private ArrayList<Point> cluster;

    public Cluster(ArrayList<Point> cluster){
        this.cluster = cluster;
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
}
