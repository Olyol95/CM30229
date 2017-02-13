package com.bjdody.cm30229;

/**
 * Created by Benjamin on 13/02/2017.
 */
public class SensorController extends Thread{
    private BotControls Controls;
    private boolean KillFlag;
    private boolean Finished;

    private int left_scan = 255;
    private int right_scan = 255;
    private int front_scan = 255;
    private int back_scan = 255;

    public SensorController(BotControls in_controls)
    {
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

    public void Run()
    {
        while (!KillFlag)
        {
            left_scan = Controls.ScanLeft();
            front_scan = Controls.ScanForward();
            right_scan = Controls.ScanRight();
            back_scan = Controls.ScanBackward();
        }
        Finished = true;
    }

    public int GetLeftScan()
    {
        return  left_scan;
    }

    public int GetRightScan()
    {
        return  right_scan;
    }

    public int GetFrontScan()
    {
        return  front_scan;
    }

    public int GetBackScan()
    {
        return  back_scan;
    }
}
