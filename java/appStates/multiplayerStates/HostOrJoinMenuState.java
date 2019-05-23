package appStates.multiplayerStates;

import appStates.singleplayerStates.NumberOfPlayersMenuState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;

import static appStates.Game.GAME;

public class HostOrJoinMenuState extends NumberOfPlayersMenuState {
    @Override
    public void createButtons() {
        buttons = new Container();
        buttons.setPreferredSize(new Vector3f(tabWidth, tabHeight, 0.0f));
        buttons.setLocalTranslation(windowWidth / 2 - tabWidth / 2, windowHeight / 2 + tabHeight / 2, 0);
        guiNode.attachChild(buttons);
        createJoinServerButton();
        createNewServerButton();
    }


    private void createJoinServerButton() {
        Button joinServer = buttons.addChild(new Button("joinServer"));
        joinServer.setColor(ColorRGBA.Green);
        joinServer.addClickCommands((Command<Button>) source -> switchState(GAME.joinGameMenuState));
    }

    private void createNewServerButton() {
        Button createNewServer = buttons.addChild(new Button("newServer"));
        createNewServer.setColor(ColorRGBA.Green);
        createNewServer.addClickCommands((Command<Button>) source -> switchState(GAME.multiNumberOfPlayers));
    }

}
