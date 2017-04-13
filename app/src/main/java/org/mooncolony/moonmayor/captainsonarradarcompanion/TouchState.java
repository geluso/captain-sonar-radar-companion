package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.view.MotionEvent;

/**
 * Created by moonmayor on 2017-04-13.
 */
public class TouchState {
    public boolean isTouching;

    public int touchStartRow;
    public int touchStartCol;

    public int touchCurrentRow;
    public int touchCurrentCol;

    public int touchEndRow;
    public int touchEndCol;

    public int lastRow;
    public int lastCol;

    public TouchState() {
        isTouching = false;

        touchStartRow = -1;
        touchStartCol = -1;

        touchCurrentRow = -1;
        touchCurrentCol = -1;

        touchEndRow = -1;
        touchEndCol = -1;

        lastRow = -1;
        lastCol = -1;
    }

    public void startTouch(int row, int col) {
        isTouching = true;
        touchStartRow = row;
        touchStartCol = col;
    }

    public void updateTouch(int row, int col) {
        touchCurrentRow = row;
        touchCurrentCol = col;
    }

    public void endTouch() {
        isTouching = false;
        touchEndRow = touchCurrentRow;
        touchEndCol = touchCurrentCol;

        lastRow = touchCurrentRow;
        lastCol = touchCurrentCol;
    }
}
