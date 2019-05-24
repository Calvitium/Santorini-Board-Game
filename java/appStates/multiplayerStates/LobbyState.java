package appStates.multiplayerStates;

import appStates.SantoriniMenuState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;


import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.TextField;
import model.Player;

import static appStates.Game.players;
import static appStates.multiplayerStates.JoinGameMenuState.client;
import static appStates.Game.GAME;




public class LobbyState extends SantoriniMenuState {

    private Container playerListCont;
    private TextField playerList;
    private String allPlayers;
    private TextField ownIP;


    @Override
    public void initialize(AppStateManager stateManager, Application appImp) {
        super.initialize(stateManager, appImp);
        createButtons();
        if(!client.isHost()) {
            GAME.setPlayerNumber(client.askForPlayerLimit());
            createPlayerArray();
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        guiNode.detachAllChildren();
    }

    @Override
    public void update(float tpf) {

        if(client.checkIfGameStarted() == true)
            moveToInitialization();
        updatePlayerList();
    }
    @Override
    public void createButtons() {



        super.createButtons();
        createPlayerList();
        createConnectionButtons();
        createIPTextBox();

    }

    @Override
    protected void createReturnButton() {

    }


    private void moveToInitialization()
    {
        stateManager.detach(this);
        stateManager.attach(GAME.initializationState);
    }


    private void updatePlayerList() {
        String allPlayers = client.askForPlayerList();
        if(allPlayers.equals(""))
            returnToLastState();
        else
            playerList.setText(allPlayers);
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

    private void updatePlayer()
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
            Button startGame = buttons.addChild(new Button("\n\n\n\t           PLAY"));
            startGame.setColor(ColorRGBA.Green);
            startGame.addClickCommands((Command<Button>) source -> {

                if(client.askForPlayerCount() == GAME.getPlayerNumber())
                    client.sendGameTrigger();
            });
        }

    }

    private void createIPTextBox() {
        Container ownIPTextBox = new Container();
        ownIPTextBox.setPreferredSize(new Vector3f(120, 50, 0.0f));
        ownIPTextBox.setLocalTranslation(windowWidth - 300, windowHeight - 50,  0);
        ownIP = ownIPTextBox.addChild(new TextField("Your IP address: \n" + GAME.IP_ADDRESS));
        ownIP.setPreferredSize(new Vector3f(tabWidth / 2, tabHeight / 6, 0.0f));
        guiNode.attachChild(ownIPTextBox);
    }


    private void returnToLastState()
    {
        client.closeConnection(client.isHost());
        stateManager.attach(GAME.mainMenuState);
        stateManager.detach(this);
    }
}
