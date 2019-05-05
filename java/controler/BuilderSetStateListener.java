package controler;

import appStates.BuilderSetState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import model.Player;

public class BuilderSetStateListener extends SantoriniActionListener {

    private int buildersCount;
    private boolean builderWasSet;


    public BuilderSetStateListener(BuilderSetState builderSetState) {
        super(builderSetState);
        this.buildersCount = builderSetState.buildersCount;
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {

        updateActionFlags(name, keyPressed);

        for (Player currentPlayer : players) {
            if (mousePointsBoardTile() && canSetBuilder())
                setBuilderOnTheBoard(currentPlayer);

            if (allBuildersSet())
                stateManager.detach(santoriniState);

        }
    }


    private void setBuilderOnTheBoard(Player currentPlayer) {
        for (int column = 0; column < 5; column++) {
            for (int row = 0; row < 5; row++)
                if (isTileOccupable(column, row)) {
                    if (!currentPlayer.isBuilderSet(currentPlayer.male)) {
                        currentPlayer.attachBuilder(currentPlayer.male, column, row);
                        builderWasSet = true;
                        buildersCount++;
                        break;
                    } else if (!currentPlayer.isBuilderSet(currentPlayer.female)) {
                        currentPlayer.attachBuilder(currentPlayer.female, column, row);
                        builderWasSet = true;
                        buildersCount++;
                        break;
                    }
                }
            if (builderWasSet) break;
        }
    }

    private boolean allBuildersSet() {
        return buildersCount == 2 * players.length;
    }

    private boolean mousePointsBoardTile() {
        builderWasSet = false;
        CollisionResults results = new CollisionResults();
        Vector2f click2d = inputManager.getCursorPosition().clone();
        Vector3f click3d = cam.getWorldCoordinates(click2d, 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(click2d, 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        board.getBoardNode().collideWith(ray, results);
        if (results.size() > 0) {
            closestCursorCollision = results.getClosestCollision();
            return true;
        } else return false;
    }

    private boolean canSetBuilder() {
        return actionName.equals("selectTile") && !adequateKeyPressed;
    }

    private boolean isTileOccupable(int column, int row) {
        return board.collidingTile(column, row, closestCursorCollision) != null &&
                board.getTile(column, row).isBuildable() &&
                board.getTile(column, row).isBuildable() &&
                !board.getTile(column, row).isCompleted();
    }
}
