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

public class NumberOfPlayersMenuState extends SantoriniMenuState {

    @Override
    public void initialize(AppStateManager stateManager, Application appImp) {

        super.initialize(stateManager, appImp);
        GAME.setIsMultiMode(true);
    }

    @Override
    public void createButtons() {
        super.createButtons();
        createPlayerButton(2, buttons);
        createPlayerButton(3, buttons);
        createPlayerButton(4, buttons);
    }

    public void createPlayerButton(int numberOfPlayers, Container playerNumberButtons) {
        Button newButton = playerNumberButtons.addChild(new Button(numberOfPlayers + " players"));
        newButton.setColor(ColorRGBA.Green);
        newButton.addClickCommands((Command<Button>) source -> {
            GAME.setPlayerNumber(numberOfPlayers);
            switchState(GAME.rulesSelectionMenuState);
        });
    }

    @Override
    protected void createReturnButton() {
        returnContainer = new Container();
        returnContainer.setPreferredSize(new Vector3f(75, 37, 0.0f));
        returnContainer.setLocalTranslation(windowWidth-100, windowHeight-50, 0);
        guiNode.attachChild(returnContainer);


        returnButton = returnContainer.addChild(new Button("BACK"));
        returnButton.setColor(ColorRGBA.Red);
        returnButton.addClickCommands((Command<Button>) source ->{
            GAME.setIsMultiMode(false);
            switchState(GAME.mainMenuState);
        });
    }



}
