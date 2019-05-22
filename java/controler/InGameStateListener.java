package controler;

import appStates.singleplayerStates.InGameState;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import model.Builder;
import model.showTilesMode;

import static appStates.singleplayerStates.InGameState.active;
import static appStates.singleplayerStates.InGameState.roundPhase;
import static controler.GamePhases.BUILDING_PHASE;
import static controler.GamePhases.MOVEMENT_PHASE;
import static controler.GamePhases.SELECTION_PHASE;
import static java.lang.Integer.parseInt;
import static java.lang.System.exit;
import static model.Board.BOARD;

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
        } else if (playerChangedHisMindWhileSelecting())
            cancelSelectedBuilder();

        else if (isMovingBuilderPossible()) {
            players[active].moveBuilder(cursorRay, results, selectedBuilder);
            if (selectedBuilder.hasMoved()) {
                checkWinCondition();
                //sendBufferWithInfo();
                goToBuildingPhase();
            }
        } else if (isBuildingPossible()) {
            players[active].orderBuild(cursorRay, results, selectedBuilder);
            if (selectedBuilder.hasBuilt()) {
                //send
                endPlayersTurn();
            }
        }
    }

    private void sendInfoBuffer(Builder selectedBuilder) {
        String infoBuffer = "";
        infoBuffer += roundPhase.getPhaseID();
        infoBuffer += selectedBuilder.getSex();
        infoBuffer += selectedBuilder.getColumn();
        infoBuffer += selectedBuilder.getRow();

    }

    private void readInfoBuffer(String infoBuffer) {
        switch (infoBuffer.charAt(0)) //handles info of which phase should be considered;
        {
            case 'M':
                executeOthersMovementPhase(infoBuffer);
                break;
            case 'B':
                executeOthersBuildingPhase(infoBuffer);
                break;
            case 'W':
                executeOthersWinCondition(infoBuffer);
                break;
        }

    }

    private void executeOthersWinCondition(String infoBuffer) {
        System.out.print("Player " + active + " WINS!!!!!!!!!!!!!!!!!!!");
        exit(1);
    }

    private void executeOthersBuildingPhase(String infoBuffer) {
        char sex = infoBuffer.charAt(1);
        int column = parseInt(infoBuffer.substring(2, 2));
        int row = parseInt(infoBuffer.substring(3, 3));

        if (sex == 'M')
            players[active].getRules().buildOnSelectedTile(BOARD.getTile(column, row), players[active].male);
        else if (sex == 'F')
            players[active].getRules().buildOnSelectedTile(BOARD.getTile(column, row), players[active].female);
    }

    private void executeOthersMovementPhase(String infoBuffer) {
        char sex = infoBuffer.charAt(1);
        int column = parseInt(infoBuffer.substring(2, 2));
        int row = parseInt(infoBuffer.substring(3, 3));

        if (sex == 'M')
        players[active].getRules().moveToSelectedTile(players[active].male, BOARD.getTile(column, row));
        else if (sex == 'F')
        players[active].getRules().moveToSelectedTile(players[active].female, BOARD.getTile(column, row));
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
        BOARD.showAvailableTiles(selectedBuilder, showTilesMode.showTiles);

    }

    private void checkWinCondition() {
        if (players[active].isWinAccomplished(selectedBuilder)) {
            System.out.println("Player " + (active + 1) + " WINS!!!!!");
            exit(1);
        }
    }

    private boolean isMovingBuilderPossible() {
        return actionName.equals("moveBuilder") && !adequateKeyPressed && selectedBuilder != null;
    }

    private void cancelSelectedBuilder() {
        BOARD.showAvailableTiles(selectedBuilder, showTilesMode.hideTiles); //hide available tiles
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
        BOARD.showAvailableTiles(selectedBuilder, showTilesMode.showTiles);

        if (!inputManager.hasMapping("moveBuilder"))
            addFunctionality("moveBuilder", MouseInput.BUTTON_LEFT);
    }

    private void removeTilesAvailableForPreviousBuilder() {
        BOARD.showAvailableTiles(selectedBuilder, showTilesMode.hideTiles);
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
