package ellus.ESM.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.Machine.helper;
import ellus.ESM.media.PlayerAudio;



public class WordEng {
	// spell of word.
	private String				word		= null;
	// audio file path.
	private String				audioFile	= null;
	// defi
	private String[]			defi		= null;
	// the new form.
	private ArrayList <String>	definition	= null;

	// create new.
	public WordEng( String word, ArrayList <String> def ) {}

	// get a word form the disk.
	public WordEng( String word, String path, String path2 ) {
		if( ( new File( path + "/" + word ) ).exists() ){
			this.word= helper.getFileName( word );
			Scanner rdr;
			String line;
			try{
				rdr= new Scanner( new File( path + "/" + word ) );
				ArrayList <String> lines= new ArrayList <>();
				lines.add( "<< " + helper.getFileName( word ) + " >>" );
				lines.add( "" );
				while( rdr.hasNextLine() ){
					line= rdr.nextLine();
					if( line.charAt( 0 ) == 'r' ){
						continue;
					}
					if( line.charAt( 0 ) == 'D' ){
						int ind= line.indexOf( ':', 6 );
						if( ind == -1 )
							lines.add( line );
						else{
							lines.add( line.substring( 0, ind ) );
							lines.add( "Example: " + line.substring( ind + 1, line.length() ) );
						}
						continue;
					}
					if( line.charAt( 0 ) == '-' ){
						lines.add( "" );
						lines.add( line );
						lines.add( "" );
						continue;
					}
					if( line.charAt( 0 ) == 'b' ){
						lines.add( "" );
						lines.add( line );
						continue;
					}
					lines.add( line );
				}
				definition= lines;
				//
				String wordT;
				if( path2 != null ){
					File af1= new File( path2 + "/" + this.word + "-2- us.mp3" );
					if( af1.exists() ){
						audioFile= af1.getAbsolutePath();
						return;
					}else{
						af1= new File( path2 + "/" + this.word + "-1- us.mp3" );
						if( af1.exists() ){
							audioFile= af1.getAbsolutePath();
							return;
						}
					}
				}
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
	}

	/* ----------------------------------------------------------------------------------------------
	 * // get spell of the word.
	 * ---------------------------------------------------------------------------------------------- */
	public String getSpell() {
		return word;
	}

	public String[] getDefi() {
		return this.defi;
	}

	public ArrayList <String> getDefiV2() {
		return definition;
	}

	/* ----------------------------------------------------------------------------------------------
	 * // play the pronouciation of the word.
	 * ---------------------------------------------------------------------------------------------- */
	public void pronounce() {
		try{
			if( audioFile != null ){
				PlayerAudio ap= new PlayerAudio( audioFile.replace( '\\', '/' ) );
				ap.setVolume( 0.17 );
				ap.play();
			}
		}catch ( Exception ee ){
			ee.printStackTrace();
		}
	}

	// give a new word and defi.
	public WordEng( String word, Type[] defi ) {
		this.word= word;
	}
}

//////////////////////////////////////////////////////////////////////////////////////////////
class Type {
	String			type;
	Definition[]	defi= null;
}

class Definition {
	String	definition	= null;
	String	example		= null;
}
/*
	// get a word form the disk. ( old form )
	public WordEng( String word, String path, String path2 ) {
		if( ( new File( path + "/" + word ) ).exists() ){
			Scanner rdr;
			try{
				rdr= new Scanner( new File( path + "/" + word ) );
				ArrayList <String> lines= new ArrayList <>();
				while( rdr.hasNextLine() ){
					lines.add( rdr.nextLine() );
				}
				this.defi= new String[lines.size()];
				int ind= 0;
				for( String tt : lines ){
					this.defi[ind++ ]= tt;
				}
				this.word= helper.getFileName( word );
				File pro= new File( path2 + "/" + helper.getFileName( word ) + ".mp3" );
				if( pro.exists() )
					audioFile= pro.getAbsolutePath();
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
	}
*/