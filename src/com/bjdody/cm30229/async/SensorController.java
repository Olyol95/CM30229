package com.bjdody.cm30229.async;

import com.bjdody.cm30229.HybridAgent;
import com.bjdody.cm30229.model.BumpPercept;
import com.bjdody.cm30229.model.UltrasoundPercept;
import com.bjdody.cm30229.util.Direction;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

/**
 * Created by Ollie on 18/02/17.
 */
public class SensorController extends Thread {

    public static final int MAX_ROTATION_ANGLE = 50,
                            PERCEPT_FREQUENCY  = 40,
                            ROTATION_STEP      = 50;

    private UltrasonicSensor ultrasonicSensor;
    private TouchSensor leftTouchSensor, rightTouchSensor;
    private RegulatedMotor sensorPlatform;

    private int platformRotation, lastRotation;
    private boolean isActive;

    public SensorController() {
        leftTouchSensor = new TouchSensor( SensorPort.S1 );
        rightTouchSensor = new TouchSensor( SensorPort.S2 );
        ultrasonicSensor = new UltrasonicSensor( SensorPort.S3 );
        ultrasonicSensor.continuous();
        sensorPlatform = Motor.A;
        sensorPlatform.setSpeed( 900 );
        platformRotation = 0;
        lastRotation = 0;
    }

    @Override
    public void run() {
        isActive = true;
        while ( isActive ) {
            if ( leftTouchSensor.isPressed() && rightTouchSensor.isPressed() ) {
                HybridAgent.instance.enqueuePercept(
                        new BumpPercept( Direction.FORWARD )
                );
            } else if ( leftTouchSensor.isPressed() ) {
                HybridAgent.instance.enqueuePercept(
                        new BumpPercept( Direction.LEFT )
                );
            } else if ( rightTouchSensor.isPressed() ) {
                HybridAgent.instance.enqueuePercept(
                        new BumpPercept( Direction.RIGHT )
                );
            }

            HybridAgent.instance.enqueuePercept(
                    new UltrasoundPercept( ultrasonicSensor.getDistance(), platformRotation )
            );

            if ( lastRotation >= 0 ) {
                if ( platformRotation + ROTATION_STEP <= MAX_ROTATION_ANGLE ) {
                    rotateSensor( ROTATION_STEP );
                } else {
                    rotateSensor( -ROTATION_STEP );
                }
            } else {
                if ( Math.abs( platformRotation - ROTATION_STEP ) <= MAX_ROTATION_ANGLE ) {
                    rotateSensor( -ROTATION_STEP );
                } else {
                    rotateSensor( ROTATION_STEP );
                }
            }
            try {
                sleep( PERCEPT_FREQUENCY );
            } catch ( InterruptedException e ) {
                //break;
            }
        }
        rotateSensor( -platformRotation );
    }

    private void rotateSensor( int degrees ) {
        platformRotation += degrees;
        sensorPlatform.rotate( degrees );
        lastRotation = degrees;
    }

    public void shutDown() {
        isActive = false;
    }

}
