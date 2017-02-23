package ellus.ESM.data.dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.Machine.BArray;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.SQL.mySQLportal;
import ellus.ESM.data.SQL.sqlResult;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.setting.SMan;



/*||----------------------------------------------------------------------------------------------
 ||| a static dictinoary class for word learning.
||||--------------------------------------------------------------------------------------------*/
public class dictDB {
	// existing database.
	protected final static int					updateMultiMax		= 16;
	protected static BArray <String>			updateDB			= new BArray <>();
	private static ArrayList <dictDB_dfGetter>	updater				= null;
	private static int							updateDB_tot;
	private static int							updateDB_lastPerc	= 0;
	private static ButtonTextFS					updateBut			= null;
	private static boolean						updateIng			= false;
	//
	private static ArrayList <String>			sqlName				= null;
	private static final String					sqlfuncName			= "DictionaryEng";
	// for update db.
	//private final ArrayList <dictDB_dfGetter>	dfGetter			= new ArrayList <>();

	/*||----------------------------------------------------------------------------------------------
	 ||| get def for a word.
	||||--------------------------------------------------------------------------------------------*/
	public static String getWord( String word ) {
		ArrayList <String> name= new ArrayList <>();
		ArrayList <String> val= new ArrayList <>();
		name.add( "keyword" );
		val.add( word.toLowerCase() );
		ArrayList <sqlResult> res= mySQLportal.getByFunc( "DictionaryEng", 5, name, val );
		if( res.size() > 0 ){
			ArrayList <String> def= helper.str2ALstr( (String)res.get( 0 ).val.get( 4 ) );
			def.add( 0, "" );
			def.add( 0, ">> " + word + " <<" );
			char s;
			for( int i= 2; i < def.size(); i++ ){
				s= def.get( i ).charAt( 0 );
				if( s == 'r' || s == 'S' ){
					def.remove( i );
					i-- ;
					continue;
				}
				if( s == '-' ){
					def.remove( i );
					def.add( i, "" );
				}
			}
			return helper.ALstr2str( def );
		}else return null;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| stop a update in progress.
	||||--------------------------------------------------------------------------------------------*/
	public static void updateDB_Stop() {
		if( updateIng ){
			try{
				updateIng= false;
				updateDB= null;
				updateBut.addSeconMsg( null );
				//
				dictDB_dfGetter up;
				while( updater != null ){
					for( int i= 0; i < updater.size(); i++ ){
						up= updater.get( i );
						if( !up.running ){
							updater.remove( i );
							i-- ;
						}
					}
					if( updater.size() == 0 )
						updater= null;
				}
			}catch ( Exception e ){
				e.printStackTrace();
			}
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| start update the db.
	||||--------------------------------------------------------------------------------------------*/
	public static void updateDB( ButtonTextFS inp ) {
		updateIng= !updateIng;
		if( !updateIng ){
			updateDB= null;
			inp.addSeconMsg( null );
			return;
		}
		//
		updateBut= inp;
		inp.addSeconMsg( "loading all word..." );
		updateDB= new BArray <>();
		Scanner rdr;
		Scanner rdr1;
		String line;
		String word;
		int ind= 1;
		//
		for( String wordFile : helper.getDirFile( SMan.getSetting( 0 ) + SMan.getSetting( 205 ),
				"" ) ){
			File words= new File( wordFile );
			display.println( "dictionary.dictDB", "Doing: " + wordFile );
			try{
				rdr= new Scanner( words );
				while( rdr.hasNextLine() ){
					line= rdr.nextLine();
					rdr1= new Scanner( helper.clearNonAlpha( line ).toLowerCase() );
					while( rdr1.hasNext() ){
						word= rdr1.next();
						if( !updateDB.contain( word ) )
							updateDB.add( word );
					}
					rdr1.close();
				}
				rdr.close();
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
			updateBut.addSeconMsg( "file: " + ind++ );
		}
		//
		display.println( "dictionary.dictDB", "total of: " + updateDB.size() + " of new words to DL" );
		updateDB_lastPerc= 0;
		updateDB_tot= updateDB.size();
		//
		updater= new ArrayList <>();
		for( int i= 0; i < updateMultiMax; i++ ){
			dictDB_dfGetter dg= new dictDB_dfGetter( "updater_" + ( i + 1 ) );
			updater.add( dg );
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| create the folder if needed.
	||||--------------------------------------------------------------------------------------------*/
	public static void creatDict() {
		File dictM= new File( SMan.getSetting( 0 ) + SMan.getSetting( 3 ) );
		if( !dictM.exists() ){
			dictM.mkdirs();
		}
		File dictMp3= new File( SMan.getSetting( 0 ) + SMan.getSetting( 201 ) );
		if( !dictMp3.exists() ){
			dictMp3.mkdirs();
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| insert a local file into sql db.
	||||--------------------------------------------------------------------------------------------*/
	public static synchronized void insert2SQL( String path ) {
		if( path == null || ! ( new File( path ).exists() ) )
			return;
		ArrayList <String> cont= helper.readFile( path );
		if( cont == null )
			return;
		if( sqlName == null ){
			sqlName= new ArrayList <>();
			sqlName.add( "functional" );
			sqlName.add( "keyword" );
			sqlName.add( "content" );
		}
		//
		ArrayList <String> sqlVal= new ArrayList <>();
		sqlVal.add( sqlfuncName );
		sqlVal.add( helper.getFileName( helper.getFilePathName( path ) ) );
		sqlVal.add( helper.ALstr2str( cont ) );
		//
		if( mySQLportal.insert( sqlName, sqlVal ) ){
			( new File( path ) ).delete();
			display.println( "dictionary.dictDB", "good insert: " + sqlVal.get( 1 ) );
			try{
				Thread.sleep( 5 );
			}catch ( InterruptedException e ){}
		}else display.println( "dictionary.dictDB", "bad insert: " + path );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get the next word that need to be dl, only used for update.
	||||--------------------------------------------------------------------------------------------*/
	protected synchronized static String getNextUpdateWord() {
		if( updateDB == null || updateDB.size() == 0 ){
			updateBut.addSeconMsg( null );
			return null;
		}
		//
		if( updateDB_lastPerc != (int) ( ( updateDB_tot - updateDB.size() ) / (double)updateDB_tot * 100 ) ){
			updateDB_lastPerc= (int) ( ( updateDB_tot - updateDB.size() ) / (double)updateDB_tot * 100 );
			display.println( "dictionary.dictDB", "currently: " + updateDB_lastPerc + "% is done." );
			if( updateBut != null ){
				updateBut.addSeconMsg( updateDB_lastPerc + "% done" );
			}
		}
		String res= updateDB.get( 0 );
		updateDB.remove( 0 );
		return res;
	}
}