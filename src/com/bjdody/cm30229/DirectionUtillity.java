package com.bjdody.cm30229;

/**
 * Created by Benjamin on 10/02/2017.
 */

public class DirectionUtillity {
    public static Direction InverseDirection(Direction in_dir)
    {
        switch (in_dir)
        {
            case Forward:
                return Direction.Backward;
            case Right:
                return Direction.Left;
            case Left:
                return  Direction.Right;
            case Backward:
                return Direction.Forward;
            default:
                System.out.println("Unexpected direction in DirectionUtillity:InverseDirection");
                return Direction.Forward;
        }
    }
}
