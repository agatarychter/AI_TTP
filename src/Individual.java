import java.util.ArrayList;
import java.util.List;

public class Individual {
    private List<City> array;
    private double measure;

    public double getMeasure()  {
        return measure;
    }

    public void setMeasure(double measure) {
        this.measure = measure;
    }


    public Individual(List<City> array)
    {
        this.array = array;
        measure = 0;
    }


    public List<City> getArray() {
        return array;
    }

    public void setArray(ArrayList<City> array) {
        this.array = array;
    }

    public String toString()
    {
        StringBuilder stringBuffer = new StringBuilder();
        for (City i:
                array
             ) {
            stringBuffer.append(i.getId());
            stringBuffer.append("\t");

        }
        return stringBuffer.toString();
    }
}
