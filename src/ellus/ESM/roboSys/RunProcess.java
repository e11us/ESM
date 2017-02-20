package ellus.ESM.roboSys;

public class RunProcess {
	public static void runProcess( String path ) throws Exception {// ex: path to the exe
		Process process= ProcessFactory.getProcess( path );
	}
}
