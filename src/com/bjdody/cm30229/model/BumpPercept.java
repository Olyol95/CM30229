package com.bjdody.cm30229.model;

import com.bjdody.cm30229.util.Direction;

/**
 * Created by Ollie on 18/02/17.
 */
public class BumpPercept extends Percept {

    private Direction direction;

    public BumpPercept( Direction direction ) {
        super( PerceptType.BUMP_SENSOR_READING );
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

}
