package org.mooncolony.moonmayor.captainsonarradarcompanion.roles;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.mooncolony.moonmayor.captainsonarradarcompanion.R;
import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;

public class RadarRoleFragment extends RoleFragment {
    TextView movementHistory;

    View silenceButton;
    View surfaceButton;

    View mineButton;
    View torpedoButton;
    View droneButton;

    TextView radarRolePrompt;
    View compass;
    View torpedoMenu;
    Button confirmTorpedo;
    Button cancelTorpedo;

    View droneMenu;
    TextView regionText;
    Button positiveDrone;
    Button negativeDrone;
    Button cancelDrone;

    View sonarButton;
    View sonarMenu;

    TextView sonarRow;
    TextView sonarColumn;
    TextView sonarSector;

    Button confirmSonar;
    Button cancelSonar;
    Button toggleSonarMode;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.role_radar, container, false);

        movementHistory = rootView.findViewById(R.id.movementHistory);
        silenceButton = rootView.findViewById(R.id.silenceButton);
        surfaceButton = rootView.findViewById(R.id.surfaceButton);

        mineButton = rootView.findViewById(R.id.mineButton);
        torpedoButton = rootView.findViewById(R.id.torpedoButton);
        droneButton = rootView.findViewById(R.id.droneButton);

        radarRolePrompt = rootView.findViewById(R.id.radarRolePrompt);
        compass = rootView.findViewById(R.id.compass);
        torpedoMenu = rootView.findViewById(R.id.torpedoMenu);
        confirmTorpedo = rootView.findViewById(R.id.confirmTorpedo);
        cancelTorpedo = rootView.findViewById(R.id.cancelTorpedo);

        droneMenu = rootView.findViewById(R.id.droneMenu);
        regionText = rootView.findViewById(R.id.region);
        positiveDrone = rootView.findViewById(R.id.positiveDrone);
        negativeDrone = rootView.findViewById(R.id.negativeDrone);
        cancelDrone = rootView.findViewById(R.id.cancelDrone);

        sonarButton = rootView.findViewById(R.id.sonarButton);
        sonarMenu = rootView.findViewById(R.id.sonarMenu);

        sonarRow = rootView.findViewById(R.id.sonarRow);
        sonarColumn = rootView.findViewById(R.id.sonarColumn);
        sonarSector = rootView.findViewById(R.id.sonarSector);

        confirmSonar = rootView.findViewById(R.id.confirmSonar);
        cancelSonar = rootView.findViewById(R.id.cancelSonar);
        toggleSonarMode = rootView.findViewById(R.id.changeSonar);

        rootView.findViewById(R.id.northButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("N");
                updateMap(GridPoint.NORTH);
            }
        });

        rootView.findViewById(R.id.eastButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("N");
                updateMap(GridPoint.EAST);
            }
        });

        rootView.findViewById(R.id.southButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("N");
                updateMap(GridPoint.SOUTH);
            }
        });

        rootView.findViewById(R.id.westButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("N");
                updateMap(GridPoint.WEST);
            }
        });

        rootView.findViewById(R.id.mineButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("mine");
                gameState.addMine();
                drawer.draw();
            }
        });

        rootView.findViewById(R.id.silenceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("silence");
                gameState.addSilence();
                drawer.draw();
            }
        });

        rootView.findViewById(R.id.surfaceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("surfaced");
                gameState.surface();
                drawer.draw();
            }
        });

        rootView.findViewById(R.id.torpedoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTorpedoMenu();

                // target the center of the board at first and allow user to move it around.
                gameState.placingTorpedo = true;
                gameState.placingTorpedoRow = gameState.radar.map.rows / 2;
                gameState.placingTorpedoCol = gameState.radar.map.cols / 2;

                drawer.draw();
            }
        });

        rootView.findViewById(R.id.confirmTorpedo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("torpedo");
                showCompass();

                gameState.addTorpedo();
                gameState.placingTorpedo = false;

                updateMap(GridPoint.TORPEDO);
            }
        });

        rootView.findViewById(R.id.cancelTorpedo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCompass();

                gameState.placingTorpedo = false;
                drawer.draw();
            }
        });

        rootView.findViewById(R.id.droneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDroneMenu();
            }
        });

        rootView.findViewById(R.id.positiveDrone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDroneResult(true);
            }
        });

        rootView.findViewById(R.id.negativeDrone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDroneResult(false);
            }
        });

        rootView.findViewById(R.id.cancelDrone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDroneMenu();
            }
        });

        rootView.findViewById(R.id.sonarButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sonarButtonClick();
            }
        });

        rootView.findViewById(R.id.changeSonar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSonar();
            }
        });

        rootView.findViewById(R.id.confirmSonar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSonar();
            }
        });

        rootView.findViewById(R.id.cancelSonar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSonar();
            }
        });

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

    void showTorpedoMenu() {
        closeAllMenus();
        torpedoMenu.setVisibility(View.VISIBLE);
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

    void confirmSonar() {
        gameState.sonarConfirm();
        showCompass();
        drawer.draw();
    }

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

        radarRolePrompt.setVisibility(View.VISIBLE);
        compass.setVisibility(View.VISIBLE);

        droneButton.setVisibility(View.VISIBLE);
        sonarButton.setVisibility(View.VISIBLE);
    }

    void closeAllMenus() {
        gameState.isRunningSonar = false;
        gameState.isAskingDrone = false;
        gameState.placingTorpedo = false;

        drawer.draw();

        radarRolePrompt.setVisibility(View.GONE);
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
