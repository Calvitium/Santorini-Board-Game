package appStates;

import com.jme3.app.SimpleApplication;
import controler.AppMode;
import model.Board;
import model.Player;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import static controler.AppMode.PLAY;


public class Game extends SimpleApplication {

    public static void main(String[] args) {
        GAME.start();
    }

    //States
    static MenuState menuState = new MenuState();
    static InitializationState initializationState = new InitializationState();
    static BuilderSetState builderSetState = new BuilderSetState();
    static MultiPlayerLobbyState multiPlayerLobbyState = new MultiPlayerLobbyState();
    static InGameState inGameState;

    //Others
    public static AppMode appMode = PLAY;
    public static final Game GAME = new Game();
    static Player[] player;
    private int playerNumber;
    private boolean isMultiMode = false;


    @Override
    public void simpleInitApp() {
        stateManager.attach(menuState);
    }

    int getPlayerNumber() {
        return playerNumber;
    }

    void setPlayerNumber(int n) {

        playerNumber = n;
    }
    public boolean getIsMultiMode(){return isMultiMode;}
    public void setIsMultiMode(boolean isMultiMode){ this.isMultiMode = isMultiMode;}
}