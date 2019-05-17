package appStates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;

import static appStates.Game.GAME;


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

    private void switchToOtherContainer(Container previous, Container present) {
        guiNode.detachChild(previous);
        guiNode.attachChild(present);
    }



}

