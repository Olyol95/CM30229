package com.bjdody.cm30229;

import com.bjdody.cm30229.async.SensorController;
import com.bjdody.cm30229.async.SoundController;
import com.bjdody.cm30229.layers.Layer;
import com.bjdody.cm30229.layers.PlanningLayer;
import com.bjdody.cm30229.layers.ReactiveLayer;
import com.bjdody.cm30229.util.MotorController;
import lejos.nxt.Button;

/**
 * Created by Ollie on 18/02/17.
 */
public class HybridAgent {

    public static HybridAgent instance;

    private Layer rootLayer;
    private SensorController sensorController;

    public HybridAgent() {
        instance = this;

        rootLayer = new ReactiveLayer();
        rootLayer.setParentLayer( new PlanningLayer() );

        sensorController = new SensorController();
        sensorController.start();
    }

    public Layer getRootLayer() {
        return rootLayer;
    }

    public void onExit() {
        sensorController.shutDown();
        try {
            sensorController.join();
        } catch ( InterruptedException e ) {
            System.err.println( "Join on sensor thread failed" );
        }
        MotorController.stop();
    }

    public static void main( String[] args ) {
        new HybridAgent();

        Button.waitForAnyPress();
        instance.onExit();
        Button.waitForAnyPress();
    }

}
