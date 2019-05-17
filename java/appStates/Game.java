package appStates;

import com.jme3.app.SimpleApplication;
import model.Board;
import model.Player;


public class Game extends SimpleApplication {

    public static void main(String[] args) {
        GAME.start();
    }

    //States
    MenuState menuState = new MenuState();
    InitializationState initializationState = new InitializationState();
    BuilderSetState builderSetState = new BuilderSetState();
    MultiPlayerLobbyState multiPlayerLobbyState  = new MultiPlayerLobbyState();
    InGameState inGameState;

    //Others
    public static final Game GAME = new Game();
    Player[] player;
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