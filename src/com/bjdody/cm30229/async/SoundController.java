package com.bjdody.cm30229.async;

import lejos.nxt.Sound;

import java.util.HashMap;

/**
 * Created by Ollie on 18/02/17.
 */
public class SoundController extends Thread {

    private HashMap<String, Integer> noteMap;

    public SoundController() {
        noteMap = new HashMap<>();

        noteMap.put( "C0", 16 );
        noteMap.put( "Db0", 17 );
        noteMap.put( "D0", 18 );
        noteMap.put( "Eb0", 19 );
        noteMap.put( "E0", 21 );
        noteMap.put( "F0", 22 );
        noteMap.put( "Gb0", 23 );
        noteMap.put( "G0", 25 );
        noteMap.put( "Ab0", 26 );
        noteMap.put( "LA0", 28 );
        noteMap.put( "Bb0", 29 );
        noteMap.put( "B0", 31 );
        noteMap.put( "C1", 33 );
        noteMap.put( "Db1", 35 );
        noteMap.put( "D1", 37 );
        noteMap.put( "Eb1", 39 );
        noteMap.put( "E1", 41 );
        noteMap.put( "F1", 44 );
        noteMap.put( "Gb1", 46 );
        noteMap.put( "G1", 49 );
        noteMap.put( "Ab1", 52 );
        noteMap.put( "LA1", 55 );
        noteMap.put( "Bb1", 58 );
        noteMap.put( "B1", 62 );
        noteMap.put( "C2", 65 );
        noteMap.put( "Db2", 69 );
        noteMap.put( "D2", 73 );
        noteMap.put( "Eb2", 78 );
        noteMap.put( "E2", 82 );
        noteMap.put( "F2", 87 );
        noteMap.put( "Gb2", 93 );
        noteMap.put( "G2", 98 );
        noteMap.put( "Ab2", 104 );
        noteMap.put( "LA2", 110 );
        noteMap.put( "Bb2", 117 );
        noteMap.put( "B2", 123 );
        noteMap.put( "C3", 131 );
        noteMap.put( "Db3", 139 );
        noteMap.put( "D3", 147 );
        noteMap.put( "Eb3", 156 );
        noteMap.put( "E3", 165 );
        noteMap.put( "F3", 175 );
        noteMap.put( "Gb3", 185 );
        noteMap.put( "G3", 196 );
        noteMap.put( "Ab3", 208 );
        noteMap.put( "LA3", 220 );
        noteMap.put( "Bb3", 233 );
        noteMap.put( "B3", 247 );
        noteMap.put( "C4", 262 );
        noteMap.put( "Db4", 277 );
        noteMap.put( "D4", 294 );
        noteMap.put( "Eb4", 311 );
        noteMap.put( "E4", 330 );
        noteMap.put( "F4", 349 );
        noteMap.put( "Gb4", 370 );
        noteMap.put( "G4", 392 );
        noteMap.put( "Ab4", 415 );
        noteMap.put( "LA4", 440 );
        noteMap.put( "Bb4", 466 );
        noteMap.put( "B4", 494 );
        noteMap.put( "C5", 523 );
        noteMap.put( "Db5", 554 );
        noteMap.put( "D5", 587 );
        noteMap.put( "Eb5", 622 );
        noteMap.put( "E5", 659 );
        noteMap.put( "F5", 698 );
        noteMap.put( "Gb5", 740 );
        noteMap.put( "G5", 784 );
        noteMap.put( "Ab5", 831 );
        noteMap.put( "LA5", 880 );
        noteMap.put( "Bb5", 932 );
        noteMap.put( "B5", 988 );
        noteMap.put( "C6", 1047 );
        noteMap.put( "Db6", 1109 );
        noteMap.put( "D6", 1175 );
        noteMap.put( "Eb6", 1245 );
        noteMap.put( "E6", 1319 );
        noteMap.put( "F6", 1397 );
        noteMap.put( "Gb6", 1480 );
        noteMap.put( "G6", 1568 );
        noteMap.put( "Ab6", 1661 );
        noteMap.put( "LA6", 1760 );
        noteMap.put( "Bb6", 1865 );
        noteMap.put( "B6", 1976 );
        noteMap.put( "C7", 2093 );
        noteMap.put( "Db7", 2217 );
        noteMap.put( "D7", 2349 );
        noteMap.put( "Eb7", 2489 );
        noteMap.put( "E7", 2637 );
        noteMap.put( "F7", 2794 );
        noteMap.put( "Gb7", 2960 );
        noteMap.put( "G7", 3136 );
        noteMap.put( "Ab7", 3322 );
        noteMap.put( "LA7", 3520 );
        noteMap.put( "Bb7", 3729 );
        noteMap.put( "B7", 3951 );
        noteMap.put( "C8", 4186 );
        noteMap.put( "Db8", 4435 );
        noteMap.put( "D8", 4699 );
        noteMap.put( "Eb8", 4978 );
    }

