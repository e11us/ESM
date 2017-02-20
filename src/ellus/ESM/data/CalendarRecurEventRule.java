package ellus.ESM.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import org.joda.time.DateTime;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.setting.SCon;



public class CalendarRecurEventRule implements CalendarEvent {
	private int					syear, smon, sday;
	private int					nyear, nmon, nday;
	private int					interval;
	private String				name;
	private String				path;
	private ArrayList <String>	log	= new ArrayList <>();

	public CalendarRecurEventRule( String path ) {
		if( path == null )
			return;
		this.path= path;
		load();
	}

	public CalendarRecurEventRule( String name, String path, int[] inp ) {// syear, smon,sday, interval.
		this.name= name;
		this.path= path;
		this.nyear= this.syear= inp[0];
		this.nmon= this.smon= inp[1];
		this.nday= this.sday= inp[2];
		this.interval= inp[3];
		File spath= new File( path + "/" + name + SCon.ExtCalREvn );
		if( spath.exists() && spath.isFile() ){
			display.printErr( this.getClass().toGenericString(), "error: " + name + " rule already exists" );
			return;
		}
		//
		store();
		//display.println( this.getClass().toString(), "new CalendarRecurEventRule created" );
	}

	@Override
	public String getNt() {
		return new String( nyear + " " + nmon + " " + nday );
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getSecLeft() {
		DateTime nt= new DateTime( nyear, nmon, nday, 0, 0 );
		DateTime cur= new DateTime();
		return (int) ( ( nt.getMillis() - cur.getMillis() ) / 1000 );
	}

	@Override
	public int getInterval() {
		return interval;
	}

	@Override
	public String getTimeLeft() {
		DateTime nt= new DateTime( nyear, nmon, nday, 0, 0 );
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

	public void log() {
		DateTime cur= new DateTime();
		DateTime nt= new DateTime( nyear, nmon, nday, 0, 0 );
		log.add( cur.getYear() + " " + cur.getMonthOfYear() + " " + cur.getDayOfMonth() + " " +
				cur.getHourOfDay() + " " + cur.getMinuteOfHour() + " " + cur.getSecondOfMinute() + " " +
				(int) ( cur.getMillis() - nt.getMillis() / 1000 ) );
		nt= nt.plusDays( interval );
		nyear= nt.getYear();
		nmon= nt.getMonthOfYear();
		nday= nt.getDayOfMonth();
		store();
	}

	public void logReset( int intval ) {
		DateTime cur= new DateTime();
		DateTime nt= new DateTime( nyear, nmon, nday, 0, 0 );
		log.add( cur.getYear() + " " + cur.getMonthOfYear() + " " + cur.getDayOfMonth() + " " +
				cur.getHourOfDay() + " " + cur.getMinuteOfHour() + " " + cur.getSecondOfMinute() + " " +
				(int) ( cur.getMillis() - nt.getMillis() / 1000 ) );
		log.add( 0, (char)SCon.calendarEventPastRuleSignal + " " + syear + " " + smon + " " + sday + " "
				+ interval );
		syear= cur.getYear();
		smon= cur.getMonthOfYear();
		sday= cur.getDayOfMonth();
		if( intval > 0 )
			interval= intval;
		nt= cur.plusDays( interval );
		nyear= nt.getYear();
		nmon= nt.getMonthOfYear();
		nday= nt.getDayOfMonth();
		store();
	}

	private void store() {
		File spath= new File( path );
		if( !spath.exists() )
			spath.mkdirs();
		try{
			PrintWriter pw= new PrintWriter( spath.getCanonicalPath() + "/" + name + SCon.ExtCalREvn );
			pw.println( syear + " " + smon + " " + sday + " " + interval );
			pw.println( nyear + " " + nmon + " " + nday );
			pw.println( "=== past log ====================" );
			for( String tmp : log ){
				if( tmp.length() > 0 && tmp.charAt( 0 ) != '=' )
					pw.println( tmp );
			}
			pw.close();
			display.println( this.getClass().toGenericString(), name + " Store all good." );
		}catch ( IOException e ){
			display.printErr( this.getClass().toGenericString(), "error: " + e.getMessage() );
			e.printStackTrace();
		}
	}

	private void load() {
		File spath= new File( path );
		if( !spath.exists() || !spath.isFile() ){
			display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			return;
		}
		this.path= spath.getParent();
		this.name= helper.getFileName( spath.getName() );
		try{
			Scanner rdr= new Scanner( spath );
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
				interval= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				nyear= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				nmon= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			if( rdr.hasNextInt() )
				nday= rdr.nextInt();
			else{
				display.printErr( this.getClass().toGenericString(), "error: invalid file" );
			}
			while( rdr.hasNextLine() ){
				log.add( rdr.nextLine() );
			}
			//display.println( this.getClass().toGenericString(), name + " Load all good." );
		}catch ( FileNotFoundException e ){
			display.printErr( this.getClass().toGenericString(), "error: " + e.getMessage() );
			e.printStackTrace();
		}
	}
}
