package appStates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.BaseStyles;
import model.Player;

import static appStates.Game.GAME;
import static appStates.Game.players;

public abstract class SantoriniMenuState extends AbstractAppState {
    protected Node guiNode;
    protected AssetManager assetManager;
    protected AppStateManager stateManager;
    protected InputManager inputManager;
    protected Container buttons;
    protected Container returnContainer;
    protected Container playerNumberButtons;
    protected Container myWindow;
    protected Button exitButton;
    protected Button returnButton;
    protected float windowHeight, windowWidth;
    protected float tabHeight, tabWidth;


    public void createButtons() {
        buttons = new Container();
        buttons.setPreferredSize(new Vector3f(tabWidth, tabHeight, 0.0f));
        buttons.setLocalTranslation(windowWidth / 2 - tabWidth / 2, windowHeight / 2 + tabHeight / 2, 0);
        guiNode.attachChild(buttons);
    }

    protected abstract void createReturnButton();

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, GAME);
        this.guiNode = GAME.getGuiNode();
        this.assetManager = GAME.getAssetManager();
        this.stateManager = GAME.getStateManager();
        this.inputManager = GAME.getInputManager();
        Camera cam = GAME.getCamera();
        windowHeight = cam.getHeight();
        windowWidth = cam.getWidth();
        tabWidth = windowWidth / 6;
        tabHeight = windowHeight / 3;
        createBackground();
        createButtons();
        createReturnButton();

    }

    @Override
    public void cleanup() {
        super.cleanup();
        guiNode.detachAllChildren();
    }

    private void createBackground() {
        GuiGlobals.initialize(GAME);
        BaseStyles.loadGlassStyle();

        // Set 'glass' as the default style when not specified
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        QuadBackgroundComponent background = new QuadBackgroundComponent();
        background.setTexture(assetManager.loadTexture("Textures/Textures/Sand.jpg"));

        // Create a simple container for our elements
        myWindow = new Container();
        myWindow.setBackground(background);
        myWindow.setPreferredSize(new Vector3f(windowWidth, windowHeight, 0.0f));
        guiNode.attachChild(myWindow);
        myWindow.setLocalTranslation(0f, windowHeight, 0);
    }
  
    protected void switchState(SantoriniMenuState menuState) {
        stateManager.cleanup();
        stateManager.detach(this);
        stateManager.attach(menuState);
    }
    protected void createPlayerArray()
    {
        GAME.players = GAME.players = new Player[GAME.getPlayerNumber()];
        String[] teamColors = {"Blue", "Red", "Green"};
        if(players.length < 4)
            for(int i = 0; i<players.length; i++)
                players[i] = new Player(teamColors[i]);
        else if(players.length == 4) {
            players[0] = new Player("Blue");
            players[1] = new Player("Red");
            players[2] = new Player(players[0]);
            players[3] = new Player(players[1]);
        }
    }
}
