import lejos.nxt.*;
import java.io.*;

public class Interface {

	public static void main(String [] args) {
		selectAngles(3);
	}

	static void selectAngles(int angles){
		LCD.drawString("Select number of",0,0);
		LCD.drawString("angles:",1,0);
		if((angles<100)){LCD.drawString(angles,4,8)};
		else {LCD.drawString(angles,4,7)};
		while (true) {
		  if (Button.ENTER.isPressed()) selectLength(angles,500);
		  if (Button.ESCAPE.isPressed()) System.exit();
		  if (Button.LEFT.isPressed()){ 
			if((angles-1)>=3){selectAngles((angles-1))};
			else { selectAngles(angles)};
			};
		  if (Button.RIGHT.isPressed()) selectAngles((angles+1));
		}
	}
	
	static void selectLength(int angles, int length){
		LCD.drawString("Select the",0,0);
		LCD.drawString("length:",1,0);
		if((length<100)){LCD.drawString(length,4,8)};
		else {LCD.drawString(length,4,7)};
		while (true) {
		  if (Button.ENTER.isPressed()) veelhoek();
		  if (Button.ESCAPE.isPressed()) System.exit();
		  if (Button.LEFT.isPressed()){ 
			if((length-10)>=10){selectLength((length-10))};
			else { selectLength(length)};
			};
		  if (Button.RIGHT.isPressed()) selectLength((length+10));
		}
	
	
	}


}