package com.bjdody.cm30229.async;

import com.bjdody.cm30229.HybridAgent;
import com.bjdody.cm30229.model.UltrasoundPercept;
import com.bjdody.cm30229.util.Direction;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
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
    private RegulatedMotor sensorPlatform;

    private int platformRotation, lastRotation;
    private boolean isActive;

    public SensorController() {
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