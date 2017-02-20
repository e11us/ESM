package ellus.ESM.roboSys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;



public class CMD extends Thread {
	private BufferedReader		INP;
	private BufferedWriter		OUT;
	private Process				p		= null;
	private ArrayList <String>	out;
	//
	public boolean				alive	= false;

	public CMD( String name, ArrayList <String> out ) {
		super( name );
		this.out= out;
		this.start();
	}

	public synchronized void pipe( String msg ) {
		if( !alive )
			return;
		try{
			OUT.write( msg + "\n" );
			OUT.flush();
		}catch ( Exception err ){
			err.printStackTrace();
		}
	}

	public synchronized void terminate() {
		if( p.isAlive() ){
			p.destroyForcibly();
			alive= false;
			p= null;
		}
	}

	@Override
	public void run() {
		try{
			p= ProcessFactory.getProcess( "cmd" );
			INP= new BufferedReader( new InputStreamReader( p.getInputStream() ) );
			OUT= new BufferedWriter( new OutputStreamWriter( p.getOutputStream() ) );
			String inp= null;
			while( p != null && p.isAlive() ){
				alive= true;
				if( ( inp= INP.readLine() ) != null ){
					out.add( "cmd>> " + inp );
				}
			}
			alive= false;
			//
			if( p != null )
				p.destroy();
		}catch ( IOException e ){
			e.printStackTrace();
			if( p != null )
				p.destroy();
			return;
		}catch ( Exception e ){
			e.printStackTrace();
		}
	}
}
