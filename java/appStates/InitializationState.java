package appStates;


import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.ViewPort;
import model.*;
import view.CameraControl;
import view.Scene;


class InitializationState extends SantoriniState {
    private ViewPort viewPort;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        setClassFields(application);

        for (int i = 0; i < game.player.length; i++)
            players[i] = new Player(game, "Blue");

        new Scene(assetManager, rootNode, viewPort);
        new CameraControl(cam, board.boardCentre(), inputManager);
        moveToBuilderSetState();
    }

    @Override
    protected void setClassFields(Application application){
        super.setClassFields(application);
        this.viewPort = this.game.getViewPort();
        ((Game) application).board = new Board((Game) application);
        this.board = ((Game) application).board;
        ((Game) application).player = new Player[((Game) application).getPlayerNumber()];
        this.players = ((Game)application).player;
    }

    @Override
    protected void initializeKeys() {

    }

    private void moveToBuilderSetState(){ stateManager.attach(game.builderSetState); }

}