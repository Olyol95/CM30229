package com.bjdody.cm30229.model;

/**
 * Created by Ollie on 18/02/17.
 */
public abstract class Percept {

    public enum PerceptType {
        ULTRASONIC_READING,
        BUMP_SENSOR_READING,
    }

    private PerceptType perceptType;
    private boolean isHandled;

    public Percept( PerceptType perceptType ) {
        this.perceptType = perceptType;
        isHandled = false;
    }

    public PerceptType getPerceptType() {
        return perceptType;
    }

    public void setHandled( boolean isHandled ) {
        this.isHandled = isHandled;
    }

    public boolean isHandled() {
        return isHandled;
    }

}
