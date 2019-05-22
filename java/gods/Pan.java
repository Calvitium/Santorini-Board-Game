package gods;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import model.Builder;
import model.Floor;

import static model.Floor.FIRST;
import static model.Floor.ZERO;

public final class Pan extends BasicRules {

       private Floor previousHeight;

       public static final Pan PAN = new Pan();

       private Pan() {
           super();
           godName = "Pan";
       }

        @Override
        public void move(Ray ray, CollisionResults results, Builder selected) {
            previousHeight = selected.getFloorLvl();
            super.move(ray, results, selected);

        }
        @Override
        public boolean isWinAccomplished(Builder builder) {
            return (previousHeight == FIRST && builder.getFloorLvl() == ZERO)
                    || builder.getFloorLvl() == Floor.SECOND;
        }

}
