package controler;

import appStates.SantoriniState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.Camera;
import model.Board;
import model.Player;

abstract class SantoriniActionListener implements ActionListener {

    SantoriniState santoriniState;
    InputManager inputManager;
    Camera cam;
    AppStateManager stateManager;
    String actionName;
    boolean adequateKeyPressed;
    CollisionResult closestCursorCollision;
    Board board;
    Player[] players;

    SantoriniActionListener(SantoriniState santoriniState){
        this.santoriniState = santoriniState;
        this.inputManager = this.santoriniState.inputManager;
        this.cam = this.santoriniState.cam;
        this.stateManager = this.santoriniState.stateManager;
        this.board = this.santoriniState.board;
        this.players = this.santoriniState.players;
    }

    void updateActionFlags(String name, boolean keyPressed) {
        adequateKeyPressed = keyPressed;
        actionName = name;
    }

}
