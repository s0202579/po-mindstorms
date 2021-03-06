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
public class TurnAction extends BaseAction {

  public TurnAction(Model m, int angle) {
    super(m);
    setDistance(0);
    setAngle(angle);
  }
  private boolean first = true;

  @Override
  public int getNextAction() {
    if (first) {
      first = false;
      return Navigator.TURN;
    }
    return Navigator.NONE;
  }

  @Override
  public boolean isComplete() {
    return !getModel().isMoving() && !first;
  }
}