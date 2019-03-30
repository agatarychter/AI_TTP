import java.util.ArrayList;
import java.util.List;

public class City {
    private int id;
    private double x;
    private double y;


    private List<Item> items;

    public City(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        items = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString()
    {
        return id + "\t" + x+ "\t" + y ;
    }

    public double getDistance(City city)
    {
        return Math.sqrt(Math.pow((city.x- this.x),2) + Math.pow((city.y - this.y),2));

    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item)
    {
       items.add(item);
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof City && ((City) obj).id == id;
    }
}
