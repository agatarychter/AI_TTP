import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainClass {
    private List<City> cities;
    private List<Item> items;
    private int capacityOfKnapsack;
    private double minSpeed;
    private double maxSpeed;
    private int popSize;
    private List<Individual> initialPopulation;
    private List<Individual> parentsPopulation;
    private double mutationProbability;
    private double crossProbability;
    private static final int LOTTERY_NUMBER = 100;
    private static final int TOUR = 5;
    private static final int GENERATION_SIZE = 100;
    private static final String CSV_FILE_PATH = "Results_1.csv";
    private Random random;
    private int citiesNumber;

    private FunctionsManagerClass functionsManagerClass;
    public MainClass(List<City> cities, List<Item> items, int capacityOfKnapsack, double minSpeed, double maxSpeed, int popSize, double mutationProbability, double crossProbability)
    {
        this.cities = cities;
        this.items = items;
        this.capacityOfKnapsack = capacityOfKnapsack;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.popSize = popSize;
        functionsManagerClass = new FunctionsManagerClass();
        this.mutationProbability = mutationProbability;
        this.crossProbability = crossProbability;
        random = new Random();
        citiesNumber = cities.size();
        writeHeader();
    }
    public List<City> getCities() {
        return cities;
    }
    public List<Item> getItems() {
        return items;
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
    public int getPopSize() {
        return popSize;
    }

    private void generatePopulation(List<City> cityList)
    {
        initialPopulation = functionsManagerClass.generatePopulation(cityList, popSize);

    }

    private void evaluatePopulation(List<Individual>population)
    {
        for (Individual individual : population
             ) {
            individual.setMeasure(functionsManagerClass.G(individual,minSpeed,maxSpeed,capacityOfKnapsack));
        }
    }

    public Individual solveTTP()
    {
        int gen=0;
        generatePopulation(cities);
        evaluatePopulation(initialPopulation);
        parentsPopulation = initialPopulation;
        while(gen< GENERATION_SIZE)
        {
            writeStatistics(gen);
            parentsPopulation = selectionTourney(initialPopulation);
            crossing();
            mutation();
            initialPopulation = parentsPopulation;
            evaluatePopulation(initialPopulation);
            gen++;
        }

        return findBestSolution();
    }

    public Individual solveRandom()
    {
        generatePopulation(cities);
        evaluatePopulation(initialPopulation);
        return findBestSolution();
    }


    public Individual solveGreedy()
    {
        List<City> path = new ArrayList<>();
        City firstCity = cities.get(random.nextInt(citiesNumber));
        path.add(firstCity);
        for(int i=0;i<citiesNumber-1;i++)
        {
            City nextCity = getClosestCity(firstCity,path);
            path.add(nextCity);
            firstCity = nextCity;
        }
        Individual individual = new Individual(path);
        List<Individual> singleIndList = new ArrayList<>();
        singleIndList.add(individual);
        evaluatePopulation(singleIndList);
        return individual;
    }

    private City getClosestCity(City fromCity, List<City> path)
    {
        City toCity = cities.get(random.nextInt(citiesNumber));

        while(toCity.getId()==fromCity.getId()||path.contains(toCity))
            toCity = cities.get(random.nextInt(citiesNumber));
        for(City city: cities)
        {
            if(!path.contains(city)) {
                if (fromCity.getDistance(city) < fromCity.getDistance(toCity))
                    toCity = city;
            }
        }
        return toCity;
    }

    private Individual findBestSolution()
    {
        Individual bestIndividual = initialPopulation.get(0);
        for (Individual individual :
                initialPopulation
             ) {
            if(individual.getMeasure()> bestIndividual.getMeasure())
                bestIndividual = individual;

        }
        return bestIndividual;
    }

    private List<Individual> selectionTourney(List<Individual> population)
    {
        if(TOUR ==0)
            return population;
        List<Individual> selected = new ArrayList<>();
        int chosenTSPs = 0;
        int [] positions = new int[TOUR];
        Individual[] tourneyGroup = new Individual[TOUR];
        while(chosenTSPs < popSize) {
            for(int i=0;i<positions.length;i++)
            {
                positions[i] = random.nextInt(population.size());
                tourneyGroup[i]=population.get(positions[i]);
            }

            selected.add(selectionTourneyHelper(tourneyGroup));
            chosenTSPs++;
        }
        return selected;
    }

    private List<Individual> selectionRoulette(List<Individual> population)
    {
        double min_val = -findWorstMeasure(population);
        List<Individual> selected = new ArrayList<>();
        int chosenIndividuals = 0;
        double[] perc = new double[population.size()];
        double sumMeasure = 0;
        for(int i=0;i< perc.length;i++)
        {
            sumMeasure+=population.get(i).getMeasure()+min_val;
        }
        for(int i=0;i<perc.length;i++)
        {
            if(i==0)
                perc[i]=(population.get(i).getMeasure()+min_val)/sumMeasure;
            else
                perc[i] = perc[i-1] + (population.get(i).getMeasure()+min_val)/sumMeasure;
        }
        double rand = random.nextDouble();
        while(chosenIndividuals<popSize)
        {
            int index = 0;
            while(rand>perc[index])
            {
                index++;
            }
            selected.add(population.get(index));
            chosenIndividuals++;
            rand = random.nextDouble();
        }
        return selected;
    }


    private Individual selectionTourneyHelper(Individual[]tourneyGroup)
    {
        Individual bestIndividual = tourneyGroup[0];
        for(int i=1 ;i<tourneyGroup.length;i++)
        {
            Individual currIndividual = tourneyGroup[i];
            if(currIndividual.getMeasure()> bestIndividual.getMeasure()){
                bestIndividual = currIndividual;
            }
        }
        return bestIndividual;
    }


    private void mutation()
    {
        for(int i=0;i<parentsPopulation.size();i++)
        {
            double rand = random.nextInt(LOTTERY_NUMBER);
            if(rand<=mutationProbability*LOTTERY_NUMBER)
            {
                int firstPosition = random.nextInt(citiesNumber);
                int secondPosition = random.nextInt(citiesNumber);
                while(firstPosition==secondPosition)
                {
                    secondPosition = random.nextInt(citiesNumber);
                }
                List<City> tspArray = parentsPopulation.get(i).getArray();
                City firstCity =tspArray.get(firstPosition);
                City secondCity = tspArray.get(secondPosition);
                tspArray.set(firstPosition,secondCity);
                tspArray.set(secondPosition,firstCity);
            }
        }
    }

    private void crossing()
    {
        List<Individual> childrenPopulation = new ArrayList<>();
        for(int i=0;i<parentsPopulation.size(); i++) {
            Individual first = parentsPopulation.get(i);
            Individual second = parentsPopulation.get(random.nextInt(parentsPopulation.size()));
            double rand = random.nextInt(LOTTERY_NUMBER);
            if (rand <= crossProbability * LOTTERY_NUMBER) {
                childrenPopulation.add(crossOX(first, second));
            }
        }
        parentsPopulation.addAll(childrenPopulation);
    }

    public Individual crossOX(Individual first, Individual second)
    {
        List<City> firstParent = first.getArray();
        List<City> secondParent = second.getArray();
        int firstPos = random.nextInt(firstParent.size());
        int secondPos = random.nextInt(firstParent.size());
        while(firstPos==secondPos)
        {
            secondPos = random.nextInt(firstParent.size());
        }
        int temp = firstPos;
        if(firstPos>secondPos) {
            firstPos = secondPos;
            secondPos = temp;
        }
        List<City> child = new ArrayList<>(firstParent.size());
        for(int i=0;i<firstParent.size();i++)
        {
            child.add(null);
        }
        for(int i=firstPos;i<=secondPos;i++)
        {
            child.set(i,firstParent.get(i));
        }
        int nextPos = 0;
        for(int i=0;i<firstPos;i++)
        {
            boolean found = false;
            for(int j=nextPos;j<secondParent.size() && !found;j++) {
                if(!child.contains(secondParent.get(j))) {
                    child.set(i, secondParent.get(j));
                    nextPos = j+1;
                    found = true;
                }
            }
        }
        for(int i=secondPos+1;i<child.size();i++)
        {
            boolean found = false;
            for(int j=nextPos;j<secondParent.size() && !found;j++) {
                if(!child.contains(secondParent.get(j))) {
                    child.set(i, secondParent.get(j));
                    nextPos = j+1;
                    found = true;
                }
            }
        }
        return new Individual(child);
    }



    public Individual crossCX(Individual first, Individual second)
    {
        List<City> cycle = new ArrayList<>();
        List<City> firstParent = first.getArray();
        List<City> secondParent = second.getArray();
        List<City> child = new ArrayList<>(firstParent.size());
        int rootId = firstParent.get(0).getId();
        cycle.add(firstParent.get(0));
        int lastId = -1;
        int index = 0;
        while(lastId!=rootId)
        {
            index = crossHelper(secondParent.get(index).getId(),firstParent);
            cycle.add(firstParent.get(index));
            lastId = secondParent.get(index).getId();

        }
        for (int i=0;i <firstParent.size() ;i++)
        {
            if(cycle.contains(firstParent.get(i)))
            {
                child.add(firstParent.get(i));
            }
            else
            {
                child.add(secondParent.get(i));
            }
        }
        return new Individual(child);


    }

    private int crossHelper(int id, List<City> list)
    {
        for(int i=0;i< list.size() ;i++)
        {
            if(list.get(i).getId()==id)
                return i;
        }
        return -1;
    }

    public void writeHeader()
    {
        try {
            FileWriter fileWriter = new FileWriter(CSV_FILE_PATH, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            StringBuilder sb = new StringBuilder();
            sb.append("genNumber");
            sb.append(',');
            sb.append("bestMeasure");
            sb.append(',');
            sb.append("avgMeasure");
            sb.append(',');
            sb.append("worstMeasure");
            sb.append("\n");
            printWriter.print(sb.toString());
            printWriter.flush();

            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void writeStatistics(int genNum)
    {
        try {
            FileWriter fileWriter = new FileWriter(CSV_FILE_PATH, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            StringBuilder sb = new StringBuilder();
            sb.append(genNum);
            sb.append(',');
            sb.append(findBestMeasure(initialPopulation));
            sb.append(',');
            sb.append(findAvgMeasure(initialPopulation));
            sb.append(',');
            sb.append(findWorstMeasure(initialPopulation));
            sb.append("\n");
            printWriter.print(sb.toString());
            printWriter.flush();

            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
       }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    private double findBestMeasure(List<Individual> population)
    {
        double bestMeasure = population.get(0).getMeasure();
        for (Individual individual : population
             ) {
            if(individual.getMeasure()>bestMeasure) {
                bestMeasure = individual.getMeasure();

            }

        }
        return bestMeasure;
    }

    private double findWorstMeasure(List<Individual> population)
    {
        double worstMeasure = population.get(0).getMeasure();
        for (Individual individual : population
        ) {
            if(individual.getMeasure()<worstMeasure) {
                worstMeasure = individual.getMeasure();
            }

        }
        return worstMeasure;
    }

    private double findAvgMeasure(List<Individual> population)
    {
        double sum = 0;
        for (Individual individual : population
        ) {
            sum+= individual.getMeasure();
        }
        return sum/population.size();
    }
}
