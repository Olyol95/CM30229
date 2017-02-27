package com.bjdody.cm30229;

import lejos.nxt.*;
import lejos.robotics.RegulatedMotor;

/**
 * Created by Benjamin on 12/02/2017.
 */
public class BotControls {
    public RegulatedMotor LeftWheel;
    public RegulatedMotor RightWheel;

    public RegulatedMotor SensorPlatform;
    public UltrasonicSensor USSensor;

    public TouchSensor TSensor1;
    public TouchSensor TSensor2;

    public LightSensor LSensor;

    private static final int LightLevelBound = 90;
    private static final int SenseDelay = 20;
    
    private static final int MaxWheelSpeed  = 360;
    private static final int MaxSensorSpeed = 900;

    public BotControls()
    {
        LeftWheel = Motor.B;
        RightWheel = Motor.C;
        SensorPlatform = Motor.A;

        LeftWheel.setSpeed( MaxWheelSpeed );
        RightWheel.setSpeed( MaxWheelSpeed );
        SensorPlatform.setSpeed( MaxSensorSpeed );

        USSensor = new UltrasonicSensor(SensorPort.S3);
        USSensor.continuous();

        TSensor1 = new TouchSensor(SensorPort.S1);
        TSensor2 = new TouchSensor(SensorPort.S2);

        LSensor = new LightSensor(SensorPort.S4, false);
    }

    //<editor-fold desc="Movement - BotControls movement and turning of the robot">
    //Movement - BotControls movement and turning of the robot

    public void Move(float left_ratio, float right_ratio)
    {
        if (left_ratio >= 0)
        {
            LeftWheel.setSpeed((int)(MaxWheelSpeed * left_ratio));
            LeftWheel.forward();
        }
        else
        {
            LeftWheel.setSpeed((int)(MaxWheelSpeed * -left_ratio));
            LeftWheel.backward();
        }

        if (right_ratio >= 0)
        {
            RightWheel.setSpeed((int)(MaxWheelSpeed * right_ratio));
            RightWheel.forward();
        }
        else
        {
            RightWheel.setSpeed((int)(MaxWheelSpeed * right_ratio));
            RightWheel.backward();
        }
    }

    public void Stop()
    {
        LeftWheel.stop();
        RightWheel.stop();
    }

    //</editor-fold>

    private int ScanAngle = 0;

    public int ScanTo(int new_angle)
    {
        int rotation = new_angle - ScanAngle;
        SensorPlatform.rotate(rotation);
        ScanAngle = new_angle;

        int dark_light_level = LSensor.getNormalizedLightValue();
        LSensor.setFloodlight(true);
        Utility.Wait(SenseDelay);
        int bright_light_level = LSensor.getNormalizedLightValue();
        LSensor.setFloodlight(false);
        Utility.Wait(SenseDelay);

        if (dark_light_level + LightLevelBound < bright_light_level)
        {
            Sound.beepSequenceUp();
            return 1;
        }
        else
        {
            return USSensor.getDistance();
        }
    }

    public boolean DetectTouch()
    {
        return TSensor1.isPressed() || TSensor2.isPressed();
    }
}
