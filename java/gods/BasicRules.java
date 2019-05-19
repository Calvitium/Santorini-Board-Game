package gods;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import controler.AppMode;
import model.Board;
import model.Builder;
import model.Floor;
import model.showTilesMode;

import static appStates.Game.appMode;
import static java.lang.Math.abs;
import static model.Board.BOARD;

public class BasicRules {

    public void move(Ray ray, CollisionResults results, Builder selected) {
        BOARD.getBoardNode().collideWith(ray, results); // Cursor is going to collide with BOARD tiles
        if(results.size() > 0) // cursor collided with the BOARD
        {

            Board.BoardTile target = findCollidingTile(selected,results) ;
            if(target != null)
                moveToSelectedTile(selected,target);

        }
    }

    private Board.BoardTile findCollidingTile(Builder selected, CollisionResults results) {
        CollisionResult closest = results.getClosestCollision(); // closest tile that was hit
        for(Vector2f coordinates : selected.getAdjacentTiles())  // Target comparison
        {
            Board.BoardTile target = BOARD.getCollidingTile((int)coordinates.x, (int)coordinates.y, closest); // an attempt of setting the target tile
            if(target != null && target.getCoordinates().equals(coordinates))
                return target;

        }
        return null;
    }

    public void moveToSelectedTile(Builder selected, Board.BoardTile target) {
        Vector2f coordinates = target.getCoordinates();
        if(coordinates.x <0 || coordinates.x > 4 || coordinates.y < 0 || coordinates.y > 4)
            throw new IndexOutOfBoundsException("Moving outside the board is forbidden");
        if(abs(coordinates.x - selected.getColumn()) > 1 || abs(coordinates.y - selected.getRow()) > 1 )
            throw new IndexOutOfBoundsException("Builder can move to adjacent tile only");
        if(target.getHeight().height - selected.getFloorLvl().height > 1)
            throw new IndexOutOfBoundsException("Builder cannot move higher than 1 floor at once");
        if(target.isCompleted() && selected.getFloorLvl() == Floor.SECOND)
            throw new IndexOutOfBoundsException("Builder cannot move on a dome");
        if(coordinates.equals(new Vector2f(selected.getColumn(), selected.getRow())))
            throw new IndexOutOfBoundsException("Builder cannot move on a tile he started his turn");
        if(!target.isMovable())
            throw new IndexOutOfBoundsException("Builder cannot move on the occupied tile");
        BOARD.showAvailableTiles(selected, showTilesMode.hideTiles);
        BOARD.getTile(selected.getColumn(), selected.getRow()).setMovable(true); // Current tile is no longer occupied
        BOARD.getTile(selected.getColumn(), selected.getRow()).setBuildable(true); // Current tile is no longer occupied
        selected.setFloorLvl(target.getHeight());
        selected.getBuilderModel().setLocalTranslation(-52.0f + coordinates.x*20.0f, Builder.OFFSETS[selected.getFloorLvl().height],-52.0f+coordinates.y*20.f);
        selected.setCoordinates((int)coordinates.x, (int)coordinates.y);
        target.setMovable(false);   // Target tile is going to be occupied now
        target.setBuildable(false);   // Target tile is going to be occupied now
        selected.setMoved(true);

    }

    public Vector2f build(Ray ray, CollisionResults results, Builder selected) {
        BOARD.getBoardNode().collideWith(ray, results); // Cursor is going to collide with BOARD tiles
        if(results.size() > 0) // cursor collided with the BOARD
        {
            Board.BoardTile target = findCollidingTile(selected,results);
            if(target != null)
                return buildOnSelectedTile(target,selected);
        }
        return null; // It must be here to avoid an non-return error
    }

    public Vector2f buildOnSelectedTile(Board.BoardTile target, Builder selected) {
        Vector2f coordinates = target.getCoordinates();
        if(coordinates.x <0 || coordinates.x > 4 || coordinates.y < 0 || coordinates.y > 4)
            throw new IndexOutOfBoundsException("Building outside the board is forbidden");
        if(abs(coordinates.x - selected.getColumn()) > 1 || abs(coordinates.y - selected.getRow()) > 1 )
            throw new IndexOutOfBoundsException("Builder can build on adjacent tile only");
        if(!target.isBuildable())
            throw new IndexOutOfBoundsException("Selected tile is already occupied by a builder");
        if(target.isCompleted())
            throw new IndexOutOfBoundsException("One cannot build on fully built tile");
        BOARD.showAvailableTiles(selected, showTilesMode.hideTiles);
        BOARD.buildTile((int) coordinates.x, (int) coordinates.y);
        selected.setBuilt(true);
        return new Vector2f(coordinates.x, coordinates.y);
    }

    public boolean isWinAccomplished(Builder builder)
    {
        return builder.getFloorLvl().equals(Floor.SECOND);
    }
}