package ellus.ESM.roboSys;

import java.util.ArrayList;



public class ProcessFactory {
	public synchronized static Process getProcess( ArrayList <String> inp ) throws Exception {
		if( inp == null || inp.size() == 0 )
			return null;
		return ( new ProcessBuilder( inp ) ).start();
	}

	public synchronized static Process getProcess( String inp ) throws Exception {
		if( inp == null || inp.length() == 0 )
			return null;
		return ( new ProcessBuilder( inp ) ).start();
	}
}
