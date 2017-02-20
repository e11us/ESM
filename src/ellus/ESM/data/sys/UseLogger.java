package ellus.ESM.data.sys;

import java.io.File;
import java.util.ArrayList;
import ellus.ESM.Machine.helper;
import ellus.ESM.setting.SMan;



public class UseLogger {
	private static String	logFile	= SMan.getSetting( 0 ) + SMan.getSetting( 108 ) +
			"/" + helper.getCurrentDateStamp() + ".txt";
	private static boolean	_init	= false;

	public static void log( String inp ) {
		if( !_init )
			createDir();
		helper.append2File( logFile, "Time: " + helper.getCurrentTimeStampMS() + " Event: " + inp );
	}

	public static void log( ArrayList <String> inp ) {
		if( !_init )
			createDir();
		helper.append2File( logFile, inp );
	}

	private static void createDir() {
		new File( SMan.getSetting( 0 ) + SMan.getSetting( 108 ) ).mkdirs();
		_init= true;
	}
}
