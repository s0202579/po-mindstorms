/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.RotatingSonarSensor;
import penoplatinum.sensor.WrappedLightSensor;
import penoplatinum.simulator.ExtendedVector;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.ReferencePosition;
import penoplatinum.simulator.RobotAPI;

/**
 * Responsible for providing access to the hardware of the robot
 * TODO: WARNING, WHEN CHANGIN PORTS THIS ENTIRE CLASS MUST BE CHECKED
 * @author: Team Platinum
 */
public class Angie implements RobotAPI {

    private Motor motorLeft;
    private Motor motorRight;
    private TouchSensor touchLeft;
    private TouchSensor touchRight;
    private WrappedLightSensor light;
    private RotatingSonarSensor sonar;
    private AngieCalibrationData calibrationData;
    private RotationMovement movement;
    private static final int sensorNumberTouchLeft = Model.S1;
    private static final int sensorNumberTouchRight = Model.S2;
    private static final int sensorNumberLight = Model.S4;
    private static final int sensorNumberSonar = Model.S3;
    private static final int sensorNumberMotorLeft = Model.M1;
    private static final int sensorNumberMotorRight = Model.M2;
    private static final int sensorNumberMotorSonar = Model.M3;

    public Angie() {

        //Reset tacho's
        Motor.A.resetTachoCount();
        Motor.B.resetTachoCount();
        Motor.C.resetTachoCount();        
        
        
        motorLeft = Motor.B;
        motorRight = Motor.C;

        
        
        touchLeft = new TouchSensor(SensorPort.S2);
        touchRight = new TouchSensor(SensorPort.S1);
        light = new WrappedLightSensor(null, null);
        light.calibrate();
        sonar = new RotatingSonarSensor(Motor.A, new UltrasonicSensor(SensorPort.S3));

        calibrationData = new AngieCalibrationData();
        movement = new RotationMovement();

        //TODO: WARNING: Depedency inconsistent between Angie and RotationMovement

    }

    public void step() {
        sonar.updateSonarMovement();
    }

    public RotationMovement getMovement() {
        return movement;
    }

    public AngieCalibrationData getCalibrationData() {
        return calibrationData;
    }

    public WrappedLightSensor getLight() {
        return light;
    }

    public Motor getMotorLeft() {
        return motorLeft;
    }

    public Motor getMotorRight() {
        return motorRight;
    }

    public RotatingSonarSensor getSonar() {
        return sonar;
    }

    public TouchSensor getTouchLeft() {
        return touchLeft;
    }

    public TouchSensor getTouchRight() {
        return touchRight;
    }

    public void move(double distance) {
        getMovement().driveDistance(distance);
    }

    public void turn(double angle) {
        getMovement().turnAngle(angle);
    }

    public void stop() {
        getMovement().stop();
    }

    public int[] getSensorValues() {
        //TODO: GC
        int[] values = new int[Model.SENSORVALUES_NUM];
        values[sensorNumberMotorLeft] = motorLeft.getTachoCount();
        values[sensorNumberMotorRight] = motorRight.getTachoCount();
        values[sensorNumberMotorSonar] = -sonar.getMotor().getTachoCount();


        values[sensorNumberTouchLeft] = touchLeft.isPressed() ? 255 : 0;
        values[sensorNumberTouchRight] = touchRight.isPressed() ? 255 : 0;
        values[sensorNumberLight] = light.getLightValue();
        values[sensorNumberSonar] = (int) sonar.getDistance();
        
        //TODO: change on port change
        values[Model.MS3] = getMotorState(Motor.A);
        values[Model.MS1] = getMotorState(Motor.B);
        values[Model.MS2] = getMotorState(Motor.C);



        return values;
    }

    private int getMotorState(Motor m) {
        for(int i=0;i<3; i++){
            if (!m.isMoving() || m.isStopped() || m.isFloating()) {
                return Model.MOTORSTATE_STOPPED;
            }
            if (m.isForward()) {
                return Model.MOTORSTATE_FORWARD;
            }
            if (m.isBackward()) {
                return Model.MOTORSTATE_BACKWARD;
            }
        }
        Utils.Error("Syncronized??? "+(m.isMoving()?1:0)+","+(m.isForward()?1:0)+","+(m.isBackward()?1:0)+","+(m.isStopped()?1:0)+","+(m.isFloating()?1:0));
        //1, 0, 0, 1, 0
        Utils.Error("I M P O S S I B L E !");
        return 0;
    }

    public void turn(int angle) {
        getMovement().turnAngle(angle);
    }

    public void setSpeed(int motor, int speed) {

        switch (motor) {
            case Model.M3:
                Motor.A.setSpeed(speed);
                break;
            case Model.M1:
                Motor.B.setSpeed(speed);
                break;
            case Model.M2:
                Motor.C.setSpeed(speed);
                break;
        }

    }

    public void beep() {
        lejos.nxt.Sound.beep();
    }

    public void setReferencePoint(ReferencePosition reference) {
       //TODO: throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExtendedVector getRelativePosition(ReferencePosition reference) {
       //TODO:  throw new UnsupportedOperationException("Not supported yet.");
       return null;
    }
}
