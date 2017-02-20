package ellus.ESM.setting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;



/*||----------------------------------------------------------------------------------------------
 ||| settting that is stored in file.
||||--------------------------------------------------------------------------------------------*/
public class SMan {
	// setting for a given path. ( for each object. )
	private String				path			= null;
	private ArrayList <cK>		indiStr			= null;
	//
	private static final int	commentStartInd	= 70;

	/*||----------------------------------------------------------------------------------------------
	 ||| reload all setting of a given path. ( path is a folder & has .setting file in it. )
	||||--------------------------------------------------------------------------------------------*/
	public SMan( String path ) {
		File sett= new File( path );
		if( !sett.exists() || !sett.isDirectory() )
			return;
		sett= new File( path + "/.setting" );
		if( !sett.exists() || !sett.isFile() )
			return;
		this.path= path;
		indiStr= new ArrayList <>();
		try{
			Scanner rdr= new Scanner( sett );
			Scanner rdr2;
			String line;
			String type= null, cont= null;
			String tmp;
			String com;
			int ind= -1;
			while( rdr.hasNextLine() ){
				ind= -1;
				type= cont= com= null;
				//
				line= rdr.nextLine();
				if( line.length() >= 2 && line.charAt( 0 ) == '/' && line.charAt( 1 ) == '/' )
					continue;
				//
				rdr2= new Scanner( line );
				if( rdr2.hasNextInt() )
					ind= rdr2.nextInt();
				else continue;
				if( rdr2.hasNext() )
					type= rdr2.next();
				else continue;
				if( rdr2.hasNext() ){
					cont= "";
					while( rdr2.hasNext() ){
						tmp= rdr2.next();
						if( tmp.equals( "//" ) ){
							while( rdr2.hasNext() ){
								if( com == null )
									com= rdr2.next();
								else com+= " " + rdr2.next();
							}
							break;
						}else if( cont.length() == 0 )
							cont= tmp;
						else cont+= " " + tmp;
					}
				}else continue;
				populateIndToInd( ind );
				indiStr.get( ind ).ind= ind;
				indiStr.get( ind ).type= type;
				indiStr.get( ind ).cont= cont;
				indiStr.get( ind ).comment= com;
			}
			rdr.close();
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get a certain setting at a slot. for individual settings.
	||||--------------------------------------------------------------------------------------------*/
	public String getSettingInd( int ind ) {
		if( indiStr == null )
			return null;
		if( ind <= indiStr.size() - 1 && ind >= 0 ){
			return indiStr.get( ind ).cont;
		}
		return null;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| print valid settting. ( skip empty slots )
	||||--------------------------------------------------------------------------------------------*/
	public void printValidSettingInd() {
		if( indiStr == null )
			return;
		//
		int x= 0;
		if( indiStr.size() > 0 ){
			display.println( ( new SMan() ).getClass().toString(),
					"Printing all valid settings for: " + path + " ==============" );
			for( cK key : indiStr ){
				if( key.type != null && key.cont != null ){
					display.println( this.getClass().toString(), key.toString() );
					x++ ;
				}
			}
			display.println( this.getClass().toString(), "valid setting printing done. total: " + x
					+ " ====================================================" );
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| populate the setting array to sertain index.
	||||--------------------------------------------------------------------------------------------*/
	private void populateIndToInd( int ind ) {
		while( indiStr.size() < ind + 1 ){
			indiStr.add( new cK() );
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| change a particular line in the setting.
	||||--------------------------------------------------------------------------------------------*/
	public synchronized void changeSettingInd( int ind, String inp ) {
		if( ind < 0 || inp == null || indiStr.get( ind ) == null )
			return;
		//
		// change the setting.
		indiStr.get( ind ).cont= inp;
		//
		ArrayList <String> sett= helper.readFile( path + "/.setting" );
		Scanner rdr;
		String tmp;
		String li;
		int iii;
		String empty= "                                                                                              ";
		for( int i= 0; i < sett.size(); i++ ){
			tmp= sett.get( i );
			rdr= new Scanner( tmp );
			// for each line in .setting file.
			if( rdr.hasNextInt() ){
				iii= rdr.nextInt();
				if( indiStr.get( iii ).cont != null ){
					sett.remove( i );
					li= iii + " " + indiStr.get( iii ).type + " " + indiStr.get( iii ).cont;
					if( li.length() < commentStartInd ){
						li= li + empty.substring( 0, commentStartInd - li.length() );
					}
					li= li + " // " + indiStr.get( iii ).comment;
					sett.add( i, li );
				}
			}
			rdr.close();
		}
		try{
			PrintWriter pw= new PrintWriter( path + "/.setting" );
			for( String tt : sett ){
				pw.println( tt );
			}
			pw.close();
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| static method: for settingChanager only, to get the key.
	||||--------------------------------------------------------------------------------------------*/
	protected cK getCKInd( int ind ) {
		if( ind > indiStr.size() - 1 || indiStr.get( ind ).cont == null )
			return null;
		return indiStr.get( ind );
	}

	// setttings stroed in here, need to know setting ind and type. ( general setting for ESM. )
	private static ArrayList <cK>	conStr		= null;
	private static int				validSetting= 0;

	/*||----------------------------------------------------------------------------------------------
	 ||| constructor to read in settings for ESM & call SCon.SLCVE() to set all others.
	||||--------------------------------------------------------------------------------------------*/
	public SMan() {
		conStr= new ArrayList <>();
		validSetting= 0;
		File ESMst= new File( SCon.PathESMsetting );
		if( !ESMst.exists() ){
			try{
				ESMst.createNewFile();
			}catch ( IOException e ){
				e.printStackTrace();
			}
		}
		try{
			Scanner rdr= new Scanner( ESMst );
			Scanner rdr2;
			String line;
			String type= null, cont= null;
			String tmp;
			String com;
			int ind= -1;
			while( rdr.hasNextLine() ){
				ind= -1;
				type= cont= com= null;
				//
				line= rdr.nextLine();
				if( line.length() >= 2 && line.charAt( 0 ) == '/' && line.charAt( 1 ) == '/' )
					continue;
				//
				rdr2= new Scanner( line );
				if( rdr2.hasNextInt() )
					ind= rdr2.nextInt();
				else continue;
				if( rdr2.hasNext() )
					type= rdr2.next();
				else continue;
				if( rdr2.hasNext() ){
					cont= "";
					while( rdr2.hasNext() ){
						tmp= rdr2.next();
						if( tmp.equals( "//" ) ){
							while( rdr2.hasNext() ){
								if( com == null )
									com= rdr2.next();
								else com+= " " + rdr2.next();
							}
							break;
						}else if( cont.length() == 0 )
							cont= tmp;
						else cont+= " " + tmp;
					}
				}else continue;
				//
				populateToInd( ind );
				conStr.get( ind ).ind= ind;
				conStr.get( ind ).type= type;
				conStr.get( ind ).cont= cont;
				conStr.get( ind ).comment= com;
				validSetting++ ;
			}
			rdr.close();
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
		}
		//
		// call const setting to see others. --------------------- very important, must be done, after init the SM.
		SCon.SLCVE();
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| static method: reload all settings.
	||||--------------------------------------------------------------------------------------------*/
	public static void reloadAllSetting() {
		initSM();
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| static method: get a certain setting at a slot.
	||||--------------------------------------------------------------------------------------------*/
	public static String getSetting( int ind ) {
		// check if init, if not init it once.
		if( conStr == null )
			initSM();
		// now get the setting requested.
		if( ind <= conStr.size() - 1 && ind >= 0 ){
			return conStr.get( ind ).cont;
		}
		return null;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| static method: print all settting.
	||||--------------------------------------------------------------------------------------------*/
	public static void printAllSetting() {
		// check if init, if not init it once.
		if( conStr == null )
			initSM();
		//
		display.println( ( new SMan() ).getClass().toString(),
				"Printing all the settings, total: " + conStr.size() + " ==============" );
		for( cK key : conStr ){
			display.println( ( new SMan() ).getClass().toString(), key.toString() );
		}
		display.println( ( new SMan() ).getClass().toString(),
				"all setting printing done. total: " + conStr.size() + " ==============" );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| static method: print valid settting. ( skip empty slots )
	||||--------------------------------------------------------------------------------------------*/
	public static void printValidSetting() {
		// check if init, if not init it once.
		if( conStr == null )
			initSM();
		//
		if( conStr.size() > 0 ){
			display.println( ( new SMan() ).getClass().toString(),
					"Printing all valid settings, total: " + validSetting + " ==============" );
			for( cK key : conStr ){
				if( key.type != null && key.cont != null )
					display.println( ( new SMan() ).getClass().toString(), key.toString() );
			}
			display.println( ( new SMan() ).getClass().toString(),
					"valid setting printing done. total: " + validSetting + " ==============" );
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| static method: change a particular line in the setting.
	||||--------------------------------------------------------------------------------------------*/
	public static synchronized void changeSetting( int ind, String inp ) {
		if( ind < 0 || inp == null || conStr.get( ind ) == null )
			return;
		//
		// change the setting.
		conStr.get( ind ).cont= inp;
		//
		ArrayList <String> sett= helper.readFile( SCon.PathESMsetting );
		Scanner rdr;
		String tmp;
		String li;
		int iii;
		String empty= "                                                                                              ";
		for( int i= 0; i < sett.size(); i++ ){
			tmp= sett.get( i );
			rdr= new Scanner( tmp );
			// for each line in .setting file.
			if( rdr.hasNextInt() ){
				iii= rdr.nextInt();
				if( conStr.get( iii ).cont != null ){
					sett.remove( i );
					li= iii + " " + conStr.get( iii ).type + " " + conStr.get( iii ).cont;
					if( li.length() < commentStartInd ){
						li= li + empty.substring( 0, commentStartInd - li.length() );
					}
					li= li + " // " + conStr.get( iii ).comment;
					sett.add( i, li );
				}
			}
			rdr.close();
		}
		try{
			PrintWriter pw= new PrintWriter( SCon.PathESMsetting );
			for( String tt : sett ){
				pw.println( tt );
			}
			pw.close();
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| static method: for settingChanager only, to get the key.
	||||--------------------------------------------------------------------------------------------*/
	protected static cK getCK( int ind ) {
		if( ind > conStr.size() - 1 || conStr.get( ind ).cont == null )
			return null;
		return conStr.get( ind );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| static method: populate the setting array to sertain index.
	||||--------------------------------------------------------------------------------------------*/
	private static void populateToInd( int ind ) {
		while( conStr.size() < ind + 1 ){
			conStr.add( new cK() );
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| static method: first time init.
	||||--------------------------------------------------------------------------------------------*/
	private static void initSM() {
		SMan SM= new SMan();
	}
}
