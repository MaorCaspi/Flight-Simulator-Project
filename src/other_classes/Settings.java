package other_classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Settings implements Serializable {
    private static final long serialVersionUID = 1L;
    public List<Item> Item=new ArrayList<>();
    public int RateSeconds;
    public String ProperFlightFileName;
    public String AlgoFileName;
}