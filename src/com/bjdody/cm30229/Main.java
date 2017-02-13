package com.bjdody.cm30229;

import lejos.nxt.Button;

public class Main {

    public static void main( String[] args ) {
        BotControls controls = new BotControls();
        SensorController s_controller = new SensorController(controls);
        s_controller.run();

        BotBrain brain = new BotBrain(BotBrain.BotState.Moving_Forward, controls, s_controller);
        BrainController b_controller = new BrainController(brain);
        b_controller.run();

        Button.waitForAnyPress();
        s_controller.Kill();
        b_controller.Kill();

        while (!s_controller.IsFinished() || !b_controller.IsFinished())
        {
            Utility.Wait(250);
        }

        controls.MoveForward();
    }

}
