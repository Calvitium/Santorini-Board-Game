package model;

import appStates.Game;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

import static appStates.InGameState.*;

public final class Board {
    private AssetManager assetManager;
    private Node tilesNode;             // every single board tile is attached to it
    private Node boardNode;             // includes tilesNode and board frame
    private BoardTile tiles[][];

    public Board(Game game) {
        // 1. preliminary initialization
        this.tilesNode = new Node("Tiles");
        this.boardNode = new Node("Board");
        this.tiles = new BoardTile[5][5];
        this.assetManager = game.getAssetManager();
        // 2. loading board frame model
        Spatial boardFrame = assetManager.loadModel("Models/Board/Board.j3o");
        boardFrame.setLocalTranslation(-21.5f, 0.1f, -1.5f);
        boardFrame.setLocalScale(20.0f);
        boardNode.attachChild(boardFrame);
        // 3. loading board tiles
        for(byte column = 0; column<5; column++)
            for(byte row = 0; row<5; row++)
            {
                tiles[column][row] = new BoardTile(column, row);
                tilesNode.attachChild(tiles[column][row].tileNode);
            }
        boardNode.attachChild(tilesNode);
        // 4. final attachment of the clear board to the world
        highlightBoard();
        attachBoard(game.getRootNode());
    }

    private void highlightBoard() {
        for(int i = 0; i<5; i++)
            for(int j = 0; j<5; j++)
                tiles[i][j].makeColored();
    }

    private void attachBoard(Node node){
        node.attachChild(boardNode);
    }

/** Returns a board tile that a mouse cursor collides with OR returns null if the click missed the board */
    public BoardTile collidingTile(int column, int row, CollisionResult collisionResult) {
    Node n = collisionResult.getGeometry().getParent();
    while(n.getParent() != null)
    {
        if(n.equals(tiles[column][row].tileNode))
            return tiles[column][row];
        n = n.getParent();
    }
    return null;
}

/** Returns a board tile which location is defined by "column" and "row" indexes in a 5x5 matrix */
    public BoardTile getTile(int column, int row) {
    return tiles[column][row]; }

/** Actually it returns a tilesNode */
    public Node getBoardNode() {
    return tilesNode; }

/** Returns a tile (a Spatial) in the exact middle of the board */
    public Spatial boardCentre() {return tiles[2][2].tile;}

/** Builds up a tile that was selected by a players during the building phase */
    public void buildTile(int column, int row) { tiles[column][row].buildUp(); }

/** Shows tiles available to enter by the builder during the movement phase */
    public void showAvailableTiles(Builder selected, showTilesMode mode)
    {
    //case 1: we remove tiles that were available in a previous phase
        if(mode == showTilesMode.hideTiles)
            hideAllAdjacentTiles(selected);
        else // mode == showTilesMode.showTiles, case 2: we are checking every single adjacent tile around the builder
            showAllAdjacentTiles(selected);
    }
    private void hideAllAdjacentTiles(Builder selected)
    {
        Texture tileSwitchedOff = assetManager.loadTexture("Textures/Terrain/Grass.jpg");
        for(Vector2f coordinates : selected.getAdjacentTiles())
            tiles[(int)coordinates.x][(int)coordinates.y].tileMat.setTexture("ColorMap", tileSwitchedOff);
        selected.removeAdjacentTiles();
    }
    private void showAllAdjacentTiles(Builder selected)
    {
        int tempColumn, tempRow ;
        Texture tileHighlighted = assetManager.loadTexture("Textures/Terrain/Selected.jpg");
        for(tempColumn = selected.getColumn() - 1; tempColumn <= selected.getColumn() + 1; tempColumn++)
        {
            // allows to avoid ArrayIndexOutOfBoundsException
            if(tempColumn<0 || tempColumn > 4)
                continue;
            for(tempRow = selected.getRow() - 1; tempRow <= selected.getRow() + 1; tempRow++)
            {
                // as above
                if(tempRow<0 || tempRow > 4)
                    continue;
                if(canWeBuildHere(tiles[tempColumn][tempRow]) )
                {
                    tiles[tempColumn][tempRow].tileMat.setTexture("ColorMap", tileHighlighted);
                    selected.addAdjacentTile(tempColumn, tempRow);
                }
                else if(canWeMoveHere(tiles[tempColumn][tempRow],selected))
                {
                    tiles[tempColumn][tempRow].tileMat.setTexture("ColorMap", tileHighlighted);
                    selected.addAdjacentTile(tempColumn, tempRow);
                }
            }

        }
    }
    private boolean canWeBuildHere(BoardTile tile)
    {
        return !tile.isCompleted() && tile.isBuildable() && roundPhase == BUILDING_PHASE;
    }
    private boolean canWeMoveHere(BoardTile tile,Builder selected)
    {
        return roundPhase == MOVEMENT_PHASE && !tile.isCompleted() && tile.isMovable() && tile.getHeight().height - selected.getFloorLvl().height < 2;
    }




