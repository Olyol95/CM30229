package com.bjdody.cm30229.util;

import com.bjdody.cm30229.async.SensorController;

/**
 *  @author bjd, ody
 *
 *  Class representing a direction of travel
 */
public enum Direction {

    FORWARD  ( 0 ),
    RIGHT    ( 1 ),
    BACKWARD ( 2 ),
    LEFT     ( 3 );

    private int value;

    Direction( int value ) {
        this.value = value;
    }

    public Direction invert() {
        if ( values().length % 2 != 0 ) {
            throw new RuntimeException(
                    "Failed to invert direction: odd number of values"
            );
        }
        return getDirection(
                getValue() + ( values().length / 2 )
        );
    }

    public Direction clockwise() {
        return clockwise( 1 );
    }

    public Direction clockwise( int step ) {
        return getDirection(
                getValue() + step
        );
    }

    public Direction anticlockwise() {
        return anticlockwise( 1 );
    }

    public Direction anticlockwise( int step ) {
        step %= values().length;
        return getDirection(
                getValue() + values().length - step
        );
    }

    private Direction getDirection( int value ) throws RuntimeException {
        if ( value < 0 ) {
            throw new RuntimeException( "Unknown direction value: " + value );
        }
        value %= values().length;
        for ( Direction direction : values() ) {
            if ( direction.getValue() == value ) {
                return direction;
            }
        }
        throw new RuntimeException( "Unknown direction value: " + value );
    }

    private int getValue() {
        return value;
    }

    public static Direction fromRotation( int degrees ) {
        degrees %= 360;
        if ( Math.abs( degrees ) > SensorController.MAX_ROTATION_ANGLE ) {
            return Direction.BACKWARD;
        } else {
            double discriminationAngle = SensorController.MAX_ROTATION_ANGLE * ( 1.0 / 3.0 );
            if ( degrees < -discriminationAngle ) {
                return Direction.LEFT;
            } else if ( degrees > discriminationAngle ) {
                return Direction.RIGHT;
            } else {
                return Direction.FORWARD;
            }
        }
    }

}