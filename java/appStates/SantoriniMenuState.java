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
    Container returnContainer;
    Container playerNumberButtons;
    Button exitButton;
    Button returnButton;

    public abstract void createButtons();

    public abstract void createReturnButton();

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
        createPlayerNumberButtons();
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
        Container myWindow = new Container();
        myWindow.setBackground(background);
        myWindow.setPreferredSize(new Vector3f(windowWidth, windowHeight, 0.0f));
        guiNode.attachChild(myWindow);
        myWindow.setLocalTranslation(0f, windowHeight, 0);
    }

    private void createPlayerNumberButtons() {
        playerNumberButtons = new Container();
        playerNumberButtons.setPreferredSize(new Vector3f(tabWidth, tabHeight, 0.0f));
        playerNumberButtons.setLocalTranslation(windowWidth / 2 - tabWidth / 2, windowHeight / 2 + tabHeight / 2, 0);

        createPlayerButton(2, playerNumberButtons);
        createPlayerButton(3, playerNumberButtons);
        createPlayerButton(4, playerNumberButtons);


    }

    public void createPlayerButton(int numberOfPlayers, Container playerNumberButtons) {
        Button newButton = playerNumberButtons.addChild(new Button(numberOfPlayers + " players"));
        newButton.setColor(ColorRGBA.Green);
        newButton.addClickCommands((Command<Button>) source -> {
            GAME.setPlayerNumber(numberOfPlayers);
            stateManager.cleanup();
            stateManager.attach(GAME.initializationState);
            stateManager.detach(GAME.menuState);

        });
    }
}
