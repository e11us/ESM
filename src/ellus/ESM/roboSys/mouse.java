package ellus.ESM.roboSys;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;



public class mouse {
	private static Robot r= null;
	private static Toolkit tk= Toolkit.getDefaultToolkit();

	public static void moveTo( int x, int y ) {
		initRobot();
		r.mouseMove( x, y );
	}
	
	public static void moveOSet( int x, int y ) {
		initRobot();		
		r.mouseMove( x, y );
	}
	
	public static void moveToCenter() {
		initRobot();		
		r.mouseMove( (int)(tk.getScreenSize().getWidth() / 2),
				(int)(tk.getScreenSize().getHeight() / 2 ) );
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
