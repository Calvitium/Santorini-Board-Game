package appStates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import model.Board;
import model.Player;

public abstract class SantoriniState extends AbstractAppState {
    protected Game game;
    protected AssetManager assetManager;
    public AppStateManager stateManager;
    public InputManager inputManager;
    public Camera cam;
    protected Node rootNode;
    public Board board;
    public Player[] players;

    protected void setClassFields(Application application){
        this.game = (Game) application; // can cast Application to something more specific
        this.rootNode = this.game.getRootNode();
        this.assetManager = this.game.getAssetManager();
        this.stateManager = this.game.getStateManager();
        this.inputManager = this.game.getInputManager();
        this.cam = this.game.getCamera();
        this.board = ((Game) application).board;
        this.players = ((Game) application).player;
    }

    protected abstract void initializeKeys();
}
