package penoplatinum.navigators;

import penoplatinum.Utils;
import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.AlignNotLineAction;
import penoplatinum.actions.AlignPerpendicularAction;
import penoplatinum.actions.DriveForwardAction;
import penoplatinum.actions.MoveAction;
import penoplatinum.actions.StopAction;
import penoplatinum.actions.TurnAction;
import penoplatinum.modelprocessor.ColorInterpreter;
import penoplatinum.simulator.Barcode;
import penoplatinum.simulator.GoalDecider;
import penoplatinum.simulator.Line;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 * TestNavigator
 * 
 * This Navigator implementation does nothing by itself. It can be controlled 
 * externaly. It can be used to test functionality.
 * 
 * @author: Team Platinum
 */
public class SectorNavigator implements Navigator {

  private boolean proximityBlocked = true;
  private ActionQueue queue = new ActionQueue();
  ColorInterpreter interpreter;
  private DriveForwardAction driveForwardAction = new DriveForwardAction(true);

  public SectorNavigator() {
    // Fill with initial action
    queue.add(driveForwardAction);
    interpreter = new ColorInterpreter();

  }
  private Model model;
  private GoalDecider controler = new GoalDecider() {

    public Boolean reachedGoal() {
      return false;
    }
  };

  public int nextAction() {

    //Utils.Log(model.getSensorValue(Model.S4)+"");
    //updateWallWarnings(); // process sensory data, if more complex should be modelprocessor
    processWorldEvents();


    if (queue.getCurrentAction().isComplete()) {
      queue.dequeue();

    }

    if (queue.getCurrentAction() == null) {
      newEvent("Idle", "", "Drive");
      driveForwardAction.reset();
      queue.add(driveForwardAction);
    }

    return queue.getCurrentAction().getNextAction();
  }

  private void processWorldEvents() {
    //Utils.Log(model.getSensorValue(Model.S4) + "");
    if (queue.getCurrentAction().isNonInterruptable()) {
      return;
    }

//
    //if (!proximityBlocked) {
    //  checkProximityEvent();
    //}
    //checkBarcodeEvent();
    //checkLineEvent();
    //checkSonarCollisionEvent();
    checkFollowableWallEvent();
//    checkCollisionEvent();
  }
//  private double lastBarcodeAngle;

  private void newEvent(String eventName, String source, String action) {
    event = eventName;
    eventSource = source;
    eventAction = action;
//    Utils.Log("Event: " + eventName);
    queue.clearActionQueue();
    proximityBlocked = false;
  }

  private void checkFollowableWallEvent(){
    
    newEvent("Followable Wall Detected", "Sonar", "Align perpendicular");
    
    int[] sonarValues = model.getSonarValues();
    int angle = 90-Math.abs(sonarValues[1]);
    if(sonarValues[0]<23){  
        int sign = sonarValues[3] < sonarValues[1]  ? -1 : 1;
          if(sonarValues[2]>30){
            queue.add(new TurnAction(model, sign*angle));
            // queue.add(new DriveForwardAction(false));
          }
     }
  
  }
  
  private void checkLineEvent() {  //Dit werkt goed
    if (model.getLine() == Line.NONE) {
      return;
    }
    // Line detected
    newEvent("Line " + (model.getLine() == Line.BLACK ? "Black" : "White"), "Lightsensor", "Align and evade"); //TODO: maybe
    queue.add(new TurnAction(model, model.getLine() == Line.BLACK ? -10 : 10));
    queue.add(new AlignNotLineAction(model, model.getLine() == Line.BLACK).setIsNonInterruptable(true));
//    queue.add(new MoveAction(model, 0.05f).setIsNonInterruptable(true));
//    queue.add(new DriveForwardAction());
  }


  private void checkSonarCollisionEvent() {
    if (numCollisionWallWarnings <= OBSTACLE_DETECTION_THRESHOLD) {
      return;
    }
    // Obstacle detected
    newEvent("Sonar Collision", "Sonar", "Align perpendicular");

    queue.add(new MoveAction(model, -.05f).setIsNonInterruptable(true));

    int[] sonarValues = model.getSonarValues();
    //sonarValues[3]
    int angle = model.getSensorValue(Model.M3);
    int targetAngle = 90;


    int diff = (sonarValues[3] - sonarValues[1] + 360) % 360;
    int rotation = diff > 180 ? -5 : 5;

    queue.add(new AlignPerpendicularAction(model).setIsNonInterruptable(true));
    queue.add(new MoveAction(model, .05f).setIsNonInterruptable(true));
    //TODO: Create the correct barcode actions
    //      actionQueue.add(new TurnAction(model, -180));
  }

  public SectorNavigator setModel(Model model) {
    this.model = model;
    interpreter.setModel(model);
    return this;
  }

  public SectorNavigator setControler(GoalDecider controler) {
    this.controler = controler;
    return this;
  }

  public Boolean reachedGoal() {
    return this.controler.reachedGoal();
  }
  int numCollisionWallWarnings;
  private static final int COLLISION_WALL_AVOID_DISTANCE = 15;
  final int OBSTACLE_DETECTION_THRESHOLD = 2;
  int proximityOrientTimeout = 0;

  private void updateWallWarnings() {

    if (model.getSensorValue(Model.S3) < COLLISION_WALL_AVOID_DISTANCE) {
      numCollisionWallWarnings++;
    } else {
      numCollisionWallWarnings--;
    }

    if (numCollisionWallWarnings < 0) {
      numCollisionWallWarnings = 0;
    }
  }

  public double getDistance() {
    return queue.getCurrentAction() == null ? 1 : queue.getCurrentAction().getDistance();
  }

  public double getAngle() {
    return queue.getCurrentAction() == null ? 0 : queue.getCurrentAction().getAngle();
  }
  String event;
  String eventSource;
  String eventAction;
  StringBuilder builder = new StringBuilder();

  @Override
  public String toString() {

    String actionQueue = queue.toString();

    String currentAction = "";
    String currentActionArgument = "";

    if (queue.getCurrentAction() != null) {
      currentAction = queue.getCurrentAction().getKind();
      currentActionArgument = queue.getCurrentAction().getArgument();
      if (currentActionArgument == null) {
        currentActionArgument = "";
      }
    }
    builder.delete(0, builder.length());
    builder
            .append('\"').append(event)
            .append("\",\"").append(eventSource)
            .append("\",\"").append(eventAction)
            .append("\",\"").append(actionQueue)
            .append("\",\"").append(currentAction).append("\",\"")
            .append(currentActionArgument).append('\"');
    return builder.toString();

//    return "\"" + event + "\",\"" + eventSource + "\", \"" + eventAction + "\", \"" + actionQueue + "\", \"" + currentAction + "\", \"" + currentActionArgument + "\"";
  }
}
