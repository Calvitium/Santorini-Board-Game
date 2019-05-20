package appStates;

import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import model.Board;
import model.Player;

import static appStates.Game.GAME;

public abstract class SantoriniState extends AbstractAppState {
    AssetManager assetManager;
    public AppStateManager stateManager;
    public InputManager inputManager;
    public Camera cam;
    Node rootNode;
    public Player[] players;

    protected void setClassFields(){
        this.rootNode = GAME.getRootNode();
        this.assetManager = GAME.getAssetManager();
        this.stateManager = GAME.getStateManager();
        this.inputManager = GAME.getInputManager();
        this.cam = GAME.getCamera();
        this.players = GAME.player;
    }

    protected abstract void initializeKeys();
}
