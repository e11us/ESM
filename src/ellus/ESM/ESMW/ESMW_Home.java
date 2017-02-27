package ellus.ESM.ESMW;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.ArrayList;
import ellus.ESM.ESMP.AudioPlayer;
import ellus.ESM.ESMP.Calendar;
import ellus.ESM.ESMP.Dictionary;
import ellus.ESM.ESMP.FileSys;
import ellus.ESM.ESMP.NoteEntry;
import ellus.ESM.ESMP.PasswordManager;
import ellus.ESM.ESMP.YoutubeDL;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.SQL.mySQLportal;
import ellus.ESM.data.Source.SourceDouble;
import ellus.ESM.data.dictionary.dictDB;
import ellus.ESM.data.sys.BackupEr;
import ellus.ESM.pinnable.pin2GridLocationer;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Able.AbleClickHighlight;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.pinnable.Button.ButtonTextFS2;
import ellus.ESM.pinnable.ButtonAni.ButtonSpikeHori;
import ellus.ESM.pinnable.LF.PaneRect;
import ellus.ESM.pinnable.Note.NoteText;
import ellus.ESM.pinnable.SS.PanelBackgroundDotCenterFlow;
import ellus.ESM.pinnable.SS.PanelBackgroundDotGrid;
import ellus.ESM.pinnable.SS.PanelBackgroundPicSuff;
import ellus.ESM.pinnable.SS.PanelBackgroundRLetter;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundStripCenterFlow;
import ellus.ESM.pinnable.SS.PanelBackgroundStripSnake;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS2;
import ellus.ESM.pinnable.SS.PanelCupInfo;
import ellus.ESM.pinnable.SS.PanelDateTimeInfo;
import ellus.ESM.pinnable.SS.PanelFpsInfo;
import ellus.ESM.pinnable.SS.PanelMemInfo;
import ellus.ESM.pinnable.panel.PanelPopUpMsg;
import ellus.ESM.roboSys.DeskTop;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManX;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;
import notUsed.PicScraping_AllPicInPage;
import notUsed.magLinkChecker;



/*
 * font index 23 support chinese and english.
 *
 *
 */
