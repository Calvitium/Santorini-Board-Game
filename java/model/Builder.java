package model;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import controler.AppMode;

import java.util.ArrayList;

import static TESTS.BoardTest.testBoard;
import static appStates.Game.appMode;

public class Builder {
   public static final float[] OFFSETS = new float[]{3.0f, 13.0f, 20.2f, 26.0f};
// basic info about builder's location
    private int tileColumn;
    private int tileRow;
    private Floor floorLVL;
// contains "available" tiles during movement/building phase
    private ArrayList<Vector2f> adjacentTiles;
// flags
    private boolean placed;
    private boolean moved;
    private boolean built;
// placed of variables necessary to load builder's model
    private Node builderNode;
    private Geometry builderModel;
    private Material material;

    public Builder(int column, int row) {
        floorLVL = Floor.ZERO;
        adjacentTiles = new ArrayList<>();
        moved = false;
        built = false;
        placed = true;
        tileColumn = column;
        tileRow = row;
        testBoard.getTile(column, row).setBuildable(false);
        testBoard.getTile(column, row).setMovable(false);
    }

    public Builder(AssetManager assetManager, String color) {
    // 1. preliminary initialization
        floorLVL = Floor.ZERO;
        adjacentTiles = new ArrayList<>();
        moved = false;
        built = false;
        placed = false;
        builderNode = new Node("BuilderNode");
    // 2. loading builder model
        Box box = new Box(1f, 3f, 1f);
        builderModel = new Geometry("Builder", box);
        builderModel.scale(3.0f);
        if(appMode == AppMode.TEST) return;
        material = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        builderModel.setMaterial(material);
        setTeamColor(color);
        builderNode.attachChild(builderModel);
    }

    private void setTeamColor(String color)  {
        switch (color) {
            case "Blue":
                material.setColor("Color", ColorRGBA.Blue);   // placed color of material to blue
                break;
            case "Red":
                material.setColor("Color", ColorRGBA.Red);   // placed color of material to blue
                break;
            case "Green":
                material.setColor("Color", ColorRGBA.Green);   // placed color of material to blue
                break;
        }

    }

/** Sets builder's BOARD coordinates */
    public void setCoordinates(int column, int row) {
        tileRow = row;
        tileColumn =  column;
    }

/** Returns a builder's model */
    public Geometry getBuilderModel(){
        return builderModel;
    }

/** "Set" flag becomes true after setting builder during the initialization phase */
    public void setPlaced(boolean b) { placed = b; }

/** Adds a tile which has [column][row] coordinates to the adjacency list (builder can move/build there depending on the phase)  */
public void addAdjacentTile(int column, int row){
        adjacentTiles.add(new Vector2f(column, row));
    }

/** Clears the array of the adjacency list*/
public void removeAdjacentTiles() {
        adjacentTiles.clear();
    }

/** Returns true if a builder was placed on the BOARD OR false otherwise*/
public boolean isPlaced() { return placed; }

/** Returns a node which the builder is directly attached to  */
    Node getBuilderNode() { return builderNode;  }

/** Returns builder's column coordinate */
    public int getColumn() { return tileColumn; }

/** Returns builder's row coordinate */
    public int getRow() { return tileRow; }

/** Returns a list of adjacent tiles that the builder can move to/build on */
    public ArrayList<Vector2f> getAdjacentTiles(){
        return adjacentTiles;
    }

/** Flag describing whether */
    public void setMoved(boolean b){
        moved = b;
    }
    public boolean hasMoved() { return moved;
    }
    public Floor getFloorLvl() { return floorLVL;
    }
    public void setFloorLvl(Floor update){
        floorLVL = update;
    }
    public void setBuilt(boolean b) {
        built = b;
    }
    public boolean hasBuilt() {
        return built;
    }
}
