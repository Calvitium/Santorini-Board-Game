package appStates.multiplayerStates;

import Multiplayer.Client;
import appStates.SantoriniMenuState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.QuadBackgroundComponent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import static appStates.Game.GAME;

public class JoinGameMenuState extends SantoriniMenuState {
    private TextField insertIP;
    private ActionListener actionListener;
    public static String insertedIP = "";
    public static Client client;

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
        initializeKeys();
        createTextFields();
    }

    @Override
    public void createButtons() {
       super.createButtons();
        createClearButton();
        createConnectButton();
    }

    @Override
    protected void createReturnButton() {
        returnContainer = new Container();
        returnContainer.setPreferredSize(new Vector3f(75, 37, 0.0f));
        returnContainer.setLocalTranslation(windowWidth-100, windowHeight-50, 0);
        guiNode.attachChild(returnContainer);
        returnButton = returnContainer.addChild(new Button("BACK"));
        returnButton.setColor(ColorRGBA.Red);
        returnButton.addClickCommands((Command<Button>) source -> switchState(GAME.hostOrJoinMenuState));
    }

    @Override
    public void update(float tpf) {
        insertIP.setText(insertedIP);
    }

    private void createConnectButton() {
        Button connect = buttons.addChild(new Button("connect"));
        connect.setColor(ColorRGBA.Green);
        connect.addClickCommands((Command<Button>) source -> {
                    try {
                        if(!isValidAddress())
                            throw new IOException();
                        client = new Client(insertedIP, 6666,false);
                        if(client.sendAcknowledgement()==0) {
                            stateManager.attach(new LobbyState());
                            stateManager.detach(this);
                        }
                    } catch(SocketTimeoutException e) {
                        System.out.println("Connection timeout, could not connect.");
                        insertedIP = "";
                    } catch(IOException e) {
                        System.out.print("Failed to connect.");
                        insertedIP = "";
                    }
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

    private void createTextFields() {
        QuadBackgroundComponent sth = new QuadBackgroundComponent();
        sth.setTexture(assetManager.loadTexture("Textures/Textures/CobbleRoad.jpg"));
        Container insertIPTextFields = new Container();
        insertIPTextFields.setPreferredSize(new Vector3f(tabWidth, tabHeight / 6, 0.0f));
        insertIPTextFields.setLocalTranslation(windowWidth / 2 - tabWidth / 2, tabHeight, 0);
        insertIPTextFields.setBackground(sth);
        TextField prompt = insertIPTextFields.addChild(new TextField("Enter server's IP"));
        prompt.setColor(ColorRGBA.Orange);
        insertIP = insertIPTextFields.addChild(new TextField(insertedIP));
        insertIP.setColor(ColorRGBA.White);
        guiNode.attachChild(insertIPTextFields);
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

    private boolean isValidAddress() {
        try {
            return !(InetAddress.getByName(insertedIP).isReachable(100) == false || insertedIP.equals(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Should not get here
    }
}
