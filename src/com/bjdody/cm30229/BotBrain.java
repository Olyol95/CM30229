package com.bjdody.cm30229;

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
        TOOCLOSE,
        ULTRACLOSE;

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

    private static final int MillTimeToForget = 6000;

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

        for (Direction direction : Direction.values())
        {
            RangeBound.DETECT.putRange(direction, 65);
            RangeBound.TOOFAR.putRange(direction, 35);
            RangeBound.ULTRACLOSE.putRange(direction, 15);
        }

        RangeBound.TOOCLOSE.putRange(Direction.FORWARD, 30);
        RangeBound.TOOCLOSE.putRange(Direction.LEFT, 27);
        RangeBound.TOOCLOSE.putRange(Direction.RIGHT, 27);
        RangeBound.TOOCLOSE.putRange(Direction.BACKWARD, 40);

        OnEnter();
    }

    public void SwitchState(BotState new_state)
    {
        State = new_state;
        OnEnter();
    }

    //</editor-fold>

    //<editor-fold desc="Think - Called every 'frame' as an update function">

    //Think - Called every 'frame' as an update function
    public void Think()
    {
        switch (State)
        {
            case WALLSEARCH:
                Think_WallSearch();
                break;
            case AVOIDRIGHT:
                Think_AvoidRight();
                break;
            case AVOIDLEFT:
                Think_AvoidLeft();
                break;
            case AVOIDFORWARD:
                Think_AvoidForward();
                break;
            case STAYWITHLEFT:
                Think_StayWithLeft();
                break;
            case STAYWITHRIGHT:
                Think_StayWithRight();
                break;
            case COLLISIONRECOVERY:
                Think_CollisionRecovery();
                break;
            default:
                System.out.println("Unexpected state in BotBrain:Think()");
                break;
        }
    }

    private void Think_WallSearch()
    {
        float left = USSensorData.GetLeftScan();
        float right = USSensorData.GetRightScan();
        float move_scalar = GetMovementScalar();

        Controls.Move(1.0f * move_scalar, 1.0f * move_scalar);
        if (InTightSpot())
        {
            SwitchState(BotState.COLLISIONRECOVERY);
        }
        else if (left < right)
        {
            if (left < RangeBound.DETECT.getRange(Direction.LEFT))
            {
                SwitchState(BotState.STAYWITHLEFT);
            }
        }
        else if (right < RangeBound.DETECT.getRange(Direction.RIGHT))
        {
            SwitchState(BotState.STAYWITHRIGHT);
        }
    }

    private void Think_AvoidForward()
    {
        float forward = USSensorData.GetFrontScan();

        if (InTightSpot())
        {
            SwitchState(BotState.COLLISIONRECOVERY);
        }
        else if (forward > RangeBound.TOOCLOSE.getRange(Direction.FORWARD))
        {
            if (LastSideWallFound == Direction.LEFT)
            {
                SwitchState(BotState.STAYWITHLEFT);
            }
            else
            {
                SwitchState(BotState.STAYWITHRIGHT);
            }
        }
    }

    private void Think_AvoidLeft()
    {
        float left = USSensorData.GetLeftScan();
        float forward = USSensorData.GetFrontScan();
        float move_scalar = GetMovementScalar();

        if (InTightSpot())
        {
            SwitchState(BotState.COLLISIONRECOVERY);
        }
        else if (forward < left)
        {
            SwitchState(BotState.AVOIDFORWARD);
        }
        else if (left > RangeBound.TOOCLOSE.getRange(Direction.LEFT))
        {
            SwitchState(BotState.STAYWITHLEFT);
        }
        else if (left < RangeBound.ULTRACLOSE.getRange(Direction.LEFT))
        {
            Controls.Move(1.0f * move_scalar, -0.25f * move_scalar);
        }
        else
        {
            Controls.Move(1.0f * move_scalar, 0.25f * move_scalar);
        }
    }

    private void Think_AvoidRight()
    {
        float right = USSensorData.GetRightScan();
        float forward = USSensorData.GetFrontScan();
        float move_scalar = GetMovementScalar();

        if (InTightSpot())
        {
            SwitchState(BotState.COLLISIONRECOVERY);
        }
        else if (forward < right)
        {
            SwitchState(BotState.AVOIDFORWARD);
        }
        else if (right > RangeBound.TOOCLOSE.getRange(Direction.RIGHT))
        {
            SwitchState(BotState.STAYWITHRIGHT);
        }
        else if (right < RangeBound.ULTRACLOSE.getRange(Direction.RIGHT))
        {
            Controls.Move(-0.25f * move_scalar, 1.0f * move_scalar);
        }
        else
        {
            Controls.Move(0.25f * move_scalar, 1.0f * move_scalar);
        }
    }

    private void Think_StayWithLeft()
    {
        float left = USSensorData.GetLeftScan();
        float forward = USSensorData.GetFrontScan();
        float move_scalar = GetMovementScalar();

        if (InTightSpot())
        {
            SwitchState(BotState.COLLISIONRECOVERY);
        }
        else if (forward < RangeBound.TOOCLOSE.getRange(Direction.FORWARD))
        {
            SwitchState(BotState.AVOIDFORWARD);
        }
        else if (left < RangeBound.TOOCLOSE.getRange(Direction.LEFT))
        {
            SwitchState(BotState.AVOIDLEFT);
        }
        else if (left > RangeBound.TOOFAR.getRange(Direction.LEFT))
        {
            long DeltaTimeOfWallFind = System.currentTimeMillis() - TimeOfLastWallFind;
            if (DeltaTimeOfWallFind < MillTimeToForget)
            {
                Controls.Move(0.5f * move_scalar, 1.0f * move_scalar);
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
    }

    private void Think_StayWithRight()
    {
        float right = USSensorData.GetRightScan();
        float forward = USSensorData.GetFrontScan();
        float move_scalar = GetMovementScalar();

        if (InTightSpot())
        {
            SwitchState(BotState.COLLISIONRECOVERY);
        }
        else if (forward < RangeBound.TOOCLOSE.getRange(Direction.FORWARD))
        {
            SwitchState(BotState.AVOIDFORWARD);
        }
        else if (right < RangeBound.TOOCLOSE.getRange(Direction.RIGHT))
        {
            SwitchState(BotState.AVOIDRIGHT);
        }
        else if (right > RangeBound.TOOFAR.getRange(Direction.RIGHT))
        {
            long DeltaTimeOfWallFind = System.currentTimeMillis() - TimeOfLastWallFind;
            if (DeltaTimeOfWallFind < MillTimeToForget)
            {
                Controls.Move(1.0f * move_scalar, 0.5f * move_scalar);
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
    }

    private void Think_CollisionRecovery()
    {
        if (!InTightSpot())
        {
            SwitchState(BotState.WALLSEARCH);
        }
    }

    private float GetMovementScalar()
    {
        float left = USSensorData.GetLeftScan();
        float right = USSensorData.GetRightScan();
        float forward = USSensorData.GetFrontScan();

        if (left < RangeBound.ULTRACLOSE.getRange(Direction.LEFT)
                || right < RangeBound.ULTRACLOSE.getRange(Direction.RIGHT)
                || forward < RangeBound.ULTRACLOSE.getRange(Direction.FORWARD))
        {
            return 0.5f;
        }
        else
        {
            return 1.0f;
        }
    }

    private boolean InTightSpot()
    {
        float left = USSensorData.GetLeftScan();
        float right = USSensorData.GetRightScan();
        float forward = USSensorData.GetFrontScan();
        boolean touch = TouchData.GetTouch();

        return touch ||
                (left < RangeBound.ULTRACLOSE.getRange(Direction.LEFT)
                && right < RangeBound.ULTRACLOSE.getRange(Direction.RIGHT)
                && forward < RangeBound.ULTRACLOSE.getRange(Direction.FORWARD));
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
                OnEnter_WallSearch();
                break;
            case AVOIDLEFT:
                OnEnter_AvoidLeft();
                break;
            case AVOIDRIGHT:
                OnEnter_AvoidRight();
                break;
            case AVOIDFORWARD:
                OnEnter_AvoidForward();
                break;
            case STAYWITHLEFT:
                OnEnter_StayWithLeft();
                break;
            case STAYWITHRIGHT:
                OnEnter_StayWithRight();
                break;
            case COLLISIONRECOVERY:
                OnEnter_CollisionRecovery();
                break;
            default:
                System.out.println("Unexpected state in BotBrain:OnEnter()");
                break;
        }
    }

    private void OnEnter_WallSearch()
    {
        USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Forward);
    }

    private void OnEnter_AvoidForward()
    {
        USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Forward);
        int left = USSensorData.GetLeftScan();
        int right = USSensorData.GetRightScan();

        if (left <= right && left < RangeBound.TOOCLOSE.getRange(Direction.LEFT))
        {
            Controls.Move(0.5f, -0.5f);
        }
        else if (right < left && right < RangeBound.TOOCLOSE.getRange(Direction.RIGHT))
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
    }

    private void OnEnter_AvoidLeft()
    {
        USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Left);
        LastSideWallFound = Direction.LEFT;
    }

    private void OnEnter_AvoidRight()
    {
        USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Right);
        LastSideWallFound = Direction.RIGHT;
    }

    private void OnEnter_StayWithLeft()
    {
        USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Left);
        Controls.Move(1.0f, 1.0f);
        LastSideWallFound = Direction.LEFT;
        TimeOfLastWallFind = System.currentTimeMillis();
    }

    private void OnEnter_StayWithRight()
    {
        USSensorData.SetScanMode(VisibilitySensorController.ScanMode.Right);
        Controls.Move(1.0f, 1.0f);
        LastSideWallFound = Direction.RIGHT;
        TimeOfLastWallFind = System.currentTimeMillis();
    }

    private void OnEnter_CollisionRecovery()
    {
        Controls.Move(-1.0f, -1.0f);
    }
    //</editor-fold>
}
