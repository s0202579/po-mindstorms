/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

/**
 *
 * @author MHGameWork
 */
public interface ViewRobot {

  void trackMovement(Graphics2D g2d);

  void renderRobot(Graphics2D g2d, ImageObserver board);

  void renderSonar(Graphics2D g2d);

  int getX();

  int getY();

  int getDirection();
}
