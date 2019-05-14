package appStates;


import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import controler.BuilderSetStateListener;
import model.Builder;

import static appStates.Game.GAME;
import static model.Board.BOARD;

public class BuilderSetState extends SantoriniState {

    private BuilderSetStateListener builderSetStateListener;
    private TextField turnPanel;
    private Builder phantomBuilder;
    public int buildersCount;
    private CollisionResult currentTile;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        setClassFields();
        createPhantomBuilder();
        createTurnIndicator();
        initializeKeys();
    }

    @Override
    public void update(float tpf) {
        if (cursorPointsBoardTile())
            updatePhantomBuilderPosition();
        turnPanel.setText("Player" + (1 + buildersCount / 2) + ", set your builders.");
    }

    @Override
    public void setEnabled(boolean value) {
        if (!value)
            builderSetStateListener = null;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        builderSetStateListener = null;

        inputManager.deleteMapping("selectTile");

        rootNode.detachChild(phantomBuilder.getBuilderModel());
        GAME.getGuiNode().detachAllChildren();
        GAME.inGameState = new InGameState();
        stateManager.attach(GAME.inGameState);
    }

    @Override
    protected void setClassFields() {
        super.setClassFields();
        this.buildersCount = 0;
        this.builderSetStateListener = new BuilderSetStateListener(this);

    }

    @Override
    protected void initializeKeys() {
        inputManager.addMapping("selectTile", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(builderSetStateListener, "selectTile");
    }

    private void createPhantomBuilder() {
        phantomBuilder = new Builder(assetManager, "White");
        rootNode.attachChild(phantomBuilder.getBuilderModel());
    }

    private void createTurnIndicator() {
        QuadBackgroundComponent sth = new QuadBackgroundComponent();
        sth.setTexture(assetManager.loadTexture("Textures/Textures/CobbleRoad.jpg"));
        Container textContainer = new Container();
        textContainer.setLocalTranslation(0.0f, 100.0f, 0.0f);
        textContainer.setPreferredSize(new Vector3f(cam.getWidth() / 9, cam.getHeight() / 15, 0.0f));
        textContainer.setBackground(sth);
        turnPanel = textContainer.addChild(new TextField("Turn indicator"));
        turnPanel.setColor(ColorRGBA.Orange);
        GAME.getGuiNode().attachChild(textContainer);
    }

    private void updatePhantomBuilderPosition() {
        for (int column = 0; column < 5; column++)
            for (int row = 0; row < 5; row++)
                if (isTileOccupable(column, row))
                    updatePhantomBuilder(column, row);
    }

    private void updatePhantomBuilder(int column, int row) {
        phantomBuilder.getBuilderModel().setLocalTranslation(-52.0f + column * 20.0f, 3.0f, -52.0f + row * 20.f);
    }

    private boolean isTileOccupable(int column, int row) {
        return BOARD.getCollidingTile(column, row, currentTile) != null &&
               !BOARD.getTile(column, row).isCompleted() ;
    }

    private boolean cursorPointsBoardTile() {
        CollisionResults results = new CollisionResults();
        Vector2f click2d = inputManager.getCursorPosition().clone();
        Vector3f click3d = cam.getWorldCoordinates(click2d, 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(click2d, 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        BOARD.getBoardNode().collideWith(ray, results);
        if (results.size() > 0) {
            currentTile = results.getClosestCollision();
            return true;
        }
        return false;
    }
}

