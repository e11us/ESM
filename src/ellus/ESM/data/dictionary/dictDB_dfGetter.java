package ellus.ESM.data.dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.setting.SMan;



public class dictDB_dfGetter implements Runnable {
	//
	private Thread				tm;
	private String				name				= null;
	private static final String	cambridgeSitePath	= "http://dictionary.cambridge.org/us/dictionary/english/";
	private static final String	dictionarySitePre	= "http://www.dictionary.com/browse/";
	private static final String	dictionarySitePost	= "?s=t";
	private static final int	waitingTimeOut		= 3333;
	//
	protected boolean			running				= false;

	// batch update db.
	public dictDB_dfGetter( String name ) {
		this.name= name;
		tm= new Thread( this, "tm_" + name );
		tm.start();
		running= true;
	}

	public dictDB_dfGetter() {
		name= "DB_new_word_getter";
	}

	// single update db.
	public void updateAword( String word ) {
		getAndStore( word, false, null );
	}

	@Override
	public void run() {
		display.println( this.getClass().toString(), "dictUpdater " + name + " is started" );
		String word;
		while( ( word= dictDB.getNextUpdateWord() ) != null ){
			// get def.
			getAndStore( word, false, null );
			// try get sound.
			File wd= new File(
					SMan.getSetting( 0 ) + SMan.getSetting( 202 ) + "/" + word + ".txt" );
			if( wd.exists() ){
				new dictDB_pronGetter( wd );
			}
		}
		display.println( this.getClass().toString(), "dictUpdater " + name + " is ended." );
		running= false;
	}

