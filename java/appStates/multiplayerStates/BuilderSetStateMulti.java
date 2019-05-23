package appStates.multiplayerStates;

import appStates.singleplayerStates.BuilderSetState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import view.CameraControl;

import static appStates.Game.GAME;
import static appStates.multiplayerStates.JoinGameMenuState.client;
import static java.lang.Integer.parseInt;
import static model.Board.BOARD;


public class BuilderSetStateMulti extends BuilderSetState {
    public static int clientIndex;
    public static int activePlayer;
    public static int builderCounter=0;

    @Override
    public void initialize(AppStateManager stateManager, Application appImp){
        super.initialize(stateManager,appImp);
        client.sendInitOrder();
        clientIndex =client.askForIndex();
        activePlayer = 0;

    }

    @Override
    public void update(float tpf)
    {
        if(builderCounter == 2*GAME.getPlayerNumber() )
        {
            stateManager.attach(new InGameStateMulti());
            stateManager.detach(this);
        }
        if(clientIndex != activePlayer) {
            String updates = client.askForUpdates();
            if(!updates.isEmpty()) {
                executeUpdates(updates);
                builderCounter++;
            }
        }


        if(clientIndex == activePlayer && cursorPointsBoardTile())
            updatePhantomBuilderPosition();
        activePlayer = builderCounter/2;

    }
    private void executeUpdates(String updates) {
        if (updates.charAt(0) == 'P') {
            char sex = updates.charAt(1);
            int column = parseInt(updates.substring(2, 3));
            int row = parseInt(updates.substring(3, 4));
            if (sex == 'M')
                players[activePlayer].attachBuilder(players[activePlayer].male, column, row);
            else if (sex == 'F')
                players[activePlayer].attachBuilder(players[activePlayer].female, column, row);
        }
    }
}
