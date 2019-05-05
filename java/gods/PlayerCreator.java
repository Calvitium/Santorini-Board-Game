/* package gods;

import appStates.Game;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import model.Board;
import model.Builder;
import model.Floor;
import model.Player;

public interface PlayerCreator {
    default void createPlayers(Game app, Board board) {
        String playerColor;
        for(int i=0; i<((Game)app).players.length;i++) {
            if(i == 0)
                playerColor = "Blue";
            else if(i==1)
                playerColor = "Red";
            else if(i==2)
                playerColor = "Green";
            else if(i==3)
                playerColor = "Black";
            else
                playerColor = "White";

            Gods randomGod = Gods.getRandomGod();
            switch (randomGod)
            {
                case Pan:
                    System.out.println("Player " + (i+1)+ " is Pan.");
                    ((Game) app).players[i] = new Player(((Game) app), playerColor) {

                        int previousHeight = 0;
                        @Override
                        public void move(Board board, Ray ray, CollisionResults results, Builder selected) {
                            previousHeight = selected.getFloorLvl().height;
                            super.move(board, ray, results, selected);

                        }
                        @Override
                        public boolean isWinAccomplished(Builder builder) {
                            return ((previousHeight - builder.getFloorLvl().height >= 2) || builder.getFloorLvl() == Floor.SECOND);
                        }

                    };
                    break;

                case Chronos:
                    System.out.println("Player " +( i+1)+ " is Chronos.");
                    ((Game) app).players[i] = new Player(((Game) app), playerColor) {


                        @Override
                        public boolean isWinAccomplished(Builder builder) {
                            return builder.getFloorLvl().equals(Floor.SECOND) || calculateNumberOfTowers() >= 5;
                        }

                        int calculateNumberOfTowers() {
                            int towersCount = 0;
                            for(int i =0;i<5;i++)
                            {
                                for(int j =0;j<5;j++)
                                {
                                    if(board.getTile(i,j).getHeight().height==4)
                                        towersCount++;
                                }
                            }
                            return  towersCount;

                        }

                    };
                    break;
                case Artemis:
                    System.out.println("Player " + (i+1)+ " is Artemis.");
                    ((Game) app).players[i] = new Player(((Game) app), playerColor) {
                        boolean afterFirstMove = false;
                        @Override
                        public void move(Board board, Ray ray, CollisionResults results, Builder selected) {

                            super.move(board, ray, results, selected);
                            if(!afterFirstMove) {
                                afterFirstMove =true;
                                selected.setMoved(false);
                            }
                            else
                                afterFirstMove = false;

                        }


                    };
                    break;
                 case Hephaestus:
                    System.out.println("Player " + (i+1)+ " is Hephaestus.");
                    ((Game) app).players[i] = new Player(((Game) app), playerColor) {

                        @Override
                        public Vector2f build(Board board, Ray ray, CollisionResults results, Builder selected) {

                            Vector2f previousTarget = super.build(board, ray, results, selected);
                            board.buildTile((int)previousTarget.x,(int)previousTarget.y);
                            return null;
                        }


                    };
                    break;
                default:
                    ((Game) app).players[i] = new Player(((Game) app), playerColor);
                    break;

            }
        }
}}
*/