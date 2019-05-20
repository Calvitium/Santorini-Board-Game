package TESTS;

import model.Board;
import model.Builder;
import org.junit.BeforeClass;
import org.junit.Test;
import static appStates.Game.appMode;
import static controler.AppMode.TEST;
import static model.Floor.SECOND;
import static model.Floor.ZERO;
import static org.junit.Assert.*;
import static TESTS.BoardTest.testBoard;

public class BuilderTest {

    @BeforeClass
    public static void setTestMode() {
        appMode = TEST;
        testBoard = Board.getTestInstance();
    }

    @Test
    public void getColumnTest() {
        Builder builder = new Builder(1,2);
        assertEquals(builder.getColumn(), 1);
    }

    @Test
    public void getRowTest() {
        Builder builder = new Builder(1,2);
        assertEquals(builder.getRow(), 2);
    }

    @Test
    public void setCoordinatesTest() {
        Builder builder = new Builder(1,2);
        builder.setCoordinates(2,2);
        assertEquals(builder.getRow(), 2);
        assertEquals(builder.getColumn(), 2);
    }

    @Test
    public void isPlacedTest() {
        Builder builder = new Builder(0,0);
        assertTrue(builder.isPlaced());
    }

    @Test
    public void setPlacedTest() {
        Builder builder = new Builder(2,2);
        builder.setPlaced(false);
        assertFalse(builder.isPlaced());
    }

    @Test
    public void getAdjacentTilesTest() {
        Builder builder = new Builder(1,1);
        assertEquals(builder.getAdjacentTiles().size(), 0);
    }

    @Test
    public void addAdjacentTileTest() {
        Builder builder = new Builder(1,1);
        builder.addAdjacentTile(1,0);
        builder.addAdjacentTile(1,2);
        assertEquals(builder.getAdjacentTiles().size(), 2);
    }

    @Test
    public void removeAdjacentTilesTest() {
        Builder builder = new Builder(1,1);
        builder.addAdjacentTile(1,0);
        builder.addAdjacentTile(1,2);
        builder.removeAdjacentTiles();
        assertEquals(builder.getAdjacentTiles().size(), 0);
    }

    @Test
    public void hasMovedTest() {
        Builder builder = new Builder(0,0);
        assertFalse(builder.hasMoved());
    }

    @Test
    public void setMovedTest() {
        Builder builder = new Builder(2,3);
        builder.setMoved(true);
        assertTrue(builder.hasMoved());
    }

    @Test
    public void getFloorLvl() {
        Builder builder = new Builder(1,2);
        assertEquals(builder.getFloorLvl(), ZERO);
    }

    @Test
    public void setFloorLvl() {
        Builder builder = new Builder(2,2);
        builder.setFloorLvl(SECOND);
        assertEquals(builder.getFloorLvl(), SECOND);
    }

    @Test
    public void hasBuilt() {
        Builder builder = new Builder(2,2);
        assertFalse(builder.hasBuilt());
    }

    @Test
    public void setBuilt() {
        Builder builder = new Builder(2,3);
        builder.setBuilt(true);
        assertTrue(builder.hasBuilt());
    }
}