	private void getAndStore( String word, boolean returnImm, String root ) {
		File path;
		ArrayList <String> definition;
		PrintWriter pw;
		//
		path= new File( SMan.getSetting( 0 ) + SMan.getSetting( 202 ) + "/" + word + ".txt" );
		if( path.exists() )
			return;
		//
		path= new File( SMan.getSetting( 0 ) + SMan.getSetting( 204 ) + "/" + word + ".txt" );
		if( path.exists() )
			return;
		//
		if( root != null )
			definition= getDef_Cambridge( root );
		else definition= getDef_Cambridge( word );
		if( definition != null ){
			//display.println( this.getClass().toString(), "word: " + word + " is a good" );
			try{
				pw= new PrintWriter(
						SMan.getSetting( 0 ) + SMan.getSetting( 202 ) + "/" + word + ".txt" );
				for( String tmp : definition ){
					pw.println( tmp );
				}
				pw.close();
				return;
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}else{
			//display.println( this.getClass().toString(), "word: " + word + " is not a valid word" );
			try{
				// try if can get root.
				root= null;
				if( !returnImm )
					root= getRoot( word );
				if( root != null ){
					getAndStore( word, true, root );
				}else{
					pw= new PrintWriter(
							SMan.getSetting( 0 ) + SMan.getSetting( 204 ) + "/" + word + ".txt" );
					pw.println( "_bad_word_" );
					pw.close();
					return;
				}
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
		return;
	}

	private ArrayList <String> getDef_Cambridge( String word ) {
		ArrayList <String> definition= new ArrayList <>();
		try{
			Document doc= Jsoup.parse( new URL( cambridgeSitePath + word ), waitingTimeOut );
			Elements entry= doc.getElementsByAttributeValueContaining( "class", "entry-body__el clrd js-share-holder" );
			if( entry.size() == 0 ){
				//display.printErr( this.getClass().toString(), "error getting: " + word );
				return null;
			}
			for( Element entryI : entry ){
				// for each entry of part_of_speech.
				Elements title= entryI.getElementsByAttributeValueContaining( "class",
						"di-title cdo-section-title-hw" );
				Elements pronoRegi= entryI.getElementsByAttributeValue( "class", "pron-info" );
				for( Element pron : pronoRegi ){
					String path= null;
					for( Element src : pron.getAllElements() ){
						if( src.hasAttr( "data-src-mp3" ) )
							path= src.attr( "data-src-mp3" );
					}
					definition.add( "regin&pron: " + pron.getElementsByTag( "span" ).get( 0 ).getElementsByTag( "span" )
							.get( 0 ).getElementsByTag( "span" ).get( 0 ).text() + " - " + path );
				}
				Elements spell= title.get( 0 ).getElementsByAttributeValue( "class", "headword" );
				definition.add( "Spell: " + spell.text() );
				Elements POS= title.get( 0 ).getElementsByAttributeValue( "class", "posgram ico-bg" );
				definition.add( "POS: " + POS.text() );
				// get POS for this def block.
				Elements entryy= entryI.getElementsByAttributeValue( "class", "sense-block" );
				for( Element defBlock : entryy ){
					// for each small bock.
					Elements smallBk_deader= defBlock.getElementsByAttributeValue( "class",
							"txt-block txt-block--alt2" );
					if( smallBk_deader != null && smallBk_deader.size() > 0 )
						definition.add( "block_Header: " + smallBk_deader.get( 0 ).text() );
					Elements entryyy= defBlock.getElementsByAttributeValue( "class", "def-block pad-indent" );
					for( Element def : entryyy ){
						definition.add( "Defi: " + def.text() );
					}
				}
				definition.add( "-----------------------------------" );
			}
		}catch ( MalformedURLException e ){
			e.printStackTrace();
		}catch ( IOException e ){
			e.printStackTrace();
		}
		return definition;
	}

	private String getRoot( String word ) {
		try{
			Document doc= Jsoup.parse( new URL( dictionarySitePre + word + dictionarySitePost ), waitingTimeOut );
			if( doc.getElementsByAttributeValue( "class", "head-entry" ).size() > 0 ){
				Scanner rdr= new Scanner( helper.clearNonAlpha(
						doc.getElementsByAttributeValue( "class", "head-entry" ).get( 0 ).text() ) );
				if( rdr.hasNext() ){
					String ret= rdr.next();
					rdr.close();
					return ret;
				}
			}
		}catch ( MalformedURLException e ){
			e.printStackTrace();
		}catch ( IOException e ){
			e.printStackTrace();
		}
		return null;
	}
}
/*


private String		MW					= GCSV.dictGetterSiteMW;
	private String		CB					= GCSV.dictGetterSiteCB;
	private String		DC					= GCSV.dictGetterSiteDC;
	private int			getword_retry_max	= GCSV.dictGetterGetword_retry_max;


private boolean getWordDC_Dual( String inp ) throws IOException {
		// get input box.
		List <WebElement> inpboxs= driver.findElements( By
				.xpath( "//input[contains(@id,'q')]" ) );
		// get word.
		WebElement inpbox= driver.findElement( By
				.xpath( "//input[contains(@id,'q')]" ) );
		inpbox.clear();
		inpbox.sendKeys( inp );
		inpbox.sendKeys( Keys.RETURN );
		// try to get txt definition.
		List <WebElement> def= driver.findElements(
				By.xpath( "//section[contains(@class,'def-pbk ce-spot')]" ) );
		// for each type.
		if( def.size() > 0 ){
			Type[] tps= new Type[def.size()];
			Type tp;
			for( int i= 0; i < def.size(); i++ ){
				WebElement webE= def.get( i );
				tp= new Type();
				tps[i]= tp;
				WebElement type= webE.findElement( By.xpath( "./header" ) );
				tp.type= type.getText();
				// get body.
				List <WebElement> body= webE.findElements( By.xpath( "./div" ) );
				// for body.
				tp.defi= new Definition[body.size()];
				int index= 0;
				for( WebElement defi : body ){
					String defini= "";
					String exm= "";
					for( WebElement con : defi.findElements(
							By.xpath( "./div/span/span" ) ) ){
						defini+= con.getText() + " ";
					}
					for( WebElement con : defi.findElements(
							By.xpath( "./div/div/span/span/span" ) ) ){
						exm+= con.getText() + " ";
					}
					Definition defs= new Definition();
					defs.definition= defini;
					defs.example= exm;
					tp.defi[index++ ]= defs;
				}
			}
			// try to get audio pronunciation. ( this is not tested )
			List <WebElement> snd= driver.findElements(
					By.xpath( "//div[contains(@class,'audio-wrapper cts-disabled')]" +
							"//audio//source[contains(@type,'audio/mpeg')]" ) );
			String audioPath= null;
			if( snd.size() > 0 ){
				audioPath= snd.get( 0 ).getAttribute( "src" ).toString();
			}
			WordEng eWord;
			if( audioPath == null )
				eWord= new WordEng( inp, tps );
			else eWord= new WordEng( inp, tps, audioPath );
			if( eWord.store( GCSV.folderDictTxt ) ){
				display.println( this.getClass().toString() + " || word: " + inp + "\t\t is logged successfully." );
			}
			return true;
		}
		display.println( this.getClass().toString() + " || word: " + inp + "\t\t is bad." );
		( new File( GCSV.folderDictBadTxt + "/" + inp ) ).createNewFile();
		return false;
	}


private String getWordCBmp3( String inp ) {	// CB mp3 is better.
			int tryN= 1;
			List <WebElement> inpbox;
			// get word
			inpbox= driver.findElements( By
					.xpath( "//div[contains(@class,'cdo-search__bar')]"
							+ "//input[contains(@class,'cdo-search__input')]" ) );
			inpbox.get( 0 ).clear();
			inpbox.get( 0 ).sendKeys( inp );
			inpbox.get( 0 ).sendKeys( Keys.RETURN );
			if( driver
					.findElements(
							By.xpath( "//span[contains(@class,'pron-info')]"
									+ "//span[contains(@class,'us')]"
									+ "//span[contains(@class,'circle circle-btn sound audio_play_button us')]" ) )
					.size() > 0 ){
				WebElement pronBut= driver
						.findElement( By
								.xpath( "//span[contains(@class,'pron-info')]"
										+ "//span[contains(@class,'us')]"
										+ "//span[contains(@class,'circle circle-btn sound audio_play_button us')]" ) );
				display.println(
						this.getClass().toString() + " || word: " + inp + "\t\t mp3 is retrived successfully." );
				return pronBut.getAttribute( "data-src-mp3" ).toString();
			}
			try{
				( new File( GCSV.folderDictBadMp3 + "/" + inp ) ).createNewFile();
			}catch ( IOException e ){
				e.printStackTrace();
			}
			return null;
		}

		private boolean getWordDCtxt( String inp ) throws IOException {
			// get input box.
			List <WebElement> inpboxs= driver.findElements( By
					.xpath( "//input[contains(@id,'q')]" ) );
			// get word.
			WebElement inpbox= driver.findElement( By
					.xpath( "//input[contains(@id,'q')]" ) );
			inpbox.clear();
			inpbox.sendKeys( inp );
			inpbox.sendKeys( Keys.RETURN );
			// try to get txt definition.
			List <WebElement> def= driver.findElements(
					By.xpath( "//section[contains(@class,'def-pbk ce-spot')]" ) );
			// for each type.
			if( def.size() > 0 ){
				Type[] tps= new Type[def.size()];
				Type tp;
				for( int i= 0; i < def.size(); i++ ){
					WebElement webE= def.get( i );
					tp= new Type();
					tps[i]= tp;
					WebElement type= webE.findElement( By.xpath( "./header" ) );
					tp.type= type.getText();
					// get body.
					List <WebElement> body= webE.findElements( By.xpath( "./div" ) );
					// for body.
					tp.defi= new Definition[body.size()];
					int index= 0;
					for( WebElement defi : body ){
						String defini= "";
						String exm= "";
						for( WebElement con : defi.findElements(
								By.xpath( "./div/span/span" ) ) ){
							defini+= con.getText() + " ";
						}
						for( WebElement con : defi.findElements(
								By.xpath( "./div/div/span/span/span" ) ) ){
							exm+= con.getText() + " ";
						}
						Definition defs= new Definition();
						defs.definition= defini;
						defs.example= exm;
						tp.defi[index++ ]= defs;
					}
				}
				WordEng eWord= new WordEng( inp, tps );
				if( eWord.store( GCSV.folderDictTxt ) ){
					display.println( this.getClass().toString() + " || word: " + inp + "\t\t is logged successfully." );
				}
				return true;
			}
			display.println( this.getClass().toString() + " || word: " + inp + "\t\t is bad." );
			( new File( GCSV.folderDictBadTxt + "/" + inp ) ).createNewFile();
			return false;
		}

private void getWordMW( String inp ) {
			driver.get( MW );
			// get word
			WebElement inpbox= driver.findElement( By
					.xpath( "//div[contains(@class,'s-term-cnt border-box')]"
							+ "//input[contains(@id,'s-term')]" ) );
			inpbox.clear();
			inpbox.sendKeys( inp );
			inpbox.sendKeys( Keys.RETURN );
			// see if press skip.
			if( driver.findElements(
					By.xpath( "//div[contains(@class,'skip-btn skip-btn-top')]"
							+ "//a[contains(@id,'skip-link')]" ) )
					.size() > 0 ){
				driver.findElement(
						By.xpath( "//div[contains(@class,'skip-btn skip-btn-top')]"
								+ "//a[contains(@id,'skip-link')]" ) )
						.click();
			}
			// not finished.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		}

*/