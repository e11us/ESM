package ellus.ESM.roboSys;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;



public class mouse {
	static Robot r= null;

	public static void moveTo( int x, int y ) {
		initRobot();
		r.mouseMove( x, y );
	}

	public static void clickLeft() {
		initRobot();
		r.mousePress( InputEvent.BUTTON1_MASK );
		r.mouseRelease( InputEvent.BUTTON1_MASK );
	}

	public static void wheelDown() {
		initRobot();
		r.mouseWheel( 1 );
	}

	public static void wheelUp() {
		initRobot();
		r.mouseWheel( -1 );
	}

	private static void initRobot() {
		if( r == null ){
			try{
				r= new Robot();
			}catch ( AWTException e ){
				e.printStackTrace();
			}
		}
	}
}
