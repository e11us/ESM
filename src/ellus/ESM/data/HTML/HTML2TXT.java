package ellus.ESM.data.HTML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;



// this class use lib from jsoup. doc at: https://jsoup.org/apidocs/ ( not used )
//
// the folder need to contains the html files and all image should be inside the folder named images.
//
public class HTML2TXT {
	//
	private String					path				= null;
	private int						lastReadLine		= 0;
	private int						ReadPerc			= 0;
	private ArrayList <String>		bookLines			= new ArrayList <>();
	private ArrayList <bookLine>	content				= new ArrayList <>();
	private ArrayList <String>		learnWords			= new ArrayList <>();
	private ArrayList <String>		bookMarks			= new ArrayList <>();
	//
	private static final String[]	ext					= { "html", "htm", "xhtml" };
	private static final String		readingSetting		= "/_ESM_reading.setting";
	private static final String		learnWordsSetting	= "/_ESM_newWords.txt";
	private static final String		bookMarkSetting		= "/_ESM_bookMark.txt";
	public static final String		_imgSig				= "_@#$%^&*_this_is_a_Image_";
	public static final String		_otherSig			= "_@#$%^&*_";

	public HTML2TXT( String path ) {
		this.path= path;
		loadSetting();
	}

	public void parseAll() {
		display.println( this.getClass().toString(), "parsing book path: " + path );
		HTML_parse();
		display.println( this.getClass().toString(), "parsing done for path: " + path );
	}

	public ArrayList <bookLine> getCont() {
		return (ArrayList <bookLine>)content.clone();
	}

	public String getName() {
		File spath= new File( path );
		return spath.getName();
	}

	public void setLastReadLine( int lines ) {
		lastReadLine= lines;
	}

	public int getLastReadLine() {
		return lastReadLine;
	}

	public int getReadPerc() {
		return ReadPerc;
	}

	public String getPath() {
		return path;
	}

	public String getBookMark( int line, int ind ) {
		Scanner rdr;
		String ret= null;
		for( String bm : bookMarks ){
			try{
				rdr= new Scanner( bm );
				if( line == rdr.nextInt() && ind == rdr.nextInt() ){
					ret= rdr.nextInt() + " " + rdr.nextInt();
					rdr.close();
					return ret;
				}
			}catch ( Exception ee ){}
		}
		return null;
	}

	public void addLearnWords( String word, int line, int index ) {
		learnWords.add( word + " " + helper.getCurrentTimeStamp() + " " + line + " " + index );
	}

	public void addBookMark( String inp ) {
		bookMarks.add( inp );
	}

	public void setSetting() {
		try{
			PrintWriter pw= new PrintWriter( path + readingSetting );
			pw.println( lastReadLine + "" );
			if( content.size() == 0 )
				pw.println( "0" );
			else{
				double read= 0;
				double unread= 0;
				for( int i= 0; i < lastReadLine; i++ ){
					read+= content.get( i ).getAllWords().size();
				}
				for( int i= lastReadLine; i < content.size(); i++ ){
					unread+= content.get( i ).getAllWords().size();
				}
				// a little read get to 1%.
				if( (int) ( Math.round( read / ( read + unread ) * 100 ) ) == 0 && lastReadLine > 1 )
					pw.println( "1" );
				else pw.println( (int) ( Math.round( read / ( read + unread ) * 100 ) ) + "" );
			}
			//
			pw.close();
			if( learnWords.size() > 0 ){
				pw= new PrintWriter( path + learnWordsSetting );
				for( String tw : learnWords ){
					pw.println( tw );
				}
				pw.close();
			}
			if( bookMarks.size() > 0 ){
				pw= new PrintWriter( path + bookMarkSetting );
				for( String tw : bookMarks ){
					pw.println( tw );
				}
				pw.close();
			}
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
		}
	}

