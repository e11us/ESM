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
	public static final int					SB_Home_HOCirButX				= 15;
	public static final int					SB_Home_HOCirButY				= 230;
	public static final int					SB_Home_HOCirButDisY			= 120;
	public static final int					SB_Home_HOCirButSize			= 70;
	public static final int					SB_Home_TopNWSBTouchButtonW		= 610;
	public static final int					SB_Home_ButtonW					= 630;
	public static final int					SB_Home_ButtonH					= 135;
	public static final int					SB_Home_DoneLogButtonW			= 1330;
	public static final int					SB_Home_DoneLogButtonH			= 105;
	public static final int					SB_Home_ButtonWOS				= 32;
	public static final int					SB_Home_ButtonHOS				= 17;
	public static final int					SB_Home_ButtonCMDH				= 31;
	public static final int					SB_Home_ButtonCMDWOS			= 2;
	public static final int					SB_Home_ButtonCMDHOS			= 1;
	public static final int					SB_Home_CMDgroupX				= 270;
	public static final int					SB_Home_CMDgroupY				= 70;
	public static final int					SB_Home_SettingIndS				= 2000;
	public static final int					SB_Home_SettingIndE				= 2199;
	//
	// SB_NoteWall
	public static final int					SB_NW_ButtonW					= 315;
	public static final int					SB_NW_ButtonH					= 85;
	public static final int					SB_NW_SideShadeOS				= 7;
	public static final int					SB_NW_SideShadeDepth			= 40;
	public static final int					SB_NW_ButtonWOS					= 32;
	public static final int					SB_NW_ButtonHOS					= 14;
	public static final int					SB_NW_SearchBarX				= 500 + SB_NW_SideShadeDepth;
	public static final int					SB_NW_SearchBarY				= 50 + SB_NW_SideShadeDepth;
	public static final int					SB_NW_NewCatoButtonX			= 40 + SB_NW_SideShadeDepth;
	public static final int					SB_NW_NewCatoButtonY			= 50 + SB_NW_SideShadeDepth;
	public static final int					SB_NW_ButtonGroupX				= 40 + SB_NW_SideShadeDepth;
	public static final int					SB_NW_ButtonGroupY				= 200 + SB_NW_SideShadeDepth;
	public static final int					SB_NW_ButtonGroupDisX			= 25;
	public static final int					SB_NW_ButtonGroupDisY			= 15;
	public static final int					SB_NW_ButtonGroupTEX			= 40 + SB_NW_SideShadeDepth;
	public static final int					SB_NW_SettingIndS				= 2200;
	public static final int					SB_NW_SettingIndE				= 2399;
	//
	// SB_password manager
	public static final int					SB_PM_ButtonW					= 295;
	public static final int					SB_PM_ButtonH					= 95;
	public static final int					SB_PM_SideShadeOS				= 9;
	public static final int					SB_PM_SideShadeDepth			= 95;
	public static final int					SB_PM_ButtonWOS					= 35;
	public static final int					SB_PM_ButtonHOS					= 17;
	public static final int					SB_PM_SearchBarW				= 550;
	public static final int					SB_PM_SearchBarX				= 50 + SB_PM_SideShadeDepth;
	public static final int					SB_PM_SearchBarY				= 50 + SB_PM_SideShadeDepth;
	public static final int					SB_PM_PassWordBarW				= 550;
	public static final int					SB_PM_PassWordBarX				= 650 + SB_PM_SideShadeDepth;
	public static final int					SB_PM_PassWordBarY				= 50 + SB_PM_SideShadeDepth;
	public static final int					SB_PM_PassWordOptionX			= 1250 + SB_PM_SideShadeDepth;
	public static final int					SB_PM_ButtonGroupX				= 50 + SB_PM_SideShadeDepth;
	public static final int					SB_PM_ButtonGroupY				= 200 + SB_PM_SideShadeDepth;
	public static final int					SB_PM_ButtonGroupDisX			= 25;
	public static final int					SB_PM_ButtonGroupDisY			= 25;
	public static final int					SB_PM_ButtonGroupTEX			= 90 + SB_PM_SideShadeDepth;
	public static final int					SB_PW_SettingIndS				= 2400;
	public static final int					SB_PW_SettingIndE				= 2599;
	//
	// SB_Pixiv viewer
	public static final int					SB_PV_SideShadeOS				= 3;
	public static final int					SB_PV_SideShadeDepth			= 22;
	public static final int					SB_PV_Button1W					= 200;
	public static final int					SB_PV_Button1H					= 75;
	public static final int					SB_PV_Button2W					= 176;
	public static final int					SB_PV_Button2H					= 176;
	public static final int					SB_PV_ButtonWOS					= 30;
	public static final int					SB_PV_ButtonHOS					= 13;
	public static final int					SB_PV_SearchBarW				= 550;
	//
	public static final int					SB_PV_ButtonGroupDisX			= 12;
	public static final int					SB_PV_ButtonGroupDisY			= 12;
	public static final int					SB_PV_ButtonGroup2DisX			= 5;
	public static final int					SB_PV_ButtonGroup2DisY			= 5;
	public static final int					SB_PV_ButtonGroup1X				= 30 + SB_PV_SideShadeDepth;
	public static final int					SB_PV_ButtonGroup1Y				= 30 + SB_PV_SideShadeDepth;
	public static final int					SB_PV_SearchBarX				= 30 + SB_PV_SideShadeDepth;
	public static final int					SB_PV_SearchBarY				= SB_PV_ButtonGroup1Y + SB_PV_Button1H + 10;
	public static final int					SB_PV_ButtonGroupTEX			= 20 + SB_PV_SideShadeDepth;
	public static final int					SB_PV_SettingIndS				= 2600;
	public static final int					SB_PV_SettingIndE				= 2799;
	//
	// webLink manager
	public static final int					SB_WL_ButtonW					= 295;
	public static final int					SB_WL_ButtonH					= 82;
	public static final int					SB_WL_ButtonlinkW				= 570;
	public static final int					SB_WL_ButtonlinkH				= 61;
	public static final int					SB_WL_SideShadeOS				= 4;
	public static final int					SB_WL_SideShadeDepth			= 38;
	public static final int					SB_WL_ButtonWOS					= 38;
	public static final int					SB_WL_ButtonHOS					= 14;
	public static final int					SB_WL_SearchCatoBarW			= 550;
	public static final int					SB_WL_SearchCatoBarX			= 400 + SB_WL_SideShadeDepth;
	public static final int					SB_WL_SearchCatoBarY			= 50 + SB_WL_SideShadeDepth;
	public static final int					SB_WL_SearchLinkBarW			= 550;
	public static final int					SB_WL_SearchLinkBarX			= 1000 + SB_WL_SideShadeDepth;
	public static final int					SB_WL_SearchLinkBarY			= 50 + SB_WL_SideShadeDepth;
	public static final int					SB_WL_NewCatoButtonX			= 50 + SB_WL_SideShadeDepth;
	public static final int					SB_WL_NewCatoButtonY			= 50 + SB_WL_SideShadeDepth;
	public static final int					SB_WL_ButtonGroupX				= 50 + SB_WL_SideShadeDepth;
	public static final int					SB_WL_ButtonGroupY				= 50 + SB_WL_SideShadeDepth;
	public static final int					SB_WL_ButtonGroupDisX			= 12;
	public static final int					SB_WL_ButtonGroupDisY			= 12;
	public static final int					SB_WL_ButtonGroupTEX			= 50 + SB_WL_SideShadeDepth;
	public static final int					SB_WL_SettingIndS				= 2800;
	public static final int					SB_WL_SettingIndE				= 2999;
	//
	// Youtube DLer.
	public static final int					SB_YTD_ButtonW					= 1300;
	public static final int					SB_YTD_ButtonH					= 78;
	public static final int					SB_YTD_SideShadeOS				= 3;
	public static final int					SB_YTD_SideShadeDepth			= 18;
	public static final int					SB_YTD_ButtonWOS				= 35;
	public static final int					SB_YTD_ButtonHOS				= 17;
	public static final int					SB_YTD_ButtonGroupX				= 80 + SB_YTD_SideShadeDepth;
	public static final int					SB_YTD_ButtonGroupY				= 80 + SB_YTD_SideShadeDepth;
	public static final int					SB_YTD_ButtonGroupDisY			= 12;
	public static final int					SB_YTD_ButtonGroupTEX			= 50 + SB_YTD_SideShadeDepth;
	public static final int					SB_YTD_SettingIndS				= 3000;
	public static final int					SB_YTD_SettingIndE				= 3199;
	//
	// calendar SB.
	public static final int					SB_CL_maxLin					= 31;
	public static final int					SB_CL_TEX						= 45;
	public static final int					SB_CL_TEY						= 110;
	public static final int					SB_CL_ButtonWOS					= 8;
	public static final int					SB_CL_ButtonHOS					= 4;
	public static final int					SB_CL_ButtonGroupDisX			= 4;
	public static final int					SB_CL_ButtonGroupDisY			= 6;
	public static final int					SB_CL_InpBarW					= 640;
	public static final int					SB_CL_InpBarH					= 160;
	public static final int					SB_CL_InpBarCalH				= 70;
	public static final int					SB_CL_InpBarCalW				= 970;
	public static final int					SB_CL_SettingIndS				= 3200;
	public static final int					SB_CL_SettingIndE				= 3399;
	//
	// SB_BookRead
	public static final int					SB_BR_ButtonW					= 715;
	public static final int					SB_BR_ButtonH					= 85;
	public static final int					SB_BR_TFPButtonW				= 266;
	public static final int					SB_BR_SideShadeOS				= 7;
	public static final int					SB_BR_SideShadeDepth			= 40;
	public static final int					SB_BR_ButtonWOS					= 32;
	public static final int					SB_BR_ButtonHOS					= 14;
	public static final int					SB_BR_ButtonGroupX				= 40 + 40;
	public static final int					SB_BR_ButtonGroupY				= 40 + 40;
	public static final int					SB_BR_ButtonGroupDisX			= 25;
	public static final int					SB_BR_ButtonGroupDisY			= 15;
	public static final int					SB_BR_ButtonGroupTEX			= 40 + 40;
	public static final int					SB_BR_defPanOX					= 500;
	public static final int					SB_BR_defPanOY					= 150;
	public static final int					SB_BR_SettingIndS				= 3400;
	public static final int					SB_BR_SettingIndE				= 3599;
	//
	// DoneEvent SB.
	public static final int					SB_DE_ButtonGroupX				= 80;
	public static final int					SB_DE_ButtonGroupY				= 80;
	public static final int					SB_DE_TFPButtonW				= 266;
	public static final int					SB_DE_SettingIndS				= 3600;
	public static final int					SB_DE_SettingIndE				= 3799;
	//
	public static final String				SB_SMXCnfg_SManX_Name			= "SB_SMXCnfg";
	public static final int					SB_SMXCnfg_ButtonW				= 500;
	public static final int					SB_SMXCnfg_ButtonH				= 44;
	public static final int					SB_SMXCnfg_ButtonWOS			= 18;
	public static final int					SB_SMXCnfg_ButtonHOS			= 9;

	//
	// Service board setting. --- END -------------------------------------------------------------------------------
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
			for( String tmp : helper.getAllFile( SMan.getSetting( 0 ) +
					SMan.getSetting( 9 ), "mp3" ) ){
				//systemSound.add( new PlayerAudio( tmp, helper.getFileName( helper.getFilePathName( tmp ) ) ) );
			}
			showBoardSound= new PlayerAudio( SMan.getSetting( 0 ) +
					SMan.getSetting( 9 ) + SMan.getSetting( 601 ) );
			showBoardSound.setVolume( Double.parseDouble( SMan.getSetting( 1104 ) ) );
			//
			// create folder for dictDB.
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
/*
// not used.
//
// not good for mp3.
public static final String			dictGetterSiteMW			= "http://www.merriam-webster.com/dictionary/a";
// this good for mp3.
public static final String			dictGetterSiteCB			= "http://dictionary.cambridge.org/us/dictionary/english/a";
// this good for mp3.
public static final String			dictGetterSiteDC			= "http://www.dictionary.com/browse/a";
public static final String			youtubeDLsiteKV				= "http://keepvid.com/";
public static final String			youtubeDLsiteOVC			= "http://www.onlinevideoconverter.com/video-converter";
//
*/
