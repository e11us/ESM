package ellus.ESM.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.joda.time.DateTime;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.setting.SCon;



public class CalendarOneTimeEvent implements CalendarEvent {
	private int		syear, smon, sday, shur;
	private int		eyear, emon, eday;
	private int		fyear	= 0, fmon= 0, fday= 0, fhur= 0;
	private String	name, info, path;
	private boolean	done	= false;

	public CalendarOneTimeEvent( String path ) {
		this.path= path;
		load();
	}

	public String getInitDate() {
		return syear + "-" + smon + "-" + sday + " Hour: " + shur;
	}

	public String getInfo() {
		return info;
	}

	public boolean isDone() {
		return done;
	}

	private void load() {
		File spath= new File( path );
		if( !spath.exists() || !spath.isFile() ){
			display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			return;
		}
		try{
			Scanner rdr= new Scanner( spath );
			if( rdr.hasNextLine() ){
				if( rdr.nextLine().equals( "false" ) )
					done= false;
				else{
					done= true;
				}
			}else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextLine() )
				name= rdr.nextLine();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				syear= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				smon= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				sday= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				shur= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				eyear= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				emon= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				eday= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				fyear= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				fmon= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				fday= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				fhur= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			info= "";
			while( rdr.hasNext() ){
				if( info.length() == 0 )
					info= rdr.next();
				else info+= " " + rdr.next();
			}
			//display.println( this.getClass().toGenericString(), name + " Load all good." );
		}catch ( FileNotFoundException e ){
			display.printErr( this.getClass().toGenericString(), "error: " + e.getMessage() );
			e.printStackTrace();
		}
	}

	public void log() {
		DateTime cur= new DateTime();
		fyear= cur.getYear();
		fmon= cur.getMonthOfYear();
		fday= cur.getDayOfMonth();
		fhur= cur.getHourOfDay();
		// delete existing one.
		( new File( path ) ).delete();
		done= true;
		path= ( new File( path ) ).getParent().toString();
		store();
	}

	public CalendarOneTimeEvent( String name, String info, String path, int[] inp ) {
		this.name= name;
		this.info= info;
		this.path= path;
		eyear= inp[0];
		emon= inp[1];
		eday= inp[2];
		//
		store();
		//display.println( this.getClass().toString(), "new one-time event rule created" );
	}

	private void store() {
		File spath= new File( path );
		if( !spath.exists() )
			spath.mkdirs();
		try{
			PrintWriter pw= new PrintWriter( spath.getCanonicalPath() + "/" + helper.getCurrentTimeStamp() + " " + name
					+ SCon.ExtCalOEvn );
			DateTime cur= new DateTime();
			// finished or not.
			pw.println( done );
			// name
			pw.println( name );
			// startding date.
			pw.println(
					cur.getYear() + " " + cur.getMonthOfYear() + " " + cur.getDayOfMonth() + " " + cur.getHourOfDay() );
			// end date.
			pw.println( eyear + " " + emon + " " + eday );
			// actual finish date.
			pw.println( fyear + " " + fmon + " " + fday + " " + fhur );
			// info.
			if( info == null || info.length() == 0 )
				pw.println( " " );
			else pw.println( info );
			pw.close();
			display.println( this.getClass().toGenericString(), name + " Store all good." );
		}catch ( IOException e ){
			display.printErr( this.getClass().toGenericString(), "error: " + e.getMessage() );
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getNt() {
		return new String( eyear + " " + emon + " " + eday );
	}

	@Override
	public long getSecLeft() {
		DateTime nt= new DateTime( eyear, emon, eday, 0, 0 );
		DateTime cur= new DateTime();
		return (int) ( ( nt.getMillis() - cur.getMillis() ) / 1000 );
	}

	@Override
	public int getInterval() {
		return 0;
	}

	@Override
	public String getTimeLeft() {
		DateTime nt= new DateTime( eyear, emon, eday, 0, 0 );
		DateTime cur= new DateTime();
		int day= (int) ( ( nt.getMillis() - cur.getMillis() ) / 1000.0 / 3600 / 24 );
		if( day > 1 )
			return day + " days";
		else if( day == 1 )
			return "1 day";
		else if( day == 0 ){
			int hour= (int) ( ( nt.getMillis() - cur.getMillis() ) / 1000.0 / 3600 );
			if( hour > 1 )
				return hour + " hours";
			else if( hour == 1 )
				return "1 hour";
			else return "due now";
		}else if( day == -1 ){
			return "past due 1 day";
		}else{
			return "past due " + ( -1 * day ) + " day";
		}
	}
}
