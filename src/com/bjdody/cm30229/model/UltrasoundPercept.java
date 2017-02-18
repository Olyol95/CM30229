package com.bjdody.cm30229.model;

/**
 * Created by Ollie on 18/02/17.
 */
public class UltrasoundPercept extends Percept {

    private int distance;
    private int rotation;

    public UltrasoundPercept( int distance, int rotation ) {
        super( PerceptType.ULTRASONIC_READING );
        this.distance = distance;
        this.rotation = rotation;
    }

    public int getDistance() {
        return distance;
    }

    public int getRotation() {
        return rotation;
    }

}
