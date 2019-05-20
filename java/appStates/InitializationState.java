package appStates;


import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.ViewPort;
import model.*;
import view.CameraControl;
import view.Scene;

import static appStates.Game.GAME;
import static model.Board.BOARD;


class InitializationState extends SantoriniState {
    private ViewPort viewPort;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        setClassFields();
        setTeamColors();
        new Scene(assetManager, rootNode, viewPort);
        new CameraControl(cam, BOARD.boardCentre(), inputManager);
        moveToBuilderSetState();
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

    @Override
    protected void setClassFields(){
        super.setClassFields();
        this.viewPort = GAME.getViewPort();
        GAME.player = new Player[GAME.getPlayerNumber()];
        this.players = GAME.player;
    }

    @Override
    protected void initializeKeys() {

    }

    private void moveToBuilderSetState(){ stateManager.attach(GAME.builderSetState); }

}