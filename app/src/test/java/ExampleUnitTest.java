package org.mooncolony.moonmayor.captainsonarradarcompanion;

import org.junit.Test;
import org.mooncolony.moonmayor.captainsonarradarcompanion.GridPoint;

import dalvik.annotation.TestTargetClass;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
  @Test
  public void addition_isCorrect() throws Exception {
    assertEquals(4, 2 + 2);
  }

  // North
  @Test
  public void gridPointAddNorth() {
    GridPoint start = new GridPoint(2, 3);
    GridPoint result = start.add(GridPoint.NORTH);

    assertEquals(2, start.row, "initial GridPoint row is not changed.");
    assertEquals(3, start.col, "initial GridPoint col is not changed.");

    assertEquals(1, result.row, "final GridPoint row is moved north.");
    assertEquals(3, result.col, "final GridPoint col is not changed.");
  }

  // South
  @Test
  public void gridPointAddNorth() {
    GridPoint start = new GridPoint(2, 3);
    GridPoint result = start.add(GridPoint.SOUTH);

    assertEquals(2, start.row, "initial GridPoint row is not changed.");
    assertEquals(3, start.col, "initial GridPoint col is not changed.");

    assertEquals(3, result.row, "final GridPoint row is moved north.");
    assertEquals(3, result.col, "final GridPoint col is not changed.");
  }

  // East
  @Test
  public void gridPointAddNorth() {
    GridPoint start = new GridPoint(2, 3);
    GridPoint result = start.add(GridPoint.EAST);

    assertEquals(2, start.row, "initial GridPoint row is not changed.");
    assertEquals(3, start.col, "initial GridPoint col is not changed.");

    assertEquals(1, result.row, "final GridPoint row is moved north.");
    assertEquals(4, result.col, "final GridPoint col is not changed.");
  }

  // West
  @Test
  public void gridPointAddNorth() {
    GridPoint start = new GridPoint(2, 3);
    GridPoint result = start.add(GridPoint.WEST);

    assertEquals(2, start.row, "initial GridPoint row is not changed.");
    assertEquals(3, start.col, "initial GridPoint col is not changed.");

    assertEquals(1, result.row, "final GridPoint row is moved north.");
    assertEquals(2, result.col, "final GridPoint col is not changed.");
  }
}