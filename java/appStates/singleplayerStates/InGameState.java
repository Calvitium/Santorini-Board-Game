package appStates.singleplayerStates;


import appStates.SantoriniState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import controler.GamePhases;
import controler.InGameStateListener;
import model.Builder;

import static appStates.Game.GAME;
import static controler.GamePhases.SELECTION_PHASE;

public class InGameState extends SantoriniState {
    public static int active; // players[active] is the one whose turn is currently considered
    
    public static GamePhases roundPhase;
    private TextField turnPanel;
    private InGameStateListener actionListener;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        setClassFields();
        setTurnPanel();
        initializeKeys();
    }

    @Override
    protected void setClassFields(){
        super.setClassFields();
        actionListener = new InGameStateListener(this);
        active = 0;
        roundPhase = SELECTION_PHASE;
    }

    @Override
    protected void initializeKeys() {
        inputManager.addMapping("selectBuilder", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("cancelBuilder", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "selectBuilder");
        inputManager.addListener(actionListener, "cancelBuilder");
    }

    @Override
    public void update(float fpt) {
        switch (roundPhase)
        {
            case SELECTION_PHASE:
                turnPanel.setText("Player " + (active + 1) + "'s turn." + '\n' + "Select one of your builders");
                break;
            case MOVEMENT_PHASE:
                turnPanel.setText("Player " + (active + 1) + "'s turn." + '\n' + "Choose where you want to move");
                break;
            case BUILDING_PHASE:
                turnPanel.setText("Player " + (active + 1) + "'s turn." + '\n' + " Choose the tile on which you want to build.");
                break;

                default:
                    break;
        }
    }


    private void setTurnPanel() {
        QuadBackgroundComponent sth= new QuadBackgroundComponent();
        sth.setTexture(assetManager.loadTexture("Textures/Textures/CobbleRoad.jpg"));
        Container textContainer = new Container();
        textContainer.setLocalTranslation(40,cam.getHeight() - 40 ,0.0f);
        textContainer.setPreferredSize(new Vector3f(260,50,0.0f));
        textContainer.setBackground(sth);
        turnPanel = textContainer.addChild(new TextField("Turn indicator"));
        turnPanel.setColor(ColorRGBA.Orange);
        turnPanel.setText("Player " + (active +1)+ "'s turn.");
        turnPanel.setSingleLine(false);
        GAME.getGuiNode().attachChild(textContainer);
    }

}
