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

    private Container rulesSelectionMode;

    @Override
    public void initialize(AppStateManager stateManager, Application appImp) {
        super.initialize(stateManager, appImp);
        createRulesSelectionButtons();
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
            stateManager.detach(GAME.mainMenuState);
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

    @Override
    public void createPlayerButton(int numberOfPlayers, Container playerNumberButtons) {
        Button newButton = playerNumberButtons.addChild(new Button(numberOfPlayers + " players"));
        newButton.setColor(ColorRGBA.Green);
        newButton.addClickCommands((Command<Button>) source -> {
            GAME.setPlayerNumber(numberOfPlayers);
            switchToOtherContainer(this.playerNumberButtons, rulesSelectionMode);
        });
    }

    private void createRulesSelectionButtons() {
        rulesSelectionMode = new Container();
        rulesSelectionMode.setPreferredSize(new Vector3f(tabWidth, tabHeight, 0.0f));
        rulesSelectionMode.setLocalTranslation(windowWidth / 2 - tabWidth / 2, windowHeight / 2 + tabHeight / 2, 0);

        Button basicRulesMode = rulesSelectionMode.addChild(new Button("Basic\nrules"));
        Button randomGods = rulesSelectionMode.addChild(new Button("Random \nrules"));
        Button pickGods = rulesSelectionMode.addChild(new Button("Chosen \nrules"));
        basicRulesMode.setColor(ColorRGBA.Green);
        randomGods.setColor(ColorRGBA.Blue);
        pickGods.setColor(ColorRGBA.Red);

        basicRulesMode.addClickCommands((Command<Button>) source -> setBasicRules());
        randomGods.addClickCommands((Command<Button>) source -> setRandomRules());
        pickGods.addClickCommands((Command<Button>) source -> selectPrefferedRules());

    }

    private void setBasicRules() {
        setTeamColors();
        stateManager.cleanup();
        stateManager.detach(GAME.mainMenuState);
        stateManager.attach(GAME.initializationState);
    }

    private void selectPrefferedRules() {
    }

    private void setRandomRules() {
        setTeamColors();
        for(Player player : players)
            player.setRules(GODS.getRandomGod());
        stateManager.cleanup();
        stateManager.detach(GAME.mainMenuState);
        stateManager.attach(GAME.initializationState);
    }

    private void switchToOtherContainer(Container previous, Container present) {
        guiNode.detachChild(previous);
        guiNode.attachChild(present);
        if(previous == buttons) {
            returnContainer.detachChildAt(0);
            returnContainer.addChild(returnButton);
        }
        else if(present == buttons){
            returnContainer.detachChildAt(1);
           returnContainer.attachChildAt(exitButton, 0);
        }
    }

    private void setTeamColors() {
        String[] teamColors = {"Blue", "Red", "Green"};
        if(players.length < 4)
            for(int i = 0; i<players.length; i++)
                players[i] = new Player(teamColors[i]);
        else if(players.length == 4) {
            players[0] = new Player("Blue");
            players[1] = new Player("Red");
            players[2] = new Player(players[0]);
            players[3] = new Player(players[1]);
        }
    }

}

