package controler;

import appStates.InGameState;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import model.Builder;
import model.showTilesMode;

import static appStates.InGameState.BUILDING_PHASE;
import static appStates.InGameState.MOVEMENT_PHASE;
import static appStates.InGameState.SELECTION_PHASE;
import static appStates.InGameState.active;
import static appStates.InGameState.roundPhase;

public class InGameStateListener extends SantoriniActionListener {

    private Builder selectedBuilder;
    private CollisionResults results;
    private Ray cursorRay;

    public InGameStateListener(InGameState inGameState) {
        super(inGameState);

    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        updateActionFlags(name, keyPressed);
        updateCursorPosition();

        if (noBuilderYetSelected() && cursorPointsBuilder()) {

            if (playerSwitchesBetweenBuilders())
                    removeTilesAvailableForPreviousBuilder();
            selectBuilderToMove();
        }

        else if (playerChangedHisMindWhileSelecting())
            cancelSelectedBuilder();

        else if (isMovingBuilderPossible()) {
            players[active].moveBuilder(board, cursorRay, results, selectedBuilder);
            if (selectedBuilder.hasMoved()) {
                checkWinCondition();
                goToBuildingPhase();
            }
        }
        else if (isBuildingPossible()) {
            players[active].orderBuild(board, cursorRay, results, selectedBuilder);
            if (selectedBuilder.hasBuilt())
                endPlayersTurn();
        }
    }


    private void endPlayersTurn() {
        players[active].resetBuilderPhaseFlags(selectedBuilder);
        inputManager.deleteMapping("buildPhase");
        addFunctionality("cancelBuilder", MouseInput.BUTTON_RIGHT);
        addFunctionality("selectBuilder", MouseInput.BUTTON_LEFT);
        selectedBuilder = null;
        active = (active + 1) % (players.length);
        roundPhase = SELECTION_PHASE;
    }

    private boolean isBuildingPossible() {
        return actionName.equals("buildPhase") && !adequateKeyPressed;
    }

    private void goToBuildingPhase() {
        inputManager.deleteMapping("selectBuilder");
        inputManager.deleteMapping("cancelBuilder");
        inputManager.deleteMapping("moveBuilder");
        addFunctionality("buildPhase", MouseInput.BUTTON_LEFT);
        roundPhase = BUILDING_PHASE;
        board.showAvailableTiles(selectedBuilder, showTilesMode.showTiles);

    }

    private void checkWinCondition() {
        if (players[active].isWinAccomplished(selectedBuilder)) {
            System.out.println("Player " + (active + 1) + " WINS!!!!!");
            System.exit(1);
        }
    }

    private boolean isMovingBuilderPossible() {
        return  actionName.equals("moveBuilder") && !adequateKeyPressed && selectedBuilder != null;
    }

    private void cancelSelectedBuilder() {
        board.showAvailableTiles(selectedBuilder, showTilesMode.hideTiles); //hide available tiles
        selectedBuilder = null; // builder is canceled
        inputManager.deleteMapping("moveBuilder");
        roundPhase = SELECTION_PHASE;
    }

    private boolean playerChangedHisMindWhileSelecting() {
        return actionName.equals("cancelBuilder") && !adequateKeyPressed && selectedBuilder != null;
    }

    private void selectBuilderToMove() {
        roundPhase = MOVEMENT_PHASE;
        selectedBuilder = players[active].collidingBuilder(closestCursorCollision);
        board.showAvailableTiles(selectedBuilder, showTilesMode.showTiles);

        if (!inputManager.hasMapping("moveBuilder"))
            addFunctionality("moveBuilder", MouseInput.BUTTON_LEFT);
    }

    private void removeTilesAvailableForPreviousBuilder() {
        board.showAvailableTiles(selectedBuilder, showTilesMode.hideTiles);
    }

    private boolean playerSwitchesBetweenBuilders() {
        return selectedBuilder != null;
    }

    private boolean cursorPointsBuilder() {
        if (!(results.size() > 0))
            return false;
        closestCursorCollision = results.getClosestCollision();
        return players[active].collidingBuilder(closestCursorCollision) != null;
    }

    private boolean noBuilderYetSelected() {
        return actionName.equals("selectBuilder") && !adequateKeyPressed;
    }

    private void updateCursorPosition() {
        results = new CollisionResults();
        Vector2f click2d = inputManager.getCursorPosition().clone();
        Vector3f click3d = cam.getWorldCoordinates(click2d, 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(click2d, 1f).subtractLocal(click3d).normalizeLocal();
        cursorRay = new Ray(click3d, dir);
        players[active].getBuildersNode().collideWith(cursorRay, results);
    }

    private void addFunctionality(String mappingName, int mouseButton) {
        inputManager.addMapping(mappingName, new MouseButtonTrigger(mouseButton));
        inputManager.addListener(this, mappingName);
    }
}
