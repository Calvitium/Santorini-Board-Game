package appStates.singleplayerStates;

import appStates.SantoriniMenuState;
import appStates.multiplayerStates.LobbyState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import model.Player;

import static appStates.Game.GAME;

import static appStates.Game.players;
import static gods.ListOfGods.GODS;

public class RulesSelectionMenuState extends SantoriniMenuState {

    @Override
    public void initialize(AppStateManager stateManager, Application appImp)
    {
        super.initialize(stateManager,appImp);

    }
    @Override
    public void createButtons() {
        super.createButtons();
        Button basicRulesMode = buttons.addChild(new Button("\n\n\t       Basic Rules"));
        Button randomGods = buttons.addChild(new Button("\n\n\t    Random Rules"));
        Button pickGods = buttons.addChild(new Button("\n\n\t  Preferred Rules"));
        basicRulesMode.setColor(ColorRGBA.Green);
        randomGods.setColor(ColorRGBA.Green);
        pickGods.setColor(ColorRGBA.Green);

        basicRulesMode.addClickCommands((Command<Button>) source -> setBasicRules());
        randomGods.addClickCommands((Command<Button>) source -> setRandomRules());
        pickGods.addClickCommands((Command<Button>) source -> selectPrefferedRules());

    }

    @Override
    protected void createReturnButton() {
        returnContainer = new Container();
        returnContainer.setPreferredSize(new Vector3f(75, 37, 0.0f));
        returnContainer.setLocalTranslation(windowWidth-100, windowHeight-50, 0);
        guiNode.attachChild(returnContainer);

        returnButton = returnContainer.addChild(new Button("BACK"));
        returnButton.setColor(ColorRGBA.Red);
        returnButton.addClickCommands((Command<Button>) source -> switchState(GAME.numberOfPlayersMenuState));
    }

    private void setBasicRules() {
        createPlayerArray();
        stateManager.cleanup();
        stateManager.detach(GAME.mainMenuState);
        if (GAME.getIsMultiMode())
            stateManager.attach(new LobbyState());
        else
            stateManager.attach(GAME.initializationState);
    }

    private void selectPrefferedRules() {
    }

    private void setRandomRules() {
        createPlayerArray();
        for(Player player : players)
            player.setRules(GODS.getRandomGod());
        stateManager.cleanup();
        stateManager.detach(GAME.mainMenuState);
        if (GAME.getIsMultiMode())
            stateManager.attach(new LobbyState());
        else
            stateManager.attach(GAME.initializationState);
    }



}
