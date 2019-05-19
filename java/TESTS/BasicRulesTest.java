package TESTS;

import model.Board;
import model.Floor;
import model.Player;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static TESTS.BoardTest.testBoard;
import static appStates.Game.appMode;
import static controler.AppMode.TEST;
import static model.Floor.ZERO;
import static org.junit.Assert.*;

public class BasicRulesTest {
    @BeforeClass
    public static void setTestMode() {
        appMode = TEST;
        testBoard = Board.getTestInstance();
    }

    @Test
    public void moveBuilderTest() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 1,2);
        player.getRules().moveToSelectedTile(player.female, testBoard.getTile(1,1));

        assertTrue(player.female.hasMoved());
        assertFalse(testBoard.getTile(1,1).isMovable());
        assertFalse(testBoard.getTile(1,1).isBuildable());
    }

    @After
    public void clear1() {
        testBoard.getTile(1,1).setBuildable(true);
        testBoard.getTile(1,1).setMovable(true);
        testBoard.getTile(1,2).setMovable(true);
        testBoard.getTile(1,2).setBuildable(true);
    }

    @Test
    public void whenMovingNotOnAdjacentTileExceptionIsThrown() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 1,2);
        boolean thrown = false;
        try {
            player.getRules().moveToSelectedTile(player.female, testBoard.getTile(3,3));
        }catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    @After
    public void clear2() {
        testBoard.getTile(1,2).setMovable(true);
        testBoard.getTile(1,2).setBuildable(true);
    }

    @Test
    public void whenMovingHigherThan1FloorAtOnceExceptionIsThrown() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 1,2);
        testBoard.buildTile(1,1);
        testBoard.buildTile(1,1);
        boolean thrown = false;
        try {
            player.getRules().moveToSelectedTile(player.female, testBoard.getTile(1,1));
        }catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @After
    public void clear3() {
        clear2();
        testBoard.getTile(1,1).setFloorLVL(ZERO);
    }


    @Test
    public void whenMovingOnADomeExceptionIsThrown() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 1,2);
        testBoard.buildTile(1,1);
        testBoard.buildTile(1,1);
        testBoard.buildTile(1,1);
        testBoard.buildTile(1,1);
        player.female.setFloorLvl(Floor.SECOND);
        boolean thrown = false;
        try {
            player.getRules().moveToSelectedTile(player.female, testBoard.getTile(1,1));
        }catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
        clear3();
    }

    @Test
    public void whenNotMovingAtAllExceptionIsThrown() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 1,2);
        boolean thrown = false;
        try {
            player.getRules().moveToSelectedTile(player.female, testBoard.getTile(1,2));
        }catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
        clear2();
    }

    @Test
    public void whenMovingOnTheOccupiedTileExceptionIsThrown() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 0,2);
        player.attachBuilder(player.male, 0,3);
        boolean thrown = false;
        try {
            player.getRules().moveToSelectedTile(player.female, testBoard.getTile(0,3));
        }catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @After
    public void clear4() {
        testBoard.getTile(0,2).setBuildable(true);
        testBoard.getTile(0,2).setMovable(true);
        testBoard.getTile(0,3).setBuildable(true);
        testBoard.getTile(0,3).setMovable(true);
    }

    @Test
    public void whenMovingOutsideTheBoardExceptionIsThrown() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 0,2);
        boolean thrown = false;
        try {
            player.getRules().moveToSelectedTile(player.female, testBoard.getTile(-1,2));
        }catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @After
    public void clear5() {
        testBoard.getTile(0,2).setMovable(true);
        testBoard.getTile(0,2).setBuildable(true);
    }

    @Test
    public void whenBuildingOutsideTheBoardExceptionIsThrown() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 0,2);
        boolean thrown = false;
        try {
            player.getRules().buildOnSelectedTile(testBoard.getTile(-1,2), player.female);
        }catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
        clear5();
    }

    @Test
    public void whenBuildingNotOnTheAdjacentTileExceptionIsThrown() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 0,2);
        boolean thrown = false;
        try {
            player.getRules().buildOnSelectedTile(testBoard.getTile(1,4), player.female);
        }catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
        clear5();
    }

    @Test
    public void whenBuildingOnTheOccupiedTileExceptionIsThrown() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 0,2);
        player.attachBuilder(player.male, 0,3);
        boolean thrown = false;
        try {
            player.getRules().buildOnSelectedTile(testBoard.getTile(0,3), player.female);
        }catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
        clear4();
    }

    @Test
    public void whenBuildingOnTheCompletedTileExceptionIsThrown() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 0,2);
        testBoard.buildTile(0,1);
        testBoard.buildTile(0,1);
        testBoard.buildTile(0,1);
        testBoard.buildTile(0,1);
        boolean thrown = false;
        try {
            player.getRules().buildOnSelectedTile(testBoard.getTile(0,1), player.female);
        }catch (IndexOutOfBoundsException exc) {
            thrown = true;
        }
        assertTrue(thrown);
    }


}
