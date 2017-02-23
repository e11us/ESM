package ellus.ESM.Machine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ellus.ESM.roboSys.DeskTop;
import ellus.ESM.setting.SCon;



/* -----------------------------------------------------------------------------
 *
 * -----------------------------------------------------------------------------
 */
public class helper {
	/* --------------------------------------------------------------------------
	 * --- convert coordinate in the wall to the coordinate of where is it on
	 * the board.
	 * ----------------------------------------------------------------
	 * ------------- */
	public static cor2D cor2Dw2b( cor2D orign, cor2D inp ) {
		return new cor2D( inp.getX() - orign.getX(), inp.getY() - orign.getY() );
	}

	public static int w2b( int orign, int inp ) {
		return inp - orign;
	}

	/* --------------------------------------------------------------------------
	 * --- convert coordinate in the board to the coordinate of where is it on
	 * the wall.
	 * ----------------------------------------------------------------
	 * ------------- */
	public static cor2D cor2Db2w( cor2D orign, cor2D inp ) {
		return new cor2D( inp.getX() + orign.getX(), inp.getY() + orign.getY() );
	}

	public static int b2w( int orign, int inp ) {
		return orign + inp;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| return current date.
	||||--------------------------------------------------------------------------------------------*/
	public static String getCurrentDateStamp() {
		SimpleDateFormat sdfDate= new SimpleDateFormat( "yyyy_MM_dd" );// dd/MM/yyyy
		Date now= new Date();
		String strDate= sdfDate.format( now );
		return strDate;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| return current date and time and ms as int.
	||||--------------------------------------------------------------------------------------------*/
	public static String getCurrentDate() {
		LocalDate date= LocalDate.now();
		DateTimeFormatter fmt= DateTimeFormat.forPattern( "yyyyMMdd" );
		return date.toString( fmt );
	}

	public static String getCurrentTime() {
		SimpleDateFormat sdfDate= new SimpleDateFormat( "HHmmss" );
		Date now= new Date();
		String strDate= sdfDate.format( now );
		return strDate;
	}

	public static String getCurrentTimeMS() {
		DateTime dt= new DateTime();
		DateTimeFormatter fmt2= DateTimeFormat.forPattern( "SSSS" );
		return dt.toString( fmt2 );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| return current time and date.
	||||--------------------------------------------------------------------------------------------*/
	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate= new SimpleDateFormat( "yyyy_MM_dd_HH_mm_ss" );// dd/MM/yyyy
		Date now= new Date();
		String strDate= sdfDate.format( now );
		return strDate;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| return current time and date. also milli second.
	||||--------------------------------------------------------------------------------------------*/
	public static String getCurrentTimeStampMS() {
		SimpleDateFormat sdfDate= new SimpleDateFormat( "yyyy_MM_dd_HH_mm_ss_ms" );// dd/MM/yyyy
		Date now= new Date();
		String strDate= sdfDate.format( now );
		return strDate;
	}

	public static long getTimeLong() {
		return new Date().getTime();
	}

	/* --------------------------------------------------------------------------
	 * --- get filename without ext.
	 * --------------------------------------------
	 * --------------------------------- */
	public static String getFileName( String inp ) {
		if( inp == null )
			return null;
		for( int i= inp.length() - 1; i >= 0; i-- ){
			if( inp.charAt( i ) == '.' )
				return inp.substring( 0, i );
		}
		return inp;
	}

	/* --------------------------------------------------------------------------
	 * --- get the file ext. ( res without the '.' ) ( return txt instead of
	 * .txt )
	 * --------------------------------------------------------------------
	 * --------- */
	public static String getFileExt( String inp ) {
		if( inp == null )
			return null;
		for( int i= inp.length() - 1; i >= 0; i-- ){
			if( inp.charAt( i ) == '.' )
				return inp.substring( i + 1, inp.length() );
		}
		return "";
	}

	/* --------------------------------------------------------------------------
	 * --- return a uniq file name by adding # at end if input filename exits.
	 * input is file path.
	 * ------------------------------------------------------
	 * ----------------------- */
	public static String existingFileName( String inp ) {
		File ext= null;
		String name= inp;
		boolean sig= true;
		int ind= 1;
		while( sig ){
			ext= new File( name );
			if( ext.exists() ){
				name= helper.getFileName( inp ) + "-" + ind++ + "."
						+ helper.getFileExt( inp );
			}else{
				sig= false;
			}
		}
		return name;
	}

	/* --------------------------------------------------------------------------
	 * --- create a new board by create the folder in the main pin folder.
	 * --------------------------------------------------------------------------*/
	public static boolean createFolder( String name ) {
		if( name == null )
			return false;
		if( getValidfolder( name ) == null )
			return false;
		name= getValidfolder( name );
		File newFolder= new File( name );
		if( !newFolder.exists() ){
			return newFolder.mkdirs();
		}
		return false;
	}

	/* --------------------------------------------------------------------------
	 * --- if a string begin with match.
	 * --------------------------------------------------------------------------*/
	public static boolean beginW( String inp, String match ) {
		if( inp == null || match == null || inp.length() == 0
				|| match.length() == 0 || match.length() > inp.length() )
			return false;
		for( int i= 0; i < match.length(); i++ ){
			if( match.charAt( i ) != inp.charAt( i ) )
				return false;
		}
		return true;
	}

	/* --------------------------------------------------------------------------
	 * --- return total line number in file.
	 * --------------------------------------------------------------------------*/
	public static int getTotFileLines( File inp ) throws FileNotFoundException {
		if( inp == null )
			return 0;
		Scanner rdrL= new Scanner( inp );
		int lines= 0;
		while( rdrL.hasNextLine() ){
			rdrL.nextLine();
			lines++ ;
		}
		rdrL.close();
		return lines;
	}

	/* --------------------------------------------------------------------------
	 * get valid file name, file path.
	 * 		change invalid char in file path to '_'
	 * --------------------------------------------------------------------------*/
	public static String getValidName( String inp ) {
		if( inp == null || inp.length() == 0 )
			return null;
		char[] arr= inp.toCharArray();
		boolean allSpace= true;
		String ret= "";
		for( int i= 0; i < arr.length; i++ ){
			if( arr[i] != ' ' )
				allSpace= false;
			if( arr[i] == '/' || arr[i] == '\\' || arr[i] == '*'
					|| arr[i] == ':' || arr[i] == '?' || arr[i] == '"'
					|| arr[i] == '<' || arr[i] == '>' || arr[i] == '|'
					|| arr[i] == '\n' || arr[i] == '\t' ){
				arr[i]= '_';
			}
			ret+= arr[i];
		}
		if( allSpace )
			return null;
		return ret;
	}

	/* --------------------------------------------------------------------------
	 * --- get valid folder name.
	  --------------------------------------------------------------------------*/
	public static String getValidfolder( String inp ) {
		if( inp == null || inp.length() == 0 )
			return null;
		char[] arr= inp.toCharArray();
		boolean allSpace= true;
		String ret= "";
		for( int i= 0; i < arr.length; i++ ){
			if( arr[i] != ' ' )
				allSpace= false;
			if( arr[i] == '\\' || arr[i] == '*' || arr[i] == ':'
					|| arr[i] == '?' || arr[i] == '"' || arr[i] == '<'
					|| arr[i] == '>' || arr[i] == '|' ){
				arr[i]= '_';
			}
			ret+= arr[i];
		}
		if( allSpace )
			return null;
		return ret;
	}

	/* --------------------------------------------------------------------------
	 * --- return 32 to 126. printable char.
	 * --------------------------------------------------------------------------*/
	public static String randvalidchar() {
		return ( (char) ( Math.round( Math.random() * 96 + 32 ) ) ) + "";
	}

	/* --------------------------------------------------------------------------
	 * if it is all aph num
	 * --------------------------------------------------------------------------*/
	public static boolean isAlphNum( String inp ) {
		if( inp == null || inp.length() == 0 )
			return true;
		char[] arr= inp.toCharArray();
		int val;
		for( char t : arr ){
			val= t;
			if( val < 48 || ( val > 57 && val < 65 ) ||
					( val > 90 && val < 97 ) || val > 122 )
				return false;
		}
		return true;
	}

	/* --------------------------------------------------------------------------
	 * if it all number.
	 * --------------------------------------------------------------------------*/
	public static boolean isNum( String inp ) {
		if( inp == null || inp.length() == 0 )
			return true;
		char[] arr= inp.toCharArray();
		int val;
		for( char t : arr ){
			val= t;
			if( val < 48 || val > 57 )
				return false;
		}
		return true;
	}

	public static boolean containsAlphNum( String inp ) {
		if( inp == null || inp.length() == 0 )
			return false;
		char[] arr= inp.toCharArray();
		for( char t : arr ){
			int val= t;
			if( val < 48 || ( val > 57 && val < 65 ) ||
					( val > 90 && val < 97 ) || val > 122 )
				;
			else return true;
		}
		return false;
	}

	public static String clearNonAlpha( String inp ) {
		if( inp == null || inp.length() == 0 )
			return "";
		char[] arr= inp.toCharArray();
		for( int i= 0; i < arr.length; i++ ){
			int val= arr[i];
			if( val < 65 || ( val > 90 && val < 97 ) || val > 122 )
				arr[i]= ' ';
		}
		return new String( arr );
	}

	/* --------------------------------------------------------------------------
	 * --- return a random alphanumerical.
	 * -------------------------------------------------------------------------*/
	public static char randAlphNum() {
		char tmp= (int)0;
		int rand= (int) ( Math.random() * 62 );
		switch( rand ){
			case 0 :
				tmp= '0';
				break;
			case 1 :
				tmp= '1';
				break;
			case 2 :
				tmp= '2';
				break;
			case 3 :
				tmp= '3';
				break;
			case 4 :
				tmp= '4';
				break;
			case 5 :
				tmp= '5';
				break;
			case 6 :
				tmp= '6';
				break;
			case 7 :
				tmp= '7';
				break;
			case 8 :
				tmp= '8';
				break;
			case 9 :
				tmp= '9';
				break;
			case 10 :
				tmp= 'a';
				break;
			case 11 :
				tmp= 'b';
				break;
			case 12 :
				tmp= 'c';
				break;
			case 13 :
				tmp= 'd';
				break;
			case 14 :
				tmp= 'e';
				break;
			case 15 :
				tmp= 'f';
				break;
			case 16 :
				tmp= 'g';
				break;
			case 17 :
				tmp= 'h';
				break;
			case 18 :
				tmp= 'i';
				break;
			case 19 :
				tmp= 'j';
				break;
			case 20 :
				tmp= 'k';
				break;
			case 21 :
				tmp= 'l';
				break;
			case 22 :
				tmp= 'm';
				break;
			case 23 :
				tmp= 'n';
				break;
			case 24 :
				tmp= 'o';
				break;
			case 25 :
				tmp= 'p';
				break;
			case 26 :
				tmp= 'q';
				break;
			case 27 :
				tmp= 'r';
				break;
			case 28 :
				tmp= 's';
				break;
			case 29 :
				tmp= 't';
				break;
			case 30 :
				tmp= 'u';
				break;
			case 31 :
				tmp= 'v';
				break;
			case 32 :
				tmp= 'w';
				break;
			case 33 :
				tmp= 'x';
				break;
			case 34 :
				tmp= 'y';
				break;
			case 35 :
				tmp= 'z';
				break;
			case 36 :
				tmp= 'A';
				break;
			case 37 :
				tmp= 'B';
				break;
			case 38 :
				tmp= 'C';
				break;
			case 39 :
				tmp= 'D';
				break;
			case 40 :
				tmp= 'E';
				break;
			case 41 :
				tmp= 'F';
				break;
			case 42 :
				tmp= 'G';
				break;
			case 43 :
				tmp= 'H';
				break;
			case 44 :
				tmp= 'I';
				break;
			case 45 :
				tmp= 'J';
				break;
			case 46 :
				tmp= 'K';
				break;
			case 47 :
				tmp= 'L';
				break;
			case 48 :
				tmp= 'M';
				break;
			case 49 :
				tmp= 'N';
				break;
			case 50 :
				tmp= 'O';
				break;
			case 51 :
				tmp= 'P';
				break;
			case 52 :
				tmp= 'Q';
				break;
			case 53 :
				tmp= 'R';
				break;
			case 54 :
				tmp= 'S';
				break;
			case 55 :
				tmp= 'T';
				break;
			case 56 :
				tmp= 'U';
				break;
			case 57 :
				tmp= 'V';
				break;
			case 58 :
				tmp= 'W';
				break;
			case 59 :
				tmp= 'X';
				break;
			case 60 :
				tmp= 'Y';
				break;
			case 61 :
				tmp= 'Z';
				break;
			default :
				break;
		}
		return tmp;
	}

	/* --------------------------------------------------------------------------
	 * --- return a random cap letter and number.
	 * -------------------------------------------------------------------------*/
	public static char randAlphcapNum() {
		char tmp= (int)0;
		int rand= (int) ( Math.random() * 36 );
		switch( rand ){
			case 0 :
				tmp= '0';
				break;
			case 1 :
				tmp= '1';
				break;
			case 2 :
				tmp= '2';
				break;
			case 3 :
				tmp= '3';
				break;
			case 4 :
				tmp= '4';
				break;
			case 5 :
				tmp= '5';
				break;
			case 6 :
				tmp= '6';
				break;
			case 7 :
				tmp= '7';
				break;
			case 8 :
				tmp= '8';
				break;
			case 9 :
				tmp= '9';
				break;
			case 10 :
				tmp= 'z';
				break;
			case 11 :
				tmp= 'A';
				break;
			case 12 :
				tmp= 'B';
				break;
			case 13 :
				tmp= 'C';
				break;
			case 14 :
				tmp= 'D';
				break;
			case 15 :
				tmp= 'E';
				break;
			case 16 :
				tmp= 'F';
				break;
			case 17 :
				tmp= 'G';
				break;
			case 18 :
				tmp= 'H';
				break;
			case 19 :
				tmp= 'I';
				break;
			case 20 :
				tmp= 'J';
				break;
			case 21 :
				tmp= 'K';
				break;
			case 22 :
				tmp= 'L';
				break;
			case 23 :
				tmp= 'M';
				break;
			case 24 :
				tmp= 'N';
				break;
			case 25 :
				tmp= 'O';
				break;
			case 26 :
				tmp= 'P';
				break;
			case 27 :
				tmp= 'Q';
				break;
			case 28 :
				tmp= 'R';
				break;
			case 29 :
				tmp= 'S';
				break;
			case 30 :
				tmp= 'T';
				break;
			case 31 :
				tmp= 'U';
				break;
			case 32 :
				tmp= 'V';
				break;
			case 33 :
				tmp= 'W';
				break;
			case 34 :
				tmp= 'X';
				break;
			case 35 :
				tmp= 'Y';
				break;
			case 36 :
				tmp= 'Z';
				break;
			default :
				break;
		}
		return tmp;
	}

	public static char randNum() {
		char tmp= (int)0;
		int rand= (int) ( Math.random() * 36 );
		switch( rand ){
			case 0 :
				tmp= '0';
				break;
			case 1 :
				tmp= '1';
				break;
			case 2 :
				tmp= '2';
				break;
			case 3 :
				tmp= '3';
				break;
			case 4 :
				tmp= '4';
				break;
			case 5 :
				tmp= '5';
				break;
			case 6 :
				tmp= '6';
				break;
			case 7 :
				tmp= '7';
				break;
			case 8 :
				tmp= '8';
				break;
			case 9 :
				tmp= '9';
				break;
		}
		return tmp;
	}

	/* --------------------------------------------------------------------------
	 * --- return a random alphanumerical string of length 32.
	 * ------------------
	 * ----------------------------------------------------------- */
	public static String rand32AN() {
		StringBuilder ret= new StringBuilder( "" );
		for( int i= 0; i < 32; i++ ){
			ret.append( randAlphNum() );
		}
		return ret.toString();
	}

	/* --------------------------------------------------------------------------
	 * --- return a random alphanumerical string of length 40.
	 * ------------------
	 * ----------------------------------------------------------- */
	public static String rand40AN() {
		StringBuilder ret= new StringBuilder( "" );
		for( int i= 0; i < 40; i++ ){
			ret.append( randAlphNum() );
		}
		return ret.toString();
	}

	/* --------------------------------------------------------------------------
	 * --- return a random alphanumerical string of length 40.
	 * ------------------
	 * ----------------------------------------------------------- */
	public static String randAN( int itor ) {
		if( itor <= 0 )
			return "";
		StringBuilder ret= new StringBuilder( "" );
		for( int i= 0; i < itor; i++ ){
			ret.append( randAlphNum() );
		}
		return ret.toString();
	}

	/* --------------------------------------------------------------------------
	 * --- open an window and let user ch7oose a file and return the file as
	 * File.
	 * --------------------------------------------------------------------
	 * --------- */
	public static File selectFile( FileFilter ff, File cf ) {
		JFileChooser fc1= new JFileChooser();
		fc1.setCurrentDirectory( cf );
		fc1.addChoosableFileFilter( ff );
		fc1.setDialogTitle( "Choose a File" );
		fc1.setAcceptAllFileFilterUsed( false );
		fc1.setPreferredSize( new Dimension( 900, 600 ) );
		JFrame jfcFrame= new JFrame();
		jfcFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		int result= fc1.showOpenDialog( jfcFrame );
		while( result != JFileChooser.APPROVE_OPTION && jfcFrame.isActive() ){
			;
		}
		if( result == JFileChooser.APPROVE_OPTION ){
			File selectedFile= fc1.getSelectedFile();
			display.println( ( (Object)new helper() ).getClass().toString(),
					" || file: " + selectedFile.getAbsolutePath()
							+ " is choosen." );
			return selectedFile;
		}else{
			display.println( ( (Object)new helper() ).getClass().toString(), " || No file is choosen." );
			return null;
		}
	}

	public static File selectDir( File cf ) {
		JFileChooser fc1= new JFileChooser();
		fc1.setCurrentDirectory( cf );
		fc1.setDialogTitle( "Choose a Directory" );
		fc1.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		fc1.setAcceptAllFileFilterUsed( false );
		fc1.setPreferredSize( new Dimension( 900, 600 ) );
		JFrame jfcFrame= new JFrame();
		jfcFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		int result= fc1.showOpenDialog( jfcFrame );
		while( result != JFileChooser.APPROVE_OPTION && jfcFrame.isActive() ){
			;
		}
		if( result == JFileChooser.APPROVE_OPTION ){
			File selectedFile= fc1.getSelectedFile();
			display.println( ( (Object)new helper() ).getClass().toString(),
					" || file: " + selectedFile.getAbsolutePath()
							+ " is choosen." );
			return selectedFile;
		}else{
			display.println( ( (Object)new helper() ).getClass().toString(), " || No file is choosen." );
			return null;
		}
	}

	public static File selectDirAndFile( File cf ) {
		JFileChooser fc1= new JFileChooser();
		fc1.setCurrentDirectory( cf );
		fc1.setDialogTitle( "Choose a File or Directory" );
		fc1.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
		fc1.setAcceptAllFileFilterUsed( false );
		fc1.setPreferredSize( new Dimension( 900, 600 ) );
		JFrame jfcFrame= new JFrame();
		jfcFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		int result= fc1.showOpenDialog( jfcFrame );
		while( result != JFileChooser.APPROVE_OPTION && jfcFrame.isActive() ){
			;
		}
		if( result == JFileChooser.APPROVE_OPTION ){
			File selectedFile= fc1.getSelectedFile();
			display.println( ( (Object)new helper() ).getClass().toString(),
					" || file: " + selectedFile.getAbsolutePath()
							+ " is choosen." );
			return selectedFile;
		}else{
			display.println( ( (Object)new helper() ).getClass().toString(), " || No file is choosen." );
			return null;
		}
	}

	/* --------------------------------------------------------------------------
	 * --- test if given file is image, if so return the image file.
	 * ------------
	 * ----------------------------------------------------------------- */
	public static Image testImg( File inp ) {
		if( inp == null || !inp.exists() || !inp.isFile() )
			return null;
		String ext= getFileExt( inp.getAbsolutePath() );
		for( String t : SCon.ImageExtList ){
			if( t.equals( ext ) ){
				Image image= null;
				try{
					image= ImageIO.read( inp );
				}catch ( IOException e ){
					e.printStackTrace();
					return null;
				}
				display.println( ( (Object)new helper() ).getClass().toString(),
						"Image selection successful for: " + inp.getAbsolutePath() );
				return image;
			}
		}
		display.println( ( (Object)new helper() ).getClass().toString(),
				"selected file is not an supported image: " + inp.getAbsolutePath() );
		return null;
	}

	/* --------------------------------------------------------------------------
	 * --- test if given file is image, if so return the image file.
	 * ------------
	 * ----------------------------------------------------------------- */
	public static Image testImg( String inp ) {
		if( inp == null )
			return null;
		File path= new File( inp );
		if( !path.exists() || !path.isFile() )
			return null;
		String ext= getFileExt( inp );
		for( String t : SCon.ImageExtList ){
			if( t.equals( ext ) ){
				Image image= null;
				try{
					image= ImageIO.read( path );
				}catch ( IOException e ){
					e.printStackTrace();
					return null;
				}
				display.println( ( (Object)new helper() ).getClass().toString(),
						"Image selection successful for: " + inp );
				return image;
			}
		}
		display.println( ( (Object)new helper() ).getClass().toString(),
				"selected file is not an supported image: " + inp );
		return null;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| test and see if it has the right ratio, and if it has at least the size.
	||||--------------------------------------------------------------------------------------------*/
	public static boolean imgSizeRati( Image img, int x, int y, double ratio1, double ratio2 ) {
		if( img == null )
			return false;
		int wid= img.getWidth( new JPanel() );
		int hei= img.getHeight( new JPanel() );
		if( wid < x || hei < y )
			return false;
		if( ( (double)wid / hei ) >= ratio1 && ( (double)wid / hei ) <= ratio2 )
			return true;
		return false;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get all file from given path with the given ext.
	|||| ex: "./home", "txt" will get all txt in home. returning is complete path.
	||||--------------------------------------------------------------------------------------------*/
	public static ArrayList <String> getAllFile( String path, String ext ) {
		ArrayList <String> file= new ArrayList <>();
		File dbPath= new File( path );
		if( dbPath.exists() ){
			try{
				Files.walk( Paths.get( path ) ).forEach(
						filePath -> {
							if( !Files.isDirectory( filePath ) ){
								file.add( filePath.toAbsolutePath().toString() );
							}
						} );
			}catch ( IOException e ){
				e.printStackTrace();
			}
			if( ext != null && ext.length() != 0 ){
				for( int i= 0; i < file.size(); i++ ){
					if( !getFileExt( file.get( i ) ).toLowerCase().equals( ext.toLowerCase() ) )
						file.remove( i-- );
				}
			}
			//display.println( ( (Object)new helper() ).getClass().toString(), "total of " + file.size() + " <" + ext + "> is found in " + path );
			return file;
		}else{
			//display.println( ( (Object)new helper() ).getClass().toString(), "bad path." );
			return file;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get dir file from given path with the given ext.
	|||| ex: "./home", "txt" will get all txt in home. returning is complete path.
	||||--------------------------------------------------------------------------------------------*/
	public static ArrayList <String> getDirFile( String spath, String ext ) {
		if( spath == null || ext == null )
			return null;
		File folder= new File( spath );
		File[] listOfFiles= folder.listFiles();
		ArrayList <String> res= new ArrayList <>();
		if( ext.length() == 0 ){
			for( int i= 0; i < listOfFiles.length; i++ ){
				if( listOfFiles[i].isFile() )
					res.add( listOfFiles[i].toString() );
			}
			return res;
		}else{
			for( int i= 0; i < listOfFiles.length; i++ ){
				if( listOfFiles[i].isFile()
						&& getFileExt( listOfFiles[i].toString() ).toLowerCase().equals( ext.toLowerCase() ) )
					res.add( listOfFiles[i].toString() );
			}
			return res;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get dir file and folder from given path with the given ext.
	|||| ex: "./home", "txt" will get all txt in home. returning is complete path.
	||||--------------------------------------------------------------------------------------------*/
	public static ArrayList <String> getDirFF( String spath ) {
		if( spath == null )
			return null;
		File folder= new File( spath );
		File[] listOfFiles= folder.listFiles();
		ArrayList <String> res= new ArrayList <>();
		for( int i= 0; i < listOfFiles.length; i++ ){
			res.add( listOfFiles[i].getAbsolutePath() );
		}
		return res;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get dir file from given path with all the given ext.
	|||| ex: "./home", "txt" will get all txt in home. returning is complete path.
	||||--------------------------------------------------------------------------------------------*/
	public static ArrayList <String> getDirFile( String spath, String[] ext ) {
		if( spath == null || ext == null )
			return null;
		File folder= new File( spath );
		File[] listOfFiles= folder.listFiles();
		ArrayList <String> res= new ArrayList <>();
		if( ext.length == 0 ){
			for( int i= 0; i < listOfFiles.length; i++ ){
				if( listOfFiles[i].isFile() )
					res.add( listOfFiles[i].toString() );
			}
			return res;
		}else{
			for( int i= 0; i < listOfFiles.length; i++ ){
				for( String tmp : ext ){
					if( listOfFiles[i].isFile()
							&& getFileExt( listOfFiles[i].toString() ).toLowerCase().equals( tmp.toLowerCase() ) ){
						res.add( listOfFiles[i].toString() );
						break;
					}
				}
			}
			return res;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get dir file from given path with all the given ext.
	|||| ex: "./home", "txt" will get all txt in home. returning is complete path.
	||||--------------------------------------------------------------------------------------------*/
	public static ArrayList <String> getDirFile( String spath, ArrayList <String> ext ) {
		if( spath == null || ext == null )
			return null;
		File folder= new File( spath );
		File[] listOfFiles= folder.listFiles();
		ArrayList <String> res= new ArrayList <>();
		if( ext.size() == 0 ){
			for( int i= 0; i < listOfFiles.length; i++ ){
				if( listOfFiles[i].isFile() )
					res.add( listOfFiles[i].toString() );
			}
			return res;
		}else{
			for( int i= 0; i < listOfFiles.length; i++ ){
				for( String tmp : ext ){
					if( listOfFiles[i].isFile()
							&& getFileExt( listOfFiles[i].toString() ).toLowerCase().equals( tmp.toLowerCase() ) ){
						res.add( listOfFiles[i].toString() );
						break;
					}
				}
			}
			return res;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get all files that match the name + ext.
	||||--------------------------------------------------------------------------------------------*/
	public static ArrayList <String> getAllFileMatch( String root, String file ) {
		ArrayList <String> res= new ArrayList <>();
		if( root == null || file == null )
			return res;
		//
		File dbPath= new File( root );
		if( dbPath.exists() ){
			try{
				Files.walk( Paths.get( root ) ).forEach(
						filePath -> {
							if( !Files.isDirectory( filePath ) &&
									getFilePathName( filePath.getFileName().toString() ).equals( file ) ){
								res.add( filePath.toString() );
							}
						} );
			}catch ( IOException e ){
				e.printStackTrace();
			}
			return res;
		}else{
			//display.println( ( (Object)new helper() ).getClass().toString(), "bad path." );
			return res;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get the name for the given path.
	||||--------------------------------------------------------------------------------------------*/
	public static String getFilePathName( String path ) {
		try{
			File file= new File( path );
			return file.getName();
		}catch ( Exception ee ){
			ee.printStackTrace();
			return null;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get the name for the given path.
	||||--------------------------------------------------------------------------------------------*/
	public static ArrayList <String> getFilePathName( ArrayList <String> path ) {
		if( path == null || path.size() == 0 )
			return null;
		ArrayList <String> ret= new ArrayList <>();
		for( String tmp : path ){
			ret.add( getFilePathName( tmp ) );
		}
		return ret;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get the name for the given path.
	||||--------------------------------------------------------------------------------------------*/
	public static String getFilePathParentName( String path ) {
		File file= new File( path );
		return file.getParent();
	}

	/* --------------------------------------------------------------------------
	 * --- get all the sub folder name in the given folder.
	 *
	 * ( need to recheck )
	 *
	 * ----------------------
	 * ------------------------------------------------------- */
	public static ArrayList <String> getAllSubFolder( String path ) {
		ArrayList <String> folders= new ArrayList <>();
		File dbPath= new File( path );
		if( dbPath.exists() ){
			try{
				Files.walk( Paths.get( dbPath.getAbsolutePath() ) ).forEach(
						filePath -> {
							if( Files.isDirectory( filePath ) ){
								folders.add( filePath.toString().replace( '\\',
										'/' ) );
							}
						} );
			}catch ( IOException e ){
				e.printStackTrace();
			}
			display.println( ( (Object)new helper() ).getClass().toString(), " || total of: " + ( folders.size() )
					+ " sub folders found." );
			return folders;
		}else{
			display.println( ( (Object)new helper() ).getClass().toString(),
					" || given path: " + path + " is not valid." );
			return null;
		}
	}

	/* --------------------------------------------------------------------------
	 * --- get only the direct sub folder name in the given folder. ( return is complete path of sub folder. )
	 * --------------
	 * --------------------------------------------------------------- */
	public static ArrayList <String> getDirSubFolder( String path ) {
		File file= new File( path );
		String[] directories= file.list( new FilenameFilter() {
			@Override
			public boolean accept( File current, String name ) {
				return new File( current, name ).isDirectory();
			}
		} );
		if( directories == null )
			return new ArrayList <>();
		ArrayList <String> ret= new ArrayList <>();
		ArrayList <String> ret2= new ArrayList <>();
		String T;
		//
		String pre= null;
		try{
			pre= file.getCanonicalPath();
		}catch ( IOException e ){
			e.printStackTrace();
		}
		//
		for( String tmp : directories ){
			if( tmp.charAt( 0 ) == '_' )
				ret2.add( pre.replace( '\\', '/' ) + "/" + tmp );
			else ret.add( pre.replace( '\\', '/' ) + "/" + tmp );
		}
		ret2.addAll( ret );
		return ret2;
	}

	/* --------------------------------------------------------------------------
	 * --- get a .jpg image if the path is good.
	 * --------------------------------
	 * --------------------------------------------- */
	public static ImageIcon getImageIcon( String path ) {
		BufferedImage img= null;
		ImageIcon imgIC= null;
		if( path == null )
			return null;
		File spath= new File( path );
		if( spath.exists() && spath.isFile()
				&& getFileExt( spath.getName() ).equals( "jpg" ) ){
			try{
				img= ImageIO.read( new File( path ) );
				imgIC= new ImageIcon( img );
				return imgIC;
			}catch ( IOException e ){
				e.printStackTrace();
			}
		}
		return null;
	}

	/* --------------------------------------------------------------------------
	 * get image.
	 * ------------------------------------------------------------------------*/
	public static Image getImage( String path ) {
		BufferedImage img= null;
		if( path == null )
			return null;
		File spath= new File( path );
		if( spath.exists() && spath.isFile() ){
			try{
				img= ImageIO.read( new File( path ) );
				return img;
			}catch ( IOException e ){
				e.printStackTrace();
			}
		}
		return null;
	}

	/* --------------------------------------------------------------------------
	 * --- delete a file.
	 * --------------------------------------------------------
	 * --------------------- */
	public static boolean deleteFile( String path ) {
		File spath= new File( path );
		if( spath.exists() && spath.isFile() ){
			spath.delete();
			return true;
		}
		return false;
	}

	public static String Color2Str( Color inp ) {
		if( inp == null )
			return "";
		return " " + inp.getRed() + " " + inp.getGreen() + " "
				+ inp.getBlue() + " " + inp.getAlpha() + " ";
	}

	public static Color Str2Color( String inp ) {
		if( inp == null )
			return null;
		Scanner rdr= new Scanner( inp );
		int r= 0, g= 0, b= 0, a= 0;
		if( rdr.hasNextInt() ){
			r= rdr.nextInt();
		}
		if( rdr.hasNextInt() ){
			g= rdr.nextInt();
		}
		if( rdr.hasNextInt() ){
			b= rdr.nextInt();
		}
		if( rdr.hasNextInt() ){
			a= rdr.nextInt();
		}
		try{
			return new Color( r, g, b, a );
		}catch ( Exception ee ){
			ee.printStackTrace();
			return null;
		}
	}

	public static boolean bufferedImagesEqual( BufferedImage img1, BufferedImage img2 ) {
		if( img1 == null || img2 == null )
			return false;
		if( img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight() ){
			for( int x= 0; x < img1.getWidth(); x++ ){
				for( int y= 0; y < img1.getHeight(); y++ ){
					if( img1.getRGB( x, y ) != img2.getRGB( x, y ) )
						return false;
				}
			}
		}else{
			return false;
		}
		return true;
	}

	public static void bufferedImagesStore( String path, BufferedImage img, String name ) {
		( new File( path ) ).mkdirs();
		try{
			ImageIO.write( img, "bmp", new File( path + "/" + name + ".bmp" ) );
		}catch ( IOException e ){
			e.printStackTrace();
		}
	}

	public static String randomSubFolder( String spath ) {
		if( spath == null )
			return null;
		File path= new File( spath );
		if( path.exists() && path.isDirectory() ){
			ArrayList <String> sub= getDirSubFolder( spath );
			return sub.get( (int) ( Math.random() * sub.size() ) );
		}
		return null;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| reutrn full path.
	||||--------------------------------------------------------------------------------------------*/
	public static String randomFile( String spath ) {
		if( spath == null )
			return null;
		File path= new File( spath );
		if( path.exists() && path.isDirectory() ){
			ArrayList <String> sub= getDirFile( spath, "" );
			return sub.get( (int) ( Math.random() * sub.size() ) );
		}
		return null;
	}

	public static cor2D getLocation( String co ) {
		if( co == null )
			return null;
		co= co.replace( '(', ' ' ).replace( ')', ' ' ).replace( ',', ' ' );
		Scanner rdr= new Scanner( co );
		int x= 0, y= 0;
		if( rdr.hasNextInt() )
			x= rdr.nextInt();
		else return null;
		if( rdr.hasNextInt() )
			y= rdr.nextInt();
		else return null;
		return new cor2D( x, y );
	}

	public static Color getColor( String co ) {
		if( co == null )
			return null;
		co= co.replace( '(', ' ' ).replace( ')', ' ' ).replace( ',', ' ' );
		Scanner rdr= new Scanner( co );
		int r, g, b, i= 255;
		try{
			if( rdr.hasNextInt() )
				r= rdr.nextInt();
			else return null;
			if( rdr.hasNextInt() )
				g= rdr.nextInt();
			else return null;
			if( rdr.hasNextInt() )
				b= rdr.nextInt();
			else return null;
			if( rdr.hasNextInt() ){
				i= rdr.nextInt();
				return new Color( r, g, b, i );
			}else return new Color( r, g, b );
		}catch ( Exception ee ){
			return null;
		}
	}

	public static String color2Str( Color co ) {
		if( co == null )
			return "";
		if( co.getAlpha() != 255 )
			return "(" + co.getRed() + "," + co.getGreen() + "," + co.getBlue() + "," + co.getAlpha() + ")";
		else return "(" + co.getRed() + "," + co.getGreen() + "," + co.getBlue() + ")";
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| change the color alpha .
	||||--------------------------------------------------------------------------------------------*/
	public static Color changeAlpha( Color co, int change ) {
		int r= co.getRed();
		int g= co.getGreen();
		int b= co.getBlue();
		int a= co.getAlpha() + change;
		if( a > 255 )
			a= 255;
		if( a < 0 )
			a= 0;
		return new Color( r, g, b, a );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get all the file content and return as ArrayList<String>.
	||||--------------------------------------------------------------------------------------------*/
	public static ArrayList <String> getAllText( String path ) {
		if( path == null )
			return null;
		File file= new File( path );
		if( !file.exists() || !file.isFile() )
			return null;
		ArrayList <String> ret= new ArrayList <>();
		Scanner rdr;
		try{
			rdr= new Scanner( file );
			while( rdr.hasNextLine() ){
				ret.add( rdr.nextLine() );
			}
			return ret;
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
			return null;
		}
	}

	public static String getDateOfWeekAsString( int i ) {
		switch( i ){
			case 0 :
				return "Sunday";
			case 1 :
				return "Monday";
			case 2 :
				return "Tuesday";
			case 3 :
				return "WednesDay";
			case 4 :
				return "Thursday";
			case 5 :
				return "Friday";
			case 6 :
				return "Saturday";
			default :
				return null;
		}
	}

	public static String getMonthName( int num ) {
		String month= null;
		DateFormatSymbols dfs= new DateFormatSymbols();
		String[] months= dfs.getMonths();
		if( num >= 0 && num <= 11 ){
			month= months[num];
		}
		return month;
	}

	public static String getWeekDayName( int num ) {
		switch( num ){
			case 1 :
				return "Monday";
			case 2 :
				return "Tuesday";
			case 3 :
				return "Wednesday";
			case 4 :
				return "Thusday";
			case 5 :
				return "Friday";
			case 6 :
				return "Saturday";
			case 7 :
				return "Sunday";
			default :
				return null;
		}
	}

	public static int getMonthNum( String inp ) {
		if( inp == null || inp.length() < 3 )
			return 0;
		switch( inp.substring( 0, 3 ) ){
			case "Jan" :
				return 1;
			case "Feb" :
				return 2;
			case "Mar" :
				return 3;
			case "Apr" :
				return 4;
			case "May" :
				return 5;
			case "Jun" :
				return 6;
			case "Jul" :
				return 7;
			case "Aug" :
				return 8;
			case "Sep" :
				return 9;
			case "Oct" :
				return 10;
			case "Nov" :
				return 11;
			case "Dec" :
				return 12;
			default :
				return 0;
		}
	}

	/* ----------------------------------------------------------------------------------------------
	 * extract the common string of all files inside a folder.
	 * ---------------------------------------------------------------------------------------------- */
	public static String extractName( String subfolder ) throws Exception {
		ArrayList <String> files;
		String tmp;
		String[] res= null;
		//
		//display.println( "doing: " + subfolder );
		files= helper.getDirFile( subfolder, "" );
		if( files == null || files.size() < 2 )
			return null;
		//
		tmp= helper.getFileName( ( new File( files.get( 0 ) ) ).getName() ).toLowerCase().replace( '-', ' ' )
				.replace( '_', ' ' ).replace( '(', ' ' ).replace( ')', ' ' ).replace( '.', ' ' )
				.replace( '[', ' ' ).replace( ']', ' ' );
		res= tmp.split( "\\s+" );
		//
		for( int i= 1; i < files.size(); i++ ){
			tmp= helper.getFileName( ( new File( files.get( i ) ) ).getName() ).toLowerCase().replace( '-', ' ' )
					.replace( '_', ' ' ).replace( '(', ' ' ).replace( ')', ' ' ).replace( '.', ' ' )
					.replace( '[', ' ' ).replace( ']', ' ' );
			String[] resT= tmp.split( "\\s+" );
			//
			for( int e= 0; e < res.length; e++ ){
				String tmpS= res[e];
				if( tmpS == null )
					continue;
				else{
					boolean sig= false;
					for( String comp : resT ){
						if( comp.equals( tmpS ) ){
							sig= true;
							break;
						}
					}
					if( !sig )
						res[e]= null;
				}
			}
		}
		//
		tmp= null;
		for( String tt : res ){
			if( tt != null ){
				if( tmp == null )
					tmp= tt;
				else tmp+= " " + tt;
			}
		}
		//display.println( tmp + '\n' );
		return tmp;
	}

	/* ----------------------------------------------------------------------------------------------
	 * rename the folder to the common string inside a folder.
	 * ---------------------------------------------------------------------------------------------- */
	public static void renameAllsubfolder( String mainfolder ) {
		if( mainfolder == null || ! ( new File( mainfolder ) ).exists() || ! ( new File( mainfolder ) ).isDirectory() )
			return;
		File folder;
		String name;
		try{
			for( String subfolder : helper.getDirSubFolder( mainfolder ) ){
				name= helper.extractName( subfolder );
				if( name != null && ! ( new File( mainfolder + "/" + name ) ).exists() ){
					( new File( subfolder ) ).renameTo( new File( mainfolder + "/" + name ) );
				}
			}
		}catch ( Exception ee ){
			ee.printStackTrace();
		}
	}

	/* ----------------------------------------------------------------------------------------------
	 * read all lines into a al of string.
	 * ---------------------------------------------------------------------------------------------- */
	public static ArrayList <String> readFile( String path ) {
		String str;
		ArrayList <String> lines= new ArrayList <>();
		try{
			BufferedReader in= new BufferedReader( new FileReader( path ) );
			while( ( str= in.readLine() ) != null ){
				lines.add( str );
			}
			in.close();
		}catch ( IOException e ){
			e.printStackTrace();
			return lines;
		}
		return lines;
		/*
		if( path == null )
			return null;
		File spath= new File( path );
		if( !spath.exists() || !spath.isFile() )
			return null;
		ArrayList <String> res= new ArrayList <>();
		try{
			Scanner rdr= new Scanner( spath );
			while( rdr.hasNextLine() ){
				res.add( rdr.nextLine() );
			}
			return res;
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
			return null;
		}
		*/
	}

	public static String[] str2arry( String inp ) {
		if( inp == null )
			return null;
		Scanner rdr= new Scanner( inp );
		ArrayList <String> res= new ArrayList <>();
		while( rdr.hasNext() ){
			res.add( rdr.next() );
		}
		String[] ret= new String[res.size()];
		int i= 0;
		for( String tmp : res ){
			ret[i++ ]= tmp;
		}
		return ret;
	}

	public static void append2File( String path, String inp ) {
		File spath= new File( path );
		if( !spath.exists() )
			try{
				spath.getParentFile().mkdirs();
				spath.createNewFile();
			}catch ( IOException e1 ){
				e1.printStackTrace();
				return;
			}
		//
		FileWriter fw;
		try{
			fw= new FileWriter( path, true );
			BufferedWriter bw= new BufferedWriter( fw );
			bw.write( inp + "\n" );
			bw.close();
			fw.close();
		}catch ( IOException e ){
			e.printStackTrace();
		}
	}

	public static void append2File( String path, ArrayList <String> inp ) {
		if( inp == null || inp.size() == 0 )
			return;
		File spath= new File( path );
		if( !spath.exists() )
			try{
				spath.getParentFile().mkdirs();
				spath.createNewFile();
			}catch ( IOException e1 ){
				e1.printStackTrace();
				return;
			}
		//
		FileWriter fw;
		try{
			fw= new FileWriter( path, true );
			BufferedWriter bw= new BufferedWriter( fw );
			for( String str : inp )
				bw.write( str + "\n" );
			bw.close();
			fw.close();
		}catch ( IOException e ){
			e.printStackTrace();
		}
	}

	public static void writeFile( String path, boolean OverRideExist, ArrayList <String> cont ) {
		if( path == null || cont == null || cont.size() == 0 )
			return;
		File spath= new File( path );
		if( !spath.exists() || OverRideExist ){
			spath.getParentFile().mkdirs();
			try{
				PrintWriter pw= new PrintWriter( path );
				for( String tmp : cont )
					pw.println( tmp );
				pw.close();
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
	}

	public static void writeFile( String path, boolean OverRideExist, String cont ) {
		if( path == null || cont == null || cont.length() == 0 )
			return;
		File spath= new File( path );
		if( !spath.exists() || OverRideExist ){
			spath.getParentFile().mkdirs();
			try{
				PrintWriter pw= new PrintWriter( path );
				pw.println( cont );
				pw.close();
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
	}

	public static void createFile( String path ) {
		File st= new File( path );
		if( !st.exists() ){
			try{
				st.createNewFile();
			}catch ( IOException e ){}
		}
	}

	public static ArrayList <String> str2ALstr( String inp ) {
		ArrayList <String> ret= new ArrayList <>();
		if( inp == null || inp.length() == 0 )
			return ret;
		while( inp.indexOf( '\n' ) != -1 ){
			if( inp.indexOf( '\n' ) == inp.length() - 1 ){
				inp= inp.substring( 0, inp.indexOf( '\n' ) );
				break;
			}
			ret.add( inp.substring( 0, inp.indexOf( '\n' ) ) );
			inp= inp.substring( inp.indexOf( '\n' ) + 1, inp.length() );
		}
		ret.add( inp );
		return ret;
	}

	public static String ALstr2str( ArrayList <String> inp ) {
		StringBuilder ret= null;
		for( String tmp : inp ){
			if( ret == null )
				ret= new StringBuilder( tmp );
			else ret.append( "\n" + tmp );
		}
		return ret.toString();
	}

	public static boolean AllEmptySpace( String string ) {
		if( string == null )
			return true;
		for( int i= 0; i < string.length(); i++ ){
			if( string.charAt( i ) != ' ' && string.charAt( i ) != '\n' &&
					string.charAt( i ) != '\t' )
				return false;
		}
		return true;
	}

	public static void openFolderWithWindows( String tmp2 ) {
		if( tmp2 == null )
			return;
		try{
			DeskTop.getDeskTop().open( new File( tmp2 ) );
		}catch ( IOException e ){
			e.printStackTrace();
			return;
		}
	}
}
//
//
//
//
//
//
//
