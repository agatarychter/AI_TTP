import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionsManagerClass {

    public Individual generateRandomTSP(List<City> cityList)
    {
        List<City> cities = new ArrayList<>(cityList);
        Collections.shuffle(cities);
        return new Individual(cities);
    }

    public List<Individual> generatePopulation(List<City> cityList, int popSize)
    {
        List<Individual> population = new ArrayList<>();
        for(int i =0;i<popSize ;i++)
            population.add(generateRandomTSP(cityList));
        return population;
    }


    /*
    zachłanny algorytm wyboru przedmiotów wybierający wiele przedmiotów w mieście
    */
    public List<Item> chooseObjects(List<Item> items, double knapsackCapacity, double currCapacity)
    {
        List<Item> chosenItems = new ArrayList<>();
        items.sort(new ItemComparator());
        for (Item item: items
             ) {
            if((currCapacity + item.getWeight()) <= knapsackCapacity) {
                chosenItems.add(item);
                currCapacity+=  item.getWeight();
            }
        }
        return chosenItems;
    }

    /*
    zachłanny algorytm wyboru przedmiotów wybierający jeden przedmiot w mieście
    */
    public List<Item> chooseObject(List<Item> items, double knapsackCapacity, double currCapacity)
    {
        List<Item> chosenItems = new ArrayList<>();
        items.sort(new ItemComparator());
        if(items.size()==0)
            return chosenItems;
        if((currCapacity + items.get(0).getWeight()) <= knapsackCapacity) {
            chosenItems.add(items.get(0));
        }
        return chosenItems;
    }
    
    public int g(List<Item> items)
    {
        int sum = 0;
        for (Item item: items
             ) {
            sum+= item.getProfit();
        }
        return sum;
    }

    public double G(Individual individual, double vmin, double vmax, double knapsackCapacity)
        {
        List<City> cities = individual.getArray();
        double currentCapacity = 0;
        double v = vmax;
        double time = 0;
        double g = 0;
        City lastCity = cities.get(0);
        List<Item> chosenItems = chooseObject(lastCity.getItems(),knapsackCapacity,currentCapacity);
        g+= g(chosenItems);
        for(int i=0;i< chosenItems.size() ;i++)
        {
            currentCapacity += chosenItems.get(i).getWeight();
            v = vmax - (currentCapacity*(vmax-vmin)/knapsackCapacity);
        }
        for(int i=1;i< cities.size(); i++)
        {
            time += cities.get(i).getDistance(lastCity)/v;
            chosenItems = chooseObject(cities.get(i).getItems(),knapsackCapacity,currentCapacity);
            g+= g(chosenItems);
            for(Item item: chosenItems)
            {
                currentCapacity += item.getWeight();
            }
            v = vmax - currentCapacity*(vmax-vmin)/knapsackCapacity;
            lastCity = cities.get(i);

        }
        lastCity = cities.get(cities.size()-1);
        time+= lastCity.getDistance(cities.get(0))/v;
        return g - time;
    }
}
