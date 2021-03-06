/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 *
 * @author MHGameWork
 */
public class MoveAction extends BaseAction {

  public MoveAction(Model m, float distance) {
    super(m);
    setDistance(distance);
    setAngle(0);
  }
  private boolean first = true;

  @Override
  public int getNextAction() {
    if (first) {
      first = false;
      return Navigator.MOVE;
    }

    return Navigator.NONE;
  }

  @Override
  public boolean isComplete() {
    return !getModel().isMoving() && !first;

  }
}
