package com.bjdody.cm30229;

import lejos.nxt.Sound;

import java.util.HashMap;

/**
 * Created by Benjamin on 10/02/2017.
 */
public class BotBrain {

    private BotControls Controls;
    private VisibilitySensorController USSensorData;
    private TouchSensorController TouchData;

    //<editor-fold desc="State Control">
    //State Control - State Management

    public enum BotState{
        WALLSEARCH,
        AVOIDLEFT,
        AVOIDRIGHT,
        AVOIDFORWARD,
        STAYWITHLEFT,
        STAYWITHRIGHT,
        COLLISIONRECOVERY;
    }

    public enum RangeBound {
        DETECT,
        TOOFAR,
        TOOCLOSE;

        private HashMap<Direction, Integer> ranges;

        RangeBound() {
            ranges = new HashMap<>();
        }

        public Integer getRange( Direction direction ) {
            if ( ranges.containsKey( direction ) ) {
                return ranges.get( direction );
            } else {
                throw new RuntimeException(
                        "No RangeBound value specified for direction: " + direction
                );
            }
        }

        public void putRange( Direction direction, Integer range ) {
            ranges.put( direction, range );
        }
    }

    private BotState State;
    private Direction LastSideWallFound;
    private long TimeOfLastWallFind;

    public BotBrain(BotState initial_state, BotControls in_controls,
                    VisibilitySensorController in_us_sensor_data, TouchSensorController in_touch_data){
        State = initial_state;
        Controls = in_controls;
        USSensorData = in_us_sensor_data;
        TouchData = in_touch_data;

        LastSideWallFound = Direction.RIGHT;

        OnEnter();
    }

    public void SwitchState(BotState new_state)
    {
        OnExit();
        State = new_state;
        OnEnter();
    }

    //</editor-fold>

    //<editor-fold desc="Think - Called every 'frame' as an update function">

    private static final int DetectRange = 85;
    private static final int TooFarRange = 60;
    private static final int UltraSlowRange = 15;
    private static final int TooCloseSideRange = 30;
    private static final int TooCloseFrontRange = 30;
    private static final int MillTimeToGoFaster = 1000;
    private static final int MillTimeToForget = 6000;

    //Think - Called every 'frame' as an update function
    public void Think()
    {
        float left = USSensorData.GetLeftScan();
        float right = USSensorData.GetRightScan();
        float forward = USSensorData.GetFrontScan();
        boolean touch = TouchData.GetTouch();

        float move_scalar;
        if (left < UltraSlowRange || right < UltraSlowRange || forward < UltraSlowRange)
        {
            move_scalar = 0.5f;
        }
        else
        {
            move_scalar = 1.0f;
        }

        switch (State)
        {
            case WALLSEARCH:
                Controls.Move(1.0f * move_scalar, 1.0f * move_scalar);
                if (touch)
                {
                  SwitchState(BotState.COLLISIONRECOVERY);
                }
                else if (left < right)
                {
                    if (left < DetectRange)
                    {
                        SwitchState(BotState.STAYWITHLEFT);
                    }
                }
                else if (right < DetectRange)
                {
                    SwitchState(BotState.STAYWITHRIGHT);
                }
                break;
            case AVOIDRIGHT:
                Controls.Move(-0.25f * move_scalar, 1.0f * move_scalar);
                if (touch)
                {
                    SwitchState(BotState.COLLISIONRECOVERY);
                }
                else if (left < right)
                {
                    if (forward < left)
                    {
                        SwitchState(BotState.AVOIDFORWARD);
                    }
                    else
                    {
                        SwitchState(BotState.AVOIDLEFT);
                    }
                }
                else if (right > TooCloseSideRange)
                {
                    SwitchState(BotState.STAYWITHRIGHT);
                }
                break;
            case AVOIDLEFT:
                Controls.Move(1.0f * move_scalar, -0.25f * move_scalar);
                if (touch)
                {
                    SwitchState(BotState.COLLISIONRECOVERY);
                }
                else if (right < left)
                {
                    if (forward < right)
                    {
                        SwitchState(BotState.AVOIDFORWARD);
                    }
                    else
                    {
                        SwitchState(BotState.AVOIDRIGHT);
                    }
                }
                else if (left > TooCloseSideRange)
                {
                    SwitchState(BotState.STAYWITHLEFT);
                }
                break;
            case AVOIDFORWARD:
                if (touch)
                {
                    SwitchState(BotState.COLLISIONRECOVERY);
                }
                else if (forward > TooCloseFrontRange)
                {
                    if (LastSideWallFound == Direction.LEFT)
                    {
                        if (right < left && right < TooCloseSideRange)
                        {
                            SwitchState(BotState.AVOIDRIGHT);
                        }
                        else
                        {
                            SwitchState(BotState.STAYWITHLEFT);
                        }
                    }
                    else
                    {
                        if (left < right && left < TooCloseSideRange)
                        {
                            SwitchState(BotState.AVOIDLEFT);
                        }
                        else
                        {
                            SwitchState(BotState.STAYWITHRIGHT);
                        }
                    }
                }
                break;
            case STAYWITHLEFT:
                if (touch)
                {
                    SwitchState(BotState.COLLISIONRECOVERY);
                }
                else if (forward < TooCloseFrontRange)
                {
                    SwitchState(BotState.AVOIDFORWARD);
                }
                else if (right < left && right < TooCloseSideRange)
                {
                    SwitchState(BotState.AVOIDRIGHT);
                }
                else if (left < TooCloseSideRange)
                {
                    SwitchState(BotState.AVOIDLEFT);
                }
                else if (left > TooFarRange)
                {
                    long DeltaTimeOfWallFind = System.currentTimeMillis() - TimeOfLastWallFind;
                    if (DeltaTimeOfWallFind < MillTimeToForget)
                    {
                        Controls.Move(0.0f, 1.0f * move_scalar);
                    }
                    else
                    {
                        SwitchState(BotState.WALLSEARCH);
                    }
                }
                else
                {
                    Controls.Move(1.0f * move_scalar, 1.0f * move_scalar);
                    TimeOfLastWallFind = System.currentTimeMillis();
                }
                break;
            case STAYWITHRIGHT:
                if (touch)
                {
                    SwitchState(BotState.COLLISIONRECOVERY);
                }
                else if (forward < TooCloseFrontRange)
                {
                    SwitchState(BotState.AVOIDFORWARD);
                }
                else if (left < right && left < TooCloseSideRange)
                {
                    SwitchState(BotState.AVOIDLEFT);
                }
                else if (right < TooCloseSideRange)
                {
                    SwitchState(BotState.AVOIDRIGHT);
                }
                else if (right > TooFarRange)
                {
                    long DeltaTimeOfWallFind = System.currentTimeMillis() - TimeOfLastWallFind;
                    if (DeltaTimeOfWallFind < MillTimeToForget)
                    {
                        Controls.Move(1.0f * move_scalar, 0.0f);
                    }
                    else
                    {
                        SwitchState(BotState.WALLSEARCH);
                    }
                }
                else
                {

                    Controls.Move(1.0f * move_scalar, 1.0f * move_scalar);
                    TimeOfLastWallFind = System.currentTimeMillis();
                }
                break;
            case COLLISIONRECOVERY:
                if (!touch)
                {
                    SwitchState(BotState.WALLSEARCH);
                }
                break;
        }
    }

