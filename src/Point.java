import java.util.ArrayList;
import java.util.Objects;

public class Point {

    private ArrayList<Double> data;

    public Point(){
        this.data = new ArrayList<>();
    }

    public Point(ArrayList<Double> data){
        this.data = data;
    }

    public void add(double d){
        this.data.add(d);
    }

    public ArrayList<Double> getData() {
        return data;
    }

    public int size(){
        return this.data.size();
    }

    public double get(int i){
        return this.data.get(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(data, point.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
