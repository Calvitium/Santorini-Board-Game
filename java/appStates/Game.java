package appStates;

import appStates.multiplayerStates.MultiPlayerLobbyState;
import appStates.singleplayerStates.BuilderSetState;
import appStates.singleplayerStates.InGameState;
import appStates.singleplayerStates.InitializationState;
import appStates.singleplayerStates.MainMenuState;
import com.jme3.app.SimpleApplication;
import controler.AppMode;
import model.Player;

import static controler.AppMode.PLAY;


public class Game extends SimpleApplication {

    public static void main(String[] args) {
        GAME.start();
    }

    //States
     public MainMenuState mainMenuState = new MainMenuState();
     public InitializationState initializationState = new InitializationState();
     public BuilderSetState builderSetState = new BuilderSetState();
     public MultiPlayerLobbyState multiPlayerLobbyState = new MultiPlayerLobbyState();
     public InGameState inGameState;

    //Others
    public static AppMode appMode = PLAY;
    public static final Game GAME = new Game();
    public static Player[] players;


    private Game(){
        super();
    }

    @Override
    public void simpleInitApp() {
        stateManager.attach(mainMenuState);
    }

    public void setPlayerNumber(int n) {
        players = new Player[n];
    }

    public int getPlayerNumber() {
        return players.length;
    }

}