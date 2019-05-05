package appStates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.AbstractAppState;
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


public class MenuState extends AbstractAppState {

    private SimpleApplication app;
    private Node guiNode;
    private Node rootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private Camera cam;
    private float windowHeight, windowWidth;
    private float tabHeight, tabWidth;
    private Container playerNumberButtons;
    private Container gameModeButtons;
    private Container myWindow;



    @Override
    public void initialize(AppStateManager stateManager, Application appImp) {

        super.initialize(stateManager, app);
        this.app = (Game) appImp; // can cast Application to something more specific
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.cam = this.app.getCamera();

        windowHeight = cam.getHeight();
        windowWidth = cam.getWidth();
        tabWidth = windowWidth/6;
        tabHeight = windowHeight/3;

        createBackground();
        createPlayerNumberButtons();
        createGameModeButtons();

    }

    @Override
    public void cleanup()
    {
        super.cleanup();
        guiNode.detachAllChildren();

    }
    private void createBackground()
    {
        GuiGlobals.initialize(app);
        BaseStyles.loadGlassStyle();

        // Set 'glass' as the default style when not specified
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        QuadBackgroundComponent background= new QuadBackgroundComponent();
        background.setTexture(assetManager.loadTexture("Textures/Textures/Sand.jpg"));

        // Create a simple container for our elements
        myWindow = new Container();
        myWindow.setBackground(background);
        myWindow.setPreferredSize(new Vector3f(windowWidth,windowHeight,0.0f));
        guiNode.attachChild(myWindow);
        myWindow.setLocalTranslation(0f, windowHeight, 0);
    }
    private void createGameModeButtons()
    {
        gameModeButtons = new Container();
        gameModeButtons.setPreferredSize(new Vector3f(tabWidth,tabHeight,0.0f));
        gameModeButtons.setLocalTranslation(windowWidth/2-tabWidth/2, windowHeight/2+tabHeight/2, 0);

        guiNode.attachChild(gameModeButtons);

        Button hotSeat = gameModeButtons.addChild(new Button("Hot seat"));
        hotSeat.setColor(ColorRGBA.Magenta);

        Button online = gameModeButtons.addChild(new Button("Online"));
        online.setColor(ColorRGBA.Green);

        hotSeat.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                switchToOtherContainer(gameModeButtons,playerNumberButtons);
            }
        });
        online.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                stateManager.cleanup();
                guiNode.attachChild(myWindow);
                stateManager.attach(new MultiPlayerLobbyState());
                stateManager.detach(((Game) app).menuState);
            }
        });
    }
    private void switchToOtherContainer(Container previous,Container present)
    {
        guiNode.detachChild(previous);
        guiNode.attachChild(present);
    }
    private void createPlayerNumberButtons()
    {
        playerNumberButtons = new Container();
        playerNumberButtons.setPreferredSize(new Vector3f(tabWidth,tabHeight,0.0f));
        playerNumberButtons.setLocalTranslation(windowWidth/2-tabWidth/2, windowHeight/2+tabHeight/2, 0);

        createButton(2,playerNumberButtons);
        createButton(3,playerNumberButtons);
        createButton(4,playerNumberButtons);


    }
    private Button createButton(int numberOfPlayers,Container playerNumberButtons) {
        Button newButton = playerNumberButtons.addChild(new Button(numberOfPlayers + " players"));
        newButton.setColor(ColorRGBA.Green);
        newButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                ((Game) app).setPlayerNumber(numberOfPlayers);
                stateManager.cleanup();
                stateManager.attach(((Game) app).initializationState);
                stateManager.detach(((Game) app).menuState);

            }
        });
        return newButton;
    }


}

