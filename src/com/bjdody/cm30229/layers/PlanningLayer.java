package com.bjdody.cm30229.layers;

import com.bjdody.cm30229.async.SensorController;
import com.bjdody.cm30229.model.BumpPercept;
import com.bjdody.cm30229.model.Percept;
import com.bjdody.cm30229.model.UltrasoundPercept;
import com.bjdody.cm30229.util.Direction;
import com.bjdody.cm30229.util.MotorController;

import java.util.HashMap;

/**
 * Created by Ollie on 18/02/17.
 */
public class PlanningLayer extends Layer {

    private HashMap<Direction, Integer> model;

    public PlanningLayer() {
        reactionBounds.put( Direction.FORWARD, 40 );
        reactionBounds.put( Direction.LEFT, 35 );
        reactionBounds.put( Direction.RIGHT, 35 );
        reactionBounds.put( Direction.BACKWARD, 50 );
        model = new HashMap<>();
        for ( Direction direction : Direction.values() ) {
            model.put( direction, 255 );
        }
    }

    @Override
    public void onPercept( Percept percept ) {
        updateModel( percept );

        if ( wait <= 0 ) {
            if ( !percept.isHandled() ) {
                int forwardDistance = model.get( Direction.FORWARD  );
                int leftDistance    = model.get( Direction.LEFT     );
                int rightDistance   = model.get( Direction.RIGHT    );
                int backDistance    = model.get( Direction.BACKWARD );

                if ( forwardDistance <= reactionBounds.get( Direction.FORWARD ) ) {
                    if ( leftDistance <= reactionBounds.get( Direction.LEFT ) ) {
                        int avoidanceSpeed = calculateAvoidanceSpeed( Direction.LEFT, leftDistance );
                        MotorController.left( avoidanceSpeed );
                        MotorController.right( 0 );
                    } else if ( rightDistance <= reactionBounds.get( Direction.RIGHT ) ) {
                        int avoidanceSpeed = calculateAvoidanceSpeed( Direction.RIGHT, rightDistance );
                        MotorController.left( 0 );
                        MotorController.right( avoidanceSpeed );
                    } else {
                        int avoidanceSpeed = calculateAvoidanceSpeed( Direction.FORWARD, forwardDistance );
                        MotorController.left( avoidanceSpeed );
                        MotorController.right( 0 );
                    }
                } else {
                    MotorController.left( MotorController.MAX_SPEED / 2 );
                    MotorController.right( MotorController.MAX_SPEED / 2 );
                }
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
