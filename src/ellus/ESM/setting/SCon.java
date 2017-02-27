package ellus.ESM.setting;

import static java.awt.GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.dictionary.dictDB;
import ellus.ESM.media.PlayerAudio;
import ellus.ESM.roboSys.clipBoard;



// constand settings.
public class SCon {
	// all settings ---------------------------------------------------------------------------------------------------
	//
	// general testing. ( when true, something like DB dont load. )
	public static boolean					_levelTestingGen				= false;
	// whether the sub thread will run to exec their task.
	public static final boolean				_levelTesting_TimerTask			= true;
	// whether to load native lib.
	public static final boolean				_levelTesting_loadNativeLib		= true;
	// whether to use native lib related.
	public static final boolean				_levelTesting_useNativeLib		= true;
	// show display in console.
	public static final boolean				_levelTesting_showDisplayOutput	= true;
	//
	// main setting file.
	public static final String				PathESMsetting					= "./ESM.setting";
	public static final String				PathXMLsetting					= "./ESM_XML.setting";
	// load enviro sig.
	private static boolean					envirLoaded						= false;
	//
	// pattern & static
	public static final String				maglinkHashHead					= "magnet:?xt=urn:btih:";
	public static final String				youtubeUrlHead					= "https://www.youtube.com/watch?v=";
	public static final String				youtubePreViewImgUrlHead		= "http://img.youtube.com/vi/";
	public static final String				youtubePreViewImgUrlTail		= "/hqdefault.jpg";
	public static final String				webUrlHeader					= "://";
	public static final String				DictDefiTypeSpliter				= "%";
	public static final String				GoogleApiKey					= "AIzaSyCRtJfj1s6GOD_2zc8sk6KPutkFE3_LhzM";
	// special signal in data.
	public static final int					noteTxtWebLinkUrlSignal			= 6;
	public static final int					calendarEventPastRuleSignal		= 6;
	// not used.
	//public static final String				SManXAttrSig	= "!@#$%^&*()<>?" + ((char)( 1 )) + ((char)( 2 )) + ((char)( 3 ));
	//
	// extension.
	public static final String				Extpinnable						= ".pinned";
	public static final String				Extpinnable2					= "pinned";
	public static final String				ExtpinLink						= ".pl";
	public static final String				ExtpinLink2						= "pl";
	public static final String				ExtPWM							= ".pwm";
	public static final String				ExtPWM2							= "pwm";
	public static final String				ExtpinImg						= ".pinImg";
	public static final String				ExtpinImg2						= "pinImg";
	public static final String				ExtWebLink						= ".WL";
	public static final String				ExtWebLink2						= "WL";
	public static final String				ExtSetting						= ".setting";
	public static final String				ExtSetting2						= "setting";
	public static final String				ExtWinLib						= ".dll";
	public static final String				ExtWinLib2						= "dll";
	public static final String				ExtCalREvn						= ".calREvent";
	public static final String				ExtCalREvn2						= "calREvent";
	public static final String				ExtCalOEvn						= ".calOEvent";
	public static final String				ExtCalOEvn2						= "calOEvent";
	//
	// others.
	public static ArrayList <Font>			FontList						= new ArrayList <>();
	public static ArrayList <String>		ImageExtList					= new ArrayList <>();
	public static ArrayList <String>		AudioExtList					= new ArrayList <>();
	public static ArrayList <PlayerAudio>	systemSound						= new ArrayList <>();
	public static PlayerAudio				showBoardSound					= null;
	//
	// other settings.
	public static final int					DisplayMsgLogSiz				= 500;
	//
	// Service Board settings.----------------------------------------------------------------------------------------
	// SB= service board.
	// TE= to edge.
	// OS= off set.
	//
	// SB_Home
	public static final String				SB_Home_SManX_Name				= "SB_Home";
	//
	// note Wall
	public static final String				SB_NoteWall_SManX_Name			= "SB_NoteWall";
	//
	// setting config window.
	public static final String				SB_SMXCnfg_SManX_Name			= "SB_SMXCnfg";
	public static final int					SB_SMXCnfg_ButtonW				= 500;
	public static final int					SB_SMXCnfg_ButtonH				= 44;
	public static final int					SB_SMXCnfg_ButtonWOS			= 18;
	public static final int					SB_SMXCnfg_ButtonHOS			= 9;

