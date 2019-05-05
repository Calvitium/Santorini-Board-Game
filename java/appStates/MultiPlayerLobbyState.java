package appStates;


import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.BaseStyles;
import controler.MultiPlLobbyStateListener;
import Multiplayer.*;

import java.io.IOException;


public class MultiPlayerLobbyState extends AbstractAppState {

    private SimpleApplication app;
    private Node guiNode;
    private Node rootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private Camera cam;
    private float windowHeight, windowWidth;
    private float tabHeight, tabWidth;
    private Container insertIPTextFields;
    private TextField prompt, insertIP ;
    private MultiPlLobbyStateListener actionListener;
    public static String insertedIP = "";
    private Container myWindow,buttons;




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
        actionListener = new MultiPlLobbyStateListener();


        createTextFields();
        createBackground();
        createButtons();

    }

    @Override
    public void cleanup()
    {
        super.cleanup();
        guiNode.detachAllChildren();

    }
    @Override
    public void update(float tpf)
    {
        if (insertedIP.length() == 15)
        {
            System.out.print(insertedIP);
            System.exit(15);
        }

        insertIP.setText(insertedIP);

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

    private void createButtons()
    {

            buttons = new Container();
            buttons.setPreferredSize(new Vector3f(tabWidth,tabHeight,0.0f));
            buttons.setLocalTranslation(windowWidth/2-tabWidth/2, windowHeight/2+tabHeight/2, 0);

            guiNode.attachChild(buttons);

            createJoinServerButton();
            createNewServerButton();


    }
    private void createJoinServerButton()
    {
        Button joinServer = buttons.addChild(new Button("joinServer"));
        joinServer.setColor(ColorRGBA.Green);

        joinServer.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                reshapeButtonContainer();
                initializeKeys();
                guiNode.attachChild(insertIPTextFields);
            }
        });

    }

    private void createNewServerButton()
    {
        Button createNewServer = buttons.addChild(new Button("newServer"));
        createNewServer.setColor(ColorRGBA.Green);

        createNewServer.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {

                Server server = new Server(6666);
                server.run();
                // create a client here
            }
        });

    }


    private void createClearButton()
    {
        Button clear = buttons.addChild(new Button("clear"));
        clear.setColor(ColorRGBA.Green);

        clear.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                insertIP.setText("");
                insertedIP = "";

            }
        });
    }

    private void createConnectButton()
    {
        Button connect = buttons.addChild(new Button("connect"));
        connect.setColor(ColorRGBA.Green);
        connect.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                Client client = new Client(insertedIP, 6666);
                client.sendMessage();

            }
        });
    }
    private void reshapeButtonContainer()
    {
        buttons.detachAllChildren();
        createConnectButton();
        createClearButton();
        buttons.setPreferredSize(new Vector3f(tabWidth/2,tabHeight/2,0.0f));
        buttons.setLocalTranslation(windowWidth/2-tabWidth/2, windowHeight/2, 0);


    }

    private void initializeKeys()
    {
        inputManager.addMapping(".", new KeyTrigger(KeyInput.KEY_PERIOD));
        inputManager.addListener(actionListener, ".");
        inputManager.addMapping("0", new KeyTrigger(11));
        inputManager.addListener(actionListener, "0");
        for(int i = 1;i<10;i++)
        {
            inputManager.addMapping(i + "" , new KeyTrigger(1 + i));
            inputManager.addListener(actionListener, i + "");
        }
    }


    private void createTextFields()
    {
        QuadBackgroundComponent sth= new QuadBackgroundComponent();
        sth.setTexture(assetManager.loadTexture("Textures/Textures/CobbleRoad.jpg"));
        insertIPTextFields = new Container();
        insertIPTextFields.setPreferredSize(new Vector3f(tabWidth/2,tabHeight/6,0.0f));
        insertIPTextFields.setLocalTranslation(windowWidth/2-tabWidth/2, 3*(windowHeight/2-tabHeight)+ tabHeight/6, 0);
        insertIPTextFields.setBackground(sth);
        prompt = insertIPTextFields.addChild(new TextField("Enter server's IP"));
        prompt.setColor(ColorRGBA.Orange);
        insertIP = insertIPTextFields.addChild(new TextField(insertedIP));
        insertIP.setColor(ColorRGBA.White);
    }





}