	private void loadSetting() {
		File spath= new File( path + readingSetting );
		Scanner rdr;
		if( spath.exists() ){
			try{
				rdr= new Scanner( spath );
				if( rdr.hasNextLine() )
					lastReadLine= Integer.parseInt( rdr.nextLine() );
				else{
					rdr.close();
					return;
				}
				if( rdr.hasNextLine() )
					ReadPerc= Integer.parseInt( rdr.nextLine() );
				else{
					rdr.close();
					return;
				}
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}catch ( Exception ee ){
				ee.printStackTrace();
				return;
			}
		}
		spath= new File( path + learnWordsSetting );
		if( spath.exists() ){
			try{
				rdr= new Scanner( spath );
				while( rdr.hasNextLine() ){
					learnWords.add( rdr.nextLine() );
				}
				rdr.close();
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
		spath= new File( path + bookMarkSetting );
		if( spath.exists() ){
			try{
				rdr= new Scanner( spath );
				while( rdr.hasNextLine() ){
					bookMarks.add( rdr.nextLine() );
				}
				rdr.close();
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
	}

	private void HTML_parse() {
		// get all file.
		ArrayList <String> bookfiles= helper.getDirFile( path, ext );
		//read all files.
		String tmpp;
		String str;
		for( String bkfile : bookfiles ){
			display.println( this.getClass().toString(), "Doing book file: " + bkfile );
			bookLines.addAll( helper.readFile( bkfile ) );
		}
		// parse all.
		for( String lines : bookLines ){
			lineParseV2( lines );
		}
		display.println( this.getClass().toString(), "total raw lines: " + bookLines.size() );
		display.println( this.getClass().toString(), "total lines: " + content.size() );
	}

	private void lineParseV2( String line ) {
		//
		if( line.contains( "<img " ) ){
			bookLine bl= new bookLine();
			bl.cont= _imgSig;
			String tmp;
			// replace .. to .
			if( line.contains( ".." ) )
				tmp= line.substring( line.indexOf( "src=" ) + 6, line.length() );
			else tmp= line.substring( line.indexOf( "src=" ) + 5, line.length() );
			bl.imgPath= path + "/" + tmp.substring( 0, tmp.indexOf( '"' ) );
			content.add( bl );
		}else{
			if( line.contains( "<hr" ) ){
				bookLine bl= new bookLine();
				bl.cont= _otherSig + "<hr>";
				content.add( bl );
			}
			char[] lineA= line.toCharArray();
			int level= 0;
			boolean onflag= false;
			for( int i= 0; i < lineA.length; i++ ){
				if( lineA[i] == '<' ){
					level++ ;
					lineA[i]= ' ';
					onflag= true;
				}else if( lineA[i] == '>' ){
					level-- ;
					lineA[i]= ' ';
				}else if( level != 0 ){
					lineA[i]= ' ';
				}else if( ( lineA[i] ) < 32 || ( lineA[i] ) > 126 ){
					lineA[i]= ' ';
				}else if( !onflag ){
					lineA[i]= ' ';
				}
			}
			String res= new String( lineA ) + "";
			String ret= null;
			Scanner rdr= new Scanner( res );
			while( rdr.hasNext() ){
				if( ret == null )
					ret= rdr.next();
				else ret= ret + " " + rdr.next();
			}
			if( ret != null ){
				bookLine bl= new bookLine();
				bl.cont= ret;
				content.add( bl );
			}
		}
	}

	private void lineParseV1( String line ) {
		int indTmp;
		String last= "";
		bookLine bl= null;
		String txtTmp;
		System.out.println( line );
		Document bkDoc= org.jsoup.parser.Parser.parseBodyFragment( line, path );
		for( Element elm : bkDoc.getAllElements() ){
			//
			if( elm.tagName().equals( "img" ) ){
				bl= new bookLine();
				bl.cont= "@#$%^&*_Image";
				bl.imgPath= path + "/" + elm.attr( "src" );
				content.add( bl );
			}else if( elm.text().length() > 0 && !last.contains( elm.text() ) ){
				txtTmp= elm.text();
				/*
				while( txtTmp.length() > maxCharPerLine ){
					indTmp= txtTmp.indexOf( ' ', maxCharPerLine );
					if( indTmp == -1 ){
						break;
					}
					bl= new bookLine();
					bl.cont= txtTmp.substring( 0, indTmp );
					txtTmp= txtTmp.substring( indTmp, txtTmp.length() );
					content.add( bl );
				}
				*/
				bl= new bookLine();
				bl.cont= txtTmp;
				content.add( bl );
				last= elm.text();
			}
		}
	}
}
