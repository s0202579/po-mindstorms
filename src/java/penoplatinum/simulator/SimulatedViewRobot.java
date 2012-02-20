package penoplatinum.simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.ImageObserver;
import java.util.List;

public class SimulatedViewRobot implements ViewRobot{
  // cached images
  static Image robot;
  
  private SimulatedEntity original;

  public SimulatedViewRobot(SimulatedEntity original) {
    this.original = original;
  }
  

  public void trackMovement(Graphics2D g2d) {
    g2d.setColor(Color.yellow);
    g2d.drawLine( this.getX(), this.getY(), this.getX(), this.getY());
  }
  
  public void renderRobot(Graphics2D g2d, ImageObserver board) { 
    // render robot
    AffineTransform affineTransform = new AffineTransform(); 
    affineTransform.setToTranslation( this.getX() - 20, this.getY() - 20 );
    affineTransform.rotate( -1 * Math.toRadians(this.getDirection()), 20, 20 ); 
    g2d.drawImage( SimulatedViewRobot.robot, affineTransform, board );
    
  }
  
  public void renderSonar(Graphics2D g2d) {
    if( this.getDistances() == null ) { return; }
    for( int i=0; i<this.getDistances().size()-1; i++ ) {
      int angle = (int)(this.getAngles().get(i)) + this.getDirection();
      double rads = Math.toRadians(angle+90);
      int dx = (int)(Math.cos(rads) * this.getDistances().get(i)) * Board.SCALE;
      int dy = (int)(Math.sin(rads) * this.getDistances().get(i)) * Board.SCALE;
      g2d.draw(new Line2D.Float(this.getX(), this.getY(), this.getX() + dx, this.getY() - dy));
    }
  }
  public int getX() {
    return ((int) original.getPosX())*Board.SCALE;
  }
  public int getY() {
    return ((int) original.getPosY())*Board.SCALE;
  }
  public int getDirection() {
    return (int) original.getDir();
  }
  public List<Integer> getDistances() {
    return original.getRobot().getModel().getDistances();
  }
  public List<Integer> getAngles() {
    return original.getRobot().getModel().getAngles();
  }
  
}
