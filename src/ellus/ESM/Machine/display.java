package ellus.ESM.Machine;

import java.util.ArrayList;
import ellus.ESM.setting.SCon;



public class display {
	private static ArrayList <Loger>	logRegular		= new ArrayList <>();
	private static ArrayList <Loger>	logError		= new ArrayList <>();
	private static ArrayList <Loger>	log				= new ArrayList <>();
	private static final int			outprintPedding	= 55;
	private static final String			emptySpace		= "                                                                                 ";

	/*
	public static void println( String inp ) {
		System.out.println( inp );
		log.add( inp );
		while( log.size() > GCSV.DisplayLogSizeMax ){
			log.remove( 0 );
		}
	}
	*/
	public static void println( String inp, String inp2 ) {
		Loger lg= new Loger( inp, inp2, helper.getCurrentTimeStamp() );
		if( SCon._levelTesting_showDisplayOutput ){
			if( inp.length() > outprintPedding )
				System.out.println( inp + "\n|| " + inp2 + "\n" );
			else System.out.println( inp + emptySpace.substring( 0, outprintPedding - inp.length() )
					+ "\n|| " + inp2 + "\n" );
		}
		logRegular.add( lg );
		log.add( lg );
		while( logRegular.size() > SCon.DisplayMsgLogSiz ){
			logRegular.remove( 0 );
		}
		while( log.size() > SCon.DisplayMsgLogSiz * 3 ){
			log.remove( 0 );
		}
	}

	public static void printErr( String inp, String inp2 ) {
		Loger lg= new Loger( inp, inp2, helper.getCurrentTimeStamp() );
		if( SCon._levelTesting_showDisplayOutput ){
			if( inp.length() > outprintPedding )
				System.out.println( inp + " || " + inp2 );
			else System.out.println( inp + emptySpace.substring( 0, outprintPedding - inp.length() ) + "|| " + inp2 );
		}
		logError.add( lg );
		log.add( lg );
		while( logError.size() > SCon.DisplayMsgLogSiz ){
			logError.remove( 0 );
		}
		while( log.size() > SCon.DisplayMsgLogSiz * 3 ){
			log.remove( 0 );
		}
	}

	public static ArrayList <String> getLog() {
		ArrayList <String> res= new ArrayList <>();
		for( Loger tmp : log ){
			res.add( tmp.getMsg() );
		}
		return res;
	}

	public static ArrayList <String> getLogRegular() {
		ArrayList <String> res= new ArrayList <>();
		for( Loger tmp : logRegular ){
			res.add( tmp.getMsg() );
		}
		return res;
	}

	public static ArrayList <String> getLogError() {
		ArrayList <String> res= new ArrayList <>();
		for( Loger tmp : logError ){
			res.add( tmp.getMsg() );
		}
		return res;
	}
}

class Loger {
	protected String	className;
	protected String	Msg;
	protected String	Time;

	public Loger( String i, String ii, String iii ) {
		this.className= i;
		this.Msg= ii;
		this.Time= iii;
	}

	protected String getMsg() {
		return "[" + Time + "] " + className + " " + Msg;
	}
}
