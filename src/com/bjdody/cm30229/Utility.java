package com.bjdody.cm30229;

/**
 * Created by Benjamin on 13/02/2017.
 */
public class Utility {
    public static void Wait(int time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {}
    }
}
