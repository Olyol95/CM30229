package com.bjdody.cm30229.layers;

import com.bjdody.cm30229.async.SensorController;
import com.bjdody.cm30229.model.BumpPercept;
import com.bjdody.cm30229.model.Percept;
import com.bjdody.cm30229.model.UltrasoundPercept;
import com.bjdody.cm30229.util.Direction;
import com.bjdody.cm30229.util.MotorController;

/**
 * Created by Ollie on 18/02/17.
 */
public class ReactiveLayer extends Layer {

    public ReactiveLayer() {
        reactionBounds.put( Direction.FORWARD, 20 );
        reactionBounds.put( Direction.LEFT, 20 );
        reactionBounds.put( Direction.RIGHT, 20 );
        reactionBounds.put( Direction.BACKWARD, 30 );
    }

    @Override
    public void onPercept( Percept percept ) {
        if ( wait <= 0 ) {
            switch ( percept.getPerceptType() ) {
                case ULTRASONIC_READING:
                    handleUltrasoundPercept( (UltrasoundPercept) percept );
                    break;
                case BUMP_SENSOR_READING:
                    handleBumpPercept( (BumpPercept) percept );
                    break;
                default:
                    throw new RuntimeException( "Unhandled percept: " + percept.getPerceptType() );

            }
        } else {
            wait -= SensorController.PERCEPT_FREQUENCY;
        }
        getParentLayer().onPercept( percept );
    }

    private void handleUltrasoundPercept( UltrasoundPercept percept ) {
        Direction direction = Direction.fromRotation( percept.getRotation() );
        if ( percept.getDistance() <= reactionBounds.get( direction ) ) {
            int avoidanceSpeed = calculateAvoidanceSpeed( direction, percept.getDistance() );
            switch ( direction ) {
                case FORWARD:
                    MotorController.left( -avoidanceSpeed );
                    MotorController.right( -avoidanceSpeed );
                    break;
                case LEFT:
                    MotorController.left( -avoidanceSpeed );
                    MotorController.right( -avoidanceSpeed );
                    break;
                    /*MotorController.right( -( avoidanceSpeed / 2) );
                    MotorController.left( avoidanceSpeed );
                    break;*/
                case RIGHT:
                    MotorController.left( -avoidanceSpeed );
                    MotorController.right( -avoidanceSpeed );
                    break;
                    /*MotorController.right( avoidanceSpeed );
                    MotorController.left( -( avoidanceSpeed / 2) );
                    break;*/
                case BACKWARD:
                    MotorController.left( avoidanceSpeed );
                    MotorController.right( avoidanceSpeed );
                    break;
            }
            setWait( SensorController.PERCEPT_FREQUENCY * 2 );
            percept.setHandled( true );
        }
    }

    private void handleBumpPercept( BumpPercept percept ) {

    }

}
