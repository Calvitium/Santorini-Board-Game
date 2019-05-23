package gods;

import model.Builder;
import model.Floor;

import static model.Board.BOARD;

public class Chronos extends BasicRules {

        public static final Chronos CHRONOS = new Chronos();

    private Chronos() {
        super();
        godName = "Chronos";
    }

    @Override
    public boolean isWinAccomplished(Builder builder) {
        return builder.getFloorLvl().equals(Floor.SECOND) || getCompletedTowers() >= 5;
    }

    private int getCompletedTowers() {
        int towersCount = 0;
        for(int i =0;i<5;i++) {
            for(int j =0;j<5;j++) {
                if(BOARD.getTile(i,j).getHeight().height==4)
                    towersCount++;
            }
        }
        return  towersCount;
    }
}
