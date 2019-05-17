package TESTS;

import com.jme3.math.Vector2f;
import model.Board;
import model.Board.*;
import model.Builder;
import model.showTilesMode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static appStates.InGameState.roundPhase;
import static controler.AppMode.*;
import static controler.GamePhases.BUILDING_PHASE;
import static model.Board.getTestInstance;
import static model.Floor.*;
import static org.junit.Assert.*;
import static appStates.Game.appMode;


public class BoardTest {

    public static Board testInstance;

    @BeforeClass
    public static void setTestMode(){
        appMode = TEST;
        testInstance = getTestInstance();
    }

    @Test
    public void whenTileIsCreatedItsCoordinatesAreCorrect(){
        BoardTile tile = new BoardTile(1,2);
        Vector2f real = new Vector2f(tile.getCoordinates());
        Vector2f expected = new Vector2f(1,2);
        assertEquals(real, expected);
    }

    @Test
    public void whenTileisEmptyThenItIsNotCompleted(){
        BoardTile tile = new BoardTile(0,0);
        assertFalse(tile.isCompleted());
    }

    @Test
    public void whenTileIsEmptyThenItIsBuildable(){
        BoardTile tile = new BoardTile(0,0);
        assertTrue(tile.isBuildable());
    }

    @Test
    public void whenTileIsEmptyThenItIsMovable(){
        BoardTile tile = new BoardTile(0,0);
        assertTrue(tile.isMovable());
    }

    @Test
    public void whenTileIsEmptyThenItsHeightIsGround() {
        BoardTile tile = new BoardTile(2,2);
        assertEquals(tile.getHeight(), ZERO);
    }

    @Test
    public void whenTwoTilesWithSameCoordinatesAreGivenTheyAreEqual() {
        BoardTile tile1 = new BoardTile(1,2);
        BoardTile tile2 = new BoardTile(1,2);
        assertEquals(tile1,tile2);
    }

    @Test
    public void getTileTest() {
        BoardTile expected = new BoardTile(0,0);
        BoardTile real = testInstance.getTile(0,0);
        assertEquals(real, expected);
    }

    @Test
    public void whenTileisEmptyTheFloorIsZERO() {
        BoardTile tile = new BoardTile(0,0);
        assertEquals(tile.getHeight(), ZERO);
    }

    @Test
    public void buildingGROUNDtest() {
        BoardTile tile = new BoardTile(0,0);
        tile.buildUp();
        assertEquals(tile.getHeight(), GROUND);
    }

    @Test
    public void buildingFIRSTtest() {
        BoardTile tile = new BoardTile(0,0);
        tile.buildUp();
        tile.buildUp();
        assertEquals(tile.getHeight(), FIRST);
    }

    @Test
    public void buildingSECONDtest() {
        BoardTile tile = new BoardTile(0,0);
        tile.buildUp();
        tile.buildUp();
        tile.buildUp();
        assertEquals(tile.getHeight(), SECOND);
    }

    @Test
    public void buildingDOMEtest() {
        BoardTile tile = new BoardTile(0,0);
        tile.buildUp();
        tile.buildUp();
        tile.buildUp();
        tile.buildUp();
        assertEquals(tile.getHeight(), DOME);
        assertTrue(tile.isCompleted());
        assertFalse(tile.isMovable());
        assertFalse(tile.isBuildable());
    }

    @Test
    public void whenFlatIsCompletedWeCannotBuildAnymore() {
        BoardTile tile = new BoardTile(0,0);
        tile.buildUp();
        tile.buildUp();
        tile.buildUp();
        tile.buildUp();
        boolean thrown = false;
        try {
            tile.buildUp();
        } catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void whenEmptyBoardIsGivenAllAdjacentTilesAreFreeToBuild() {
        roundPhase  = BUILDING_PHASE;
        Builder builder = new Builder(0,0);
        testInstance.showAvailableTiles(builder, showTilesMode.showTiles);
        int size = builder.getAdjacentTiles().size();
        assertEquals(size, 3);
    }

    @After
    public void clearAfterPrevious1() {
        testInstance.getTile(0,0).setMovable(true);
        testInstance.getTile(0,0).setBuildable(true);
    }

    @Test
    public void whenEmptyBoardWithTwoAdjacentBuildersIsGivenTheyAreTakenIntoAccount() {
        roundPhase = BUILDING_PHASE;
        Builder builder1 = new Builder(2,2);
        Builder builder2 = new Builder(3,3);

        testInstance.showAvailableTiles(builder1, showTilesMode.showTiles);
        int size = builder1.getAdjacentTiles().size();
        assertEquals(size, 7);
    }

    @After
    public void clearAfterPrevious2 () {
        testInstance.getTile(2,2).setMovable(true);
        testInstance.getTile(3,3).setMovable(true);
        testInstance.getTile(2,2).setBuildable(true);
        testInstance.getTile(3,3).setBuildable(true);
    }

    @Test
    public void whenTilesAroundBuilderAreCompletedTheyAreTakenIntoAccount() {
        roundPhase = BUILDING_PHASE;
        Builder builder = new Builder(2,2);
        testInstance.getTile(1,2).setCompleted(true);
        testInstance.getTile(2,3).setCompleted(true);
        testInstance.getTile(1,1).setCompleted(true);
        testInstance.showAvailableTiles(builder, showTilesMode.showTiles);
        int size = builder.getAdjacentTiles().size();
        assertEquals(size, 5);
    }

    @Test
    public void hideAdjacentTiles() {
        Builder builder = new Builder(2,2);
        testInstance.showAvailableTiles(builder, showTilesMode.showTiles);
        testInstance.showAvailableTiles(builder, showTilesMode.hideTiles);
        int size = builder.getAdjacentTiles().size();
        assertEquals(size, 0);
    }

    @After
    public void clearAfterPrevious3() {
        testInstance.getTile(1,2).setCompleted(false);
        testInstance.getTile(2,3).setCompleted(false);
        testInstance.getTile(1,1).setCompleted(false);
        testInstance.getTile(2,2).setBuildable(true);
        testInstance.getTile(2,2).setMovable(true);
    }

    @AfterClass
    public static void clear() {
        appMode = PLAY;
        testInstance = null;
    }

}
