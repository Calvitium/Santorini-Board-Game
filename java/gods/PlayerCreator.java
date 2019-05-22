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
    default void createPlayers(Game app, Board BOARD) {
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



                case Artemis:
                    System.out.println("Player " + (i+1)+ " is Artemis.");
                    ((Game) app).players[i] = new Player(((Game) app), playerColor) {
                        boolean afterFirstMove = false;
                        @Override
                        public void move(Board BOARD, Ray ray, CollisionResults results, Builder selected) {

                            super.move(BOARD, ray, results, selected);
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
                        public Vector2f build(Board BOARD, Ray ray, CollisionResults results, Builder selected) {

                            Vector2f previousTarget = super.build(BOARD, ray, results, selected);
                            BOARD.buildTile((int)previousTarget.x,(int)previousTarget.y);
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