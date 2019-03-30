public class Item {
    private int id;
    private int profit;
    private int weight;
    private City city;


    public Item(int id, int profit, int weight, City city) {
        this.id = id;
        this.profit = profit;
        this.weight = weight;
        this.city = city;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public City getCity()
    {
        return city;
    }


    public String toString()
    {
        return id + "\t" + profit + "\t" + weight + "\t" + city.getId();

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Item && ((Item) obj).id == id;
    }
}
