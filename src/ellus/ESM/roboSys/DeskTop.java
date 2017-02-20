package ellus.ESM.roboSys;

import java.awt.Desktop;
import java.util.Timer;
import java.util.TimerTask;



public class DeskTop {
	private static Desktop		dt	= null;
	private static Timer		tm	= null;
	// limit to only
	protected static Integer	left= 15;

	public synchronized static Desktop getDeskTop() {
		if( dt == null )
			init();
		left-- ;
		if( left < 0 )
			return null;
		else return dt;
	}

	private static void init() {
		dt= Desktop.getDesktop();
		tm= new Timer( "DestTop getter Renewal Timer" );
		tm.schedule( ( new renewal( left ) ), 1000, 1000 );
	}
}

class renewal extends TimerTask {
	Integer	i;
	int		lf;

	public renewal( Integer inp ) {
		lf= i= inp;
	}

	@Override
	public void run() {
		if( i < lf )
			i= i + 1;
	}
}