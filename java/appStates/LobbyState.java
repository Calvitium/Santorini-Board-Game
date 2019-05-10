package appStates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.TextField;

import static appStates.MultiPlayerLobbyState.client;


public class LobbyState extends SantoriniMenuState{

    private Container playerListCont;
    private TextField playerList;
    private String allPlayers;
    //private int playerNumber;

    @Override
    public void initialize(AppStateManager stateManager, Application appImp) {
        super.initialize(stateManager, appImp);
        createPlayerList();
    }
    @Override
    public void cleanup() {
        super.cleanup();
        guiNode.detachAllChildren();
    }

    @Override
    public void update(float tpf) {
        updatePlayerList();
    }
    @Override
    public void createButtons() {
        buttons = new Container();
        buttons.setPreferredSize(new Vector3f(tabWidth, tabHeight, 0.0f));
        buttons.setLocalTranslation(windowWidth / 2 - tabWidth / 2, windowHeight / 2 + tabHeight / 2, 0);
        guiNode.attachChild(buttons);

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
        playerList.setText(allPlayers);
    }





}
