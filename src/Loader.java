import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private List<City> cities;
    private List<Item> items;
    private int dimension;
    private int numberOfItems;
    private int capacityOfKnapsack;
    private double minSpeed;
    private double maxSpeed;
    private double rentingRatio;

    public Loader()
    {
        cities = new ArrayList<>();
        items = new ArrayList<>();
    }

    public List<City> getCities()
    {
        return cities;
    }

    public List<Item> getItems()
    {
        return items;
    }

    public void load(String filepath)
    {
        File file = new File(filepath);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int lineNumber = 0;
            String line = "";
            while((line=bufferedReader.readLine())!=null && !line.startsWith("EDGE"))
            {
                if(lineNumber>1)
                {
                    if(line.startsWith("DIMENSION"))
                    {
                        dimension = getIntFromString(line);
                    }
                    else if(line.startsWith("NUMBER"))
                    {
                        numberOfItems =  getIntFromString(line);
                    }
                    else if(line.startsWith("CAPACITY"))
                    {
                        capacityOfKnapsack = getIntFromString(line);
                    }
                    else if(line.startsWith("MIN"))
                    {
                        minSpeed = getDoubleFromString(line);
                    }
                    else if(line.startsWith("MAX"))
                    {
                        maxSpeed = getDoubleFromString(line);
                    }
                    else if(line.startsWith("RENTING"))
                    {
                        rentingRatio = getDoubleFromString(line);
                    }
                }
                lineNumber++;
            }
            lineNumber = 0;
            while((line=bufferedReader.readLine())!=null && !line.startsWith("ITEMS"))
            {
                if(lineNumber>=1)
                {
                    City city = createCity(line);
                    cities.add(city);
                }
                lineNumber++;
            }

            while((line=bufferedReader.readLine())!=null )
            {
                Item item = createItem(line);
                items.add(item);
            }



        } catch(IOException e)
        {
            e.printStackTrace();
        }


    }

    private int getIntFromString(String line)
    {
        String[] strings =line.split("\t");
        return Integer.valueOf(strings[strings.length-1]);
    }

    private double getDoubleFromString(String line)
    {
        String[] strings = line.split("\t");
        return Double.valueOf(strings[strings.length-1]);
    }

    private City createCity(String line)
    {
        String []elems = line.split("\t");
        int id = Integer.valueOf(elems[0]);
        double x = Double.valueOf(elems[1]);
        double y = Double.valueOf(elems[2]);
        return new City(id,x,y);
    }

    private Item createItem(String line)
    {
        String []elems = line.split("\t");
        int id = Integer.valueOf(elems[0]);
        int profit = Integer.valueOf(elems[1]);;
        int weight = Integer.valueOf(elems[2]);
        int cityId = Integer.valueOf(elems[3]);
        City city = getCityById(cityId);
        Item item = new Item(id,profit,weight, city);
        city.addItem(item);
        return item;
    }

    private City getCityById(int id)
    {
        for (City city:
                cities
             ) {
            if(city.getId()==id)
                return city;

        }
        return null;
    }


    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public int getCapacityOfKnapsack() {
        return capacityOfKnapsack;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getRentingRatio() {
        return rentingRatio;
    }
}