	//
	// Service board setting. ------------------------------------- END ---------------------------------------------
	//
	/*||----------------------------------------------------------------------------------------------
	 ||| set load const value enviornment. ( must be done before main program runs ).
	 ||| only load once.
	||||--------------------------------------------------------------------------------------------*/
	public static void SLCVE() {
		if( envirLoaded )
			return;
		envirLoaded= true;
		try{
			//
			// set if debug mode. see if this runs in folder that is in SDK foler.
			File curfolder= new File( "./" );
			if( curfolder.getCanonicalPath().contains( "ProjectSpace" ) ||
					curfolder.getCanonicalPath().contains( "_SDK" ) ){
				_levelTestingGen= true;
			}
			//
			// ------set graphic style for frame-------
			GraphicsEnvironment ge= GraphicsEnvironment.getLocalGraphicsEnvironment();	// Determine what the GraphicsDevice can support.
			GraphicsDevice gd= ge.getDefaultScreenDevice();
			boolean isPerPixelTranslucencySupported= gd.isWindowTranslucencySupported( PERPIXEL_TRANSLUCENT );
			if( !isPerPixelTranslucencySupported ){	//If translucent windows aren't supported, exit.
				System.exit( 0 );
			}
			//JFrame.setDefaultLookAndFeelDecorated( true ); // set style.
			//
			// start clip board.
			clipBoard cb= new clipBoard();
			//
			// set font
			Font ff= null;
			// get all the font from disk. and set them
			for( String tmp : helper.getAllFile( SMan.getSetting( 0 ) + SMan.getSetting( 101 ),
					"" ) ){
				try{
					ff= Font.createFont( Font.TRUETYPE_FONT, new File( tmp ) );
					ge.registerFont( ff );
					FontList.add( ff );
				}catch ( Exception ee ){
					ee.printStackTrace();
				}
			}
			for( String tmp : helper.getAllFile( SMan.getSetting( 0 ) + SMan.getSetting( 107 ),
					"" ) ){
				try{
					ff= Font.createFont( Font.TRUETYPE_FONT, new File( tmp ) );
					ge.registerFont( ff );
					FontList.add( ff );
				}catch ( Exception ee ){
					//ee.printStackTrace();
					display.printErr( "Class SCon", "error loading a font: " + tmp );
				}
			}
			//
			// get all supported image ext.
			String imgExtAll= SMan.getSetting( 1110 );
			if( imgExtAll != null ){
				Scanner rdr= new Scanner( imgExtAll );
				while( rdr.hasNext() ){
					ImageExtList.add( rdr.next() );
				}
			}
			// get all the supported sound et.
			String AudioExtAll= SMan.getSetting( 1111 );
			if( AudioExtAll != null ){
				Scanner rdr= new Scanner( AudioExtAll );
				while( rdr.hasNext() ){
					AudioExtList.add( rdr.next() );
				}
			}
			//
			// load native library such as for hyperic sigar. ( dll for windows, what currently is used. )
			//
			// load win lib .dll
			if( _levelTesting_loadNativeLib ){
				ArrayList <String> libs= helper.getDirFile(
						SMan.getSetting( 0 ) + SMan.getSetting( 104 ), ExtWinLib2 );
				File pareAbs= new File( ( new File( SMan.getSetting( 0 ) ) ).getParent() ).getAbsoluteFile();
				if( libs != null && libs.size() > 0 ){
					for( String tmp : libs ){
						tmp= pareAbs.getAbsolutePath().substring( 0, pareAbs.getAbsolutePath().length() - 1 )
								+ tmp.substring( 2, tmp.length() );
						try{
							System.load( tmp );
							display.println( "setting.SCon", "successful to load native lib: " + tmp );
						}catch ( UnsatisfiedLinkError e ){
							//e.printStackTrace();
							display.println( "setting.SCon", "fail to load native lib: " + tmp );
						}
					}
				}
			}
			//
			// load sound.
			/*
			for( String tmp : helper.getAllFile( SMan.getSetting( 0 ) +
					SMan.getSetting( 9 ), "mp3" ) ){
				//systemSound.add( new PlayerAudio( tmp, helper.getFileName( helper.getFilePathName( tmp ) ) ) );
			}
			showBoardSound= new PlayerAudio( SMan.getSetting( 0 ) +
					SMan.getSetting( 9 ) + SMan.getSetting( 601 ) );
			showBoardSound.setVolume( Double.parseDouble( SMan.getSetting( 1104 ) ) );
			//
			// create folder for dictDB.
			 */
			if( !_levelTestingGen ){
				dictDB.creatDict();
			}
			//
			// load SManX
			SManX.init();
			//
			// end //
		}catch ( Exception ee ){
			ee.printStackTrace();
		}
	}
}