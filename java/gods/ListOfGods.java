package gods;

import java.util.ArrayList;

import static gods.Chronos.CHRONOS;
import static gods.Pan.PAN;


public final class ListOfGods {


    public static ListOfGods GODS = new ListOfGods();

    private ArrayList<BasicRules> list = new ArrayList<>();

    private ListOfGods() {
        list.add(PAN);
        list.add(CHRONOS);
    }

    public void resetListOfGods() { GODS = new ListOfGods(); }

    public BasicRules getRandomGod() {
        int randomIndex =  (int)(Math.random()*10000 % list.size());
        BasicRules result = list.get(randomIndex);
        list.remove(randomIndex);
        return result;
    }
}
