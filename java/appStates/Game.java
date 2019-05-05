package appStates;
import Multiplayer.GreetClient;
import Multiplayer.GreetServer;
import com.jme3.app.SimpleApplication;
import model.Board;
import model.Player;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;


public class Game extends SimpleApplication {

    public static void main(String[] args) throws IOException {
       // GreetServer server = new GreetServer();
       // System.out.print(Inet4Address.getLocalHost().getHostAddress());

        //server.start(6666);



       //GreetClient client = new GreetClient();
        //client.startConnection("192.168.43.217", 6666);
        //String response = client.sendMessage("hello server");
        //System.out.print(response);

        Game app = new Game();
        app.start();
    }

    //States
    MenuState menuState = new MenuState();
    InitializationState initializationState = new InitializationState();
    BuilderSetState builderSetState = new BuilderSetState();
    InGameState inGameState;
    //Others
    Player[] player;
    public Board board;
    private int playerNumber;


    @Override
    public void simpleInitApp() {
        stateManager.attach(menuState);
    }

    int getPlayerNumber()
    {
        return playerNumber;
    }

    void setPlayerNumber(int n)
    {
        playerNumber = n;
    }
}