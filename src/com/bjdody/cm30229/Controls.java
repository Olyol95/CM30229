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

    private Direction CurrentDirection = Direction.FORWARD;

    public int ScanLeft()
    {
        switch (CurrentDirection)
        {
            case FORWARD:
            {
                Rotate(-90);
                break;
            }
            case LEFT:
            {
                //Do nothing
                break;
            }
            case RIGHT:
            {
                Rotate(-180);
                break;
            }
            case BACKWARD:
            {
                Rotate(-270);
                break;
            }
            default: System.out.println("Unexpected direction in Controls:ScanLeft");
        }

        CurrentDirection = Direction.LEFT;
        return Sensor.getDistance();
    }

    public int ScanRight()
    {
        switch (CurrentDirection)
        {
            case FORWARD:
            {
                Rotate(90);
                break;
            }
            case LEFT:
            {
                Rotate(180);
                break;
            }
            case RIGHT:
            {
                //Do nothing
                break;
            }
            case BACKWARD:
            {
                Rotate(-90);
                break;
            }
            default: System.out.println("Unexpected direction in Controls:ScanRight");
        }

        CurrentDirection = Direction.RIGHT;
        return Sensor.getDistance();
    }

    public int ScanForward()
    {
        switch (CurrentDirection)
        {
            case FORWARD:
            {
                //Do nothing
                break;
            }
            case LEFT:
            {
                Rotate(90);
                break;
            }
            case RIGHT:
            {
                Rotate(-90);
                break;
            }
            case BACKWARD:
            {
                Rotate(-180);
                break;
            }
            default: System.out.println("Unexpected direction in Controls:ScanForward");
        }

        CurrentDirection = Direction.FORWARD;
        return Sensor.getDistance();
    }

    public int ScanBackward()
    {
        switch (CurrentDirection)
        {
            case FORWARD:
            {
                Rotate(180);
                break;
            }
            case LEFT:
            {
                Rotate(270);
                break;
            }
            case RIGHT:
            {
                Rotate(90);
                break;
            }
            case BACKWARD:
            {
                //Do nothing
                break;
            }
            default: System.out.println("Unexpected direction in Controls:ScanBackward");
        }

        CurrentDirection = Direction.BACKWARD;
        return Sensor.getDistance();
    }

    private void Rotate(int rotation)
    {
        SensorPlatform.rotate(rotation);
    }
}
