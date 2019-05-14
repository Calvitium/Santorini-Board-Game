package appStates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.core.GuiComponent;
import com.simsilica.lemur.style.BaseStyles;

import static appStates.Game.GAME;

public abstract class SantoriniMenuState extends AbstractAppState {
    Node guiNode;
    AssetManager assetManager;
    AppStateManager stateManager;
    InputManager inputManager;
    float windowHeight, windowWidth;
    float tabHeight, tabWidth;
    Container buttons;
    Container myWindow;

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
        background.setTexture(assetManager.loadTexture("Textures/Textures/Backgroundd.png"));

        // Create a simple container for our elements
        myWindow = new Container();
        myWindow.setBackground(background);
        myWindow.setPreferredSize(new Vector3f(windowWidth, windowHeight, 0.0f));
        guiNode.attachChild(myWindow);
        myWindow.setLocalTranslation(0f, windowHeight, 0);
    }

    public abstract void createButtons();
}
