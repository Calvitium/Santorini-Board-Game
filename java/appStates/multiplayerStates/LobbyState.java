package appStates.multiplayerStates;

import appStates.SantoriniMenuState;
import appStates.multiplayerStates.MultiPlayerLobbyState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;


import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.TextField;

import static appStates.multiplayerStates.JoinGameMenuState.client;
import static appStates.Game.GAME;




public class LobbyState extends SantoriniMenuState {

    private Container playerListCont;
    private TextField playerList;
    private String allPlayers;


    @Override
    public void initialize(AppStateManager stateManager, Application appImp) {
        super.initialize(stateManager, appImp);
        createPlayerList();
        createConnectionButtons();
    }
    @Override
    public void cleanup() {
        super.cleanup();
        guiNode.detachAllChildren();
    }



    @Override
    public void update(float tpf) {
        updatePlayerList();
        if(client.checkIfGameStarted() == true)
            moveToInitialization();
    }
    @Override
    public void createButtons() {
        buttons = new Container();
        buttons.setPreferredSize(new Vector3f(tabWidth, tabHeight, 0.0f));
        buttons.setLocalTranslation(windowWidth / 2 - tabWidth / 2, windowHeight / 2 + tabHeight / 2, 0);
        guiNode.attachChild(buttons);

    }

    @Override
    public void createReturnButton() {

    }

    private void moveToInitialization()
    {
        stateManager.attach(GAME.initializationState);
        stateManager.detach(this);
    }

    private void createPlayerList(){
        playerListCont = new Container();
        playerListCont.setPreferredSize(new Vector3f(tabWidth / 2, tabHeight / 6, 0.0f));
        playerListCont.setLocalTranslation(windowWidth / 2 - tabWidth / 2, 3 * (windowHeight / 2 - tabHeight) + tabHeight / 6, 0);

        playerList = playerListCont.addChild(new TextField(""));
        playerList.setPreferredSize(new Vector3f(tabWidth / 2, tabHeight / 6, 0.0f));
        updatePlayerList();
        guiNode.attachChild(playerListCont);

    }
    private void updatePlayerList()
    {
        allPlayers = client.askForPlayerList();
        if(allPlayers.equals(""))
            returnToLastState();
        else
            playerList.setText(allPlayers);
    }
    private void createConnectionButtons()
    {
        Button disconnect = buttons.addChild(new Button("Disconnect"));
        disconnect.setColor(ColorRGBA.Green);
        disconnect.addClickCommands((Command<Button>) source -> {
            returnToLastState();
        });
        if(client.isHost())
        {
            Button startGame = buttons.addChild(new Button("Start game."));
            startGame.setColor(ColorRGBA.Green);
            startGame.addClickCommands((Command<Button>) source -> {

                if(client.askForPlayerCount() == GAME.getPlayerNumber())
                    client.sendGameTrigger();
            });
        }

    }
    private void returnToLastState()
    {
        client.closeConnection(client.isHost());
        stateManager.attach(GAME.mainMenuState);
        stateManager.detach(this);
    }
}
