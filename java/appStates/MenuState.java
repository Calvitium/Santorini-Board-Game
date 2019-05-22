package appStates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;

import static appStates.Game.GAME;
import static java.lang.System.exit;


public class MenuState extends SantoriniMenuState {


    @Override
    public void initialize(AppStateManager stateManager, Application appImp) {
        super.initialize(stateManager, appImp);
    }

    @Override
    public void createButtons() {
        buttons = new Container();
        buttons.setPreferredSize(new Vector3f(tabWidth, tabHeight, 0.0f));
        buttons.setLocalTranslation(windowWidth / 2 - tabWidth / 2, windowHeight / 2 + tabHeight / 2, 0);
        guiNode.attachChild(buttons);

        Button hotSeat = buttons.addChild(new Button("Hot seat"));
        hotSeat.setColor(ColorRGBA.Magenta);

        Button online = buttons.addChild(new Button("Online"));
        online.setColor(ColorRGBA.Green);
        hotSeat.addClickCommands((Command<Button>) source -> switchToOtherContainer(buttons, playerNumberButtons));
        online.addClickCommands((Command<Button>) source -> {
            stateManager.cleanup();
            stateManager.attach(GAME.multiPlayerLobbyState);
            stateManager.detach(GAME.menuState);
        });
    }

    @Override
    public void createReturnButton() {
        returnContainer = new Container();
        returnContainer.setPreferredSize(new Vector3f(75, 37, 0.0f));
        returnContainer.setLocalTranslation(windowWidth-100, windowHeight-50, 0);
        guiNode.attachChild(returnContainer);

        exitButton = returnContainer.addChild(new Button("EXIT"));
        exitButton.setColor(ColorRGBA.Red);
        exitButton.addClickCommands((Command<Button>) source -> exit(1));

        returnButton = returnContainer.addChild(new Button("BACK"));
        returnButton.setColor(ColorRGBA.Red);
        returnButton.addClickCommands((Command<Button>) source -> switchToOtherContainer(playerNumberButtons, buttons));
        returnContainer.detachChildAt(1);
    }

    private void switchToOtherContainer(Container previous, Container present) {
        guiNode.detachChild(previous);
        guiNode.attachChild(present);
        if(previous == buttons) {
            returnContainer.detachChildAt(0);
            returnContainer.addChild(returnButton);
        }
        else if(previous == playerNumberButtons) {
            returnContainer.detachChildAt(1);
           returnContainer.attachChildAt(exitButton, 0);
        }
    }


}

