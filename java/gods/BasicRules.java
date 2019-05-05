package gods;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import model.Board;
import model.Builder;
import model.Floor;
import model.showTilesMode;

public class BasicRules {

    public void move(Board board, Ray ray, CollisionResults results, Builder selected) {
        board.getBoardNode().collideWith(ray, results); // Cursor is going to collide with board tiles
        if(results.size() > 0) // cursor collided with the board
        {

            Board.BoardTile target = findCollidingTile(board,selected,results) ;
            if(target != null)
                moveToSelectedTile(board,selected,target);

        }
    }

    private final Board.BoardTile findCollidingTile(Board board, Builder selected, CollisionResults results)
    {
        CollisionResult closest = results.getClosestCollision(); // closest tile that was hit
        for(Vector2f coordinates : selected.getAdjacentTiles())  // Target comparison
        {
            Board.BoardTile target = board.collidingTile((int)coordinates.x, (int)coordinates.y, closest); // an attempt of setting the target tile
            if(target != null && target.getCoordinates().equals(coordinates))
                return target;

        }
        return null;
    }

    private final void moveToSelectedTile(Board board, Builder selected, Board.BoardTile target)
    {
        Vector2f coordinates = target.getCoordinates();
        board.showAvailableTiles(selected, showTilesMode.hideTiles);
        board.getTile(selected.getColumn(), selected.getRow()).setMovable(true); // Current tile is no longer occupied
        board.getTile(selected.getColumn(), selected.getRow()).setBuildable(true); // Current tile is no longer occupied
        selected.setFloorLvl(target.getHeight());
        selected.getBuilderModel().setLocalTranslation(-52.0f + coordinates.x*20.0f, Builder.OFFSETS[selected.getFloorLvl().height],-52.0f+coordinates.y*20.f);
        selected.setCoordinates((int)coordinates.x, (int)coordinates.y);
        board.getTile((int)coordinates.x, (int)coordinates.y).setMovable(false);   // Target tile is going to be occupied now
        board.getTile((int)coordinates.x, (int)coordinates.y).setBuildable(false);   // Target tile is going to be occupied now
        selected.setMoved(true);

    }

    public Vector2f build(Board board, Ray ray, CollisionResults results, Builder selected)
    {
        board.getBoardNode().collideWith(ray, results); // Cursor is going to collide with board tiles
        if(results.size() > 0) // cursor collided with the board
        {
            Board.BoardTile target = findCollidingTile(board,selected,results);
            if(target != null)
                return buildOnSelectedTile(board, target,selected);
        }
        return null; // It must be here to avoid an non-return error
    }

    private final Vector2f buildOnSelectedTile(Board board, Board.BoardTile target,Builder selected)
    {
        Vector2f coordinates = target.getCoordinates();
        board.showAvailableTiles(selected, showTilesMode.hideTiles);
        board.buildTile((int) coordinates.x, (int) coordinates.y);
        selected.setBuilt(true);
        return new Vector2f(coordinates.x, coordinates.y);
    }

    public boolean isWinAccomplished(Builder builder)
    {
        return builder.getFloorLvl().equals(Floor.SECOND);
    }
}