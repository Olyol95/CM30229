package com.bjdody.cm30229;

/**
 * Created by Benjamin on 10/02/2017.
 */
public class BotBrain {

    private Controls BotControls;

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

    private BotState State;

    public BotBrain(BotState initial_state, Controls in_controls){
        State = initial_state;
        BotControls = in_controls;
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
        int distance = BotControls.ScanForward();
        if (distance < 10)
        {
            SwitchState(BotState.Collision_Recovery);
        }
    }

    private void Think_Turning_Left_Gradual()
    {
        //TODO
    }

    private void Think_Turning_Right_Gradual()
    {
        //TODO
    }

    private void Think_Turning_Left_Emergency()
    {
        //TODO
    }

    private void Think_Turning_Right_Emergency()
    {
        //TODO
    }

    private void Think_Collision_Recovery()
    {
        BotControls.ScanLeft();
        BotControls.ScanRight();
    }
    //</editor-fold>

    //<editor-fold desc="OnEnter - Called when first entering a new state">
    //OnEnter - Called when first entering a new state
    public void OnEnter()
    {
        switch(State)
        {
            case Moving_Forward:
                OnEnter_Moving_Forward();
                break;
            case Turning_Left_Gradual:
                OnEnter_Turning_Left_Gradual();
                break;
            case Turning_Right_Gradual:
                OnEnter_Turning_Right_Gradual();
                break;
            case Turning_Left_Emergency:
                OnEnter_Turning_Left_Emergency();
                break;
            case Turning_Right_Emergency:
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
        BotControls.MoveForward();
    }

    private void OnEnter_Turning_Left_Gradual()
    {
        //TODO
    }

    private void OnEnter_Turning_Right_Gradual()
    {
        //TODO
    }

    private void OnEnter_Turning_Left_Emergency()
    {
        //TODO
    }

    private void OnEnter_Turning_Right_Emergency()
    {
        //TODO
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
        BotControls.Stop();
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
