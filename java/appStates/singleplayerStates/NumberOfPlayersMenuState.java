package appStates.singleplayerStates;

import appStates.SantoriniMenuState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;

import static appStates.Game.GAME;
import static java.lang.System.exit;

public class NumberOfPlayersMenuState extends SantoriniMenuState {

    @Override
    public void initialize(AppStateManager stateManager, Application appImp) {
        super.initialize(stateManager, appImp);
    }

    @Override
    public void createButtons() {
        playerNumberButtons = new Container();
        playerNumberButtons.setPreferredSize(new Vector3f(tabWidth, tabHeight, 0.0f));
        playerNumberButtons.setLocalTranslation(windowWidth / 2 - tabWidth / 2, windowHeight / 2 + tabHeight / 2, 0);

        createPlayerButton(2, playerNumberButtons);
        createPlayerButton(3, playerNumberButtons);
        createPlayerButton(4, playerNumberButtons);
    }

    @Override
    protected void createReturnButton() {
        returnContainer = new Container();
        returnContainer.setPreferredSize(new Vector3f(75, 37, 0.0f));
        returnContainer.setLocalTranslation(windowWidth-100, windowHeight-50, 0);
        guiNode.attachChild(returnContainer);


        returnButton = returnContainer.addChild(new Button("BACK"));
        returnButton.setColor(ColorRGBA.Red);
        returnButton.addClickCommands((Command<Button>) source -> switchState(GAME.mainMenuState));
    }

    @Override
    public void createPlayerButton(int numberOfPlayers, Container playerNumberButtons) {
        Button newButton = playerNumberButtons.addChild(new Button(numberOfPlayers + " players"));
        newButton.setColor(ColorRGBA.Green);
        newButton.addClickCommands((Command<Button>) source -> {
            GAME.setPlayerNumber(numberOfPlayers);
        });
    }


}
