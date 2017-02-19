package com.bjdody.cm30229.util;

import lejos.nxt.Motor;
import lejos.robotics.RegulatedMotor;

/**
 * Created by Ollie on 18/02/17.
 */
public class MotorController {

    public static final int MAX_SPEED = 360;

    private static RegulatedMotor leftWheel = Motor.B;
    private static RegulatedMotor rightWheel = Motor.C;

    public static void stop() {
        leftWheel.stop();
        rightWheel.stop();
    }

    public static void left( int speed ) {
        alterSpeed( leftWheel, speed );
    }

    public static void right( int speed ) {
        alterSpeed( rightWheel, speed );
    }

    public static void leftInc( int increment ) {
        alterSpeed( leftWheel, leftWheel.getSpeed() + increment );
    }

    public static void rightInc( int increment ) {
        alterSpeed( rightWheel, rightWheel.getSpeed() + increment );
    }

    private static void alterSpeed( RegulatedMotor motor, int speed ) {
        motor.setSpeed( Math.max( Math.abs( speed ), MAX_SPEED ) );
        if ( speed < 0 ) {
            motor.backward();
        } else if ( speed > 0 ) {
            motor.forward();
        } else {
            motor.stop();
        }
    }

}
