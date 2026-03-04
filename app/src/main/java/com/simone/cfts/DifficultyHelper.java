package com.simone.cfts;

public class DifficultyHelper {

    private static final int[] ICONS = {
            R.drawable.beginner,  // difficulty 1
            R.drawable.average,   // difficulty 2
            R.drawable.skilled,   // difficulty 3
            R.drawable.expert,    // difficulty 4
            R.drawable.spartan    // difficulty 5
    };

    public static int getIconResource(int difficulty) {
        if (difficulty >= 1 && difficulty <= 5) {
            return ICONS[difficulty - 1];
        }
        return ICONS[4]; // default to spartan
    }
}
