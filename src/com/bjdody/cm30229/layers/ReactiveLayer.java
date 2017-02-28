package com.bjdody.cm30229.layers;

import com.bjdody.cm30229.async.SensorController;
import com.bjdody.cm30229.model.BumpPercept;
import com.bjdody.cm30229.model.Percept;
import com.bjdody.cm30229.model.UltrasoundPercept;
import com.bjdody.cm30229.util.Direction;
import com.bjdody.cm30229.util.MotorController;
import lejos.nxt.Sound;

/**
 * Created by Ollie on 18/02/17.
 */
public class ReactiveLayer extends Layer {

    public ReactiveLayer() {
        reactionBounds.put( Direction.FORWARD, 28 );
        reactionBounds.put( Direction.LEFT, 23 );
        reactionBounds.put( Direction.RIGHT, 23 );
        reactionBounds.put( Direction.BACKWARD, 35 );
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
            Sound.playTone( 500, 300 );
            int avoidanceSpeed = calculateAvoidanceSpeed( direction, percept.getDistance() );
            switch ( direction ) {
                case FORWARD:
                    MotorController.left( -avoidanceSpeed );
                    MotorController.right( -avoidanceSpeed );
                    break;
                case LEFT:
                    //MotorController.left( -avoidanceSpeed );
                    //MotorController.right( -avoidanceSpeed );
                    /*try {
                        Thread.sleep( SensorController.PERCEPT_FREQUENCY );
                    } catch ( InterruptedException e ) {
                        //do nothing
                    }*/
                    MotorController.right( -avoidanceSpeed / 2 );
                    MotorController.left( avoidanceSpeed / 2 );
                    break;
                case RIGHT:
                    //MotorController.left( -avoidanceSpeed );
                    //MotorController.right( -avoidanceSpeed );
                    /*try {
                        Thread.sleep( SensorController.PERCEPT_FREQUENCY );
                    } catch ( InterruptedException e ) {
                        //do nothing
                    }*/
                    MotorController.right( avoidanceSpeed / 2 );
                    MotorController.left( -avoidanceSpeed / 2 );
                    break;
                case BACKWARD:
                    MotorController.left( avoidanceSpeed );
                    MotorController.right( avoidanceSpeed );
                    break;
            }
            getParentLayer().setWait( SensorController.PERCEPT_FREQUENCY * 2 );
            percept.setHandled( true );
        }
    }

    private void handleBumpPercept( BumpPercept percept ) {
        int avoidanceSpeed = calculateAvoidanceSpeed( percept.getDirection(), 0 );
        switch ( percept.getDirection() ) {
            case LEFT:
            case RIGHT:
            case FORWARD:
                MotorController.left( -avoidanceSpeed );
                MotorController.right( -avoidanceSpeed );
                break;
        }
        setWait( SensorController.PERCEPT_FREQUENCY * 4 );
        percept.setHandled( true );
    }

    @Override
    protected void setWait( int ms ) {
        super.setWait( ms );
        getParentLayer().setWait( ms );
    }
}
