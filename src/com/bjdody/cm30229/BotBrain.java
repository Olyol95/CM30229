package com.bjdody.cm30229;

import lejos.nxt.Sound;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Benjamin on 10/02/2017.
 */
public class BotBrain {

    private BotControls Controls;
    private SensorController SensorData;

    //<editor-fold desc="State Control">
    //State Control - State Management

    public enum BotState{
        Moving_Forward
        ,Turning_Left_Gradual
        ,Turning_Right_Gradual
        ,Turning_Left_Emergency
        ,Turning_Right_Emergency
        ,Collision_Recovery
    }

    public enum RangeBound {
        NORMAL,
        URGENT;

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
    private Direction lastTurnDirection = Direction.LEFT;

    public BotBrain(BotState initial_state, BotControls in_controls, SensorController in_sensor_data){
        State = initial_state;
        Controls = in_controls;
        SensorData = in_sensor_data;

        for ( Direction direction : Direction.values() ) {
            RangeBound.NORMAL.putRange( direction, 35 );
            RangeBound.URGENT.putRange( direction, 25 );
        }
        RangeBound.NORMAL.putRange( Direction.LEFT, 30 );
        RangeBound.URGENT.putRange( Direction.LEFT, 23 );
        RangeBound.NORMAL.putRange( Direction.RIGHT, 30 );
        RangeBound.URGENT.putRange( Direction.RIGHT, 23 );

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
    //Think - Called every 'frame' as an update function
    public void Think()
    {
        switch(State)
        {
            case Moving_Forward:
                Think_Moving_Forward();
                break;
            case Turning_Left_Gradual:
                Think_Turning_Left_Gradual();
                break;
            case Turning_Right_Gradual:
                Think_Turning_Right_Gradual();
                break;
            case Turning_Left_Emergency:
                Think_Turning_Left_Emergency();
                break;
            case Turning_Right_Emergency:
                Think_Turning_Right_Emergency();
                break;
            case Collision_Recovery:
                Think_Collision_Recovery();
                break;
            default:
                System.out.println("Unexpected State encountered in BotBrain::Think()");
        }
    }

    private void Think_Moving_Forward()
    {
        int forwardRange = SensorData.GetFrontScan();
        int rightRange   = SensorData.GetRightScan();
        int leftRange    = SensorData.GetLeftScan();

        if ( rightRange <= RangeBound.NORMAL.getRange( Direction.RIGHT ) ) {
            lastTurnDirection = Direction.LEFT;
            SwitchState( BotState.Turning_Left_Gradual );
        }
        else if ( leftRange <= RangeBound.NORMAL.getRange( Direction.LEFT ) ) {
            lastTurnDirection = Direction.RIGHT;
            SwitchState( BotState.Turning_Right_Gradual );
        }
        else if ( forwardRange <= RangeBound.NORMAL.getRange( Direction.FORWARD ) ) {
            switch ( lastTurnDirection ) {
                case LEFT:
                    SwitchState( BotState.Turning_Left_Gradual );
                    break;
                case RIGHT:
                    SwitchState( BotState.Turning_Right_Gradual );
                    break;
                default:
                    throw new RuntimeException(
                            "Forward range bound met but the direction of last turn is unknown!"
                    );
            }
        }
    }

    private void Think_Turning_Left_Gradual()
    {
        int forwardRange = SensorData.GetFrontScan();
        int rightRange   = SensorData.GetRightScan();

        if ( forwardRange >= RangeBound.NORMAL.getRange( Direction.FORWARD ) &&
                rightRange > RangeBound.NORMAL.getRange( Direction.RIGHT ) ) {
            SwitchState( BotState.Moving_Forward );
        } else if ( rightRange <= RangeBound.URGENT.getRange( Direction.RIGHT ) ||
                    forwardRange <= RangeBound.URGENT.getRange( Direction.FORWARD ) ) {
            SwitchState( BotState.Turning_Left_Emergency );
        }
    }

    private void Think_Turning_Right_Gradual()
    {
        int forwardRange = SensorData.GetFrontScan();
        int leftRange    = SensorData.GetLeftScan();

        if ( forwardRange >= RangeBound.NORMAL.getRange( Direction.FORWARD ) &&
                leftRange > RangeBound.NORMAL.getRange( Direction.LEFT ) ) {
            SwitchState( BotState.Moving_Forward );
        } else if ( leftRange <= RangeBound.URGENT.getRange( Direction.LEFT ) ||
                forwardRange <= RangeBound.URGENT.getRange( Direction.FORWARD ) ) {
                SwitchState( BotState.Turning_Right_Emergency );
        }
    }

    private void Think_Turning_Left_Emergency()
    {
        int rightRange = SensorData.GetRightScan();

        if ( rightRange > RangeBound.URGENT.getRange( Direction.RIGHT ) ) {
            SwitchState( BotState.Turning_Left_Gradual );
        }
    }

    private void Think_Turning_Right_Emergency()
    {
        int leftRange = SensorData.GetLeftScan();

        if ( leftRange > RangeBound.URGENT.getRange( Direction.LEFT ) ) {
            SwitchState( BotState.Turning_Right_Gradual );
        }
    }

    private void Think_Collision_Recovery()
    {
        //Controls.ScanLeft();
        //Controls.ScanRight();
    }
    //</editor-fold>

    //<editor-fold desc="OnEnter - Called when first entering a new state">
    //OnEnter - Called when first entering a new state
    public void OnEnter()
    {
        System.out.println( State );
        switch(State)
        {
            case Moving_Forward:
                Sound.playNote( Sound.XYLOPHONE, 392, 200 );
                OnEnter_Moving_Forward();
                break;
            case Turning_Left_Gradual:
                Sound.playNote( Sound.XYLOPHONE, 592, 300 );
                OnEnter_Turning_Left_Gradual();
                break;
            case Turning_Right_Gradual:
                Sound.playNote( Sound.XYLOPHONE, 592, 300 );
                OnEnter_Turning_Right_Gradual();
                break;
            case Turning_Left_Emergency:
                Sound.playNote( Sound.XYLOPHONE, 792, 500 );
                OnEnter_Turning_Left_Emergency();
                break;
            case Turning_Right_Emergency:
                Sound.playNote( Sound.XYLOPHONE, 792, 500 );
                OnEnter_Turning_Right_Emergency();
                break;
            case Collision_Recovery:
                OnEnter_Collision_Recovery();
                break;
            default:
                System.out.println("Unexpected State encountered in BotBrain::OnEnter()");
        }
    }

    private void OnEnter_Moving_Forward()
    {
        Controls.MoveForward();
    }

    private void OnEnter_Turning_Left_Gradual()
    {
        Controls.MoveLeftGradual();
    }

    private void OnEnter_Turning_Right_Gradual()
    {
        Controls.MoveRightGradual();
    }

    private void OnEnter_Turning_Left_Emergency()
    {
        Controls.MoveLeftUrgent();
    }

    private void OnEnter_Turning_Right_Emergency()
    {
        Controls.MoveRightUrgent();
    }

    private void OnEnter_Collision_Recovery()
    {
        //TODO
    }
    //</editor-fold>

    //<editor-fold desc="OnExit - Called when exiting a state to clean up anything needed">
    //OnExit - Called when exiting a state to clean up anything needed
    public void OnExit()
    {
        switch(State)
        {
            case Moving_Forward:
                OnExit_Moving_Forward();
                break;
            case Turning_Left_Gradual:
                OnExit_Turning_Left_Gradual();
                break;
            case Turning_Right_Gradual:
                OnExit_Turning_Right_Gradual();
                break;
            case Turning_Left_Emergency:
                OnExit_Turning_Left_Emergency();
                break;
            case Turning_Right_Emergency:
                OnExit_Turning_Right_Emergency();
                break;
            case Collision_Recovery:
                OnExit_Collision_Recovery();
                break;
            default:
                System.out.println("Unexpected State encountered in BotBrain::OnExit()");
        }
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

    private void OnExit_Turning_Left_Emergency()
    {
        //TODO
    }

    private void OnExit_Turning_Right_Emergency()
    {
        //TODO
    }

    private void OnExit_Collision_Recovery()
    {
        //TODO
    }
    //</editor-fold>
}
