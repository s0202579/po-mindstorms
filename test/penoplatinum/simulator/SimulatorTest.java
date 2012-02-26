/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.junit.*;
import static org.junit.Assert.*;
import penoplatinum.map.MapFactorySector;
import penoplatinum.simulator.tiles.TileGeometry;

/**
 *
 * @author MHGameWork
 */
public class SimulatorTest {

  @Test
  public void testFindHitDistance() throws FileNotFoundException {
    Simulator sim = new Simulator();

    Map m;
    File f = new File("..\\..\\src\\java\\penoplatinum\\simulator\\map2.track");
    Scanner sc = new Scanner(f);
    MapFactorySector fact = new MapFactorySector();
    m = fact.getMap(sc);

    sim.useMap(m);

    assertEquals(20, sim.findHitDistance(180, 1, 1, 20, 20));
    assertEquals(100, sim.findHitDistance(270, 1, 1, 20, 20));
  }
}
