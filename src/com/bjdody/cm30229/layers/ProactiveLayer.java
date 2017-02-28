package com.bjdody.cm30229.layers;

import com.bjdody.cm30229.async.SensorController;
import com.bjdody.cm30229.model.BumpPercept;
import com.bjdody.cm30229.model.Percept;
import com.bjdody.cm30229.model.UltrasoundPercept;
import com.bjdody.cm30229.util.Direction;
import com.bjdody.cm30229.util.MotorController;
import lejos.nxt.Sound;

import java.util.HashMap;

/**
 * Created by Ollie on 18/02/17.
 */
public class ProactiveLayer extends Layer {

    private HashMap<Direction, Integer> model;
    private Direction wallDirection;

    public ProactiveLayer() {
        reactionBounds.put( Direction.FORWARD, 40 );
        reactionBounds.put( Direction.LEFT, 45 );
        reactionBounds.put( Direction.RIGHT, 45 );
        reactionBounds.put( Direction.BACKWARD, 50 );
        model = new HashMap<>();
        for ( Direction direction : Direction.values() ) {
            model.put( direction, 255 );
        }
        wallDirection = Direction.FORWARD;
    }

    @Override
    public void onPercept( Percept percept ) {
        updateModel( percept );

        if ( wait <= 0 ) {
                int forwardDistance = model.get( Direction.FORWARD  );
                int leftDistance    = model.get( Direction.LEFT     );
                int rightDistance   = model.get( Direction.RIGHT    );
                int backDistance    = model.get( Direction.BACKWARD );

                if ( forwardDistance <= reactionBounds.get( Direction.FORWARD ) ) {
                    Sound.playTone( 360, 300 );
                    if ( leftDistance <= reactionBounds.get( Direction.LEFT ) ) {
                        int avoidanceSpeed = calculateAvoidanceSpeed( Direction.LEFT, leftDistance );
                        MotorController.left( avoidanceSpeed );
                        MotorController.right( 0 );
                        wallDirection = Direction.LEFT;
                    } else if ( rightDistance <= reactionBounds.get( Direction.RIGHT ) ) {
                        int avoidanceSpeed = calculateAvoidanceSpeed( Direction.RIGHT, rightDistance );
                        MotorController.left( 0 );
                        MotorController.right( avoidanceSpeed );
                        wallDirection = Direction.RIGHT;
                    } else {
                        int avoidanceSpeed = calculateAvoidanceSpeed( Direction.FORWARD, forwardDistance );
                        MotorController.left( avoidanceSpeed );
                        MotorController.right( 0 );
                        wallDirection = Direction.LEFT;
                    }
                } else {
                    if ( wallDirection != Direction.FORWARD ) {
                        int wallDistance = model.get( wallDirection );
                        if ( wallDistance > reactionBounds.get( wallDirection ) ) {
                            switch ( wallDirection ) {
                                case LEFT:
                                    MotorController.left( MotorController.MAX_SPEED / 4 );
                                    MotorController.right( MotorController.MAX_SPEED / 2 );
                                    return;
                                case RIGHT:
                                    MotorController.left( MotorController.MAX_SPEED / 2 );
                                    MotorController.right( MotorController.MAX_SPEED / 4 );
                                    return;
                            }
                        }
                    }
                    MotorController.left( MotorController.MAX_SPEED / 3 );
                    MotorController.right( MotorController.MAX_SPEED / 3 );
                }
        } else {
            wait -= SensorController.PERCEPT_FREQUENCY;
        }
    }

    private void updateModel( Percept percept ) {
        if ( percept.getPerceptType() == Percept.PerceptType.ULTRASONIC_READING ) {
            UltrasoundPercept ultrasoundPercept = (UltrasoundPercept) percept;
            model.put( Direction.fromRotation( ultrasoundPercept.getRotation() ), ultrasoundPercept.getDistance() );
        }
    }

}
