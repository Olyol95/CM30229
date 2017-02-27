package com.bjdody.cm30229;

/**
 * Created by Benjamin on 13/02/2017.
 */
public class VisibilitySensorController extends Thread{
    private BotControls Controls;
    private boolean KillFlag;
    private boolean Finished;

    public enum  ScanMode
    {
        Forward,
        Left,
        Right;
    }
    private ScanMode Mode = ScanMode.Forward;

    private int left_scan = 255;
    private int right_scan = 255;
    private int front_scan = 255;
    private int back_scan = 255;


    public VisibilitySensorController(BotControls in_controls)
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

    public void SetScanMode(ScanMode in_mode)
    {
        Mode = in_mode;
    }

    private static final int Scanability = 45;

    @Override
    public void run()
    {
        while (!KillFlag)
        {
            switch (Mode)
            {
                case Forward:
                {
                    left_scan = Scan(-Scanability);
                    front_scan = Scan(0);
                    right_scan =  Scan(Scanability);
                    front_scan = Scan(0);
                    break;
                }
                case Left:
                {
                    left_scan = Scan(-Scanability);
                    front_scan = Scan(0);
                    left_scan = Scan(-Scanability);
                    front_scan = Scan(0);
                    left_scan = Scan(-Scanability);
                    front_scan = Scan(0);
                    left_scan = Scan(-Scanability);
                    front_scan = Scan(0);
                    right_scan = Scan(Scanability);
                    break;
                }
                case Right:
                {
                    right_scan = Scan(Scanability);
                    front_scan = Scan(0);
                    right_scan = Scan(Scanability);
                    front_scan = Scan(0);
                    right_scan = Scan(Scanability);
                    front_scan = Scan(0);
                    right_scan = Scan(Scanability);
                    front_scan = Scan(0);
                    left_scan = Scan(-Scanability);
                    break;
                }
                default:
                {
                    System.out.println("Unexpected ScanMode in VisibilitySensorController:run()");
                    break;
                }
            }
        }
        Finished = true;
    }

    private int Scan(int angle)
    {
        return Controls.ScanTo(angle);
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
