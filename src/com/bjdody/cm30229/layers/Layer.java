package com.bjdody.cm30229.layers;

import com.bjdody.cm30229.model.Percept;
import com.bjdody.cm30229.util.Direction;
import com.bjdody.cm30229.util.MotorController;

import java.util.HashMap;

/**
 * Created by Ollie on 18/02/17.
 */
public abstract class Layer {

    private Layer parentLayer;
    protected int wait;
    protected HashMap<Direction, Integer> reactionBounds;

    public Layer() {
        reactionBounds = new HashMap<>();
        wait = 0;
    }

    public abstract void onPercept( Percept percept );

    public Layer getParentLayer() {
        return parentLayer;
    }

    public void setParentLayer( Layer parentLayer ) {
        this.parentLayer = parentLayer;
    }

    protected void setWait( int ms ) {
        wait = ms;
    }

    protected int calculateAvoidanceSpeed( Direction direction, int distance ) {
        int reactionBound = reactionBounds.get( direction );
        return (int) Math.min(
                (-MotorController.MAX_SPEED / reactionBound )
                    * ( distance - reactionBound )
                    + ( MotorController.MAX_SPEED / 4 ),
                MotorController.MAX_SPEED * 0.75
        );
    }

}
