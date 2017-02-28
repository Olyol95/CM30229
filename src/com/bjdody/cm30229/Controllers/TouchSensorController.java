package com.bjdody.cm30229.Controllers;

import com.bjdody.cm30229.BotControls;
import com.bjdody.cm30229.Utility;

/**
 * Created by Benjamin on 20/02/2017.
 */
public class TouchSensorController extends Thread{
    private BotControls Controls;
    private boolean KillFlag;
    private boolean Finished;

    private static final int MillTimeToForgetTouch = 500;
    private long TimeOfLastTouch;

    public TouchSensorController(BotControls in_controls)
    {
        TimeOfLastTouch = 0l;
        Controls = in_controls;
        KillFlag = false;
        Finished = false;
    }

    public void Kill()
    {
        KillFlag = true;
    }

    public boolean IsFinished()
    {
        return Finished;
    }

    @Override
    public void run()
    {
        while (!KillFlag)
        {
            if (Controls.DetectTouch())
            {
                TimeOfLastTouch = System.currentTimeMillis();
                Utility.Wait(5);
            }
        }
        Finished = true;
    }

    public boolean GetTouch()
    {
        return System.currentTimeMillis() < (TimeOfLastTouch + MillTimeToForgetTouch);
    }
}
