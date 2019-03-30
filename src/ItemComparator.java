import java.util.Comparator;

public class ItemComparator implements Comparator<Item> {

    @Override
    public int compare(Item o1, Item o2) {
        double profit1 = o1.getProfit();
        double profit2 = o2.getProfit();
        double value1 = profit1 / o1.getWeight();
        double value2 = profit2 / o2.getWeight();
        if (value1 < value2)
            return 1;
        else if (value1 == value2)
            return 0;
        else
            return -1;
    }
}