 /* This is an inner class describing each board tile from 25 tiles */
     public class BoardTile extends CollisionResult {

    /** Basic info about a tile */
        private int tileColumn;
        private int tileRow;
        private Floor height;

    /** Flags describing tile's availability in certain round phases */
        private boolean completed; // true when a full building stands on a tile
        private boolean movable;   // true
        private boolean buildable; //

        private Node tileNode;
        private Node domeNode;
        private Node floorsNode;

    /** Used to make floors colorful */
                private AmbientLight floorsLight;
        private AmbientLight domeLight;

    /** Models of buildings  */
        private Spatial ground, first, second, dome;
        private Spatial tile;
        private Material tileMat;
        private Texture tileTexture;


        BoardTile(int column, int row) {
        // 1. Initializing basic info
            tileColumn = column;
            tileRow = row;
            height = Floor.ZERO;
        // 2. Setting preliminary flags values
            completed = false;
            buildable = true;
            movable = true;
        // 3. Initializing necessary nodes
            tileNode = new Node("TileNode");
            domeNode = new Node("DomeNode");
            floorsNode = new Node("FloorsNode");

            setUpLights();
            loadBoardModel(assetManager,column,row);
            loadBlockModels(assetManager,column,row);
        // 4. Attaching nodes to the world
            tileNode.attachChild(tile);
            tileNode.attachChild(floorsNode);
            tileNode.attachChild(domeNode);
        }
        private void setUpLights()
        {
            floorsLight = new AmbientLight();
            floorsLight.setColor(ColorRGBA.White.mult(0.6f));
            domeLight = new AmbientLight();
            domeLight.setColor(ColorRGBA.Blue.mult(0.7f));
        }
        private void loadBoardModel(AssetManager assetManager,int column, int row)
        {
            Box tileShape = new Box(10.0f, 0.1f, 10.0f);
            tile = new Geometry("Tile", tileShape);
            tileMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            tileTexture = assetManager.loadTexture("Textures/Terrain/Grass.jpg");
            tileMat.setTexture("ColorMap", tileTexture);
            tile.setMaterial(tileMat);
            tile.setLocalTranslation(-51.5f + 20*column, 0.0f, -51.5f + 20*row);
        }
        private void loadBlockModels(AssetManager assetManager, int column, int row)
        {
            ground = assetManager.loadModel("Models/Floors/Ground.j3o");
            first = assetManager.loadModel("Models/Floors/First.j3o");
            second = assetManager.loadModel("Models/Floors/Second.j3o");
            dome = assetManager.loadModel("Models/Floors/Dome.j3o");
            ground.setLocalTranslation(-52.0f+column*20.0f,0.0f,-52.0f+row*20.0f);
            ground.setLocalScale(3.0f);
            first.setLocalTranslation(-52.0f+column*20.0f,6.0f,-52.0f+row*20.0f);
            first.setLocalScale(3.0f);
            second.setLocalTranslation(-52.0f+column*20.0f,17.2f,-52.0f+row*20.0f);
            second.setLocalScale(3.0f);
            dome.setLocalTranslation(-52.0f+column*20.0f,21.0f,-52.0f+row*20.0f);
            dome.setLocalScale(6.0f);
        }

        private void makeColored() { floorsNode.addLight(floorsLight); domeNode.addLight(domeLight); }

        private void buildUp() {
         if(height == Floor.ZERO)
         {
             floorsNode.attachChild(ground);
             height = Floor.GROUND;
         }
         else if(height == Floor.GROUND)
         {
             height = Floor.FIRST;
             floorsNode.attachChild(first);
         }
         else if(height == Floor.FIRST)
         {
             height = Floor.SECOND;
             floorsNode.attachChild(second);
         }
         else if(height == Floor.SECOND)
         {
             height = Floor.DOME;
             domeNode.attachChild(dome);
             completed = true;
             movable = false;
             buildable = false;
         }
     }


     /** Returns height of a building - 0 is GROUND, 1 is FIRST etc. */
        public Floor getHeight(){
         return height;
     }

     /** Returns a Vector2f with tile's matrix coordinates. "x" is a column coordinate, "y" is a row coordinate */
        public Vector2f getCoordinates() {
         return new Vector2f(tileColumn, tileRow);
     }

     /** Returns true when a building is completed */
        public boolean isCompleted(){
            return completed;
        }

    /** Returns true when a builder is able to enter a tile */
        public boolean isBuildable() {
             return buildable;
         }

    /** Returns true when a builder is able to build on a tile */
        boolean isMovable() {
             return movable;
         }

    /** Enables or disables tile's "buildability" */
        public void setBuildable(boolean b) {
                buildable = b;
         }

    /** Enables or disables a possibility to enter a certain tile */
        public void setMovable(boolean b){
                movable = b;
         }

 }
}
