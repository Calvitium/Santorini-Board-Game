package appStates;

import appStates.multiplayerStates.HostOrJoinMenuState;
import appStates.multiplayerStates.JoinGameMenuState;
import appStates.multiplayerStates.MultiNumberOfPlayers;
import appStates.singleplayerStates.*;
import com.jme3.app.SimpleApplication;
import controler.AppMode;
import model.Player;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import static controler.AppMode.PLAY;


public class Game extends SimpleApplication {

    public static void main(String[] args) {
        GAME.start();
    }

    //MultiplayerMenuStates
    public HostOrJoinMenuState hostOrJoinMenuState = new HostOrJoinMenuState();
    public JoinGameMenuState joinGameMenuState = new JoinGameMenuState();
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
    private boolean isMultiMode = false;
    public static String IP_ADDRESS;

    static {
        try {
            IP_ADDRESS = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


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

    public void setIsMultiMode(boolean b) { isMultiMode = b;}

    public boolean getIsMultiMode() { return isMultiMode;}
}