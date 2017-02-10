package com.bjdody.cm30229;

/**
 * Created by Benjamin on 10/02/2017.
 */

public class DirectionUtillity {
    public static Direction InverseDirection(Direction in_dir)
    {
        return new Direction(in_dir.getValue() * -1);
    }
}
