package appStates.multiplayerStates;


import appStates.singleplayerStates.InGameState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

import static appStates.Game.GAME;
import static appStates.multiplayerStates.MultiPlayerLobbyState.client;
import static appStates.multiplayerStates.BuilderSetStateMulti.clientIndex;
import static java.lang.Integer.parseInt;
import static java.lang.System.exit;
import static model.Board.BOARD;

public class InGameStateMulti extends InGameState {



        @Override
        public void initialize(AppStateManager stateManager, Application app) {
            super.initialize(stateManager, app);

        }

        @Override
        public void update(float tpf) {
            if(clientIndex != active) {
                if(clientIndex != active) {
                    String updates = client.askForUpdates();
                    if(!updates.isEmpty()) {
                        executeUpdates(updates);

                    }
                }
            }

        }
        private void executeUpdates(String updates){

            switch (updates.charAt(0)) //handles info of which phase should be considered;
            {
                case 'M':
                    executeOthersMovementPhase(updates);
                    break;
                case 'B':
                    executeOthersBuildingPhase(updates);
                    break;
                case 'W':
                    executeOthersWinCondition(updates);
                    break;
                }

            }
            private void executeOthersWinCondition(String updates) {
                System.out.print("Player " + active + " WINS!!!!!!!!!!!!!!!!!!!");
                exit(1);
            }
        private void executeOthersBuildingPhase(String updates) {
            char sex = updates.charAt(1);
            int column = parseInt(updates.substring(2, 3));
            int row = parseInt(updates.substring(3, 4));

            if (sex == 'M')
                players[active].getRules().buildOnSelectedTile(BOARD.getTile(column, row), players[active].male);
            else if (sex == 'F')
                players[active].getRules().buildOnSelectedTile(BOARD.getTile(column, row), players[active].female);
            active = (active+1)%GAME.getPlayerNumber();
        }
        private void executeOthersMovementPhase(String updates) {
            char sex = updates.charAt(1);
            int column = parseInt(updates.substring(2, 3));
            int row = parseInt(updates.substring(3, 4));

            if (sex == 'M')
                players[active].getRules().moveToSelectedTile(players[active].male, BOARD.getTile(column, row));
            else if (sex == 'F')
                players[active].getRules().moveToSelectedTile(players[active].female, BOARD.getTile(column, row));
            
        }



}
