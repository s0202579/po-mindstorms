package penoplatinum.simulator;

import penoplatinum.modelprocessor.LineModelProcessor;
import penoplatinum.modelprocessor.SonarModelProcessor;
import penoplatinum.modelprocessor.HistogramModelProcessor;
import penoplatinum.modelprocessor.ModelProcessor;
import penoplatinum.modelprocessor.FrontPushModelProcessor;
import penoplatinum.modelprocessor.BarcodeModelProcessor;

import penoplatinum.Utils;
import penoplatinum.modelprocessor.GapModelProcessor;
import penoplatinum.modelprocessor.LightCorruptionModelProcessor;
import penoplatinum.modelprocessor.ProximityModelProcessor;

/**
 * NavigatorRobot
 * 
 * General purpose Navigator-based Robot implementation.
 *  
 * @author: Team Platinum
 */
public class NavigatorRobot implements Robot {

  private RobotAPI api;
  private RobotAgent agent;
  private Navigator navigator;
  private Model model;

  public NavigatorRobot() {
    this.setupModel();
  }

  public NavigatorRobot(Navigator navigator) {
    this();
    this.useNavigator(navigator);
  }

  private void setupModel() {
    // setup a model with the required ModelProcessors
    this.model = new Model();
    ModelProcessor histoBuilder =
            new HistogramModelProcessor(
            new FrontPushModelProcessor(
            new SonarModelProcessor(
            new GapModelProcessor(
            new ProximityModelProcessor(
            new LightCorruptionModelProcessor(
            new BarcodeModelProcessor(
            new LineModelProcessor())))))));

    this.model.setProcessor(histoBuilder);
  }

  public Robot useNavigator(Navigator navigator) {
    // setup the navigator using the same model
    this.navigator = navigator;
    this.navigator.setModel(this.model);
    return this;
  }

  public void useRobotAPI(RobotAPI api) {
    this.api = api;
    this.initAPI();
  }

  public void useRobotAgent(RobotAgent agent) {
    this.agent = agent;

  }

  // method to perform initializing actions when an API is available
  private void initAPI() {
    if (this.api == null) {
      return;
    }
    this.api.setSpeed(Model.M3, 500); // set sonar speed to double of default
    this.api.setSpeed(Model.M2, 250); // set sonar speed to double of default
    this.api.setSpeed(Model.M1, 250); // set sonar speed to double of default
  }

  public void processCommand(String cmd) {
    // no input required
  }

  public String getModelState() {
    return this.model.toString();
  }

  public Model getModel() {
    return this.model;
  }

  public String getNavigatorState() {
    return this.navigator.toString();
  }

  /**
   * This implements one step in the Event Loop. The Robot polls its sensors
   * updates its model and asks the navigator what to do.
   */
  public void step() {
    if (this.navigator == null) {
      Utils.Log("WARNING: no navigator set");
    }
    // get sensor data and update the model (motors are sensors too)
    this.model.updateSensorValues(this.api.getSensorValues());

    // ask the navigator what to do next
    switch (this.navigator.nextAction()) {
      case Navigator.MOVE:
        this.api.move(this.navigator.getDistance());
        break;
      case Navigator.TURN:
        this.api.turn((int) this.navigator.getAngle());
        break;
      case Navigator.STOP:
        this.api.stop();
        break;
      case Navigator.NONE:
      default:
      // do nothing
    }


    agent.send("p" + (int)model.getPositionX() + "," + (int)model.getPositionY());
    
    //agent.send(getStatusMessage());

  }

  public String getStatusMessage() {
    return this.getModelState() + "," + this.getNavigatorState() + "," + "-1";
  }

  public Boolean reachedGoal() {
    return this.navigator.reachedGoal();
  }

  public void stop() {
    this.api.stop();
  }
}
