package com.bjdody.cm30229.model;

import com.bjdody.cm30229.HybridAgent;
import lejos.nxt.Sound;

import java.util.ArrayList;

/**
 * Created by Ollie on 19/02/17.
 */
public class PerceptThread extends Thread {

    private ArrayList<Percept> percepts;
    private boolean isActive;

    public PerceptThread() {
        percepts = new ArrayList<>();
    }

    @Override
    public void run() {
        isActive = true;
        while ( isActive ) {
            if ( hasPercept() ) {
                if ( getQueueSize() > 5 ) {
                    clear();
                } else {
                    HybridAgent.instance.getRootLayer().onPercept( dequeue() );
                }
            }
        }
    }

    public synchronized void enqueue( Percept percept ) {
        percepts.add( 0, percept );
    }

    public boolean hasPercept() {
        return percepts.size() > 0;
    }

    public int getQueueSize() {
        return percepts.size();
    }

    public synchronized Percept dequeue() {
        Percept percept = percepts.get( percepts.size() - 1 );
        percepts.remove( percepts.size() - 1 );
        return percept;
    }

    public void shutDown() {
        isActive = false;
    }

    public void clear() {
        Sound.playTone( 800, 500 );
        percepts.clear();
        percepts.trimToSize();
    }

}
