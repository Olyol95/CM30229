package com.bjdody.cm30229;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

/**
 * Created by Benjamin on 12/02/2017.
 */
public class Controls {
    public RegulatedMotor LeftWheel;
    public RegulatedMotor RightWheel;

    public RegulatedMotor SensorPlatform;
    public UltrasonicSensor Sensor;

    public Controls()
    {
        LeftWheel = Motor.C;
        RightWheel = Motor.B;
        SensorPlatform = Motor.A;

        Sensor = new UltrasonicSensor(SensorPort.S3);
        Sensor.continuous();
    }

    //<editor-fold desc="Movement - Controls movement and turning of the robot">
    //Movement - Controls movement and turning of the robot

    public void MoveForward()
    {
        LeftWheel.forward();
        RightWheel.forward();
    }

    public void MoveBackward()
    {
        LeftWheel.backward();
        RightWheel.backward();
    }

    public void MoveLeftGradual()
    {
        LeftWheel.forward();
        RightWheel.stop();
    }

    public void MoveRightGradual()
    {
        LeftWheel.stop();
        RightWheel.forward();
    }

    public void MoveLeftUrgent()
    {
        LeftWheel.forward();
        RightWheel.backward();
    }

    public void MoveRightUrgent()
    {
        LeftWheel.backward();
        RightWheel.forward();
    }

    public void Stop()
    {
        LeftWheel.stop();
        RightWheel.stop();
    }

    //</editor-fold>

    private enum RotationDirection
    {
        Left
        ,Right
    }

    private Direction CurrentDirection = Direction.Forward;

    public int ScanLeft()
    {
        switch (CurrentDirection)
        {
            case Forward:
            {
                Rotate(-90);
                break;
            }
            case Left:
            {
                //Do nothing
                break;
            }
            case Right:
            {
                Rotate(-180);
                break;
            }
            case Backward:
            {
                Rotate(-270);
                break;
            }
            default: System.out.println("Unexpected direction in Controls:ScanLeft");
        }

        CurrentDirection = Direction.Left;
        return Sensor.getDistance();
    }

    public int ScanRight()
    {
        switch (CurrentDirection)
        {
            case Forward:
            {
                Rotate(90);
                break;
            }
            case Left:
            {
                Rotate(180);
                break;
            }
            case Right:
            {
                //Do nothing
                break;
            }
            case Backward:
            {
                Rotate(-90);
                break;
            }
            default: System.out.println("Unexpected direction in Controls:ScanRight");
        }

        CurrentDirection = Direction.Right;
        return Sensor.getDistance();
    }

    public int ScanForward()
    {
        switch (CurrentDirection)
        {
            case Forward:
            {
                //Do nothing
                break;
            }
            case Left:
            {
                Rotate(90);
                break;
            }
            case Right:
            {
                Rotate(-90);
                break;
            }
            case Backward:
            {
                Rotate(-180);
                break;
            }
            default: System.out.println("Unexpected direction in Controls:ScanForward");
        }

        CurrentDirection = Direction.Forward;
        return Sensor.getDistance();
    }

    public int ScanBackward()
    {
        switch (CurrentDirection)
        {
            case Forward:
            {
                Rotate(180);
                break;
            }
            case Left:
            {
                Rotate(270);
                break;
            }
            case Right:
            {
                Rotate(90);
                break;
            }
            case Backward:
            {
                //Do nothing
                break;
            }
            default: System.out.println("Unexpected direction in Controls:ScanBackward");
        }

        CurrentDirection = Direction.Backward;
        return Sensor.getDistance();
    }

    private void Rotate(int rotation)
    {
        SensorPlatform.rotate(rotation);
    }
}
