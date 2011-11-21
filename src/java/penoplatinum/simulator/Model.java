package penoplatinum.simulator;

/**
 * Model
 * 
 * Stores all information about the robot and its environment. Using 
 * ModelProcessors the incoming (sensor-)data is processed and turned in a 
 * map. The Navigator can interrogate this map to decide where to go next.
 * 
 * Author: Team Platinum
 */
public class Model {

    // shorthands mapping the sensors/numbers to their technical ports
    public static final int M1 = 0;
    public static final int M2 = 1;
    public static final int M3 = 2;
    public static final int S1 = 3;
    public static final int S2 = 4;
    public static final int S3 = 5;
    public static final int S4 = 6;
    // the raw data of the 7 sensor: three motors and sensors 1, 2, 3, 4
    private int[] sensors = new int[7];
    // processors are chained using a Decorator pattern
    private ModelProcessor processor;
    // current position
    private int x, y;
    // the map constructed based on the input
    //private Map map;
    // flag indicating the robot is stuck, ModelProcessors determine this
    private Boolean stuckLeft = false;
    private Boolean stuckRight = false;
    
    private int[] sweepValues = new int[4];
    private boolean sweepChanged = true;
    
    private int barcode;

    /**
     * Sets the (top-level) processor
     */
    public void setProcessor(ModelProcessor processor) {
        this.processor = processor;
        this.processor.setModel(this);
    }

    /**
     * method to update the current sensor-values. this also includes the
     * processing of the new data.
     */
    public void updateSensorValues(int[] values) {
        this.sensors = values;
        this.process();
    }

    /**
     * triggers the processor(s) to start processing the sensordata and update
     * the map.
     */
    private void process() {
        if (this.processor != null) {
            this.processor.process();
        }
    }

    /**
     * accessors to give access to the sensor and map data. these are mainly
     * used by the ModelProcessors.
     */
    public int getSensorValue(int num) {
        return this.sensors[num];
    }

    //public Map getMap() {
    //  return this.map;
    //}
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * marks that the robot is currently stuck
     */
    public void markStuck() {
        this.stuckLeft = true;
        this.stuckRight = true;
    }

    public void markStuck(boolean left, boolean right) {
        this.stuckLeft = left;
        this.stuckRight = right;
    }

    /**
     * marks that the robot is (no longer) stuck
     */
    public void markNotStuck() {
        this.stuckLeft = false;
        this.stuckRight = false;
    }

    public void markStuckLeft() {
        this.stuckLeft = true;
        this.stuckRight = false;
    }

    public void markStuckRight() {
        this.stuckLeft = false;
        this.stuckRight = true;
    }

    /**
     * indicates wheter the robot is currently stuck
     */
    public Boolean isStuck() {
        return this.stuckLeft || this.stuckRight;
    }

    public Boolean isStuckLeft() {
        return this.stuckLeft;
    }

    public Boolean isStuckRight() {
        return this.stuckRight;
    }

    public Boolean isMoving() {
        return (this.sensors[Model.M1] > 0 || this.sensors[Model.M2] > 0);
    }

    public Boolean isTurning() {
        return (this.sensors[Model.M1] != this.sensors[Model.M2]);
    }
    
    public void setNewSweep(int min, int minA, int max, int maxA){
      this.sweepValues[0] = min;
      this.sweepValues[1] = minA;
      this.sweepValues[2] = max;
      this.sweepValues[3] = maxA;
      this.sweepChanged = !this.sweepChanged;
    }
    public boolean hasSweepChanged(){
      return sweepChanged;
    }
    public int[] getSweepValues(){
      return sweepValues.clone();
    }
    
    public int getBarcode(){
        return this.barcode;
    }
    
    public void setBarcode(int barcode){
        this.barcode = barcode;
    }
}
