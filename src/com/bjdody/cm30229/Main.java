package com.bjdody.cm30229;

import lejos.nxt.Button;

public class Main {

    public static void main( String[] args ) {
        Controls controls = new Controls();
        BotBrain brain = new BotBrain(BotBrain.BotState.Moving_Forward, controls);

        while(Button.ENTER.isUp())
        {
            brain.Think();
        }

        controls.ScanForward();
    }

}
