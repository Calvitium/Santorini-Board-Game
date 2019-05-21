package TESTS;

import controler.AppMode;
import model.Board;
import model.Floor;
import model.Player;
import org.junit.BeforeClass;
import org.junit.Test;

import static TESTS.BoardTest.testBoard;
import static appStates.Game.appMode;
import static org.junit.Assert.*;

public class PlayerTest {

    @BeforeClass
    public static void setTestMode(){
        appMode = AppMode.TEST;
        testBoard = Board.getTestInstance();
    }

    @Test
    public void whenBuildersAreNotAttachedToTheBoardTheyAreNotSet() {
        Player player = new Player("red");
        assertFalse(player.isBuilderSet(player.male));
        assertFalse(player.isBuilderSet(player.female));
    }

    @Test
    public void whenBuildersAreAttachedTheyAreSet() {
        Player player = new Player("blue");
        player.female.setPlaced(true);
        player.male.setPlaced(true);
        assertTrue(player.isBuilderSet(player.male));
        assertTrue(player.isBuilderSet(player.female));
    }

    @Test
    public void attachBuilderTest() {
        Player player = new Player("yellow");
        player.attachBuilder(player.male, 1,2);

        assertTrue(player.isBuilderSet(player.male));
        assertFalse(testBoard.getTile(1,2).isBuildable());
        assertFalse(testBoard.getTile(1,2).isMovable());
    }

    @Test
    public void whenBuilderIsOnTheSECONDfloorPlayerWins() {
        Player player = new Player("blue");
        player.attachBuilder(player.female, 1,1);
        player.female.setFloorLvl(Floor.SECOND);
        assertTrue(player.isWinAccomplished(player.female));
    }

    @Test
    public void whenBuilderIsNotOnTheSecondFloorWinIsNotAccomplished() {
        Player player1 = new Player("blue");
        Player player2 = new Player("red");
        player1.female.setFloorLvl(Floor.ZERO);
        player1.male.setFloorLvl(Floor.GROUND);
        player2.male.setFloorLvl(Floor.FIRST);

        assertFalse(player1.isWinAccomplished(player1.female));
        assertFalse(player1.isWinAccomplished(player1.male));
        assertFalse(player2.isWinAccomplished(player2.male));
    }

    @Test
    public void resetBuilderPhaseFlagsTest() {
        Player player = new Player("blue");
        player.male.setMoved(true);
        player.male.setBuilt(true);
        player.resetBuilderPhaseFlags(player.male);

        assertFalse(player.male.hasBuilt());
        assertFalse(player.male.hasMoved());
    }
}
