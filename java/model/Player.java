package model;


import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Node;
import gods.BasicRules;
import gods.Pan;

import static appStates.Game.GAME;
import static appStates.Game.appMode;
import static controler.AppMode.PLAY;
import static controler.AppMode.TEST;
import static model.Board.BOARD;

public class Player {
   // BasicRules rules;          // rules that dictate way of moving, building, checking win condition etc.
    public Builder male, female;      // each players has two Builder references
    private Node buildersNode; // keeps both builder models (necessary for checking cursor collisions
    private BasicRules rules;

    public Player(String color) {
    // 1. preliminary initialization
        this.buildersNode = new Node("BuildersNode");
        this.male = new Builder(GAME.getAssetManager(), color, "M");
        this.female = new Builder(GAME.getAssetManager(), color, "F");
        this.rules = new BasicRules();
        GAME.getRootNode().attachChild(buildersNode);
    }

    public Player(Player player) {
        this.male = player.male;
        this.female = player.female;
        this.buildersNode = player.getBuildersNode();
        this.rules = new BasicRules();
        GAME.getRootNode().attachChild(buildersNode);
    }

    /** Returns true if male/female builder of a players was set OR false if not */
    public boolean isBuilderSet(Builder builder) {
    return builder.isPlaced();
}

/** Attaches a builder to the BOARD during the initialization phase */
    public void attachBuilder(Builder builder, int column, int row) {
    // 1. move and show the builder
        builder.getBuilderModel().setLocalTranslation(-52.0f + column*20.0f, Builder.OFFSETS[builder.getFloorLvl().height],-52.0f+row*20.f);
        buildersNode.attachChild(builder.getBuilderNode());
    // 2. update builder's coordinates and movable/buildable flags
        builder.setCoordinates(column, row);
        builder.setPlaced(true);
       if(appMode == PLAY) {
           BOARD.getTile(column, row).setBuildable(false);
           BOARD.getTile(column, row).setMovable(false);
       }
       else if(appMode == TEST) {
           Board.getTestInstance().getTile(column, row).setMovable(false);
           Board.getTestInstance().getTile(column, row).setBuildable(false);
       }
    }

/** Returns a builder that the cursor collided with - basically enables a selection of chosen builder */
    public Builder collidingBuilder(CollisionResult collisionResult) {
        Node n = collisionResult.getGeometry().getParent();
        while(n.getParent() != null)
        {
            if(n.equals(male.getBuilderNode()))
                return male;
            else if(n.equals(female.getBuilderNode()))
                return female;
        }
        return null;
    }

/** Returns a node that contains both builder models (necessary for checking collisions) */
    public Node getBuildersNode() {
        return buildersNode;
    }

/** Moves a picked builder on the selected tile (redirects arguments to the rules.move(....) */
    public void  moveBuilder(Ray ray, CollisionResults collisionResults, Builder builder){

        if(builder.getBuilderNode().equals(male.getBuilderNode()))
            rules.move(ray, collisionResults, male);

        else if(builder.getBuilderNode().equals(female.getBuilderNode()))
            rules.move(ray, collisionResults, female);
    }

/** Builds a floor on the selected tile (redirects arguments to the rules.build(....) function) */
    public void orderBuild(Ray ray, CollisionResults results, Builder builder) {
        if(builder.getBuilderNode().equals(male.getBuilderNode()))
            rules.build(ray, results, male);
        else if(builder.getBuilderNode().equals(female.getBuilderNode()))
            rules.build(ray, results, female);
    }

    public boolean isWinAccomplished(Builder builder) {
        return rules.isWinAccomplished(builder);
    }

    /** After the end of a turn, we reset flags that tell us whether builder has moved/built in this turn (preparation for the next turn) */
    public void resetBuilderPhaseFlags(Builder builder) {
        if(builder.getBuilderNode().equals(male.getBuilderNode()))
        {
            male.setMoved(false);
            male.setBuilt(false);
        }

        else if(builder.getBuilderNode().equals(female.getBuilderNode()))
        {
            female.setBuilt(false);
            female.setMoved(false);
        }
    }
    public BasicRules getRules() {
        return rules;
    }

    public void setRules(BasicRules rules) {
        this.rules = rules;
        System.out.println(rules.getName());
    }
}
