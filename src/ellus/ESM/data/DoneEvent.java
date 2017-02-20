package ellus.ESM.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.Machine.helper;
import ellus.ESM.setting.SMan;



public class DoneEvent {
	private String				name	= "";
	private String				com		= "";
	private String				extr	= "";
	private String				Date	= "";
	public static final char	dateSig	= 3;

	public String getName() {
		return name;
	}

	public String getCom() {
		return com;
	}

	public String getExtr() {
		return extr;
	}

	public String getDate() {
		return Date;
	}

	public static void LogEvent( String name, String com, String extr ) {
		//
		File fol= new File( SMan.getSetting( 0 ) + SMan.getSetting( 8 ) );
		if( !fol.exists() ){
			fol.mkdirs();
		}
		//
		try{
			PrintWriter pw= new PrintWriter( SMan.getSetting( 0 ) + SMan.getSetting( 8 ) +
					"/" + helper.getCurrentTimeStampMS() + ".txt" );
			pw.println( name );
			pw.println( com );
			pw.println( extr );
			pw.close();
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
		}
	}

	public static ArrayList <DoneEvent> getAll() {
		Scanner rdr;
		ArrayList <DoneEvent> ret= new ArrayList <>();
		DoneEvent lg;
		//
		for( String file : helper.getAllFile( SMan.getSetting( 0 ) + SMan.getSetting( 8 ),
				"txt" ) ){
			try{
				rdr= new Scanner( new File( file ) );
				lg= new DoneEvent();
				lg.Date= helper.getFileName( helper.getFilePathName( file ) );
				if( rdr.hasNextLine() )
					lg.name= rdr.nextLine();
				if( rdr.hasNextLine() )
					lg.com= rdr.nextLine();
				if( rdr.hasNextLine() )
					lg.extr= rdr.nextLine();
				ret.add( lg );
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static ArrayList <String> getAllAsLine() {
		Scanner rdr;
		ArrayList <String> ret= new ArrayList <>();
		//
		for( String file : helper.getAllFile( SMan.getSetting( 0 ) + SMan.getSetting( 8 ),
				"txt" ) ){
			try{
				rdr= new Scanner( new File( file ) );
				ret.add( dateSig + " " + helper.getFileName( helper.getFilePathName( file ) ) );
				if( rdr.hasNextLine() )
					ret.add( rdr.nextLine() );
				if( rdr.hasNextLine() )
					ret.add( rdr.nextLine() );
				if( rdr.hasNextLine() )
					ret.add( rdr.nextLine() );
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
		return ret;
	}
}
