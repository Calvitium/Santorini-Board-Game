package appStates;


import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import controler.InGameStateListener;
import model.Builder;

public class InGameState extends SantoriniState {
    private Builder selectedBuilder; //a builder, who was chosen by a players to make his turn
    public static int active; // players[active] is the one whose turn is currently considered
    public static int roundPhase;
    public static final int SELECTION_PHASE = 0;
    public static final int MOVEMENT_PHASE = 1;
    public   static final int BUILDING_PHASE = 2;
    private TextField turnPanel;
    private InGameStateListener actionListener;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        setClassFields(app);
        setTurnPanel();
        initializeKeys();
    }

    @Override
    protected void setClassFields(Application application){
        super.setClassFields(application);
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
            case 0:
                turnPanel.setText("Player " + (active + 1) + "'s turn." + '\n' + "Select one of your builders");
                break;
            case 1:
                turnPanel.setText("Player " + (active + 1) + "'s turn." + '\n' + "Choose where you want to move");
                break;
            case 2:
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
        textContainer.setLocalTranslation(0.0f,100.0f,0.0f);
        textContainer.setPreferredSize(new Vector3f(cam.getWidth()/9,cam.getHeight()/15,0.0f));
        textContainer.setBackground(sth);
        turnPanel = textContainer.addChild(new TextField("Turn indicator"));
        turnPanel.setColor(ColorRGBA.Orange);
        turnPanel.setText("Player " + (active +1)+ "'s turn.");
        turnPanel.setSingleLine(false);
        game.getGuiNode().attachChild(textContainer);
    }

}
