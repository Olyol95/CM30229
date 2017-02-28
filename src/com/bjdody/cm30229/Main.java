package com.bjdody.cm30229;

import com.bjdody.cm30229.Controllers.BrainController;
import com.bjdody.cm30229.Controllers.TouchSensorController;
import com.bjdody.cm30229.Controllers.VisibilitySensorController;
import lejos.nxt.Button;

public class Main {

    public static void main( String[] args ) {
        BotControls controls = new BotControls();

        VisibilitySensorController vs_controller = new VisibilitySensorController(controls);
        vs_controller.start();

        TouchSensorController touch_controller = new TouchSensorController(controls);
        touch_controller.start();

        BotBrain brain = new BotBrain(BotBrain.BotState.WALLSEARCH, controls, vs_controller, touch_controller);
        BrainController b_controller = new BrainController(brain);
        b_controller.start();

        Button.waitForAnyPress();
        vs_controller.Kill();
        b_controller.Kill();
        touch_controller.Kill();

        while (!vs_controller.IsFinished() || !b_controller.IsFinished() ||
                !touch_controller.IsFinished())
        {
            Utility.Wait(100);
        }

        controls.ScanTo(0);
        controls.Stop();
        Button.waitForAnyPress();
    }

}
