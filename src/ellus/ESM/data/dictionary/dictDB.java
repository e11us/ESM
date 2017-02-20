package ellus.ESM.data.dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.WordEng;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.setting.SMan;



/*||----------------------------------------------------------------------------------------------
 ||| a static dictinoary class for word learning.
||||--------------------------------------------------------------------------------------------*/
public class dictDB {
	// existing database.
	protected final static ArrayList <WordEng>	englishWords		= new ArrayList <>();
	protected final static ArrayList <String>	exitingDB			= new ArrayList <>();
	protected final static int					updateMultiMax		= 16;
	//
	protected static ArrayList <String>			updateDB			= new ArrayList <>();
	private static ArrayList <dictDB_dfGetter>	updater				= null;
	private static int							updateDB_tot;
	private static int							updateDB_lastPerc	= 0;
	private static ButtonTextFS					updateBut			= null;
	private static boolean						updateIng			= false;

	// for update db.
	//private final ArrayList <dictDB_dfGetter>	dfGetter			= new ArrayList <>();
	public static WordEng getWord( String word ) {
		// check DB first.
		display.println( "dictionary.dictDB", "Getting word: " + word + " - Total in lib: " + exitingDB.size() );
		for( String spell : exitingDB ){
			if( spell.toLowerCase().equals( word.toLowerCase() ) ){
				WordEng aWord= new WordEng( word + ".txt",
						SMan.getSetting( 0 ) + SMan.getSetting( 202 ),
						SMan.getSetting( 0 ) + SMan.getSetting( 201 ) );
				englishWords.add( aWord );
				return aWord;
			}
		}
		// check if known bad word.
		if( new File( SMan.getSetting( 0 ) + SMan.getSetting( 204 ) + "/" + word ).exists() )
			return null;
		// get it now. load it in db.
		dictDB_dfGetter nwg= new dictDB_dfGetter();
		nwg.updateAword( word );
		WordEng aWord= new WordEng( word + ".txt",
				SMan.getSetting( 0 ) + SMan.getSetting( 202 ),
				SMan.getSetting( 0 ) + SMan.getSetting( 201 ) );
		if( aWord.getSpell() != null ){
			exitingDB.add( word );
			englishWords.add( aWord );
			return aWord;
		}
		return null;
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
		updateDB= new ArrayList <>();
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
						//if( !exitingDB.contains( word ))
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
		File dictBadMp3= new File( SMan.getSetting( 0 ) + SMan.getSetting( 203 ) );
		if( !dictBadMp3.exists() ){
			dictBadMp3.mkdirs();
		}
		File dictTxt= new File( SMan.getSetting( 0 ) + SMan.getSetting( 202 ) );
		if( !dictTxt.exists() ){
			dictTxt.mkdirs();
		}
		File dictBadTxt= new File( SMan.getSetting( 0 ) + SMan.getSetting( 204 ) );
		if( !dictBadTxt.exists() ){
			dictBadTxt.mkdirs();
		}
		// load db with multi loader.
		ArrayList <String> wordT;
		ArrayList <String> words= helper.getDirFile(
				SMan.getSetting( 0 ) + SMan.getSetting( 202 ), "txt" );
		int loaderInd= 1;
		while( words.size() > 5900 ){
			wordT= new ArrayList <>();
			for( int i= 0; i < 5900; i++ ){
				wordT.add( words.get( 0 ) );
				words.remove( 0 );
			}
			dictDB_Loader loader= new dictDB_Loader( exitingDB, englishWords, wordT, loaderInd++ );
		}
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

/*||----------------------------------------------------------------------------------------------
 ||| a sub class to load the dict DB on its own thread.
||||--------------------------------------------------------------------------------------------*/
class dictDB_Loader implements Runnable {
	ArrayList <String>	db		= null;
	ArrayList <WordEng>	dbE		= null;
	ArrayList <String>	words	= null;

	public dictDB_Loader( ArrayList <String> db, ArrayList <WordEng> dbE, ArrayList <String> words, int ind ) {
		this.db= db;
		this.dbE= dbE;
		this.words= words;
		Thread tt= new Thread( this, "Dict DB loader_#" + ind );
		tt.start();
	}

	@Override
	public void run() {
		display.println( this.getClass().toString(), "Starts to load Dict DB." );
		int count= 0;
		for( String words : words ){
			File spath= new File( words );
			db.add( helper.getFileName( spath.getName() ) );
		}
		display.println( this.getClass().toString(), "Dict DB loading finished, total: " + db.size() + " words." );
	}
}