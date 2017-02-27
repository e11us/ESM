package ellus.ESM.roboSys;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class ExcPython {
	private String	file	= null;
	//
	public String	msg		= null;
	public boolean	ended	= false;
	public boolean	error	= false;
	private Process	p		= null;

	public ExcPython( String file, String name ) {
		if( file == null || name == null )
			return;
		this.file= file;
		sla mini= new sla();
		Thread t= new Thread( mini, "ExcPython-" + name );
		t.start();
	}

	public void closeNow() {
		if( p != null )
			p.destroy();
	}

	private class sla implements Runnable {
		@Override
		public void run() {
			//System.out.print( "ExcPython started.." );
			File py= new File( file );
			//System.out.print( "ExcPython  " + py.getAbsoluteFile() );
			//System.out.print( "ExcPython  " + py.exists() + " " + py.isFile() );
			if( !py.exists() || !py.isFile() )
				return;
			int number1= 10;
			int number2= 32;
			// create arg cmd.
			ArrayList <String> arg= new ArrayList <>();
			arg.add( "python" );
			arg.add( file );
			arg.add( "" + number1 );
			arg.add( "" + number2 );
			try{
				p= ProcessFactory.getProcess( arg );
				//System.out.print( "ExcPython running..." );
				BufferedReader out= new BufferedReader(
						new InputStreamReader( p.getInputStream() ) );
				while( ( msg= out.readLine() ) != null ){
					;
				}
				p.destroy();
			}catch ( Exception ee ){
				ee.printStackTrace();
				ended= true;
				error= true;
				if( p != null ){
					try{
						p.destroy();
					}catch ( Exception eee ){}
				}
			}
			ended= true;
		}
	}
}
//pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
//pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
