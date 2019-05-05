package appStates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import Multiplayer.*;


public class MultiPlayerLobbyState extends SantoriniMenuState {
    private Container insertIPTextFields;
    private TextField insertIP;
    private ActionListener actionListener;
    public static String insertedIP = "";

    @Override
    public void initialize(AppStateManager stateManager, Application appImp) {
        super.initialize(stateManager, appImp);
        actionListener = new ActionListener(){
            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                if(checkIfNumberOrPeriod(name) && !keyPressed)
                    insertedIP +=name;
            }

            private boolean checkIfNumberOrPeriod(String name) {
                return ((name.charAt(0)>=48 && name.charAt(0)<=57 )|| name.equals("."));
            }
        };
        createTextFields();
    }

    @Override
    public void update(float tpf) {
        if (insertedIP.length() == 15) {
            System.out.print(insertedIP);
            System.exit(15);
        }
        insertIP.setText(insertedIP);
    }

    @Override
    public void createButtons() {
        buttons = new Container();
        buttons.setPreferredSize(new Vector3f(tabWidth, tabHeight, 0.0f));
        buttons.setLocalTranslation(windowWidth / 2 - tabWidth / 2, windowHeight / 2 + tabHeight / 2, 0);
        guiNode.attachChild(buttons);
        createJoinServerButton();
        createNewServerButton();
    }

    private void createJoinServerButton() {
        Button joinServer = buttons.addChild(new Button("joinServer"));
        joinServer.setColor(ColorRGBA.Green);

        joinServer.addClickCommands((Command<Button>) source -> {
            reshapeButtonContainer();
            initializeKeys();
            guiNode.attachChild(insertIPTextFields);
        });

    }

    private void createNewServerButton() {
        Button createNewServer = buttons.addChild(new Button("newServer"));
        createNewServer.setColor(ColorRGBA.Green);

        createNewServer.addClickCommands((Command<Button>) source -> {

            Server server = new Server(6666);
            server.run();
            // create a client here
        });
    }

    private void createClearButton() {
        Button clear = buttons.addChild(new Button("clear"));
        clear.setColor(ColorRGBA.Green);

        clear.addClickCommands((Command<Button>) source -> {
            insertIP.setText("");
            insertedIP = "";

        });
    }

    private void createConnectButton() {
        Button connect = buttons.addChild(new Button("connect"));
        connect.setColor(ColorRGBA.Green);
        connect.addClickCommands((Command<Button>) source -> {
            Client client = new Client(insertedIP, 6666);
            client.sendMessage();

        });
    }

    private void reshapeButtonContainer() {
        buttons.detachAllChildren();
        createConnectButton();
        createClearButton();
        buttons.setPreferredSize(new Vector3f(tabWidth / 2, tabHeight / 2, 0.0f));
        buttons.setLocalTranslation(windowWidth / 2 - tabWidth / 2, windowHeight / 2, 0);
    }

    private void initializeKeys() {
        inputManager.addMapping(".", new KeyTrigger(KeyInput.KEY_PERIOD));
        inputManager.addListener(actionListener, ".");
        inputManager.addMapping("0", new KeyTrigger(11));
        inputManager.addListener(actionListener, "0");
        for (int i = 1; i < 10; i++) {
            inputManager.addMapping(i + "", new KeyTrigger(1 + i));
            inputManager.addListener(actionListener, i + "");
        }
    }

    private void createTextFields() {
        QuadBackgroundComponent sth = new QuadBackgroundComponent();
        sth.setTexture(assetManager.loadTexture("Textures/Textures/CobbleRoad.jpg"));
        insertIPTextFields = new Container();
        insertIPTextFields.setPreferredSize(new Vector3f(tabWidth / 2, tabHeight / 6, 0.0f));
        insertIPTextFields.setLocalTranslation(windowWidth / 2 - tabWidth / 2, 3 * (windowHeight / 2 - tabHeight) + tabHeight / 6, 0);
        insertIPTextFields.setBackground(sth);
        TextField prompt = insertIPTextFields.addChild(new TextField("Enter server's IP"));
        prompt.setColor(ColorRGBA.Orange);
        insertIP = insertIPTextFields.addChild(new TextField(insertedIP));
        insertIP.setColor(ColorRGBA.White);
    }
}
