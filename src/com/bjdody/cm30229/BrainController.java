package com.bjdody.cm30229;

/**
 * Created by Benjamin on 13/02/2017.
 */
public class BrainController extends Thread {
    private BotBrain Brain;
    private boolean KillFlag;
    private boolean Finished;

    public BrainController(BotBrain in_brain)
    {
        Brain = in_brain;
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

    //Just keep thinking...
    public void Run(){
        while (!KillFlag)
        {
            Brain.Think();
        }
        Finished = true;
    }

}
