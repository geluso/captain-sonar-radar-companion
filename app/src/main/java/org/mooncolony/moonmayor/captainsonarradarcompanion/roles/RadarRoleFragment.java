package org.mooncolony.moonmayor.captainsonarradarcompanion.roles;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.GameState;
import org.mooncolony.moonmayor.captainsonarradarcompanion.R;
import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.MapDrawer;
import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RadarRoleFragment extends RoleFragment {
    @BindView(R.id.movementHistory) TextView movementHistory;

    @BindView(R.id.silenceButton) View silenceButton;
    @BindView(R.id.surfaceButton) View surfaceButton;

    @BindView(R.id.mineButton) View mineButton;
    @BindView(R.id.torpedoButton) View torpedoButton;
    @BindView(R.id.droneButton) View droneButton;

    @BindView(R.id.compass) View compass;
    @BindView(R.id.torpedoMenu) View torpedoMenu;
    @BindView(R.id.confirmTorpedo) Button confirmTorpedo;
    @BindView(R.id.cancelTorpedo) Button cancelTorpedo;

    @BindView(R.id.droneMenu) View droneMenu;
    @BindView(R.id.region) TextView regionText;
    @BindView(R.id.positiveDrone) Button positiveDrone;
    @BindView(R.id.negativeDrone) Button negativeDrone;
    @BindView(R.id.cancelDrone) Button cancelDrone;

    @BindView(R.id.sonarButton) View sonarButton;
    @BindView(R.id.sonarMenu) View sonarMenu;

    @BindView(R.id.sonarRow) TextView sonarRow;
    @BindView(R.id.sonarColumn) TextView sonarColumn;
    @BindView(R.id.sonarSector) TextView sonarSector;

    @BindView(R.id.confirmSonar) Button confirmSonar;
    @BindView(R.id.cancelSonar) Button cancelSonar;
    @BindView(R.id.changeSonar) Button toggleSonarMode;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.role_radar, container, false);
        View view = rootView.findViewById(R.id.movementHistory);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void processTouch(int row, int col) {
        if (gameState.placingTorpedo) {
            dealWithTorpedoTouch(row, col);
        } else if (gameState.isAskingDrone) {
            dealWithDroneTouch(row, col);
        } else if (gameState.isRunningSonar) {
            dealWithSonarTouch(row, col);
        } else {
            super.processTouch(row, col);
        }
    }

    void dealWithDroneTouch(int row, int col) {
        // determine if we're on a 2x2 or 3x3 region map.
        boolean isSmall = false;
        if(gameState.radar.map.cols < 15) {
            isSmall = true;
        }

        int regionColumns = 3;
        if (isSmall) {
            regionColumns = 2;
        }

        row /= 5;
        col /= 5;

        int regionId = regionColumns * row + col + 1;
        gameState.currentDroneRegionId = regionId;
        regionText.setText("Drone saw sub in region " + regionId + "?");
        positiveDrone.setEnabled(true);
        negativeDrone.setEnabled(true);
    }

    void dealWithTorpedoTouch(int row, int col) {
        if (row != gameState.placingTorpedoRow || col != gameState.placingTorpedoCol) {
            if (!rowOutOfBounds(row)) {
                gameState.placingTorpedoRow = row;
            }
            if (!colOutOfBounds(col)) {
                gameState.placingTorpedoCol = col;
            }
            drawer.draw();
        }
    }

    void dealWithSonarTouch(int row, int col) {
        if (row != gameState.placingSonarRow || col != gameState.placingSonarCol) {
            if (!rowOutOfBounds(row)) {
                gameState.placingSonarRow = row;
            }
            if (!colOutOfBounds(col)) {
                gameState.placingSonarCol = col;
            }

            updateSonarCoordinates();
            drawer.draw();
        }
    }

    @OnClick({R.id.northButton})
    void northButtonClick() {
        appendText("N");
        updateMap(GridPoint.NORTH);
    }
    @OnClick({R.id.eastButton})
    void eastButtonClick() {
        appendText("E");
        updateMap(GridPoint.EAST);
    }
    @OnClick({R.id.southButton})
    void southButtonClick() {
        appendText("S");
        updateMap(GridPoint.SOUTH);
    }
    @OnClick({R.id.westButton})
    void westButtonClick() {
        appendText("W");
        updateMap(GridPoint.WEST);
    }

    @OnClick({R.id.mineButton})
    void mineButtonClick() {
        appendText("mine");
        gameState.addMine();
        drawer.draw();
    }

    @OnClick({R.id.silenceButton})
    void silence() {
        appendText("silence");
        gameState.addSilence();
        drawer.draw();
    }

    @OnClick({R.id.surfaceButton})
    void surface() {
        appendText("surfaced");
        gameState.surface();
        drawer.draw();
    }

    @OnClick({R.id.torpedoButton})
    void torpedoButtonClick() {
        showTorpedoMenu();

        // target the center of the board at first and allow user to move it around.
        gameState.placingTorpedo = true;
        gameState.placingTorpedoRow = gameState.radar.map.rows / 2;
        gameState.placingTorpedoCol = gameState.radar.map.cols / 2;

        drawer.draw();
    }

    @OnClick({R.id.confirmTorpedo})
    void confirmTorpedo() {
        appendText("torpedo");
        showCompass();

        gameState.addTorpedo();
        gameState.placingTorpedo = false;

        updateMap(GridPoint.TORPEDO);
    }

    @OnClick({R.id.cancelTorpedo})
    void cancelTorpedo() {
        showCompass();

        gameState.placingTorpedo = false;
        drawer.draw();
    }

    void showTorpedoMenu() {
        closeAllMenus();
        torpedoMenu.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.droneButton})
    void droneButtonClick() {
        showDroneMenu();
    }

    @OnClick({R.id.positiveDrone})
    void positiveDroneResult() {
        addDroneResult(true);
    }

    @OnClick({R.id.negativeDrone})
    void negativeDroneResult() {
        addDroneResult(false);
    }

    void addDroneResult(boolean result) {
        gameState.analyzeDrone(result);

        GridPoint droneResult = new GridPoint(0, 0);
        droneResult.type = GridPoint.DRONE_RESULT.type;
        droneResult.droneRegionId = gameState.currentDroneRegionId;
        droneResult.droneResult = result;

        String message = "Drone " + droneResult.droneRegionId;
        if (droneResult.droneResult) {
            message += "+";
        } else {
            message += "-";
        }
        appendText(message);

        updateMap(droneResult);
        hideDroneMenu();
    }

    @OnClick({R.id.cancelDrone})
    void hideDroneMenu() {
        showCompass();
        gameState.isAskingDrone = false;
        gameState.currentDroneRegionId = -1;
    }

    void showDroneMenu() {
        closeAllMenus();
        droneMenu.setVisibility(View.VISIBLE);

        // technically the gameState doesn't have a currentDroneRegionId
        // until someone touches a region after activating sonar.
        gameState.isAskingDrone = true;
        gameState.currentDroneRegionId = -1;

        regionText.setText("Touch a region.");
        positiveDrone.setEnabled(false);
        negativeDrone.setEnabled(false);
    }

    @OnClick({R.id.sonarButton})
    void sonarButtonClick() {
        closeAllMenus();

        sonarMenu.setVisibility(View.VISIBLE);
        silenceButton.setVisibility(View.GONE);
        //scenarioButton.setVisibility(View.GONE);
        surfaceButton.setVisibility(View.GONE);
        mineButton.setVisibility(View.GONE);
        torpedoButton.setVisibility(View.GONE);
        droneButton.setVisibility(View.GONE);
        sonarButton.setVisibility(View.GONE);

        gameState.sonarStart();
        drawer.draw();
        updateSonarMenu();
    }

    @OnClick({R.id.changeSonar})
    void changeSonar() {
        gameState.sonarToggleMode();
        updateSonarMenu();
    }

    void updateSonarCoordinates() {
        String columnLetters = " ABCDEFGHIJKLMNO";
        int row = gameState.placingSonarRow + 1;
        int col = gameState.placingSonarCol + 1;
        sonarRow.setText("Row: " + row);
        sonarColumn.setText("Column: " + columnLetters.charAt(col));

        int sector = this.gameState.radar.map.pointToQuadrant(row, col);
        sonarSector.setText("Sector: " + sector);
    }

    void updateSonarMenu() {
        toggleSonarMode.setText("Mode: " + gameState.sonarMode);

        TextView noStrikeThrough1, noStrikeThrough2, strikeThrough;
        if (gameState.sonarMode.equals("row,col")) {
            noStrikeThrough1 = sonarRow;
            noStrikeThrough2 = sonarColumn;
            strikeThrough = sonarSector;
        } else if (gameState.sonarMode.equals("sector,row")) {
            noStrikeThrough2 = sonarSector;
            noStrikeThrough1 = sonarRow;
            strikeThrough = sonarColumn;
        } else if (gameState.sonarMode.equals("sector,col") || true) {
            noStrikeThrough2 = sonarSector;
            noStrikeThrough1 = sonarColumn;
            strikeThrough = sonarRow;
        }

        noStrikeThrough1.setPaintFlags(noStrikeThrough1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        noStrikeThrough2.setPaintFlags(noStrikeThrough2.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        strikeThrough.setPaintFlags(strikeThrough.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @OnClick({R.id.confirmSonar})
    void confirmSonar() {
        gameState.sonarConfirm();
        showCompass();
        drawer.draw();
    }

    @OnClick({R.id.cancelSonar})
    void cancelSonar() {
        gameState.sonarCancel();
        showCompass();
        drawer.draw();
    }

    void showCompass() {
        closeAllMenus();
        silenceButton.setVisibility(View.VISIBLE);
        //scenarioButton.setVisibility(View.VISIBLE);
        surfaceButton.setVisibility(View.VISIBLE);

        mineButton.setVisibility(View.VISIBLE);
        torpedoButton.setVisibility(View.VISIBLE);

        compass.setVisibility(View.VISIBLE);

        droneButton.setVisibility(View.VISIBLE);
        sonarButton.setVisibility(View.VISIBLE);
    }

    void closeAllMenus() {
        gameState.isRunningSonar = false;
        gameState.isAskingDrone = false;
        gameState.placingTorpedo = false;

        drawer.draw();

        compass.setVisibility(View.GONE);
        torpedoMenu.setVisibility(View.GONE);
        droneMenu.setVisibility(View.GONE);
        sonarMenu.setVisibility(View.GONE);
    }

    private void appendText(String message) {
        String current = movementHistory.getText().toString();
        if (current.length() != 0) {
            current += ", ";
        }
        movementHistory.setText(current + message);
    }

    private void updateMap(GridPoint gp) {
        gameState.updatePath(gp);
        drawer.draw();
    }

    boolean rowOutOfBounds(int row) {
        if (row < 0) {
            return true;
        }

        if (row > this.gameState.radar.map.rows - 1) {
            return true;
        }

        return false;
    }

    boolean colOutOfBounds(int col) {
        if (col < 0) {
            return true;
        }

        if (col > this.gameState.radar.map.cols - 1) {
            return true;
        }

        return false;
    }

    boolean mapRelease(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {

        }
        return true;
    }

    public void reset() {
        if (movementHistory == null || torpedoMenu == null || droneMenu == null) {
            System.out.println("skipping reset because views aren't bound.");
            return;
        }
        movementHistory.setText("");
        showCompass();

        torpedoMenu.setVisibility(View.GONE);
        droneMenu.setVisibility(View.GONE);
    }
}
