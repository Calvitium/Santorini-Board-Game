package appStates.singleplayerStates;


import appStates.Game;
import appStates.SantoriniState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.ViewPort;
import model.*;
import view.CameraControl;
import view.Scene;

import static appStates.Game.GAME;
import static model.Board.BOARD;


public class InitializationState extends SantoriniState {
    private ViewPort viewPort;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        setClassFields();
        new Scene(assetManager, rootNode, viewPort);
        new CameraControl(cam, BOARD.boardCentre(), inputManager);
        moveToBuilderSetState();
    }

    @Override
    protected void setClassFields(){
        super.setClassFields();
        this.viewPort = GAME.getViewPort();
        players = new Player[GAME.getPlayerNumber()];
        this.players = Game.players;
    }

    @Override
    protected void initializeKeys() {

    }

    private void moveToBuilderSetState(){ stateManager.attach(GAME.builderSetState); }

}