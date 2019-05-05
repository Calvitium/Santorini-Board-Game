package gods;


import java.util.Random;

public enum Gods {
    Artemis,
    Pan,
    //Demeter,
    Chronos,
    Hephaestus/*,Hera,Hermes,Bia,Medusa,Tryton*/;

    public static Gods getRandomGod()
    {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
//Atena,Ares,Apollo