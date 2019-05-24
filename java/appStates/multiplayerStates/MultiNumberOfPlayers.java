package appStates.multiplayerStates;

import Multiplayer.Client;
import Multiplayer.Server;
import appStates.singleplayerStates.NumberOfPlayersMenuState;
import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;

import static appStates.Game.GAME;
import static appStates.multiplayerStates.JoinGameMenuState.client;

public class MultiNumberOfPlayers extends NumberOfPlayersMenuState {
    private static Server server;

    @Override
    public void createPlayerButton(int numberOfPlayers, Container playerNumberButtons) {
        Button newButton = playerNumberButtons.addChild(new Button("\n\n\t       " + numberOfPlayers + " players"));
        newButton.setColor(ColorRGBA.Green);
        newButton.addClickCommands((Command<Button>) source -> {
            GAME.setPlayerNumber(numberOfPlayers);
            server = new Server(6666,GAME.getPlayerNumber());
            server.setDaemon(true);
            server.start();
            client = new Client("127.0.0.1", 6666,true);
            client.sendAcknowledgement();

            stateManager.attach(GAME.rulesSelectionMenuState);
            stateManager.detach(GAME.multiNumberOfPlayers);
        });
    }
}
