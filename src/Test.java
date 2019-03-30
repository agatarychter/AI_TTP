public class Test {
    public static void main(String[] args) {
        Loader loader = new Loader();
        loader.load("C:\\Users\\Agata Rychter\\Documents\\Studia\\AI\\student\\easy_0.ttp");
        MainClass mainClass = new MainClass(loader.getCities(),loader.getItems(),loader.getCapacityOfKnapsack(),loader.getMinSpeed(),loader.getMaxSpeed(), 100, 0.1,0.9);
        for(int i=0;i<10;i++)
            System.out.println(mainClass.solveTTP().getMeasure());
        for(int i=0;i<100;i++)
            System.out.println(mainClass.solveRandom().getMeasure());
        for(int i=0;i<10;i++) {
            System.out.println(mainClass.solveGreedy().getMeasure());
        }
    }
}
