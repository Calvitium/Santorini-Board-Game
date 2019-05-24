package appStates.singleplayerStates;

import appStates.SantoriniMenuState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import gods.BasicRules;
import gods.Gods;
import model.Player;

import static appStates.Game.GAME;
import static appStates.Game.players;
import static gods.Chronos.CHRONOS;
import static gods.ListOfGods.GODS;
import static gods.Pan.PAN;
import static java.lang.System.exit;


public class MainMenuState extends SantoriniMenuState {

    @Override
    public void initialize(AppStateManager stateManager, Application appImp) {
        super.initialize(stateManager, appImp);
    }

    @Override
    public void createButtons() {
       super.createButtons();
        Button hotSeat = buttons.addChild(new Button("\n\n\n\t       Hot seat"));
        Button online = buttons.addChild(new Button("\n\n\n\t        Online"));
        hotSeat.setColor(ColorRGBA.Green);
        online.setColor(ColorRGBA.Green);
        hotSeat.addClickCommands((Command<Button>) source -> switchState(GAME.numberOfPlayersMenuState));
        online.addClickCommands((Command<Button>) source -> {
            GAME.setIsMultiMode(true);
            switchState(GAME.hostOrJoinMenuState);
        });
    }

    @Override
    public void createReturnButton() {
        returnContainer = new Container();
        returnContainer.setPreferredSize(new Vector3f(75, 37, 0.0f));
        returnContainer.setLocalTranslation(windowWidth - 100, windowHeight - 50, 0);
        guiNode.attachChild(returnContainer);

        exitButton = returnContainer.addChild(new Button("EXIT"));
        exitButton.setColor(ColorRGBA.Red);
        exitButton.addClickCommands((Command<Button>) source -> exit(1));
    }

}

