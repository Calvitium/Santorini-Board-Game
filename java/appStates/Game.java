package appStates;

import appStates.multiplayerStates.*;
import appStates.singleplayerStates.*;
import com.jme3.app.SimpleApplication;
import controler.AppMode;
import model.Player;

import static controler.AppMode.PLAY;


public class Game extends SimpleApplication {

    public static void main(String[] args) {
        GAME.start();
    }

    //MultiplayerMenuStates
    public MultiPlayerLobbyState multiPlayerLobbyState = new MultiPlayerLobbyState();
    public HostOrJoinMenuState hostOrJoinMenuState = new HostOrJoinMenuState();
    public JoinGameMenuState joinGameMenuState = new JoinGameMenuState();
    public HostGameMenuState hostGameMenuState = new HostGameMenuState();
    public MultiNumberOfPlayers multiNumberOfPlayers = new MultiNumberOfPlayers();
    //SingleplayerMenuStates
    public MainMenuState mainMenuState = new MainMenuState();
    public NumberOfPlayersMenuState numberOfPlayersMenuState = new NumberOfPlayersMenuState();
    public RulesSelectionMenuState rulesSelectionMenuState = new RulesSelectionMenuState();
    //GameplayStates

     public InitializationState initializationState = new InitializationState();
     public BuilderSetState builderSetState = new BuilderSetState();

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