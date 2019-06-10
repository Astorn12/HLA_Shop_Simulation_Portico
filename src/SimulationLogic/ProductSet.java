package SimulationLogic;

import java.util.*;

/**
 * Created by osiza on 27.05.2019.
 */
public class ProductSet {
    List<String> list= Arrays.asList("Jabłko","Ekran","Banany","Agrest","Ćwikła","Winogrona","Agrafka","Tarta lotaryńska","Brzoskwinie","Marchweki","Herbata","Mięsko","Czekolada","Wafle","Lody","Arbuz","Łosoś","Storczyk"
            ,"Zegarek","Kolczyki","Brukiew","Słoma","Pustaki","Farba","Meblościanka","Woda","Piwo","Brykiet","Podpałka","Zapałki");
    static ProductSet instance= new ProductSet();

    public static ProductSet getInstance()
    {
        return instance;
    }
    public static final int size=8;
    private ProductSet()
    {

    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getChosenList()
    {
        List<String> tmp= new LinkedList<>();
        for(int i=0;i<size;i++)
        {
            tmp.add(this.list.get(i));
        }
        return tmp;
    }

    public List<String> getAll()
    {
        List<String> clone= new LinkedList<>();
        for(String s:list)
        {
            clone.add(s);
        }
        return clone;
    }

    public List<String> getRandomUniqueBoundedList(int bound)
    {
        List<String> tmp =this.getChosenList();
        List<String> tmp2= new LinkedList<>();
        Random r= new Random();
        for(int i=0;i<bound;i++)
        {
            int t=r.nextInt(tmp.size()-1);
            tmp2.add(tmp.get(t));
            tmp.remove(t);
        }
        return tmp2;
    }
}