    @Override
    public void run() {
        int bpm = 120;
        int quarter = 60000 / bpm;    // quarter 1/4
        int half = 2 * quarter;       // half 2/4
        int eighth = quarter / 2;      // eighth 1/8
        int sixteenth = quarter / 4;  // sixteenth 1/16

        while ( !isInterrupted() ) {
            try {
                Sound.playTone( noteMap.get( "LA3") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "LA3") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "LA3") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "F3") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "C4") , sixteenth );
                sleep( 1 + sixteenth );

                Sound.playTone( noteMap.get( "LA3") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "F3") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "C4") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "LA3") , half );
                sleep( 1 + half );

                Sound.playTone( noteMap.get( "E4") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "E4") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "E4") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "F4") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "C4") , sixteenth );
                sleep( 1 + sixteenth );

                Sound.playTone( noteMap.get( "Ab3") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "F3") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "C4") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "LA3") , half );
                sleep( 1 + half );

                Sound.playTone( noteMap.get( "LA4") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "LA3") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "LA3") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "LA4") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "Ab4") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "G4") , sixteenth );
                sleep( 1 + sixteenth );

                Sound.playTone( noteMap.get( "Gb4") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "E4") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "F4") , eighth );
                sleep( 1 + eighth );
                sleep( 1 + eighth );
                Sound.playTone( noteMap.get( "Bb3") , eighth );
                sleep( 1 + eighth );
                Sound.playTone( noteMap.get( "Eb4") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "D4") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "Db4") , sixteenth );
                sleep( 1 + sixteenth );

                Sound.playTone( noteMap.get( "C4") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "B3") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "C4") , eighth );
                sleep( 1 + eighth );
                sleep( 1 + eighth );
                Sound.playTone( noteMap.get( "F3") , eighth );
                sleep( 1 + eighth );
                Sound.playTone( noteMap.get( "Ab3") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "F3") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "LA3") , sixteenth );
                sleep( 1 + sixteenth );

                Sound.playTone( noteMap.get( "C4") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "LA3") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "C4") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "E4") , half );
                sleep( 1 + half );

                Sound.playTone( noteMap.get( "LA4") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "LA3") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "LA3") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "LA4") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "Ab4") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "G4") , sixteenth );
                sleep( 1 + sixteenth );

                Sound.playTone( noteMap.get( "Gb4") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "E4") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "F4") , eighth );
                sleep( 1 + eighth );
                sleep( 1 + eighth );
                Sound.playTone( noteMap.get( "Bb3") , eighth );
                sleep( 1 + eighth );
                Sound.playTone( noteMap.get( "Eb4") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "D4") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "Db4") , sixteenth );
                sleep( 1 + sixteenth );

                Sound.playTone( noteMap.get( "C4") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "B3") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "C4") , eighth );
                sleep( 1 + eighth );
                sleep( 1 + eighth );
                Sound.playTone( noteMap.get( "F3") , eighth );
                sleep( 1 + eighth );
                Sound.playTone( noteMap.get( "Ab3") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "F3") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "C4") , sixteenth );
                sleep( 1 + sixteenth );

                Sound.playTone( noteMap.get( "LA3") , quarter );
                sleep( 1 + quarter );
                Sound.playTone( noteMap.get( "F3") , eighth + sixteenth );
                sleep( 1 + eighth + sixteenth );
                Sound.playTone( noteMap.get( "C4") , sixteenth );
                sleep( 1 + sixteenth );
                Sound.playTone( noteMap.get( "LA3") , half );
                sleep( 1 + half );

                sleep( 2 * half );
            } catch ( InterruptedException e ) {
                break;
            }
        }
    }

}