public class ESMW_Home extends ESMW {
	// homePanel for bg and PL for use.
	private ESMW_Home		handler			= this;
	private ESMPanel		homePanel		= null;
	private ESMPL			homeRegular		= null;
	private ESMPL			homeNote		= null;
	private ESMPL			homeBgNote		= null;
	private ESMPL			homeBgRegular	= null;
	private ESMPL			homeBgLink		= null;
	private ESMPL			homeLink		= null;
	private ESMPL			homeBgNWSB		= null;
	private ESMPL			homeNWSB		= null;
	private ESMPL			homeBgApps		= null;
	private ESMPL			homeApps		= null;
	//
	// setting config.
	private SManXElm		apSE			= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "AudioPlayer" );
	private SManXElm		ytdlSE			= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "YoutubeDL" );
	private SManXElm		calSE			= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "Calendar" );
	private SManXElm		dictSE			= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "Dictionary" );
	private SManXElm		fileSysSE		= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "FileSystem" );
	private SManXElm		pswordSE		= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "PasswordManager" );
	private SManXElm		notEntSE		= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "NoteEntry" );
	private SManXElm		SE				= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name );
	private HomeOption		HO				= HomeOption.regular;
	private int				HO_index		= 1;
	// fps panel for all bg.
	private PanelFpsInfo	fpsPan			= null;

	private enum HomeOption {
		regular, note, link, noteWallSelect, apps
	};
	// pop up msg.
	private boolean ShowingEscPop= false;
	private PanelPopUpMsg escPop= null;
	private long lastESCpress= 0;
	
	
	public ESMW_Home() {
		// setup fps.
		fpsPan= setupFpsMA( SE );
		// homePanel SetUp.
		homePanel= createESMPanelHome();
		homeBgNote= SetHomePanelBGNote( homePanel.PS,
				SE.getElm( "ESMPanel", "HomeBgPanel" ).getElm( "ESMPL", "Note" ) );
		homeBgRegular= SetHomePanelBGReg( homePanel.PS,
				SE.getElm( "ESMPanel", "HomeBgPanel" ).getElm( "ESMPL", "Regular" ) );
		homeBgLink= SetHomePanelBGLink( homePanel.PS,
				SE.getElm( "ESMPanel", "HomeBgPanel" ).getElm( "ESMPL", "Link" ) );
		homeRegular= setHomePanelRegu( homePanel.PS,
				SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeRegular" ) );
		homeNote= setHomePanelNote( homePanel.PS,
				SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeNote" ) );
		homeLink= setHomePanelLink( homePanel.PS,
				SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeLink" ) );
		homeBgNWSB= SetHomePanelBGNWSB( homePanel.PS,
				SE.getElm( "ESMPanel", "HomeBgPanel" ).getElm( "ESMPL", "NoteWallSB" ) );
		homeNWSB= setHomePanelNWSB( homePanel.PS,
				SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeNoteWallSB" ) );
		homeBgApps= SetHomePanelBGApps( homePanel.PS,
				SE.getElm( "ESMPanel", "HomeBgPanel" ).getElm( "ESMPL", "Apps" ) );
		homeApps= setHomePanelApps( homePanel.PS,
				SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeApps" ) );
		//
		homePanel.bgPL= homeBgRegular;
		homePanel.Subpls.add( homeRegular );
		homePanel.masterSE= SE;
		homePanel.name= "ESMW_Home";
		//  Frame SetUp. (set it to frame bg. )
		super.bgPanel= homePanel;
		super.init( SE.getElm( "ESMW", "HomeFrame" ) );
		//super.frame.setAlwaysOnTop( true );
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPanel createESMPanelHome() {
		//( home panel always starts in full screen. & create setting package. )
		Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
		ESMPanel pp= new ESMPanel( screenSize.width, screenSize.height ) {
			@Override
			protected synchronized void KeyboardInpNGE( String lk ) {
				// always clear menu.
				homePanel.menuPL= null;
				//
				if( lk.length() == 1 ){
					switch( lk.charAt( 0 ) ){
						case 27 :// esc
							if( HO != HomeOption.regular )
								switch2HomeRegular();
							else {
								if( helper.getTimeLong() - lastESCpress < 400 ) {
									handler.miniFrame();
									escPop.B1clickAction( 0, 0 );
									escPop= null;
									return;
								}else if( !ShowingEscPop ) {
									ShowingEscPop= true;
									escPop= new PanelPopUpMsg(
											SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeRegular" ).
											getElm( "PanelPopUpMsg", "ESC_PopUpMsg" ), "Double Tap ESC to Minimize Window" ) {
										@Override
										public void B1clickAction( int x, int y ) {
											this.removeMe= true;
											ShowingEscPop= false;
										}
									};
									escPop.setSelfCloseTime( 5 );
									bgPL.add( 6, escPop ); 
								}
								lastESCpress= helper.getTimeLong();
							}
							break;
						case 10 :// enter.
							break;
						case 32 :
							if( HO == HomeOption.apps ){
								switch2HomeRegular();
								break;
							}else{
								switch2HomeApps();
								break;
							}
						default :
							// default open dict. at center of screen.
							Dictionary dic= Dictionary.getInstance( dictSE );
							if( dic != null ){
								dic.moveTo( (int) ( screenSize.getWidth() / 2 - dic.PS.getWidth() / 2 ),
										(int) ( screenSize.getHeight() / 2 - dic.PS.getHeight() / 2 ) );
								handler.addPanel( dic );
							}
							break;
					}
				}else if( lk.length() == 2 ){
					switch( lk ){
						case "AU" :
							HO_index-- ;
							switchHomebyIndex();
							break;
						case "AD" :
							HO_index++ ;
							switchHomebyIndex();
							break;
						case "AL" :
							HO_index-- ;
							switchHomebyIndex();
							break;
						case "AR" :
							HO_index++ ;
							switchHomebyIndex();
							break;
						case "F8" :
							new ESMW_SMXConfig( masterSE );
							break;
					}
				}else if( lk.length() == 3 ){
					switch( lk ){
					}
				}
			}

			@Override
			protected synchronized void CheckB1ClickNGE( MouseEvent e ) {
				//
			}

			@Override
			protected synchronized void CheckB3ClickNGE( MouseEvent e ) {
				if( HO == HomeOption.apps ){
					switch2HomeRegular();
					return;
				}
				// create a new right click panel, and add it.
				menuPL= ( getRightClickMenu( homePanel.PS, SE.getElm( "ESMPanel", "HomePanel" ).getElm(
						"Setting", "HomePanel_RightClickMenu" ) ) );
			}

			@Override
			protected void checkB2PressNGE( MouseWheelEvent e ) {
				if( PS.mousewheelNavEnable ){
					PS.changeViewCenterP( 0, e.getWheelRotation() * PS.mouseWheelNavSpeed );
					return;
				}
				//
				HO_index+= e.getWheelRotation();
				switchHomebyIndex();
			}
		};
		return pp;
	}

	private void switchHomebyIndex() {
		if( HO_index > 2 )
			HO_index= 2;
		else if( HO_index < 0 )
			HO_index= 0;
		if( HO == HomeOption.apps || HO == HomeOption.noteWallSelect )
			return;
		//
		switch( HO_index ){
			case 1 :
				switch2HomeRegular();
				break;
			case 2 :
				switch2HomeNote();
				break;
			case 0 :
				switch2HomeLink();
				break;
			case 3 :
				switch2HomeNWSB();
				break;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void switch2HomeRegular() {
		// always clear menu.
		homePanel.menuPL= null;
		//
		if( HO != HomeOption.regular ){
			HO= HomeOption.regular;
			HO_index= 1;
			homePanel.Subpls.removeAll( homePanel.Subpls );
			homePanel.Subpls.add( homeRegular );
			homePanel.bgPL= homeBgRegular;
			handler.showAllSubPanel();
			//
			homePanel.PS.EdgeViewLimitY= 0;
			homePanel.PS.setViewCenter( 0, 0 );
			homePanel.PS.mousewheelNavEnable= false;
		}
	}

	private void switch2HomeLink() {
		// always clear menu.
		homePanel.menuPL= null;
		//
		if( HO != HomeOption.link ){
			HO= HomeOption.link;
			HO_index= 0;
			homePanel.Subpls.removeAll( homePanel.Subpls );
			homePanel.Subpls.add( homeLink );
			homePanel.bgPL= homeBgLink;
			handler.hideAllSubPanel();
		}
	}

	private void switch2HomeNote() {
		// always clear menu.
		homePanel.menuPL= null;
		//
		// always clear menu.
		homePanel.menuPL= null;
		//
		if( HO != HomeOption.note ){
			HO= HomeOption.note;
			HO_index= 2;
			homePanel.Subpls.removeAll( homePanel.Subpls );
			homePanel.Subpls.add( homeNote );
			homePanel.bgPL= homeBgNote;
			handler.hideAllSubPanel();
		}
	}

	private void switch2HomeNWSB() {
		// always clear menu.
		homePanel.menuPL= null;
		//
		homeNWSB= setHomePanelNWSB( homePanel.PS,
				SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeNoteWallSB" ) );
		//
		if( HO != HomeOption.noteWallSelect ){
			HO= HomeOption.noteWallSelect;
			HO_index= 3;
			//
			homePanel.PS.EdgeViewLimitY= 200;
			homePanel.PS.setViewCenter( 0, 0 );
			//
			homePanel.bgPL= homeBgNWSB;
			homePanel.Subpls.removeAll( homePanel.Subpls );
			homePanel.Subpls.add( homeNWSB );
			handler.hideAllSubPanel();
			//
			homePanel.PS.mousewheelNavEnable= true;
			homePanel.PS.mouseWheelNavSpeed= handler.SE.getElm( "ESMPanel", "HomePanel" )
					.getAttr( AttrType._int, "HomePanelMouseWheelNavSpeed" ).getInteger();
			homePanel.PS.contLocationViewChangeLockEnable= true;
			homePanel.PS.contLocationViewChangeLockDis= handler.SE.getElm( "ESMPanel", "HomePanel" )
					.getAttr( AttrType._int, "HomePaneContLocationViewChangeLockDis" ).getInteger();
		}
	}

	private void switch2HomeApps() {
		// always clear menu.
		homePanel.menuPL= null;
		//
		if( HO != HomeOption.apps ){
			HO= HomeOption.apps;
			HO_index= 4;
			//
			homePanel.PS.EdgeViewLimitY= 200;
			homePanel.PS.setViewCenter( 0, 0 );
			//
			homePanel.bgPL= homeBgApps;
			homePanel.Subpls.removeAll( homePanel.Subpls );
			homePanel.Subpls.add( homeApps );
			handler.hideAllSubPanel();
			//
			homePanel.PS.mousewheelNavEnable= true;
			homePanel.PS.mouseWheelNavSpeed= handler.SE.getElm( "ESMPanel", "HomePanel" )
					.getAttr( AttrType._int, "HomePanelMouseWheelNavSpeed" ).getInteger();
			homePanel.PS.contLocationViewChangeLockEnable= true;
			homePanel.PS.contLocationViewChangeLockDis= handler.SE.getElm( "ESMPanel", "HomePanel" )
					.getAttr( AttrType._int, "HomePaneContLocationViewChangeLockDis" ).getInteger();
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL SetHomePanelBGNote( ESMPS PS, SManXElm config ) {
		//
		ESMPL bgPL= new ESMPL();
		for( SManXElm elm : config.getElmAll() ){
			switch( elm.getType() ){
				case "PanelBackgroundDotCenterFlow" :
					bgPL.add( 3, new PanelBackgroundDotCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundDotGrid" :
					bgPL.add( 1, new PanelBackgroundDotGrid( elm, PS ) );
					break;
				case "PanelBackgroundStripCenterFlow" :
					bgPL.add( 2, new PanelBackgroundStripCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundRLetter" :
					bgPL.add( 4, new PanelBackgroundRLetter( elm ) );
					break;
				case "PanelBackgroundStripSnake" :
					bgPL.add( 2, new PanelBackgroundStripSnake( elm, PS ) );
					break;
				case "PanelBackgroundSC" :
					bgPL.add( 0, new PanelBackgroundSC( elm, PS ) );
					break;
			}
		}
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL SetHomePanelBGLink( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		for( SManXElm elm : config.getElmAll() ){
			switch( elm.getType() ){
				case "PanelBackgroundDotCenterFlow" :
					bgPL.add( 3, new PanelBackgroundDotCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundDotGrid" :
					bgPL.add( 1, new PanelBackgroundDotGrid( elm, PS ) );
					break;
				case "PanelBackgroundStripCenterFlow" :
					bgPL.add( 2, new PanelBackgroundStripCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundRLetter" :
					bgPL.add( 4, new PanelBackgroundRLetter( elm ) );
					break;
				case "PanelBackgroundStripSnake" :
					bgPL.add( 2, new PanelBackgroundStripSnake( elm, PS ) );
					break;
				case "PanelBackgroundSC" :
					bgPL.add( 0, new PanelBackgroundSC( elm, PS ) );
					break;
			}
		}
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL SetHomePanelBGReg( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		for( SManXElm elm : config.getElmAll() ){
			switch( elm.getType() ){
				case "PanelBackgroundDotCenterFlow" :
					bgPL.add( 3, new PanelBackgroundDotCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundDotGrid" :
					bgPL.add( 1, new PanelBackgroundDotGrid( elm, PS ) );
					break;
				case "PanelBackgroundStripCenterFlow" :
					bgPL.add( 2, new PanelBackgroundStripCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundRLetter" :
					bgPL.add( 4, new PanelBackgroundRLetter( elm ) );
					break;
				case "PanelBackgroundStripSnake" :
					bgPL.add( 2, new PanelBackgroundStripSnake( elm, PS ) );
					break;
				case "PanelBackgroundSC" :
					bgPL.add( 0, new PanelBackgroundSC( elm, PS ) );
					break;
			}
		}
		//
		bgPL.add( 0, new PanelBackgroundPicSuff( SMan.getSetting( 0 )
				+ SMan.getSetting( 106 ) + SMan.getSetting( 2110 ), 5 ) );
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL SetHomePanelBGNWSB( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		for( SManXElm elm : config.getElmAll() ){
			switch( elm.getType() ){
				case "PanelBackgroundDotCenterFlow" :
					bgPL.add( 3, new PanelBackgroundDotCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundDotGrid" :
					bgPL.add( 1, new PanelBackgroundDotGrid( elm, PS ) );
					break;
				case "PanelBackgroundStripCenterFlow" :
					bgPL.add( 2, new PanelBackgroundStripCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundRLetter" :
					bgPL.add( 4, new PanelBackgroundRLetter( elm ) );
					break;
				case "PanelBackgroundStripSnake" :
					bgPL.add( 2, new PanelBackgroundStripSnake( elm, PS ) );
					break;
				case "PanelBackgroundSC" :
					bgPL.add( 0, new PanelBackgroundSC( elm, PS ) );
					break;
				case "PanelBackgroundTitleFS2" :
					bgPL.add( 6, new PanelBackgroundTitleFS2( elm, PS, "NoteWall Selector" ) );
			}
		}
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL SetHomePanelBGApps( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		for( SManXElm elm : config.getElmAll() ){
			switch( elm.getType() ){
				case "PanelBackgroundDotCenterFlow" :
					bgPL.add( 3, new PanelBackgroundDotCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundDotGrid" :
					bgPL.add( 1, new PanelBackgroundDotGrid( elm, PS ) );
					break;
				case "PanelBackgroundStripCenterFlow" :
					bgPL.add( 2, new PanelBackgroundStripCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundRLetter" :
					bgPL.add( 4, new PanelBackgroundRLetter( elm ) );
					break;
				case "PanelBackgroundStripSnake" :
					bgPL.add( 2, new PanelBackgroundStripSnake( elm, PS ) );
					break;
				case "PanelBackgroundSC" :
					bgPL.add( 0, new PanelBackgroundSC( elm, PS ) );
					break;
				case "PanelBackgroundTitleFS2" :
					bgPL.add( 6, new PanelBackgroundTitleFS2( elm, PS, "AppsShow" ) );
			}
		}
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL setHomePanelRegu( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		SManXElm SE= config;
		//
		// set up sideshade hide and expand.
		int homeSSYOS= SE.getAttr( AttrType._int, "HomeSideShadeYOS" ).getInteger();
		int homeSSHS= SE.getAttr( AttrType._int, "HomeSideShadeHideSize" ).getInteger();
		int homeSSES= SE.getAttr( AttrType._int, "HomeSideShadeExpandSize" ).getInteger();
		// left SS.
		Polygon polyLeft= new Polygon();
		polyLeft.addPoint( 0, homeSSYOS );
		polyLeft.addPoint( homeSSES, homeSSES + homeSSYOS );
		polyLeft.addPoint( homeSSES, PS.PanelH - homeSSES - homeSSYOS );
		polyLeft.addPoint( 0, PS.PanelH - homeSSYOS );
		Rectangle rectangleLeft= new Rectangle();
		rectangleLeft.x= -1;// add to -1 so that mouse most left still active
		rectangleLeft.y= homeSSYOS;
		rectangleLeft.width= homeSSHS;
		rectangleLeft.height= PS.PanelH - homeSSYOS * 2;
		// right SS.
		Polygon polyRigth= new Polygon();
		polyRigth.addPoint( PS.PanelW, homeSSYOS );
		polyRigth.addPoint( PS.PanelW - homeSSES, homeSSES + homeSSYOS );
		polyRigth.addPoint( PS.PanelW - homeSSES, PS.PanelH - homeSSES - homeSSYOS );
		polyRigth.addPoint( PS.PanelW, PS.PanelH - homeSSYOS );
		Rectangle rectangleRight= new Rectangle();
		rectangleRight.x= PS.PanelW - homeSSHS;
		rectangleRight.y= homeSSYOS;
		rectangleRight.width= homeSSHS;
		rectangleRight.height= PS.PanelH - homeSSYOS * 2;
		//SManXElm inp, ESMPS PS, ArrayList<pinnable> cont, Polygon pg, Rectangle rg  ){
		//SideShadeAutoHide homeRSS= new SideShadeAutoHide(
		//
		// add TFP
		bgPL.add( 4, new PanelDateTimeInfo( SE.getElm( "PanelDateTimeInfo", "TFP_DTpanel" ) ) );
		bgPL.add( 4, fpsPan );
		bgPL.add( 3, new PaneRect( SE.getElm( "PaneRect", "TFP_BgPanel" ) ) {
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeApps();
			}
		} );
		//
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| FPS panel for all bg.
	||||--------------------------------------------------------------------------------------------*/
	private PanelFpsInfo setupFpsMA( SManXElm config ) {
		PanelFpsInfo pp= new PanelFpsInfo( config.getElm( "pinnable.Panel", "FPSpanel" ),
				new SourceDouble() {
					@Override
					public double getDouble() {
						return handler.getRenderWaitTime();
					}
				} );
		// also set for frame fps source.
		super.fpsSource= new SourceDouble() {
			@Override
			public double getDouble() {
				return pp.getFPS();
			}
		};
		return pp;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL setHomePanelNote( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		// add all note text.
		for( String tmp : helper.getAllFile( SMan.getSetting( 0 ) +
				SMan.getSetting( 10 ), SCon.Extpinnable2 ) ){
			bgPL.add( 1, new NoteText( config.getElm( "NoteTextConfig", "HomeNote_NoteConfig" ), tmp ) );
		}
		// exit to regular.
		ButtonSpikeHori returnSS= new ButtonSpikeHori( config.getElm( "ButtonSpikeHori", "return2Home" ), PS,
				"Return Home" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				// click highlight off if needed.
				if( homePanel.highlighted != null && homePanel.highlighted instanceof AbleClickHighlight )
					( (AbleClickHighlight)homePanel.highlighted ).B1clickHighlightOff( 0, 0 );
				// simulate, as if pressed esc.
				homePanel.KeyboardInpNGE( (char)27 + "" );
			}
		};
		// center it.
		returnSS.setXY( PS.getWidth() / 2 - returnSS.getWidth() / 2,
				PS.getWidth() / 2 + returnSS.getWidth() / 2, returnSS.getYmin(), returnSS.getYmax() );
		bgPL.add( 3, returnSS );
		//
		bgPL.add( 4, fpsPan );
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL setHomePanelLink( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		// add all note text.
		for( String tmp : helper.getAllFile( SMan.getSetting( 0 ) +
				SMan.getSetting( 14 ), SCon.Extpinnable2 ) ){
			bgPL.add( 1, new NoteText( config.getElm( "NoteTextConfig", "HomeNote_NoteConfig" ), tmp ) );
		}
		// exit to regular.
		ButtonSpikeHori returnSS= new ButtonSpikeHori( config.getElm( "ButtonSpikeHori", "return2Home" ), PS,
				"Return Home" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				// click highlight off if needed.
				if( homePanel.highlighted != null && homePanel.highlighted instanceof AbleClickHighlight )
					( (AbleClickHighlight)homePanel.highlighted ).B1clickHighlightOff( 0, 0 );
				// simulate, as if pressed esc.
				homePanel.KeyboardInpNGE( (char)27 + "" );
			}
		};
		// center it.
		returnSS.setXY( PS.getWidth() / 2 - returnSS.getWidth() / 2,
				PS.getWidth() / 2 + returnSS.getWidth() / 2, returnSS.getYmin(), returnSS.getYmax() );
		bgPL.add( 3, returnSS );
		//
		bgPL.add( 4, fpsPan );
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL setHomePanelNWSB( ESMPS PS, SManXElm config ) {
		//
		ESMPL pl= new ESMPL();
		ButtonTextFS2 fs2but;
		pin2GridLocationer locator= new pin2GridLocationer(
				config.getElm( "pin2GridLocationer", "locationer" ), PS, PS.ViewOrignX, PS.ViewOrignY );
		//
		// get all catergory.
		ArrayList <String> cato= helper
				.getDirSubFolder( SMan.getSetting( 0 ) + SMan.getSetting( 1 ) );
		if( cato != null ){
			// for each cato. skip home pin and link.
			for( String tmp : cato ){
				if( helper.getFilePathName( tmp ).equals( helper.getFilePathName( SMan.getSetting( 10 ) ) ) ||
						helper.getFilePathName( tmp ).equals( helper.getFilePathName( SMan.getSetting( 14 ) ) ) )
					continue;
				//
				fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "categoryName" ),
						"Category: " + helper.getFilePathName( tmp ), 0, 0 ) {
					@Override
					public void B3clickAction( int x, int y ) {
						helper.openFolderWithWindows( tmp );
					}
				};
				// reset location.
				locator.next( fs2but );
				locator.nextRow();
				pl.add( 1, fs2but );
				// get all sub board.
				ArrayList <String> catonb= helper.getDirSubFolder( tmp );
				if( catonb != null && catonb.size() > 0 ){
					// for each sub board.
					for( String tmp2 : catonb ){
						fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
								helper.getFilePathName( tmp2 ), 0, 0 ) {
							// get into sub board.
							@Override
							public void B1clickAction( int x, int y ) {
								handler.hideFrame();
								ESMW_NoteWall nw= new ESMW_NoteWall( handler, tmp2 );
							}

							@Override
							public void B3clickAction( int x, int y ) {
								helper.openFolderWithWindows( tmp2 );
							}
						};
						// reset location.
						locator.next( fs2but );
						pl.add( 1, fs2but );
					}
				}
				locator.nextRow();
			}
		}
		// exit to regular.
		ButtonSpikeHori returnSS= new ButtonSpikeHori( config.getElm( "ButtonSpikeHori", "return2Home" ), PS,
				"Return Home" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				homePanel.KeyboardInpNGE( (char)27 + "" );
			}
		};
		// center it.
		returnSS.setXY( PS.getWidth() / 2 - returnSS.getWidth() / 2,
				PS.getWidth() / 2 + returnSS.getWidth() / 2, returnSS.getYmin(), returnSS.getYmax() );
		pl.add( 3, returnSS );
		//
		return pl;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL setHomePanelApps( ESMPS PS, SManXElm config ) {
		//
		ESMPL pl= new ESMPL();
		ButtonTextFS2 fs2but;
		pin2GridLocationer locator= new pin2GridLocationer(
				config.getElm( "pin2GridLocationer", "locationer" ), PS, PS.ViewOrignX, PS.ViewOrignY );
		//
		// ESM basic.
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "categoryName" ),
				"ESM basic", 0, 0 );
		// reset location.
		locator.next( fs2but );
		locator.nextRow();
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"Minimize ESM", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				handler.miniFrame();
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"Exit ESM", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				closeFrame();
				exitESM();
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		locator.nextRow();
		//
		// ESM utility.
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "categoryName" ),
				"System Utility", 0, 0 );
		// reset location.
		locator.next( fs2but );
		locator.nextRow();
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"FileSystem", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeRegular();
				FileSys fip= new FileSys( fileSysSE );
				handler.addPanel( fip );
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"DeskTop Link", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeLink();
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"DeskTop Note", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeNote();
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"NoteWalls", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeNWSB();
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		locator.nextRow();
		//
		// ESM utility.
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "categoryName" ),
				"Apps", 0, 0 );
		// reset location.
		locator.next( fs2but );
		locator.nextRow();
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"PasswordManager", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeRegular();
				PasswordManager pwm= PasswordManager.getInstance( pswordSE );
				if( pwm != null )
					handler.addPanel( pwm );
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"NoteEntry", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeRegular();
				NoteEntry net= NoteEntry.getInstance( notEntSE );
				if( net != null )
					handler.addPanel( net );
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"Calendar", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeRegular();
				handler.addPanel( Calendar.getInstance( calSE ) );
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"Dictionary", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeRegular();
				handler.addPanel( Dictionary.getInstance( dictSE ) );
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"YTDL", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeRegular();
				handler.addPanel( YoutubeDL.getInstance( ytdlSE ) );
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"Audio Player", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				switch2HomeRegular();
				handler.addPanel( new AudioPlayer( apSE ) );
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"Open ESM folder", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				try{
					DeskTop.getDeskTop().open( new File( SMan.getSetting( 0 ) ) );
				}catch ( Exception ee ){}
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"Magnet: On", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				if( this.msg.equals( "Magnet: On" ) ){
					magLinkChecker.stopChecker();
					this.setMsg( "Magnet: Off" );
				}else{
					magLinkChecker.startChecker();
					this.setMsg( "Magnet: On" );
				}
			}
		};
		//
		magLinkChecker.startChecker();
		//
		locator.next( fs2but );
		pl.add( 1, fs2but );
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"Pic DL: Off", 0, 0 ) {
			// get into sub board.
			@Override
			public void B1clickAction( int x, int y ) {
				if( this.msg.equals( "Pic DL: On" ) ){
					PicScraping_AllPicInPage.end();
					this.setMsg( "Pic DL: Off" );
				}else{
					PicScraping_AllPicInPage.start();
					this.setMsg( "Pic DL: On" );
				}
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		/*-------------
		//
		fs2but= new ButtonTextFS2( config.getElm( "ButtonTextFS2", "boardName" ),
				"sdfdsfsdfsdf", 0, 0 ) {
			// get into    sub board.
			@Override
			public void B1clickAction( int x, int y ) {
		
			}
		};
		locator.next( fs2but );
		pl.add( 1, fs2but );
		*///------------
			//
		pl.add( 4, new PanelDateTimeInfo( config.getElm( "PanelDateTimeInfo", "TFP_DTpanel" ) ) );
		pl.add( 4, new PanelCupInfo( config.getElm( "PanelCupInfo", "TFP_CPUpanel" ) ) );
		pl.add( 4, new PanelMemInfo( config.getElm( "PanelMemInfo", "TFP_MEMpanel" ) ) );
		pl.add( 3, new PaneRect( config.getElm( "PaneRect", "TFP_BgPanel" ) ) );
		//
		return pl;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL getRightClickMenu( ESMPS PS, SManXElm config ) {
		ESMPL PL= new ESMPL();
		Color[] NWButColor= {
				config.getAttr( AttrType._color, "ButtonColor1" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColor2" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColor3" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColor4" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColor5" ).getColor()
		};
		Color[] NWButColorH= {
				config.getAttr( AttrType._color, "ButtonColorH1" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColorH2" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColorH3" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColorH4" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColorH5" ).getColor()
		};
		int[] NWButton= {
				PS.b2wX( PS.MouseLastPositionX ), PS.b2wY( PS.MouseLastPositionY ),
				config.getAttr( AttrType._int, "ButtonWidth" ).getInteger(),
				config.getAttr( AttrType._int, "ButtonHeight" ).getInteger(),
				config.getAttr( AttrType._int, "ButtonXOS" ).getInteger(),
				config.getAttr( AttrType._int, "ButtonYOS" ).getInteger(), };
		int sepa= config.getAttr( AttrType._int, "BoardItemSeperation" ).getInteger();
		int fontInd= config.getAttr( AttrType._int, "BoardFontInd" ).getInteger();
		int fontSize= config.getAttr( AttrType._int, "BoardFontSize" ).getInteger();
		switch( HO ){
			case regular :
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"menu Test", fontInd, fontSize ) {} );
				break;
			case note :
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"new TextNote", fontInd, fontSize ) {
					@Override
					public void B1clickAction( int x, int y ) {
						// create a new blank note. and reload all home pins.
						NoteText newNote= new NoteText(
								SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeNote" )
										.getElm( "NoteTextConfig", "HomeNote_NoteConfig" ),
								SMan.getSetting( 0 ) + SMan.getSetting( 10 ) + "/ " + helper.getCurrentDateStamp() +
										helper.rand32AN().substring( 0, 5 ) + " " + x + " " + y + " ."
										+ SCon.Extpinnable2 );
						homeNote.add( 1, newNote );
					}
				} );
				NWButton[1]+= config.getAttr( AttrType._int, "ButtonHeight" ).getInteger() + 5;
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"Align All to Grid", fontInd, fontSize ) {
					@Override
					public void B1clickAction( int x, int y ) {
						int xxx, yyy;
						int alignmentX= SE.getElm( "ESMPanel", "HomeBgPanel" ).getAttr( AttrType._int, "AlignmentX" )
								.getInteger();
						int alignmentY= SE.getElm( "ESMPanel", "HomeBgPanel" ).getAttr( AttrType._int, "AlignmentY" )
								.getInteger();
						for( pinnable pin : homeNote.getAll() ){
							xxx= pin.getXmin();
							yyy= pin.getYmin();
							if( pin.getXmin() % alignmentX > alignmentX / 2 )
								xxx+= -pin.getXmin() % alignmentX + alignmentX;
							else xxx-= pin.getXmin() % alignmentX;
							if( pin.getYmin() % alignmentY > alignmentY / 2 )
								yyy+= -pin.getYmin() % alignmentY + alignmentY;
							else yyy-= pin.getYmin() % alignmentY;
							pin.setXY( xxx, xxx + pin.getWidth(), yyy, yyy + pin.getHeight() );
							// store new location.
							if( pin instanceof NoteText )
								( (NoteText)pin ).B1clickHighlightOff( 0, 0 );
						}
					}
				} );
				break;
			case link :
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"new LocalLink", fontInd, fontSize ) {
					@Override
					public void B1clickAction( int x, int y ) {
						// create a new blank note. and reload all home pins.
						NoteText newNote= new NoteText(
								SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeNote" )
										.getElm( "NoteTextConfig", "HomeNote_NoteConfig" ),
								SMan.getSetting( 0 ) + SMan.getSetting( 14 ) + "/ " + helper.getCurrentDateStamp() +
										helper.rand32AN().substring( 0, 5 ) + " " + x + " " + y + " ."
										+ SCon.Extpinnable2 );
						// ask for a link first.
						newNote.B3clickActionSec( SMan.getSetting( 500 ) );
						//
						if( newNote.hasLink() ){
							homeLink.add( 1, newNote );
						}else newNote.FunInp( "F2" );// if can get one, erase this note.
						// close menu.
						homePanel.menuPL= null;
					}
				} );
				NWButton[1]+= config.getAttr( AttrType._int, "ButtonHeight" ).getInteger() + 5;
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"new WebLink", fontInd, fontSize ) {} );
				NWButton[1]+= config.getAttr( AttrType._int, "ButtonHeight" ).getInteger() + 5;
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"Align All to Grid", fontInd, fontSize ) {
					@Override
					public void B1clickAction( int x, int y ) {
						int xxx, yyy;
						int alignmentX= SE.getElm( "ESMPanel", "HomeBgPanel" ).getAttr( AttrType._int, "AlignmentX" )
								.getInteger();
						int alignmentY= SE.getElm( "ESMPanel", "HomeBgPanel" ).getAttr( AttrType._int, "AlignmentY" )
								.getInteger();
						for( pinnable pin : homeLink.getAll() ){
							xxx= pin.getXmin();
							yyy= pin.getYmin();
							if( pin.getXmin() % alignmentX > alignmentX / 2 )
								xxx+= -pin.getXmin() % alignmentX + alignmentX;
							else xxx-= pin.getXmin() % alignmentX;
							if( pin.getYmin() % alignmentY > alignmentY / 2 )
								yyy+= -pin.getYmin() % alignmentY + alignmentY;
							else yyy-= pin.getYmin() % alignmentY;
							pin.setXY( xxx, xxx + pin.getWidth(), yyy, yyy + pin.getHeight() );
							// store new location.
							if( pin instanceof NoteText )
								( (NoteText)pin ).B1clickHighlightOff( 0, 0 );
						}
					}
				} );
				break;
		}
		return PL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	public void _ExitESM() {
		exitESM();
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void exitESM() {
		//
		display.println( this.getClass().toString(), "ESM is getting ready to exit..." );
		//
		// start by exit all sub panel if needed.
		NoteEntry.close();
		mySQLportal.close();
		//
		// close dict db update if in process.
		dictDB.updateDB_Stop();
		//
		// check if need backup.
		BackupEr.backupIfNeeded();
		//
		// close and write all the SManX setting.
		SManX.exit();
		// exit
		display.println( this.getClass().toString(), "ESM will exit now, Good Bye" );
		System.exit( 1 );
	}
}
/*
 *
 * private void HomeConstructRegular() {
		//
		if( bgLayer12 == null ){
			// cirl#, cirl thic, cirl sep, int corSize // cirC
			int[] corRai= { 9, 5, 0, 26, 210, 0, 82, 1, 990, 500 };
			bgLayer12= new PanelBackgroundCoreRaid( corRai );
		}
		if( bgLayer13 == null ){
			// cirl#, cirl thic, cirl sep, cirl Size // cirC
			int[] corCir1= { 3, 3, 4, 56, 990, 500 };
			bgLayer13= new PanelBackgroundCoreCirc( corCir1, new Color( 217, 255, 204, 95 ) );
		}
		//
		PanelBackgroundRLetter signiture= new PanelBackgroundRLetter( 5, 23, 2315, 940, -1, "E _ESM #3.74",
				new Color( 255, 255, 153, 200 ) );
	}



	ArrayList <String> f1panTxt= new ArrayList <>();
						f1panTxt.add( "F1 help menu----------------------------------------------" );
						f1panTxt.add( "" );
						f1panTxt.add( "01. right mouse on empty space for menu." );
						f1panTxt.add( "" );
						f1panTxt.add( "02. left mouse on item to highlight." );
						f1panTxt.add( "" );
						f1panTxt.add( "03. double left mouse on item to open link." );
						f1panTxt.add( "" );
						f1panTxt.add( "04. right mouse for drag item." );
						f1panTxt.add( "" );
						f1panTxt.add( "05. double right mouse for setting a file link." );
						f1panTxt.add( "" );
						f1panTxt.add( "06. highlight a item, and ctrl+v to paste text inside." );
						f1panTxt.add( "" );
						f1panTxt.add(
								"07. if pasted text is a web link, it will set web link of the highlighted item." );
						f1panTxt.add( "" );
						f1panTxt.add(
								"08. when new item link is selected. click on the first item, then the second." );
						f1panTxt.add( "" );
						f1panTxt.add( "09. F2 to delete the highlighted item." );
						f1panTxt.add( "" );
						f1panTxt.add( "10. F5 to refresh the folders." );




						// top edge.
		Shapes sp1= new Shapes();
		line2D l1= new line2D();
		l1.p1= new cor2D( GCSV.HomeShadeEdgeOffSetX1, GCSV.HomeShadeEdgeOffSetY1 );
		l1.p2= new cor2D( GCSV.HomeShadeEdgeOffSetX2, GCSV.HomeShadeEdgeOffSetY2 );
		l1.thick= 1;
		l1.c= Color.WHITE;
		sp1.add( l1 );
		line2D l2= new line2D();
		l2.p1= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX1, GCSV.HomeShadeEdgeOffSetY1 );
		l2.p2= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX2, GCSV.HomeShadeEdgeOffSetY2 );
		l2.thick= 1;
		l2.c= Color.WHITE;
		sp1.add( l2 );
		line2D l3= new line2D();
		l3.p1= new cor2D( GCSV.HomeShadeEdgeOffSetX2, GCSV.HomeShadeEdgeOffSetY2 );
		l3.p2= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX2, GCSV.HomeShadeEdgeOffSetY2 );
		l3.thick= 1;
		l3.c= Color.WHITE;
		sp1.add( l3 );
		pins.add( sp1 );
		// bottom edge.
		sp1= new Shapes();
		l1= new line2D();
		l1.p1= new cor2D( GCSV.HomeShadeEdgeOffSetX1, paintBoard.getHeight() - GCSV.HomeShadeEdgeOffSetY1 );
		l1.p2= new cor2D( GCSV.HomeShadeEdgeOffSetX2, paintBoard.getHeight() - GCSV.HomeShadeEdgeOffSetY2 );
		l1.thick= 1;
		l1.c= Color.WHITE;
		sp1.add( l1 );
		l2= new line2D();
		l2.p1= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX1, paintBoard.getHeight()
				- GCSV.HomeShadeEdgeOffSetY1 );
		l2.p2= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX2, paintBoard.getHeight()
				- GCSV.HomeShadeEdgeOffSetY2 );
		l2.thick= 1;
		l2.c= Color.WHITE;
		sp1.add( l2 );
		l3= new line2D();
		l3.p1= new cor2D( GCSV.HomeShadeEdgeOffSetX2, paintBoard.getHeight() - GCSV.HomeShadeEdgeOffSetY2 );
		l3.p2= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX2, paintBoard.getHeight()
				- GCSV.HomeShadeEdgeOffSetY2 );
		l3.thick= 1;
		l3.c= Color.WHITE;
		sp1.add( l3 );
		pins.add( sp1 );
		//
		// time display.
		xpos= paintBoard.getWidth() / 2 - 270;
		ypos= paintBoard.getHeight() / -2 + 40;
		pins.add( new Gadget_TimeDisplay( xpos, ypos ) );
		//
		// cpu usage display.
		xpos= paintBoard.getWidth() / -2 + 96;
		ypos= paintBoard.getHeight() / -2 + 20;
		pins.add( new Gadget_CPU( xpos, ypos, 400, 20 ) );
		//
		// set all.
		super.resetPinlistS( pins );

	*/