    //</editor-fold>

    //<editor-fold desc="OnEnter - Called when first entering a new state">
    //OnEnter - Called when first entering a new state
    public void OnEnter()
    {
        System.out.println( State );

        switch(State)
        {
            case WALLSEARCH:
                USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Forward);
                break;
            case AVOIDLEFT:
                USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Left);
                if (LastSideWallFound == Direction.RIGHT)
                {
                    Sound.beepSequenceUp();
                }
                LastSideWallFound = Direction.LEFT;
                break;
            case AVOIDRIGHT:
                USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Right);
                if (LastSideWallFound == Direction.LEFT)
                {
                    Sound.beepSequenceUp();
                }
                LastSideWallFound = Direction.RIGHT;
                break;
            case AVOIDFORWARD:
                USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Forward);
                int left = USSensorData.GetLeftScan();
                int right = USSensorData.GetRightScan();

                if (left <= right && left < TooCloseSideRange)
                {
                    Controls.Move(0.5f, -0.5f);
                }
                else if (right < left && right < TooCloseSideRange)
                {
                    Controls.Move(-0.5f, 0.5f);
                }
                else if (LastSideWallFound == Direction.LEFT)
                {
                    Controls.Move(0.5f, -0.5f);
                }
                else
                {
                    Controls.Move(-0.5f, 0.5f);
                }
                break;
            case STAYWITHLEFT:
                USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Left);
                Controls.Move(1.0f, 1.0f);
                if (LastSideWallFound == Direction.RIGHT)
                {
                    Sound.beepSequenceUp();
                }
                LastSideWallFound = Direction.LEFT;
                TimeOfLastWallFind = System.currentTimeMillis();
                break;
            case STAYWITHRIGHT:
                USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Right);
                Controls.Move(1.0f, 1.0f);
                if (LastSideWallFound == Direction.LEFT)
                {
                    Sound.beepSequenceUp();
                }
                LastSideWallFound = Direction.RIGHT;
                TimeOfLastWallFind = System.currentTimeMillis();
                break;
            case COLLISIONRECOVERY:
                Controls.Move(-1.0f, -1.0f);
                break;
            default: {

            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="OnExit - Called when exiting a state to clean up anything needed">
    //OnExit - Called when exiting a state to clean up anything needed
    public void OnExit()
    {

    }

    private void OnExit_Moving_Forward()
    {
        //TODO
    }

    private void OnExit_Turning_Left_Gradual()
    {
        //TODO
    }

    private void OnExit_Turning_Right_Gradual()
    {
        //TODO
    }

    private void OnExit_Collision_Recovery()
    {
        //TODO
    }
    //</editor-fold>
}
